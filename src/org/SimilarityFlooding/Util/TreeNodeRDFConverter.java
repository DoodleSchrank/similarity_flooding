package org.SimilarityFlooding.Util;

import org.SimilarityFlooding.DataTypes.Graph;
import org.sf_ref.rdf.model.Model;
import org.sf_ref.rdf.model.ModelException;
import org.sf_ref.rdf.model.NodeFactory;
import org.sf_ref.rdf.model.Resource;
import org.sf_ref.rdf.util.RDFFactory;
import org.sf_ref.rdf.util.RDFFactoryImpl;

import java.util.ArrayList;
import java.util.Optional;

public class TreeNodeRDFConverter {
    private static final RDFFactory rf = new RDFFactoryImpl();
    private static final NodeFactory nf = rf.getNodeFactory();

    public static Optional<Model> Convert(Graph graph) {
        Model model = rf.createModel();

        var nodes = new ArrayList<Resource>();
        for (var node : graph.nodes()) {
            try {
                nodes.add(nf.createResource(node.name()));
            } catch (ModelException e) {
                return Optional.empty();
            }
        }

        for (var edge : graph.edges()) {
            var n1 = nodes.stream().filter(node -> {
                        try {
                            return node.getLabel().equals(edge.parent().name());
                        } catch (ModelException e) {
                            return false;
                        }
                    })
                    .findFirst().orElseThrow();
            var n2 = nodes.stream().filter(n -> {
                try {
                    return n.getLabel().equals(edge.child().name());
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
}
