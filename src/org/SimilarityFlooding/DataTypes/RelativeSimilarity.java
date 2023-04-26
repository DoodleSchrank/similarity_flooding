package org.SimilarityFlooding.DataTypes;

import org.utils.Correspondence;

/**
 * Holds two nodes and a float indicating how similar they are (higher is more).
 * Additionally, a reverse similarity is used as the SelectThreshold operator of the paper dictates.
 * Two objects with the same nodes but different float values are considered to be equal.
 */
public class RelativeSimilarity<T> extends Correspondence<T> {
    public Double reverseSimilarity;
    public RelativeSimilarity(T nodeA, T nodeB, Double similarity, Double reverseSimilarity) {
        super(nodeA, nodeB, similarity);
        this.reverseSimilarity = reverseSimilarity;
    }
    public RelativeSimilarity(RelativeSimilarity<T> relativeSimilarity) {
        super(relativeSimilarity.nodeA(), relativeSimilarity.nodeB(), relativeSimilarity.similarity());
        this.reverseSimilarity = relativeSimilarity.reverseSimilarity;
    }

    public RelativeSimilarity(Correspondence<T> absoluteSimilarity) {
        super(absoluteSimilarity.nodeA(), absoluteSimilarity.nodeB(), absoluteSimilarity.similarity());
        this.reverseSimilarity = absoluteSimilarity.similarity;
    }

    public Double reverseSimilarity() { return reverseSimilarity; }
}
