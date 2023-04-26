package org.SimilarityFlooding;

import org.SimilarityFlooding.Algorithms.*;
import org.SimilarityFlooding.DataTypes.*;
import org.SimilarityFlooding.Util.*;
import org.javatuples.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class Main {
    private static Pair<Graph<String>, Graph<String>> SimpleExample() {
        var nodes = new HashSet<String>();
        nodes.add("first");
        nodes.add("second");
        nodes.add("third");
        nodes.add("fourth");
        var relations = new HashSet<Relation<String>>();
        var nodearr = nodes.toArray(new String[0]);
        relations.add(new Relation<>("testingRel", nodearr[0], nodearr[1]));
        relations.add(new Relation<>("otherRel", nodearr[1], nodearr[2]));
        relations.add(new Relation<>("otherRel", nodearr[1], nodearr[3]));
        var g1 = new Graph<>(new HashSet<>(nodes), new HashSet<>(relations));

        nodes = new HashSet<>();
        nodes.add("erster");
        nodes.add("zweiter");
        nodes.add("dritter");
        nodes.add("vierter");
        relations = new HashSet<>();
        nodearr = nodes.toArray(new String[0]);
        relations.add(new Relation<>("testingRel", nodearr[0], nodearr[1]));
        relations.add(new Relation<>("otherRel", nodearr[1], nodearr[2]));
        relations.add(new Relation<>("thirdRel", nodearr[1], nodearr[3]));
        var g2 = new Graph<>(nodes, relations);
        return new Pair<>(g1, g2);
    }

    private static Pair<Graph<String>, Graph<String>> ICDE02Example() {
        var nodes = new HashSet<String>();
        nodes.add("a");
        nodes.add("a1");
        nodes.add("a2");
        var nodearr = nodes.toArray(new String[0]);
        var relations = new HashSet<Relation<String>>();
        relations.add(new Relation<>("l1", nodearr[1], nodearr[0]));
        relations.add(new Relation<>("l1", nodearr[1], nodearr[2]));
        relations.add(new Relation<>("l2", nodearr[0], nodearr[2]));
        var g1 = new Graph<>(nodes, relations);


        nodes = new HashSet<>();
        nodes.add("b");
        nodes.add("b1");
        nodes.add("b2");
        nodearr = nodes.toArray(new String[0]);
        relations = new HashSet<>();
        relations.add(new Relation<>("l1", nodearr[1], nodearr[2]));
        relations.add(new Relation<>("l2", nodearr[1], nodearr[0]));
        relations.add(new Relation<>("l2", nodearr[0], nodearr[2]));
        var g2 = new Graph<>(nodes, relations);

        return new Pair<>(g1, g2);
    }

    public static void main(String[] args) throws Exception {
        var input = 0;
        Graph<String> g1 = null;
        Graph<String> g2 = null;
        switch (input) {
            case 0 -> {
                g1 = CSVParser.Parse(List.of(new String[]{"src/test_data/data1.csv"})).orElseThrow();
                g2 = CSVParser.Parse(List.of(new String[]{"src/test_data/data2.csv"})).orElseThrow();
            }
            case 1 -> {
                var graphs = ICDE02Example();
                g1 = graphs.getValue0();
                g2 = graphs.getValue1();
            }
            case 2 -> {
                var graphs = SimpleExample();
                g1 = graphs.getValue0();
                g2 = graphs.getValue1();
            }
            case 3 -> {
                g1 = YAMLParser.Parse("src/test_data/schema.yaml").orElseThrow();
                g2 = YAMLParser.Parse("src/test_data/schema.yaml").orElseThrow();
            }
        }

        var sf = new SimilarityFlooding(g1, g2, new SFConfig(StringSimilarity::AllEqual, FixpointFormula.Basic));
        //sf.test();
        sf.run(1, 0.05f);
        var distances = sf.getCorrespondants();
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