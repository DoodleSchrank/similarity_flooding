package org.SimilarityFlooding.Algorithms;

import org.MongeElkan.*;
import org.SimilarityFlooding.DataTypes.TreeNode;

import java.io.IOException;
import java.util.Map;

public final class StringSimilarity {
    public static double Levenshtein(TreeNode nodeA, TreeNode nodeB) {
        return javax0.levenshtein.Levenshtein.distance(nodeA.name(), nodeB.name()) / 10.0d;
    }


    public static double MongeElkan(TreeNode nodeA, TreeNode nodeB) {
        UnifiedLanguageCorpus ulc;
        try {
            ulc = new UnifiedLanguageCorpus(Map.of(Language.English, new WordNetInterface()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (float) ulc.semanticDiff(new StringPlusNaked(nodeA.name(), Language.English), new StringPlusNaked(nodeB.name(), Language.English));
    }

    public static double AllEqual(TreeNode nodeA, TreeNode nodeB) {
        return 1.0d;
    }
}
