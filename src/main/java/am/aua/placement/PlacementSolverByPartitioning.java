package am.aua.placement;

import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.objective.PlacementObjective;
import am.aua.placement.partitioning.PartitioningAlgorithm;

/**
 * Created by sparik on 2/11/18.
 */
public class PlacementSolverByPartitioning implements PlacementSolver {

    private PartitioningAlgorithm algorithm;
    private PlacementObjective objective;


    public PlacementSolverByPartitioning(PlacementObjective objective, PartitioningAlgorithm algorithm) {
        this.algorithm = algorithm;
        this.objective = objective;
    }

    public PlacementResult solve(Iterable<Long> modules, Iterable<Net> nets, int H, int W) {
        return null;
    }


}
