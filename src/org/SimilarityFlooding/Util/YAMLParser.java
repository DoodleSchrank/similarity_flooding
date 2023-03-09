package org.SimilarityFlooding.Util;

import org.SimilarityFlooding.DataTypes.Graph;
import org.SimilarityFlooding.DataTypes.Relation;
import org.SimilarityFlooding.DataTypes.TreeNode;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class YAMLParser {
    /**
     * Parses YAML input into a graph.
     *
     * @return Graph containing Nodes and Edges
     */
    public static Optional<Graph> Parse(List<String> schemes) {
        Yaml yaml = new Yaml();
        Map<String, Object> obj;
        try {
            obj = yaml.load(new FileInputStream(schemes.get(0)));
        } catch (FileNotFoundException e) {
            return Optional.empty();
        }
        var schematypes = new ArrayList<>(
                Arrays.asList(
                        new TreeNode("table"),
                        new TreeNode("column"),
                        new TreeNode("columntype")));


        var nodes = new HashSet<>(schematypes);
        var edges = new HashSet<Relation>();
        var datatypes = new HashSet<TreeNode>();


        ArrayList tables = (ArrayList) obj.get("tableSet");
        tables.forEach(tb -> {
            var tb1 = (HashMap<String, HashMap<String, ArrayList<HashMap<String, String>>>>) tb;
            var tb2 = (HashMap<String, ArrayList<HashMap<String, ?>>>) tb;

            // table root
            var root = new TreeNode(tb1
                    .get("name")
                    .get("segmentList")
                    .get(1)
                    .get("token"));
            nodes.add(root);

            tb2.get("columnList").forEach(tC -> {
                var cname = (HashMap<String, HashMap<String, ArrayList<HashMap<String, String>>>>) tC;
                var ctype = (HashMap<String, HashMap<String, String>>) tC;
                var colName = cname.get("name").get("segmentList").get(1).get("token");
                var colType = ctype.get("dataType").get("dataTypeEnum");

                datatypes.add(new TreeNode(colType));

                var colnode = new TreeNode(colName);
                nodes.add(colnode);
                edges.add(new Relation("column", root, colnode));
                edges.add(new Relation("type", colnode, schematypes.get(1)));
                edges.add(new Relation("sqltype", colnode, datatypes.stream()
                        .filter(dt -> dt.name().equals(colType))
                        .toList().get(0)));
            });
        });

        return Optional.of(new Graph(nodes, edges));
    }
}
