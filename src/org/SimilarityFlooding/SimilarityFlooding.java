package org.SimilarityFlooding;

import org.SimilarityFlooding.DataTypes.*;
import org.javatuples.Pair;
import org.utils.Correspondence;

import java.util.*;
import java.util.stream.IntStream;

public class SimilarityFlooding<T> {
    private final ArrayList<AbsoluteSimilarity<T>> distances = new ArrayList<>();
    private final ArrayList<PairwiseConnectivity> pcg;
    private final Graph<T> graph1;
    private final Graph<T> graph2;
    private final FixpointFormula fixpointFormula;

    public SimilarityFlooding(Graph<T> g1, Graph<T> g2, SFConfig config) {
        this.fixpointFormula = config.fixpointFormula();

        if (g1.edges().isEmpty() || g2.edges().isEmpty())
            throw new IllegalArgumentException("one of the databases was empty");
        this.graph1 = g1;
        this.graph2 = g2;

        // read database and initialize pairwise distances
        g1.nodes().forEach(nodeA ->
                g2.nodes().forEach(nodeB ->
                        distances.add(new AbsoluteSimilarity<>(nodeA, nodeB, config.similarityAlgorithm()))));

        pcg = new ArrayList<>();
        // prep PCG
        g1.edges().forEach(e -> g2.edges().stream()
                .filter(e2 -> e.relation().equals(e2.relation()))
                .forEach(matchingEdge ->
                        pcg.add(new PairwiseConnectivity(
                                distances.stream().filter(dist ->
                                                dist.nodeA().equals(e.parent()) &&
                                                        dist.nodeB().equals(matchingEdge.parent()))
                                        .findFirst().orElseThrow(), // cannot be empty
                                distances.stream().filter(dist ->
                                                dist.nodeA().equals(e.child()) &&
                                                        dist.nodeB().equals(matchingEdge.child()))
                                        .findFirst().orElseThrow(), // cannot be empty
                                e.relation()
                        ))));
        pcg.forEach(pairconn ->
        {
            //   1 / |nodes| that pairconn parents point to with same relation
            pairconn.coefficient = 1.0f / pcg.stream().filter(pairconn::hasSameParentsAs).count();
            //   1 / |nodes| that pairconn children are pointed to (reverse)
            pairconn.reversecoefficient = 1.0f / pcg.stream().filter(pairconn::hasSameChildrenAs).count();
        });
    }

    public void run(int maxRounds) {
        run(maxRounds, 0.05f);
    }

    public void run(int maxRounds, float maxresidual) {
        float residual = 1.0f;
        var prevValues = distances.stream().map(Correspondence::similarity).toList();
        int iter = 0;

        // initialize values
        iterate();
        while (residual > maxresidual && iter < maxRounds) {
            iterate();

            iter++;

            // residual computation
            java.util.List<Double> finalPrevValues = prevValues;
            residual = (float) IntStream.range(0, distances.size())
                    .mapToDouble(index -> distances.get(index).similarity() - finalPrevValues.get(index)).map(Math::abs).sum();
            prevValues = distances.stream().map(Correspondence::similarity).toList();
        }
        iterate();

        System.out.printf("Done after %d iterations.\n", iter);
        distances.sort(Comparator.comparingDouble(Correspondence::similarity));
        Collections.reverse(distances);
        System.out.print("\n");
        distances.forEach(dist -> System.out.printf(dist.toString() + "\n"));
    }

    public void iterate() {
        // iterate over each possible match
        fixpointComputation(fixpointFormula);
        // normalization
        var normalizationFactor = Collections.max(distances.stream().map(AbsoluteSimilarity::similarity).toList());
        distances.forEach(dist -> dist.similarity /= normalizationFactor);
    }

    public List<Correspondence<T>> getCorrespondants() {
        return distances.stream()
                .map(dist -> new Correspondence<>(
                        dist.nodeA(),
                        dist.nodeB(),
                        dist.similarity())).toList();
    }

    public Pair<Graph<T>, Graph<T>> getGraphs() {
        return new Pair<>(graph1, graph2);
    }

    private void fixpointComputation(FixpointFormula fixpointFormula) {
        distances.forEach(distance -> {
            distance.similarity = distance.similarityN1();
            switch (fixpointFormula) {
                case Basic:
                    break;
                case A:
                    distance.similarityN1 += distance.initialSimilarity();
                    break;
                case B:
                    distance.similarity += distance.initialSimilarity();
                    distance.similarityN1 = 0;
                    break;
                case C:
                    distance.similarity += distance.initialSimilarity();
                    distance.similarityN1 += distance.initialSimilarity();
                    break;
                default:
                    throw new IllegalArgumentException("FixpointFormula invalid");
            }
        });

        // propagate Graph
        pcg.forEach(pc -> {
            pc.children().similarityN1 += pc.parents().similarity() * pc.coefficient;
            pc.parents().similarityN1 += pc.children().similarity() * pc.reversecoefficient;
        });
        /*
        distances.stream().map(dist -> new AbsoluteSimilarity<TreeNode>(
                dist.nodeA(),
                dist.nodeB(),
                dist.similarityN1() +
                        pcg.stream().filter(pc -> pc.parents().equals(dist))
                                .mapToDouble(pc -> pc.parents().similarity() * pc.coefficient).sum() +
                        pcg.stream().filter(pc -> pc.children().equals(dist))
                                .mapToDouble(pc -> pc.children().similarity() * pc.reversecoefficient).sum(),
                dist.initialSimilarity(),
                dist.similarityN1()
        ));*/
    }
}
