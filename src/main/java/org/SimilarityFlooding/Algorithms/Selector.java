package org.SimilarityFlooding.Algorithms;

import org.SimilarityFlooding.DataTypes.Graph;
import org.SimilarityFlooding.DataTypes.Similarity;
import org.SimilarityFlooding.DataTypes.TreeNode;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Selector {
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private static Pair<List<Similarity>, Float> recursiveCumulativeSimilarity(List<Similarity> distances, List<TreeNode> remainingNodeA) {
        // TODO
        if (remainingNodeA.isEmpty()) return new Pair<>(new ArrayList<>(), 0.0f);

        var results = remainingNodeA.stream().map(node -> {
            var newRemainingNodeA = new ArrayList<>(remainingNodeA);
            newRemainingNodeA.remove(node);
            var res = recursiveCumulativeSimilarity(distances, newRemainingNodeA);
            return res;
        });
        return new Pair<>(distances, 0.0f);
    }

    public static List<Similarity> highestCumulativeSimilaritySelection(Pair<Graph, Graph> graphs, List<Similarity> distances) {
        var remainingNodeA = distances.stream().filter(distinctByKey(Similarity::nodeA)).map(Similarity::nodeA).toList();

        return recursiveCumulativeSimilarity(distances, remainingNodeA).getValue0();
    }

    public static List<Similarity> stableMarriageSelection(Pair<Graph, Graph> graphs, List<Similarity> distances) {
        var resultDistances = new ArrayList<Similarity>();
        for (var distance : distances) {
            if (resultDistances.stream().anyMatch(rd -> rd.nodeA().equals(distance.nodeA()) || rd.nodeB().equals(distance.nodeB())))
                continue;
            resultDistances.add(distance);
        }
        return resultDistances;
    }
}
