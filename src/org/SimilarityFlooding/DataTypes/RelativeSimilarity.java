package org.SimilarityFlooding.DataTypes;

/**
 * Holds two nodes and a float indicating how similar they are (higher is more).
 * Additionally, a reverse similarity is used as the SelectThreshold operator of the paper dictates.
 * Two objects with the same nodes but different float values are considered to be equal.
 */
public class RelativeSimilarity extends Similarity {
    public Double reverseSimilarity;
    public RelativeSimilarity(TreeNode nodeA, TreeNode nodeB, Double similarity, Double reverseSimilarity) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.similarity = similarity;
        this.reverseSimilarity = reverseSimilarity;
    }
    public RelativeSimilarity(RelativeSimilarity relativeSimilarity) {
        this.nodeA = relativeSimilarity.nodeA();
        this.nodeB = relativeSimilarity.nodeB();
        this.similarity = relativeSimilarity.similarity;
        this.reverseSimilarity = relativeSimilarity.reverseSimilarity;
    }

    public RelativeSimilarity(AbsoluteSimilarity absoluteSimilarity) {
        this.nodeA = absoluteSimilarity.nodeA();
        this.nodeB = absoluteSimilarity.nodeB();
        this.similarity = absoluteSimilarity.similarity;
        this.reverseSimilarity = absoluteSimilarity.similarity;
    }

    public Double reverseSimilarity() { return reverseSimilarity; }
}
