package org.SimilarityFlooding.Algorithms;

import org.SimilarityFlooding.DataTypes.*;
import org.javatuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Selector {
    // used in stream.filter to get all items with unique keyExtractor
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static List<AbsoluteSimilarity> selectThreshold(Pair<Graph, Graph> graphs, List<AbsoluteSimilarity> distances, float similarityThreshold) {
        var relativeSimilarities = new ArrayList<RelativeSimilarity>();
        distances.forEach(dist -> relativeSimilarities.add(new RelativeSimilarity(dist)));

        graphs.getValue0().nodes().forEach(node -> {
                    var maxSim = Collections.max(relativeSimilarities.stream().filter(rsim -> rsim.nodeA().equals(node)).map(RelativeSimilarity::similarity).toList());
                    relativeSimilarities.stream().filter(rsim -> rsim.nodeA().equals(node)).forEach(rsim -> rsim.similarity /= maxSim);
                }
        );
        graphs.getValue1().nodes().forEach(node -> {
                    var maxSim = Collections.max(relativeSimilarities.stream().filter(rsim -> rsim.nodeB().equals(node)).map(RelativeSimilarity::similarity).toList());
                    relativeSimilarities.stream().filter(rsim -> rsim.nodeB().equals(node)).forEach(rsim -> rsim.reverseSimilarity /= maxSim);
                }
        );
        return new ArrayList<>(distances.stream().distinct()
                .filter(relativeSimilarities.stream()
                .filter(rsim -> rsim.similarity() > similarityThreshold && rsim.reverseSimilarity() > similarityThreshold).toList()::contains).toList());
    }

    public static List<AbsoluteSimilarity> highestCumulativeSimilaritySelection(Pair<Graph, Graph> graphs, List<AbsoluteSimilarity> distances) {
        var uniqueNodeA = new ArrayList<>(distances.stream().filter(distinctByKey(AbsoluteSimilarity::nodeA)).map(AbsoluteSimilarity::nodeA).map(TreeNode::name).toList());
        var uniqueNodeB = new ArrayList<>(distances.stream().filter(distinctByKey(AbsoluteSimilarity::nodeB)).map(AbsoluteSimilarity::nodeB).map(TreeNode::name).toList());

        // ensure square matrix
        var nodeAmountDiff = uniqueNodeA.size() - uniqueNodeB.size();
        if (nodeAmountDiff > 0) {
            while (nodeAmountDiff > 0) {
                uniqueNodeB.add("null");
                nodeAmountDiff--;
            }
        } else if (nodeAmountDiff < 0) {
            while (nodeAmountDiff < 0) {
                uniqueNodeA.add("null");
                nodeAmountDiff++;
            }
        }

        var matrix = new double[uniqueNodeA.size()][uniqueNodeA.size()];
        for (var y = 0; y < matrix.length; y++) {
            for (var x = 0; x < matrix.length; x++) {
                var currentNodeA = uniqueNodeA.get(x);
                var currentNodeB = uniqueNodeB.get(y);
                matrix[y][x] = 1 - distances.stream().filter(dist ->
                                dist.nodeA().name().equals(currentNodeA) &&
                                        dist.nodeB().name().equals(currentNodeB))
                        .map(AbsoluteSimilarity::similarity).findFirst()
                        .orElse(Double.MAX_VALUE);
            }
        }

        var ha = new HungarianAlgorithm(matrix);
        var assignments = ha.findOptimalAssignment();
        var resultingAssignments = new ArrayList<AbsoluteSimilarity>();

        for (var ass : assignments) {
            resultingAssignments.add(distances.stream().filter(dist ->
                    dist.nodeA().name().equals(uniqueNodeA.get(ass[0])) &&
                            dist.nodeB().name().equals(uniqueNodeB.get(ass[1]))
            ).findFirst().orElse(
                    // one of the nodes had name "null" -> doesn't exist -> TreeNode must be created
                    new AbsoluteSimilarity(
                            graphs.getValue0().nodes().stream()
                                    .filter(node -> node.name().equals(uniqueNodeA.get(ass[0])))
                                    .findFirst().orElse(new TreeNode(uniqueNodeA.get(ass[0]))),
                            graphs.getValue1().nodes().stream()
                                    .filter(node -> node.name().equals(uniqueNodeB.get(ass[1])))
                                    .findFirst().orElse(new TreeNode(uniqueNodeB.get(ass[1]))),
                            1.0f)));
        }
        return resultingAssignments;
    }

    public static List<AbsoluteSimilarity> stableMarriageSelection(List<AbsoluteSimilarity> distances) {
        distances.sort(Comparator.comparingDouble(AbsoluteSimilarity::similarity).reversed());
        var resultDistances = new ArrayList<AbsoluteSimilarity>();
        for (var distance : distances) {
            if (resultDistances.stream().anyMatch(rd -> rd.nodeA().equals(distance.nodeA()) || rd.nodeB().equals(distance.nodeB())))
                continue;
            resultDistances.add(distance);
        }
        return resultDistances;
    }
}
