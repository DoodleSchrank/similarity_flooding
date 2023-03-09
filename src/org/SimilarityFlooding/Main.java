package org.SimilarityFlooding;

import org.SimilarityFlooding.Algorithms.Filter;
import org.SimilarityFlooding.Algorithms.Selector;
import org.SimilarityFlooding.Util.CSVParser;
import org.SimilarityFlooding.Algorithms.StringSimilarity;
import org.SimilarityFlooding.DataTypes.*;
import org.SimilarityFlooding.Util.TreeNodeRDFConverter;
import org.SimilarityFlooding.Util.YAMLParser;
import org.sf_ref.interdataworking.mm.alg.MapPair;
import org.sf_ref.interdataworking.mm.alg.Match;
import org.javatuples.Pair;

import java.util.*;

public class Main {
    private static Optional<Pair<Graph, Graph>> SimpleExample() {
        var nodes = new HashSet<TreeNode>();
        nodes.add(new TreeNode("first"));
        nodes.add(new TreeNode("second"));
        nodes.add(new TreeNode("third"));
        nodes.add(new TreeNode("fourth"));
        var relations = new HashSet<Relation>();
        var nodearr = nodes.toArray(new TreeNode[0]);
        relations.add(new Relation("testingRel", nodearr[0], nodearr[1]));
        relations.add(new Relation("otherRel", nodearr[1], nodearr[2]));
        relations.add(new Relation("otherRel", nodearr[1], nodearr[3]));
        var g1 = new Graph(new HashSet<>(nodes), new HashSet<>(relations));

        nodes = new HashSet<>();
        nodes.add(new TreeNode("erster"));
        nodes.add(new TreeNode("zweiter"));
        nodes.add(new TreeNode("dritter"));
        nodes.add(new TreeNode("vierter"));
        relations = new HashSet<>();
        nodearr = nodes.toArray(new TreeNode[0]);
        relations.add(new Relation("testingRel", nodearr[0], nodearr[1]));
        relations.add(new Relation("otherRel", nodearr[1], nodearr[2]));
        relations.add(new Relation("thirdRel", nodearr[1], nodearr[3]));
        var g2 = new Graph(nodes, relations);
        return Optional.of(new Pair<>(g1, g2));
    }

    private static Optional<Pair<Graph, Graph>> ICDE02Example() {
        var nodes = new HashSet<TreeNode>();
        nodes.add(new TreeNode("a"));
        nodes.add(new TreeNode("a1"));
        nodes.add(new TreeNode("a2"));
        var nodearr = nodes.toArray(new TreeNode[0]);
        var relations = new HashSet<Relation>();
        relations.add(new Relation("l1", nodearr[1], nodearr[0]));
        relations.add(new Relation("l1", nodearr[1], nodearr[2]));
        relations.add(new Relation("l2", nodearr[0], nodearr[2]));
        var g1 = new Graph(nodes, relations);


        nodes = new HashSet<>();
        nodes.add(new TreeNode("b"));
        nodes.add(new TreeNode("b1"));
        nodes.add(new TreeNode("b2"));
        nodearr = nodes.toArray(new TreeNode[0]);
        relations = new HashSet<>();
        relations.add(new Relation("l1", nodearr[1], nodearr[2]));
        relations.add(new Relation("l2", nodearr[1], nodearr[0]));
        relations.add(new Relation("l2", nodearr[0], nodearr[2]));
        var g2 = new Graph(nodes, relations);

        return Optional.of(new Pair<>(g1, g2));
    }

    public static void main(String[] args) throws Exception {
        var input = 3;
        Graph g1 = null;
        Graph g2 = null;
        switch (input) {
            case 0 -> {
                g1 = CSVParser.Parse(List.of(new String[]{"src/test_data/data1.csv"})).orElseThrow();
                g2 = CSVParser.Parse(List.of(new String[]{"src/test_data/data2.csv"})).orElseThrow();
            }
            case 1 -> {
                var graphs = ICDE02Example().orElseThrow();
                g1 = graphs.getValue0();
                g2 = graphs.getValue1();
            }
            case 2 -> {
                var graphs = SimpleExample().orElseThrow();
                g1 = graphs.getValue0();
                g2 = graphs.getValue1();
            }
            case 3 -> {
                g1 = YAMLParser.Parse(List.of(new String[]{"src/test_data/schema.yaml"})).orElseThrow();
                g2 = YAMLParser.Parse(List.of(new String[]{"src/test_data/schema.yaml"})).orElseThrow();
            }
        }

        var sf = new SimilarityFlooding(g1, g2, new SFConfig(StringSimilarity::AllEqual, FixpointFormula.Basic));
        //sf.test();
        sf.run(1, 0.05f);
        var distances = sf.getDistances();
        var graphs = sf.getGraphs();

        //var knowledge = Filter.Knowledge;
        //var ownKnowledge = new HashMap<>(knowledge);
        //ownKnowledge.put("first", "erster");
        //distances.forEach(dist -> System.out.printf("%f | %s   %s%n", dist.similarity(), dist.nodeA().name(), dist.nodeB().name()));
        //System.out.printf("\n\n\n");
        distances = Filter.knowledgeFilter(distances);
        //distances = Filter.typingConstraintFilter(graphs, distances);
        //distances = Filter.cardinalityConstraintFilter(distances);
        distances = Selector.stableMarriageSelection(distances);
        //var otherdistances = Selector.stableMarriageSelection(graphs, distances);
        //distances = Selector.highestCumulativeSimilaritySelection(graphs, distances);

        //System.out.print("\n\nCumulative\n");
        //System.out.print("\n\nNo Filter/Selector\n");
        //distances.forEach(dist -> System.out.printf("%f | %s   %s%n", dist.similarity(), dist.nodeA().name(), dist.nodeB().name()));
        //System.out.print("\n\nStable Marriage\n");
        //otherdistances.forEach(dist -> System.out.printf("%f | %s   %s%n", dist.similarity(), dist.nodeA().name(), dist.nodeB().name()));

        /*var G1 = TreeNodeRDFConverter.Convert(g1).orElseThrow();
        var G2 = TreeNodeRDFConverter.Convert(g2).orElseThrow();
        var matcher = new Match(G1, G2, new SFConfig(StringSimilarity::AllEqual, FixpointFormula.Basic));
        matcher.formula = Match.FORMULA_FFT;
        matcher.FLOW_GRAPH_TYPE = Match.FG_PRODUCT;
        var res = matcher.getMatch();
        MapPair.sort(res);
        Match.dump(res);
        Match.main(null);*/

    }
}