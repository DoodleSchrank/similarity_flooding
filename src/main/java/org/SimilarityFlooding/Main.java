package org.SimilarityFlooding;
import org.SimilarityFlooding.Algorithms.FixpointFormula;
import org.SimilarityFlooding.Algorithms.Selector;
import org.SimilarityFlooding.Algorithms.StringSimilarity;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        //Graph g1 = CSVParser.Parse("src/test_data/data1.csv").get();
        //Graph g2 = CSVParser.Parse("src/test_data/data2.csv").get();

        // fake it till you make it
        var nodes = new ArrayList<TreeNode>();
        nodes.add(new TreeNode("first"));
        nodes.add(new TreeNode("second"));
        nodes.add(new TreeNode("third"));
        nodes.add(new TreeNode("fourth"));
        var relations = new ArrayList<Relation>();
        var nodearr = nodes.toArray(new TreeNode[nodes.size()]);
        relations.add(new Relation("testingRel", nodearr[0], nodearr[1]));
        relations.add(new Relation("otherRel", nodearr[1], nodearr[2]));
        relations.add(new Relation("otherRel", nodearr[1], nodearr[3]));
        var g1 = new Graph(new HashSet<>(nodes), new HashSet<>(relations));

        nodes = new ArrayList<>();
        nodes.add(new TreeNode("erster"));
        nodes.add(new TreeNode("zweiter"));
        nodes.add(new TreeNode("dritter"));
        nodes.add(new TreeNode("vierter"));
        relations = new ArrayList<>();
        nodearr = nodes.toArray(new TreeNode[nodes.size()]);
        relations.add(new Relation("testingRel", nodearr[0], nodearr[1]));
        relations.add(new Relation("otherRel", nodearr[1], nodearr[2]));
        relations.add(new Relation("thirdRel", nodearr[1], nodearr[3]));
        var g2 = new Graph(new HashSet<>(nodes), new HashSet<>(relations));


        var sf = new SimilarityFlooding(g1, g2, new SFConfig(StringSimilarity::Levenshtein, FixpointFormula::Basic));
        sf.run(50);
        var distances = sf.getDistances();
        var graphs = sf.getGraphs();

        //distances.forEach(dist -> System.out.printf("%f | %s   %s%n", dist.similarity(), dist.nodeA().name(), dist.nodeB().name()));
        //System.out.printf("\n\n\n");
        //distances = Filter.knowledgeFilter(graphs, distances);
        //distances = Filter.typingConstraintFilter(graphs, distances);
        //distances = Filter.cardinalityConstraintFilter(graphs, distances);
        distances = Selector.stableMarriageSelection(graphs, distances);

        distances.forEach(dist -> System.out.printf("%f | %s   %s%n", dist.similarity(), dist.nodeA().name(), dist.nodeB().name()));

    }
}