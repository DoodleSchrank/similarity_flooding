package org.SimilarityFlooding.DataTypes;

import java.util.List;

public record Graph<T>(List<T> nodes, List<Relation<T>> edges) {}
