package org.SimilarityFlooding;

import org.SimilarityFlooding.DataTypes.*;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.IntStream;

public class SimilarityFlooding {
    private final ArrayList<AbsoluteSimilarity> distances = new ArrayList<>();
    private final ArrayList<PairwiseConnectivity> pcg;
    private final Graph graph1;
    private final Graph graph2;
    private final FixpointFormula fixpointFormula;

    SimilarityFlooding(Graph g1, Graph g2, SFConfig config) {
        this.fixpointFormula = config.fixpointFormula();

        if (g1.edges().isEmpty() || g2.edges().isEmpty())
            throw new IllegalArgumentException("one of the databases was empty");
        this.graph1 = g1;
        this.graph2 = g2;

        // read database and initialize pairwise distances
        g1.nodes().forEach(nodeA ->
                g2.nodes().forEach(nodeB ->
                        distances.add(new AbsoluteSimilarity(nodeA, nodeB, config.similarityAlgorithm()))));

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
        var prevValues = distances.stream().map(Similarity::similarity).toList();
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
            prevValues = distances.stream().map(Similarity::similarity).toList();
        }
        iterate();

        System.out.printf("Done after %d iterations.\n", iter);
        distances.sort(Comparator.comparingDouble(AbsoluteSimilarity::similarity).reversed());
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

    public List<AbsoluteSimilarity> getDistances() {
        return distances;
    }

    public Pair<Graph, Graph> getGraphs() {
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
                case default:
                    throw new IllegalArgumentException("FixpointFormula invalid");
            }
        });

        // propagate Graph
        pcg.forEach(pc -> {
            pc.children().similarityN1 += pc.parents().similarity() * pc.coefficient;
            pc.parents().similarityN1 += pc.children().similarity() * pc.reversecoefficient;
        });
    }
}
