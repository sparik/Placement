package am.aua.placement;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.entity.Slot;
import am.aua.placement.objective.PlacementObjective;
import am.aua.placement.objective.TotalWirelengthObjective;
import am.aua.placement.partitioning.ModulePartition;
import am.aua.placement.partitioning.PartitionBlock;
import am.aua.placement.partitioning.PartitionSolver;
import am.aua.placement.partitioning.PartitioningAlgorithm;
import am.aua.placement.partitioning.fm.FMPartitionSolver;
import am.aua.placement.partitioning.kl.KLPartitionSolver;

import java.util.Collection;

/**
 * Created by sparik on 2/11/18.
 */
public class PlacementSolverByPartitioning implements PlacementSolver {

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

    private static PartitionSolver getPartitionSolverInstance(PartitioningAlgorithm algorithm) {

        PartitionSolver result;

        switch (algorithm) {
            case KERNIGHAN_LEE:
                result = new KLPartitionSolver();
                break;
            case FIDUCCIA_MATTHEYSES:
                result = new FMPartitionSolver();
                break;
            default:
                result = null;
                break;
        }

        return result;
    }

    public PlacementResult solve(Collection<Module> modules, Collection<Net> nets, int height, int width) {

        // TODO reduce nets or not


        if (modules.size() != height * width) {
            throw new IllegalArgumentException("vat es ara");
        }

        PlacementResult result = new PlacementResult();

        rec(modules, nets, result, 0, 0, height, width);

        return result;
    }

    // place modules in the rectangle bounded by (lx, ly) and (lx + h - 1, rx + w - 1) (both inclusive), and save the result
    // in placement
    private void rec(Collection<Module> modules, Collection<Net> nets, PlacementResult placement, int lx, int ly, int h, int w) {
        if (modules.size() == 1) {
            placement.setSlotForModule(modules.iterator().next(), new Slot(lx, ly));
            return;
        }

        int firstPartSize, secondPartSize;

        if (h > w) {
            firstPartSize = (h / 2) * w;
            secondPartSize = ((h + 1) / 2) * w;
        } else {
            firstPartSize = (w / 2) * h;
            secondPartSize = ((w + 1) / 2) * h;
        }

        ModulePartition partition = partitionSolver.partition(modules, nets, firstPartSize, secondPartSize);
        Collection<Module> modulesInFirstPart = partition.getModulesInBlock(PartitionBlock.withId(1));
        Collection<Module> modulesInSecondPart = partition.getModulesInBlock(PartitionBlock.withId(2));

        if (h > w) {
            rec(modulesInFirstPart, nets, placement, lx, ly, h / 2, w);
            rec(modulesInSecondPart, nets, placement, lx + h / 2, ly, (h + 1) / 2, w);
        } else {
            rec(modulesInFirstPart, nets, placement, lx, ly, h, w / 2);
            rec(modulesInSecondPart, nets, placement, lx, ly + w / 2, h, (w + 1) / 2);
        }
    }


}
