package org.SimilarityFlooding.DataTypes;

import org.utils.Correspondence;
import java.util.function.BiFunction;

/**
 * Holds two nodes and a float indicating how similar they are (higher is more).
 * Two objects with the same nodes but different float values are considered to be equal.
 */
public class AbsoluteSimilarity<T> extends Correspondence<T> {
    protected double initialSimilarity;
    public double similarityN1;
    public double similarityN1() {
        return this.similarityN1;
    }
    public double initialSimilarity() {
        return this.initialSimilarity;
    }
    public AbsoluteSimilarity(T nodeA, T nodeB, double similarity) {
        super(nodeA, nodeB, similarity);
        this.initialSimilarity = similarity;
        this.similarityN1 = similarity;
    }
    public AbsoluteSimilarity(AbsoluteSimilarity<T> absoluteSimilarity, float similarity) {
        super(absoluteSimilarity.nodeA(), absoluteSimilarity.nodeB(), similarity);
        this.similarity = similarity;
        this.initialSimilarity = similarity;
        this.similarityN1 = similarity;
    }

    public AbsoluteSimilarity(T nodeA, T nodeB, BiFunction<String, String, Double> similarityAlgorithm) {
        this(nodeA, nodeB, similarityAlgorithm.apply(nodeA.toString(), nodeB.toString()));
    }

    @Override
    public String toString() {
        return "" + nodeA.toString() + "," + nodeB.toString() + " - sim=" + similarity + " nsim=" + similarityN1;
    }
}
