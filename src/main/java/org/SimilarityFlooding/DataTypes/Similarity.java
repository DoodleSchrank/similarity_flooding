package org.SimilarityFlooding.DataTypes;

import java.util.function.BiFunction;

public class Similarity implements Comparable<Similarity> {

    public float similarity;

    public Similarity(TreeNode nodeA, TreeNode nodeB, float similarity) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.similarity = similarity;
    }
    public Similarity(Similarity similarity) {
        this.nodeA = similarity.nodeA();
        this.nodeB = similarity.nodeB();
        this.similarity = similarity.similarity;
    }

    public Similarity(TreeNode nodeA, TreeNode nodeB, BiFunction<TreeNode, TreeNode, Float> similarityAlgorithm) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.similarity = similarityAlgorithm.apply(nodeA, nodeB);
    }

    public TreeNode nodeA() {
        return this.nodeA;
    }

    public TreeNode nodeB() {
        return this.nodeB;
    }

    public float similarity() {
        return this.similarity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Similarity) {
            return ((Similarity) obj).nodeA().equals(this.nodeA()) && ((Similarity) obj).nodeB().equals(this.nodeB()) ||
                    ((Similarity) obj).nodeB().equals(this.nodeA()) && ((Similarity) obj).nodeA().equals(this.nodeB());
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Similarity similarity) {
        return Math.round(Math.signum(this.similarity - similarity.similarity));
    }

    private final TreeNode nodeA;
    private final TreeNode nodeB;
}
