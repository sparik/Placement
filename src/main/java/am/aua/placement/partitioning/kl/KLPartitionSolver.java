package am.aua.placement.partitioning.kl;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.partitioning.ModulePartition;
import am.aua.placement.partitioning.PartitionBlock;
import am.aua.placement.partitioning.PartitionSolver;

import java.util.*;

/**
 * Created by sparik on 3/18/18.
 */
public class KLPartitionSolver implements PartitionSolver {

    private ModulePartition currentPartition;
    private List<Net> nets;
    private List<Module> modules;
    private int[] internalCosts;
    private int[] externalCosts;
    private int[][] graph;
    private int numModules;
    private Map<Module, Integer> moduleToIdx;


    public KLPartitionSolver() {

    }

    // assume equal parts
    public ModulePartition partition(Collection<Module> modules, Collection<Net> nets) {
        return partition(modules, nets, modules.size() / 2, modules.size() / 2);
    }

    public ModulePartition partition(Collection<Module> modules, Collection<Net> nets, int ... partSizes) {

        if (partSizes.length != 2) {
            throw new IllegalArgumentException("Only two-way partition is implemented.");
        }
        int totalSize = partSizes[0] + partSizes[1];
        if (totalSize != modules.size()) {
            throw new IllegalArgumentException("Number of modules is not equal to the sum of part sizes");
        }

        if (currentPartition != null) {
            if (currentPartition.size() != modules.size()) {
                throw new IllegalArgumentException("xz");
            }
        }
        else {
            currentPartition = getRandomPartition(modules, partSizes[0]);
        }

        this.nets = new ArrayList<>(nets);
        this.modules = new ArrayList<>(modules);
        numModules = modules.size();

        initializeGraph();

        // start of the algorithm

        boolean can_improve = true;

        while (can_improve) {
            can_improve = improvePartition();
        }

        return currentPartition;
    }

    public void setInitialPartition(ModulePartition initialPartition) {
        currentPartition = initialPartition;
    }


    private boolean improvePartition() {

        initializeCosts();

        return true;
    }


    private ModulePartition getRandomPartition(Collection<Module> modules, int firstPartSize) {
        ModulePartition result = new ModulePartition();
        PartitionBlock firstPart = new PartitionBlock(1);
        PartitionBlock secondPart = new PartitionBlock(2);

        int idx = 0;
        for (Module module : modules) {
            if (idx < firstPartSize) {
                result.put(module, firstPart);
            }
            else {
                result.put(module, secondPart);
            }
            ++idx;
        }

        return result;
    }

    private void initializeGraph() {
        graph = new int[numModules][numModules];

        for (Net net : nets) {
            List<Module> modulesInNet = net.getModules();
            int numModulesInNet = modulesInNet.size();
            for (int i = 0; i < numModulesInNet; ++i) {
                for (int j = i + 1; j < numModulesInNet; ++j) {
                    Module module1 = modulesInNet.get(i);
                    Module module2 = modulesInNet.get(j);
                    int module1Idx = moduleToIdx.get(module1);
                    int module2Idx = moduleToIdx.get(module2);

                    ++graph[module1Idx][module2Idx];
                    ++graph[module2Idx][module1Idx];
                }
            }
        }
    }

    private void initializeCosts() {

        internalCosts = new int[numModules];
        externalCosts = new int[numModules];

        for (int i = 0; i < numModules; ++i) {
            PartitionBlock blockContainingModule = currentPartition.get(modules.get(i));
            Integer internalCost = 0;
            Integer externalCost = 0;

            for (int j = i + 1; j < numModules; ++j) {
                if (currentPartition.get(modules.get(j)) == blockContainingModule) {
                    ++internalCosts[i];
                    ++internalCosts[j];
                }
                else {
                    ++externalCosts[i];
                    ++externalCosts[j];
                }
            }

        }
    }

    private int dValue(int v) {
        return externalCosts[v] - internalCosts[v];
    }

    private int gainOfInterchange(int a, int b) {
        return this.dValue(a) + this.dValue(b) - graph[a][b]; // TODO change
    }


    private void initializeModuleToIdMapping(Collection<Module> modules) {
        moduleToIdx = new HashMap<>(modules.size());

        int idx = 0;

        for (Module module : modules) {
            moduleToIdx.put(module, idx);
            ++idx;
        }
    }
}
