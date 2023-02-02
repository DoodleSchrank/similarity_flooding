package org.SimilarityFlooding;

import java.util.HashSet;

record Graph(HashSet<TreeNode> nodes, HashSet<Relation> edges) {}
record Relation(String relation, TreeNode parent, TreeNode child) {}
