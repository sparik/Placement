package am.aua.placement;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.entity.Slot;
import am.aua.placement.objective.PlacementObjective;
import am.aua.placement.partitioning.ModulePartition;
import am.aua.placement.partitioning.PartitionBlock;
import am.aua.placement.partitioning.PartitionSolverFactory;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by sparik on 2/11/18.
 */
public class PlacementSolverByPartitioning implements PlacementSolver {

    private static final Logger LOGGER = Logger.getLogger(PlacementSolverByPartitioning.class);
    private PlacementObjective objective;
    private PartitionSolverFactory partitionSolverFactory;
    private ExecutorService executor;

    public PlacementSolverByPartitioning(PlacementObjective objective, PartitionSolverFactory partitionSolverFactory) {
        this.objective = objective;
        this.partitionSolverFactory = partitionSolverFactory;
        this.executor = Executors.newCachedThreadPool();
    }


    public PlacementResult solve(Collection<Module> modules, Collection<Net> nets, int height, int width) {

        if (modules.size() > height * width) {
            throw new IllegalArgumentException("Too many modules");
        }

        List<Module> allModules = new ArrayList<>(modules);

        if (modules.size() < height * width) {
            int numDummyModules = height * width - modules.size();
            long maxModuleId = 1;
            for (Module module : modules) {
                if (module.getId() > maxModuleId) {
                    maxModuleId = module.getId();
                }
            }

            long dummyModuleId = maxModuleId + 1;
            while (numDummyModules-- > 0) {
                allModules.add(Module.withId(dummyModuleId++));
            }
        }

        PlacementResult result = new PlacementResult();

        try {
            this.executor.submit(() -> rec(allModules, nets, result, 0, 0, height, width)).get();
            this.executor.shutdown();
        } catch (ExecutionException | InterruptedException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }


        return result;
    }

    // place modules in the rectangle bounded by (lx, ly) and (lx + h - 1, rx + w - 1) (both inclusive), and save the result
    // in placement
    private void rec(Collection<Module> modules, Collection<Net> nets, PlacementResult placement, int lx, int ly, int h, int w) {
        if (modules.size() == 1) {
            synchronized (placement) {
                placement.setSlotForModule(modules.iterator().next(), new Slot(lx, ly));
            }
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

        ModulePartition partition = this.partitionSolverFactory.getNewInstance().partition(modules, nets, firstPartSize, secondPartSize);
        Collection<Module> modulesInFirstPart = partition.getModulesInBlock(PartitionBlock.withId(1));
        Collection<Module> modulesInSecondPart = partition.getModulesInBlock(PartitionBlock.withId(2));

        Future<?> task1, task2;

        if (h > w) {
            task1 = this.executor.submit(() -> rec(modulesInFirstPart, nets, placement, lx, ly, h / 2, w));
            task2 = this.executor.submit(() -> rec(modulesInSecondPart, nets, placement, lx + h / 2, ly, (h + 1) / 2, w));
        } else {
            task1 = this.executor.submit(() -> rec(modulesInFirstPart, nets, placement, lx, ly, h, w / 2));
            task2 = this.executor.submit(() -> rec(modulesInSecondPart, nets, placement, lx, ly + w / 2, h, (w + 1) / 2));
        }

        try {
            task1.get();
            task2.get();
        } catch (InterruptedException | ExecutionException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
