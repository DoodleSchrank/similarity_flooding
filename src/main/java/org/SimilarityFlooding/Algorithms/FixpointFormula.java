package org.SimilarityFlooding.Algorithms;

import org.SimilarityFlooding.DataTypes.PairwiseConnectivity;
import org.SimilarityFlooding.DataTypes.Similarity;

import java.util.List;

public class FixpointFormula {
    public static float Basic(List<PairwiseConnectivity> incEdges, List<PairwiseConnectivity> outEdges, List<Similarity> distances, List<Similarity> initialDistances, int index) {
        var incomingSum = (float) incEdges.stream()
                .map(pc -> pc.parents().similarity * pc.coefficient).mapToDouble(Float::doubleValue).sum();
        var outgoingSum = (float) outEdges.stream()
                .map(pc -> pc.children().similarity * pc.reversecoefficient).mapToDouble(Float::doubleValue).sum();

        return distances.get(index).similarity + incomingSum + outgoingSum;
    }

    public static float A(List<PairwiseConnectivity> incEdges, List<PairwiseConnectivity> outEdges, List<Similarity> distances, List<Similarity> initialDistances, int index) {
        var incomingSum = (float) incEdges.stream()
                .map(pc -> pc.parents().similarity * pc.coefficient).mapToDouble(Float::doubleValue).sum();
        var outgoingSum = (float) outEdges.stream()
                .map(pc -> pc.children().similarity * pc.reversecoefficient).mapToDouble(Float::doubleValue).sum();

        return initialDistances.get(index).similarity + incomingSum + outgoingSum;
    }

    public static float B(List<PairwiseConnectivity> incEdges, List<PairwiseConnectivity> outEdges, List<Similarity> distances, List<Similarity> initialDistances, int index) {
        var incomingSum = (float) incEdges.stream()
                .map(pc -> (pc.parents().similarity
                        + initialDistances.stream().filter(init_dist -> init_dist.equals(pc.parents())).findFirst().get().similarity)
                        * pc.coefficient).mapToDouble(Float::doubleValue).sum();
        var outgoingSum = (float) outEdges.stream()
                .map(pc -> (pc.children().similarity
                        + initialDistances.stream().filter(init_dist -> init_dist.equals(pc.children())).findFirst().get().similarity)
                        * pc.reversecoefficient).mapToDouble(Float::doubleValue).sum();

        return incomingSum + outgoingSum;
    }

    public static float C(List<PairwiseConnectivity> incEdges, List<PairwiseConnectivity> outEdges, List<Similarity> distances, List<Similarity> initialDistances, int index) {
        var incomingSum = (float) incEdges.stream()
                .map(pc -> (pc.parents().similarity
                        + initialDistances.stream().filter(init_dist -> init_dist.equals(pc.parents())).findFirst().get().similarity)
                        * pc.coefficient).mapToDouble(Float::doubleValue).sum();
        var outgoingSum = (float) outEdges.stream()
                .map(pc -> (pc.children().similarity
                        + initialDistances.stream().filter(init_dist -> init_dist.equals(pc.children())).findFirst().get().similarity)
                        * pc.reversecoefficient).mapToDouble(Float::doubleValue).sum();

        return initialDistances.get(index).similarity + distances.get(index).similarity + incomingSum + outgoingSum;
    }
}
