package am.aua.placement;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.objective.PlacementObjective;
import am.aua.placement.objective.TotalWirelengthObjective;
import am.aua.placement.partitioning.PartitionSolver;
import am.aua.placement.partitioning.PartitioningAlgorithm;
import am.aua.placement.partitioning.fm.FMPartitionSolver;
import am.aua.placement.partitioning.kl.KLPartitionSolver;

/**
 * Created by sparik on 2/11/18.
 */
public class PlacementSolverByPartitioning implements PlacementSolver {

    private PartitioningAlgorithm algorithm;
    private PlacementObjective objective;
    private PartitionSolver partitionSolver;

    public PlacementSolverByPartitioning(PlacementObjective objective, PartitionSolver partitionSolver) {
        this.objective = objective;
        this.partitionSolver = partitionSolver;
    }

    public PlacementSolverByPartitioning(PlacementObjective objective, PartitioningAlgorithm algorithm) {
        this(objective, PlacementSolverByPartitioning.getPartitionSolverInstance(algorithm));
    }

    public PlacementSolverByPartitioning() {
        this(TotalWirelengthObjective.getInstance(), PartitioningAlgorithm.KERNIGHAN_LEE);
    }

    public PlacementResult solve(Iterable<Module> modules, Iterable<Net> nets, int H, int W) {
        return null;
    }


    private static PartitionSolver getPartitionSolverInstance(PartitioningAlgorithm algorithm) {

        PartitionSolver result;

        switch (algorithm) {
            case KERNIGHAN_LEE: result = new KLPartitionSolver();
                break;
            case FIDUCCIA_MATTHEYSES: result = new FMPartitionSolver(1.0);
                break;
            default: result = null;
                break;
        }

        return result;
    }
}
