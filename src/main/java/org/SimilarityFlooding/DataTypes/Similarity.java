package org.SimilarityFlooding.DataTypes;

public abstract class Similarity {
    public double similarity;
    protected double initialSimilarity;
    public double similarityN1;

    public TreeNode nodeA() {
        return this.nodeA;
    }

    public TreeNode nodeB() {
        return this.nodeB;
    }

    public double similarity() {
        return this.similarity;
    }

    public double similarityN1() {
        return this.similarityN1;
    }
    public double initialSimilarity() {
        return this.initialSimilarity;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Similarity s)) return false;
        return this.nodeA().equals(s.nodeA()) && this.nodeB().equals(s.nodeB());

    }

    @Override
    public int hashCode() {
        return nodeA.hashCode() + nodeB.hashCode();
    }

    protected TreeNode nodeA;
    protected TreeNode nodeB;
}
