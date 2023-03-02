package org.SimilarityFlooding;

import java.util.function.BiFunction;

public record SFConfig(BiFunction<String, String, Double> similarityAlgorithm,
                       FixpointFormula fixpointFormula) {
}
