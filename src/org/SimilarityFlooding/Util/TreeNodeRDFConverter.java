/*package org.SimilarityFlooding.Util;

import org.SimilarityFlooding.DataTypes.Graph;
import org.w3c.rdf.model.Model;
import org.w3c.rdf.model.ModelException;
import org.w3c.rdf.model.NodeFactory;
import org.w3c.rdf.model.Resource;
import org.w3c.rdf.util.RDFFactory;
import org.w3c.rdf.util.RDFFactoryImpl;

import java.util.ArrayList;
import java.util.Optional;

public class TreeNodeRDFConverter {
    private static final RDFFactory rf = new RDFFactoryImpl();
    private static final NodeFactory nf = rf.getNodeFactory();

    public static Optional<Model> Convert(Graph<String> graph) {
        Model model = rf.createModel();

        var nodes = new ArrayList<Resource>();
        for (var node : graph.nodes()) {
            try {
                nodes.add(nf.createResource(node));
            } catch (ModelException e) {
                return Optional.empty();
            }
        }

        for (var edge : graph.edges()) {
            var n1 = nodes.stream().filter(node -> {
                        try {
                            return node.getLabel().equals(edge.parent());
                        } catch (ModelException e) {
                            return false;
                        }
                    })
                    .findFirst().orElseThrow();
            var n2 = nodes.stream().filter(n -> {
                try {
                    return n.getLabel().equals(edge.child());
                } catch (ModelException e) {
                    return false;
                }
            }).findFirst().orElseThrow();
            try {
                model.add(nf.createStatement(n1, nf.createResource(edge.relation()), n2));
            } catch (ModelException e) {
                return Optional.empty();
            }
        }
        return Optional.of(model);
    }
}*/
