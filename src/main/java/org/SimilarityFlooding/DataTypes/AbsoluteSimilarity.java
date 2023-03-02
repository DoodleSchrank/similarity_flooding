package org.SimilarityFlooding.DataTypes;

import java.util.function.BiFunction;

/**
 * Holds two nodes and a float indicating how similar they are (higher is more).
 * Two objects with the same nodes but different float values are considered to be equal.
 */
public class AbsoluteSimilarity extends Similarity {
    public AbsoluteSimilarity(TreeNode nodeA, TreeNode nodeB, double similarity) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.similarity = similarity;
        this.initialSimilarity = similarity;
        this.similarityN1 = similarity;
    }
    public AbsoluteSimilarity(AbsoluteSimilarity absoluteSimilarity) {
        this.nodeA = absoluteSimilarity.nodeA();
        this.nodeB = absoluteSimilarity.nodeB();
        this.similarity = absoluteSimilarity.similarity;
        this.initialSimilarity = absoluteSimilarity.initialSimilarity;
        this.similarityN1 = absoluteSimilarity.similarityN1;
    }

    public AbsoluteSimilarity(TreeNode nodeA, TreeNode nodeB, BiFunction<String, String, Double> similarityAlgorithm) {
        this(nodeA, nodeB, similarityAlgorithm.apply(nodeA.name(), nodeB.name()));
    }

    @Override
    public String toString() {
        return "" + nodeA.name() + "," + nodeB.name() + " - sim=" + similarity + " nsim=" + similarityN1;
    }
}
