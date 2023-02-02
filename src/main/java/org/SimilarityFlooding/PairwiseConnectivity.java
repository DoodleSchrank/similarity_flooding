package org.SimilarityFlooding;

public class PairwiseConnectivity {
    private final Similarity parents;
    private final Similarity children;
    private final String relation;

    public float coefficient;
    public float reversecoefficient;
    public PairwiseConnectivity(Similarity parents, Similarity children, String relation, float coefficient, float reversecoefficient) {
        this.parents = parents;
        this.children = children;
        this.relation = relation;
        this.coefficient = coefficient;
        this.reversecoefficient = reversecoefficient;
    }
    public PairwiseConnectivity(Similarity parents, Similarity children, String relation) {
        this(parents, children, relation, 0.0f, 0.0f);
    }

    public Similarity parents() { return parents; }
    public Similarity children() { return children; }

    public boolean hasSameParentsAs(PairwiseConnectivity other) {
        return this.parents.equals(other.parents) &&
                this.relation.equals(other.relation);
    }
    public boolean hasSameChildrenAs(PairwiseConnectivity other) {
        return this.children.equals(other.children) &&
                this.relation.equals(other.relation);
    }
    public boolean includesAsParents(Similarity other) {
        return this.parents.equals(other);
    }
    public boolean includesAsChildren(Similarity other) {
        return this.children.equals(other);
    }
}
