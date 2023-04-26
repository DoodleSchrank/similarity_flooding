package org.SimilarityFlooding.DataTypes;

public record Relation<T>(String relation, T parent, T child) {
}
