package org.SimilarityFlooding.Algorithms;

import org.SimilarityFlooding.DataTypes.Graph;
import org.SimilarityFlooding.DataTypes.Relation;
import org.SimilarityFlooding.DataTypes.AbsoluteSimilarity;
import org.javatuples.Pair;

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

    public static List<AbsoluteSimilarity> knowledgeFilter(List<AbsoluteSimilarity> distances) {
        return knowledgeFilter(distances, StaticMappings);
    }

    public static List<AbsoluteSimilarity> knowledgeFilter(List<AbsoluteSimilarity> distances, Map<String, String> knowledge) {
        var filteredDistances = new ArrayList<AbsoluteSimilarity>();
        for (var dist : distances) {
            var nodeA = Objects.toString(knowledge.get(dist.nodeA().name()),
                    knowledge.containsValue(dist.nodeA().name()) ? dist.nodeA().name() : null);
            var nodeB = Objects.toString(knowledge.get(dist.nodeB().name()),
                    knowledge.containsValue(dist.nodeB().name()) ? dist.nodeB().name() : null);

            if (nodeA == null && nodeB == null)
                filteredDistances.add(dist);
            else if (nodeA != null && nodeA.equals(nodeB)) {
                filteredDistances.add(new AbsoluteSimilarity(dist.nodeA(), dist.nodeB(), 1.0f));
            }
        }

        filteredDistances.sort(Comparator.comparingDouble(AbsoluteSimilarity::similarity).reversed());
        return filteredDistances;
    }

    public static List<AbsoluteSimilarity> typingConstraintFilter(Pair<Graph, Graph> graphs, List<AbsoluteSimilarity> distances) {
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

    public static List<AbsoluteSimilarity> cardinalityConstraintFilter(List<AbsoluteSimilarity> distances) {
        var filteredDistances = new ArrayList<>(distances);
        //TODO flip to add distances instead of removing them
        filteredDistances.removeIf(distance -> distances.stream()
                .map(dist -> distances.stream()
                        .filter(d -> d.nodeB().equals(dist.nodeB()))
                        .max(Comparator.comparingDouble(AbsoluteSimilarity::similarity)).get())
                .noneMatch(distance::equals));
        return filteredDistances;
    }
}

