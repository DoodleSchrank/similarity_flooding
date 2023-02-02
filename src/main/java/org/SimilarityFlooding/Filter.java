package org.SimilarityFlooding;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public final class Filter {
    private static final HashMap<String, String> staticMappings = new HashMap<>();

    static {
        staticMappings.put("int", "int");
        staticMappings.put("string", "string");
        staticMappings.put("date", "date");
        staticMappings.put("varchar", "string");
    }

    public static List<Similarity> knowledgeFilter(Pair<Graph, Graph> graphs, List<Similarity> distances) {
        var filteredDistances = new ArrayList<Similarity>();
        for (var dist : distances) {
            var x = staticMappings.get(dist.nodeA().name());
            var y = staticMappings.get(dist.nodeB().name());

            if (x == null && y == null) filteredDistances.add(dist);
            else if (!(x == null || y == null) && x.equals(y))
                filteredDistances.add(new Similarity(dist.nodeA(), dist.nodeB(), 1.0f));
        }

        return filteredDistances;
    }

    public static List<Similarity> typingConstraintFilter(Pair<Graph, Graph> graphs, List<Similarity> distances) {
        var filteredDistances = new ArrayList<>(distances);

        var typedNodes1 = graphs.getValue0().edges().stream().filter(edge -> edge.relation().equals("sqltype")).map(Relation::parent).toList();
        var typedNodes2 = graphs.getValue1().edges().stream().filter(edge -> edge.relation().equals("sqltype")).map(Relation::parent).toList();
        // remove all distances where one of the nodes doesn't have a sql type
        //TODO flip to add distances instead of removing them
        filteredDistances.removeIf(dist ->
                !typedNodes1.contains(dist.nodeA())
                        ||
                        !typedNodes2.contains(dist.nodeB()));
        return filteredDistances;
    }

    public static List<Similarity> cardinalityConstraintFilter(Pair<Graph, Graph> graphs, List<Similarity> distances) {
        var filteredDistances = new ArrayList<>(distances);
        //TODO flip to add distances instead of removing them
        filteredDistances.removeIf(distance -> distances.stream()
                .map(dist -> distances.stream()
                        .filter(d -> d.nodeB().equals(dist.nodeB()))
                        .max(Comparator.comparingDouble(Similarity::similarity)).get())
                .noneMatch(distance::equals));
        return filteredDistances;
    }

}

