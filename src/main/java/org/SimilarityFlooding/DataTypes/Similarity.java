package org.SimilarityFlooding.DataTypes;

import java.util.function.BiFunction;

public abstract class Similarity {
    public double similarity;
    protected double initialSimilarity;
    public double nextRoundSimilarity;
    public double workingSimilarity;

    public TreeNode nodeA() {
        return this.nodeA;
    }

    public TreeNode nodeB() {
        return this.nodeB;
    }

    public double similarity() {
        return this.similarity;
    }

    public double nextRoundSimilarity() {
        return this.nextRoundSimilarity;
    }
    public double initialSimilarity() {
        return this.initialSimilarity;
    }
    public double workingSimilarity() {
        return this.workingSimilarity;
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
