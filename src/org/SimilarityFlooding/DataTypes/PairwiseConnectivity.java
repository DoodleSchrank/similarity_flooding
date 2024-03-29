package org.SimilarityFlooding.DataTypes;

import org.utils.Correspondence;

public class PairwiseConnectivity {
    private final AbsoluteSimilarity parents;
    private final AbsoluteSimilarity children;
    private final String relation;

    public float coefficient;
    public float reversecoefficient;
    public PairwiseConnectivity(AbsoluteSimilarity parents, AbsoluteSimilarity children, String relation, float coefficient, float reversecoefficient) {
        this.parents = parents;
        this.children = children;
        this.relation = relation;
        this.coefficient = coefficient;
        this.reversecoefficient = reversecoefficient;
    }
    public PairwiseConnectivity(AbsoluteSimilarity parents, AbsoluteSimilarity children, String relation) {
        this(parents, children, relation, 0.0f, 0.0f);
    }

    public AbsoluteSimilarity parents() { return parents; }
    public AbsoluteSimilarity children() { return children; }

    public boolean hasSameParentsAs(PairwiseConnectivity other) {
        return this.parents.equals(other.parents) &&
                this.relation.equals(other.relation);
    }
    public boolean hasSameChildrenAs(PairwiseConnectivity other) {
        return this.children.equals(other.children) &&
                this.relation.equals(other.relation);
    }
    public boolean includesAsParents(Correspondence other) {
        return this.parents.equals(other);
    }
    public boolean includesAsChildren(Correspondence other) {
        return this.children.equals(other);
    }
    
    @Override
    public String toString() {
        return parents.nodeA().toString() + " - " +
                parents.nodeB().toString() + " < " +
                reversecoefficient + " -- " +
                coefficient + " > " +
                children.nodeA().toString() + " - " +
                children.nodeB().toString();
    }
}
