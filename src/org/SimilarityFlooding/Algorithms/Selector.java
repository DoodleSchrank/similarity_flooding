package org.SimilarityFlooding.Algorithms;

import org.SimilarityFlooding.DataTypes.*;
import org.javatuples.Pair;
import org.utils.Correspondence;

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

    public static List<Correspondence<String>> selectThreshold(Pair<Graph<String>, Graph<String>> graphs, List<Correspondence<String>> distances, float similarityThreshold) {
        var relativeSimilarities = new ArrayList<RelativeSimilarity<String>>();
        distances.forEach(dist -> relativeSimilarities.add(new RelativeSimilarity<>(dist)));

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

    public static List<Correspondence<String>> highestCumulativeSimilaritySelection(Pair<Graph<String>, Graph<String>> graphs, List<Correspondence<String>> distances) {
        var uniqueNodeA = new ArrayList<>(distances.stream().filter(distinctByKey(Correspondence::nodeA)).map(Correspondence::nodeA).map(Object::toString).toList());
        var uniqueNodeB = new ArrayList<>(distances.stream().filter(distinctByKey(Correspondence::nodeB)).map(Correspondence::nodeB).map(Object::toString).toList());

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
                                dist.nodeA().equals(currentNodeA) &&
                                        dist.nodeB().equals(currentNodeB))
                        .map(Correspondence::similarity).findFirst()
                        .orElse(Double.MAX_VALUE);
            }
        }

        var ha = new HungarianAlgorithm(matrix);
        var assignments = ha.findOptimalAssignment();
        var resultingAssignments = new ArrayList<Correspondence<String>>();

        for (var ass : assignments) {
            resultingAssignments.add(distances.stream().filter(dist ->
                    dist.nodeA().toString().equals(uniqueNodeA.get(ass[0])) &&
                            dist.nodeB().toString().equals(uniqueNodeB.get(ass[1]))
            ).findFirst().orElse(
                    // one of the nodes had name "null" -> doesn't exist -> TreeNode must be created
                    new Correspondence<>(
                            graphs.getValue0().nodes().stream()
                                    .filter(node -> node.equals(uniqueNodeA.get(ass[0])))
                                    .findFirst().orElse(uniqueNodeA.get(ass[0])),
                            graphs.getValue1().nodes().stream()
                                    .filter(node -> node.equals(uniqueNodeB.get(ass[1])))
                                    .findFirst().orElse(uniqueNodeB.get(ass[1])),
                            1.0f)));
        }
        return resultingAssignments;
    }

    public static List<Correspondence<String>> stableMarriageSelection(List<Correspondence<String>> distances) {
        distances.sort(Comparator.comparingDouble(Correspondence::similarity));
        Collections.reverse(distances);
        var resultDistances = new ArrayList<Correspondence<String>>();
        for (var distance : distances) {
            if (resultDistances.stream().anyMatch(rd -> rd.nodeA().equals(distance.nodeA()) || rd.nodeB().equals(distance.nodeB())))
                continue;
            resultDistances.add(distance);
        }
        return resultDistances;
    }
}
