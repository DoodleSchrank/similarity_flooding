package org.SimilarityFlooding.Util;

import org.SimilarityFlooding.DataTypes.Graph;
import org.SimilarityFlooding.DataTypes.Relation;
import org.SimilarityFlooding.DataTypes.TreeNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.IntStream;

public class CSVParser {
    /**
     * Parses CSV input into a graph.
     *
     * @return Pair of Nodes and Edges
     */
    public static Optional<Graph> Parse(List<String> schemes) {
        BufferedReader reader;
        List<String> columns;
        List<String> types;
        var uri = schemes.get(0);
        // Split CSV by ',' and read out column headers and types in the second row
        try {
            reader = new BufferedReader(new FileReader(uri));
            columns = Arrays.stream(reader.readLine()
                    .replace("\"", "")
                    .split(",")).toList();
            types = Arrays.stream(reader.readLine()
                    .replace("\"", "")
                    .split(",")).toList();
        } catch (Exception e) {
            return Optional.empty();
        }

        var edges = new HashSet<Relation>();

        // static schema types
        List<TreeNode> schematypes = new ArrayList<>(
                Arrays.asList(
                        new TreeNode("table"),
                        new TreeNode("column"),
                        new TreeNode("columntype")));
        var nodes = new HashSet<>(schematypes);
        // generate datatypes based on unique values for type in the CSV
        List<TreeNode> datatypes = new ArrayList<>();
        types.stream().distinct().forEach(type -> datatypes.add(new TreeNode(type)));
        datatypes.forEach(dt -> edges.add(new Relation("type", dt, schematypes.get(2))));
        nodes.addAll(datatypes);

        // root node
        var table = new TreeNode(uri);
        nodes.add(table);
        edges.add(new Relation("type", table, schematypes.get(0)));

        // columns
        IntStream.range(0, columns.size()).forEach(col -> {
            var colnode = new TreeNode(columns.get(col));
            nodes.add(colnode);
            edges.add(new Relation("column", table, colnode));
            edges.add(new Relation("type", colnode, schematypes.get(1)));
            edges.add(new Relation("sqltype", colnode, datatypes.stream()
                    .filter(dt -> dt.name().equals(types.get(col)))
                    .toList().get(0)));
        });

        return Optional.of(new Graph(nodes, edges));
    }
}
