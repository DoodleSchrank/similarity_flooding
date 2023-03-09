package org.SimilarityFlooding.DataTypes;

import java.util.HashSet;

public record Graph(HashSet<TreeNode> nodes, HashSet<Relation> edges) {}
