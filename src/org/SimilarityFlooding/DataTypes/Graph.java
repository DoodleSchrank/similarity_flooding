package org.SimilarityFlooding.DataTypes;

import java.util.HashSet;

public record Graph<T>(HashSet<T> nodes, HashSet<Relation<T>> edges) {}
