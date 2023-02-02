package org.SimilarityFlooding;

import org.SimilarityFlooding.DataTypes.Graph;
import org.SimilarityFlooding.DataTypes.PairwiseConnectivity;
import org.SimilarityFlooding.DataTypes.Similarity;
import org.SimilarityFlooding.DataTypes.TreeNode;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

@FunctionalInterface
interface Function5<T, U, V, W, X, R> {
    R apply(T t, U u, V v, W w, X x);
}
record SFConfig(BiFunction<TreeNode, TreeNode, Float> similarityAlgorithm,
                Function5<List<PairwiseConnectivity>, List<PairwiseConnectivity>, List<Similarity>, List<Similarity>, Integer, Float> fixpointFormula) {
}

public class SimilarityFlooding {
    private final ArrayList<Similarity> distances = new ArrayList<>();
    private final ArrayList<PairwiseConnectivity> pcg = new ArrayList<>();
    private final Graph graph1;
    private final Graph graph2;
    private final ArrayList<Similarity> initialDistances;
    private final Function5<List<PairwiseConnectivity>, List<PairwiseConnectivity>, List<Similarity>, List<Similarity>, Integer, Float> fixpointFormula;
    SimilarityFlooding(Graph g1, Graph g2, SFConfig config) {
        this.fixpointFormula = config.fixpointFormula();

        if (g1.edges().isEmpty() || g2.edges().isEmpty())
            throw new IllegalArgumentException("one of the databases was empty");
        this.graph1 = g1;
        this.graph2 = g2;

        // read database and initialize pairwise distances
        g1.nodes().forEach(nodeA -> g2.nodes().forEach(nodeB ->
                distances.add(new Similarity(nodeA, nodeB, config.similarityAlgorithm()))));
        initialDistances = new ArrayList<>(distances);

        // prep PCG
        g1.edges().forEach(edge -> g2.edges().stream()
                    .filter(edge2 -> edge.relation().equals(edge2.relation()))
                .forEach(matchingedge ->
                    pcg.add(
                            new PairwiseConnectivity(
                                    distances.stream().filter(dist ->
                                            dist.nodeA().equals(edge.parent()) &&
                                                    dist.nodeB().equals(matchingedge.parent())).findFirst().get(),
                                    distances.stream().filter(dist ->
                                            dist.nodeA().equals(edge.child()) &&
                                                    dist.nodeB().equals(matchingedge.child())).findFirst().get(),
                                    edge.relation()
                            ))));
        pcg.forEach(pairconn -> {
            pairconn.coefficient = 1.0f / pcg.stream().filter(pairconn::hasSameParentsAs).count(); //   1 / |nodes| that pairconn parents point to with same relation
            pairconn.reversecoefficient = 1.0f / pcg.stream().filter(pairconn::hasSameChildrenAs).count(); //   1 / |nodes| that pairconn children are pointed to (reverse)
            System.out.printf("(%s %s) -> |%f %f| -> (%s %s)     coeffcnt: %d revcoeffcnt %d\n",
                    pairconn.parents().nodeA().name(),
                    pairconn.parents().nodeB().name(),
                    pairconn.coefficient,
                    pairconn.reversecoefficient,
                    pairconn.children().nodeA().name(),
                    pairconn.children().nodeB().name(),
                    pcg.stream().filter(pairconn::hasSameParentsAs).count(),
                    pcg.stream().filter(pairconn::hasSameChildrenAs).count());
        });
    }
    public void run(int rounds) {
        //TODO some more conditions
        float residual = 1.0f;
        var prevValues = distances.stream().map(dist -> dist.similarity).toList();
        int iter = 0;
        while (residual > 0.05f && iter < 1000) {
            iterate();
            iter++;

            java.util.List<Float> finalPrevValues = prevValues;
            residual = (float) IntStream.range(0, distances.size())
                    .mapToDouble(index -> distances.get(index).similarity - finalPrevValues.get(index)).sum();
            prevValues = distances.stream().map(dist -> dist.similarity).toList();
        }

        for (var i = 0; i < rounds; i++) {
            iterate();
        }
        //TODO bipartite graph stuff
        distances.sort(Comparator.comparingDouble(Similarity::similarity).reversed());
    }

    public void iterate() {
        //TODO debug
        //var targetdistance = distances.stream().filter(dist -> dist.nodeA().name().equals("string") && dist.nodeB().name().equals("int")).findFirst().get();

        var index = 0;
        // iterate over each possible match
        for (var distance : distances) {
            // find all nodes pointing towards our match
            var incomingMatches = pcg.stream().filter(pc -> pc.includesAsChildren(distance)).toList();
            // find all nodes where our match is pointing to (reverse arrow)
            var outgoingMatches = pcg.stream().filter(pc -> pc.includesAsParents(distance)).toList();

            distance.similarity = this.fixpointFormula.apply(incomingMatches, outgoingMatches, distances, initialDistances, index);
            index++;
        }

        // normalization
        distances.forEach(dist -> dist.similarity /= Collections.max(distances.stream().map(Similarity::similarity).toList()));
    }

    public List<Similarity> getDistances() {
        return distances;
    }
    public Pair<Graph, Graph> getGraphs() {
        return new Pair<>(graph1, graph2);
    }
}
