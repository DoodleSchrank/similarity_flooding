package org.SimilarityFlooding.Algorithms;

import org.SimilarityFlooding.DataTypes.Graph;
import org.SimilarityFlooding.DataTypes.Relation;
import org.javatuples.Pair;
import org.utils.Correspondence;

import java.util.*;

public final class Filter {
    private static final Map<String, String> StaticMappings = new HashMap<>();

    static {
        StaticMappings.put("int", "int");
        StaticMappings.put("string", "string");
        StaticMappings.put("date", "date");
        StaticMappings.put("varchar", "string");
    }

    public static final Map<String, String> Knowledge = Collections.unmodifiableMap(StaticMappings);

    public static List<Correspondence<String>> knowledgeFilter(List<Correspondence<String>> correspondences) {
        return knowledgeFilter(correspondences, StaticMappings);
    }

    public static <T> List<Correspondence<T>> knowledgeFilter(List<Correspondence<T>> correspondences, Map<String, String> knowledge) {
        var filteredDistances = new ArrayList<Correspondence<T>>();
        for (var dist : correspondences) {
            var nodeA = Objects.toString(knowledge.get(dist.nodeA().toString()),
                    knowledge.containsValue(dist.nodeA().toString()) ? dist.nodeA().toString() : null);
            var nodeB = Objects.toString(knowledge.get(dist.nodeB().toString()),
                    knowledge.containsValue(dist.nodeB().toString()) ? dist.nodeB().toString() : null);

            if (nodeA == null && nodeB == null)
                filteredDistances.add(dist);
            else if (nodeA != null && nodeA.equals(nodeB)) {
                filteredDistances.add(new Correspondence<>(dist, 1.0f));
            }
        }

        filteredDistances.sort(Comparator.comparingDouble(Correspondence::similarity));
        Collections.reverse(filteredDistances);
        return filteredDistances;
    }

    public static <T> List<Correspondence<T>> typingConstraintFilter(Pair<Graph<T>, Graph<T>> graphs, List<Correspondence<T>> distances) {
        var filteredDistances = new ArrayList<>(distances);

        var typedNodes1 = graphs.getValue0().edges().stream().filter(edge -> edge.relation().equals("sqltype")).map(Relation::parent).toList();
        var typedNodes2 = graphs.getValue1().edges().stream().filter(edge -> edge.relation().equals("sqltype")).map(Relation::parent).toList();
        // remove all distances where at least one of the nodes doesn't have a sql type
        //TODO flip to add distances instead of removing them
        filteredDistances.removeIf(dist ->
                !typedNodes1.contains(dist.nodeA())
                        ||
                        !typedNodes2.contains(dist.nodeB()));
        return filteredDistances;
    }

    public static <T> List<Correspondence<T>> cardinalityConstraintFilter(List<Correspondence<T>> distances) {
        var filteredDistances = new ArrayList<>(distances);
        //TODO flip to add distances instead of removing them
        filteredDistances.removeIf(distance -> distances.stream()
                .map(dist -> distances.stream()
                        .filter(d -> d.nodeB().equals(dist.nodeB()))
                        .max(Comparator.comparingDouble(Correspondence::similarity)).orElse(null))
                .noneMatch(distance::equals));
        return filteredDistances;
    }
}

