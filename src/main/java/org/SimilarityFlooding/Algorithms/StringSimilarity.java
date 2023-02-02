package org.SimilarityFlooding.Algorithms;

import org.SimilarityFlooding.TreeNode;

public final class StringSimilarity {
    public static float Levenshtein(TreeNode nodeA, TreeNode nodeB) {
        return javax0.levenshtein.Levenshtein.distance(nodeA.name(), nodeB.name()) / 10.f;
    }
}
