package org.SimilarityFlooding;

public final class SimilarityAlgorithm {
    public static float Levenshtein(TreeNode nodeA, TreeNode nodeB) {
        return javax0.levenshtein.Levenshtein.distance(nodeA.name(), nodeB.name()) / 10.f;
    }
}
