package org.SimilarityFlooding.DataTypes;

import java.util.function.BiFunction;

/**
 * Holds two nodes and a float indicating how similar they are (higher is more).
 * Two objects with the same nodes but different float values are considered to be equal.
 */
public class AbsoluteSimilarity extends Similarity {
    public AbsoluteSimilarity(TreeNode nodeA, TreeNode nodeB, float similarity) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.similarity = similarity;
    }
    public AbsoluteSimilarity(AbsoluteSimilarity absoluteSimilarity) {
        this.nodeA = absoluteSimilarity.nodeA();
        this.nodeB = absoluteSimilarity.nodeB();
        this.similarity = absoluteSimilarity.similarity;
    }

    public AbsoluteSimilarity(TreeNode nodeA, TreeNode nodeB, BiFunction<TreeNode, TreeNode, Float> similarityAlgorithm) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.similarity = similarityAlgorithm.apply(nodeA, nodeB);
    }

    @Override
    public String toString() {
        return "" + nodeA.name() + " -- " + similarity + " -> " + nodeB.name();
    }
}
