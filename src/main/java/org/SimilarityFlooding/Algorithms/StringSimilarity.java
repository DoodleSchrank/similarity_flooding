package org.SimilarityFlooding.Algorithms;
/*import scenarioCreator.generation.processing.transformations.linguistic.helpers.biglingo.UnifiedLanguageCorpus;
import scenarioCreator.data.Language;
import scenarioCreator.generation.processing.transformations.linguistic.helpers.biglingo.WordNetInterface;
import scenarioCreator.data.primitives.StringPlusNaked;*/
import java.io.IOException;
import java.util.Map;

public final class StringSimilarity {
    public static double Levenshtein(String nodeA, String nodeB) {
        return javax0.levenshtein.Levenshtein.distance(nodeA, nodeB) / 10.0d;
    }
    public static double MongeElkan(String nodeA, String nodeB) {

        /*scenarioCreator.generation.processing.transformations.linguistic.helpers.biglingo.UnifiedLanguageCorpus ulc;
        try {
            ulc = new UnifiedLanguageCorpus(Map.of(Language.English, new WordNetInterface()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ulc.semanticDiff(new StringPlusNaked(nodeA, Language.English), new StringPlusNaked(nodeB, Language.English));*/
        return 1.0d;
    }

    public static double AllEqual(String nodeA, String nodeB) {
        return 1.0d;
    }
}
