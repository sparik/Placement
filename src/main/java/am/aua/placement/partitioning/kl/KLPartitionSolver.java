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

    private final PartitionBlock firstBlock = PartitionBlock.withId(1);
    private final PartitionBlock secondBlock = PartitionBlock.withId(2);
    private ModulePartition modulePartition;
    private List<Net> nets;
    private List<Module> modules;
    private int[] internalCosts;
    private int[] externalCosts;
    private int[][] graph;
    private int moduleCount;
    private Map<Module, Integer> moduleToIdx;


    public KLPartitionSolver() {

    }

    // assume equal parts
    public ModulePartition partition(Collection<Module> modules, Collection<Net> nets) {
        return partition(modules, nets, modules.size() / 2, modules.size() / 2);
    }

    // assume random initial partition
    public ModulePartition partition(Collection<Module> modules, Collection<Net> nets, int... partSizes) {
        if (partSizes.length != 2) {
            throw new IllegalArgumentException("Only two-way partitioning is implemented.");
        }
        int totalSize = partSizes[0] + partSizes[1];
        if (totalSize != modules.size()) {
            throw new IllegalArgumentException("Number of modules is not equal to the sum of part sizes");
        }

        return partition(ModulePartition.getRandomPartition(modules, partSizes[0]), nets);
    }

    public ModulePartition partition(ModulePartition initialPartition, Collection<Net> nets) {
        if (initialPartition.getBlocks().size() != 2) {
            throw new IllegalArgumentException("Only two-way partitioning is implemented.");
        }

        this.modules = new ArrayList<>(initialPartition.getModules());
        this.nets = new ArrayList<>(nets);
        this.moduleCount = this.modules.size();
        this.modulePartition = initialPartition;

        initializeModuleToIdMapping(modules);

        initializeGraph();

        // start of the algorithm

        boolean canImprove = true;

        while (canImprove) {
            canImprove = improvePartition();
        }

        return modulePartition;
    }

    private boolean improvePartition() {

        initializeCosts();

        Set<Module> firstPart = modulePartition.getModulesInBlock(firstBlock);
        Set<Module> secondPart = modulePartition.getModulesInBlock(secondBlock);

        int maxSwaps = firstPart.size() < secondPart.size() ? firstPart.size() : secondPart.size();

        List<ModulePair> swappedModules = new ArrayList<>(maxSwaps);
        Set<Module> lockedModules = new HashSet<>(2 * maxSwaps);

        long totalGain = 0;
        long maxTotalGain = 0;
        int bestSwapIndex = -1;

        for (int i = 0; i < maxSwaps; ++i) {

            ModulePair bestSwap = getMaxGainPair(firstPart, secondPart, lockedModules);

            swappedModules.add(bestSwap);
            lockedModules.add(bestSwap.first);
            lockedModules.add(bestSwap.second);

            int firstIdx = moduleToIdx.get(bestSwap.first);
            int secondIdx = moduleToIdx.get(bestSwap.second);

            totalGain += gainOfInterchange(firstIdx, secondIdx);
            if (totalGain > maxTotalGain) {
                maxTotalGain = totalGain;
                bestSwapIndex = i;
            }

            moveModuleAndRecalculateCosts(bestSwap.first, secondBlock);
            moveModuleAndRecalculateCosts(bestSwap.second, firstBlock);
        }

        for (int i = bestSwapIndex + 1; i < maxSwaps; ++i) {
            moveModuleAndRecalculateCosts(swappedModules.get(i).first, firstBlock);
            moveModuleAndRecalculateCosts(swappedModules.get(i).second, secondBlock);
        }

        return bestSwapIndex != -1;
    }

    private void moveModuleAndRecalculateCosts(Module module, PartitionBlock targetBlock) {
        PartitionBlock currentBlock = modulePartition.getBlockForModule(module);
        if (currentBlock.equals(targetBlock)) {
            return;
        }

        int moduleIdx = moduleToIdx.get(module);

        Set<Module> modulesInCurrentBlock = modulePartition.getModulesInBlock(currentBlock);
        Set<Module> modulesInTargetBlock = modulePartition.getModulesInBlock(targetBlock);

        internalCosts[moduleIdx] = 0;
        externalCosts[moduleIdx] = 0;

        for (Module other : modulesInCurrentBlock) {
            int otherModuleIdx = moduleToIdx.get(other);

            if (moduleIdx == otherModuleIdx) {
                continue;
            }

            internalCosts[otherModuleIdx] -= graph[otherModuleIdx][moduleIdx];
            externalCosts[otherModuleIdx] += graph[otherModuleIdx][moduleIdx];

            externalCosts[moduleIdx] += graph[otherModuleIdx][moduleIdx];
        }

        for (Module other : modulesInTargetBlock) {
            int otherModuleIdx = moduleToIdx.get(other);
            internalCosts[otherModuleIdx] += graph[otherModuleIdx][moduleIdx];
            externalCosts[otherModuleIdx] -= graph[otherModuleIdx][moduleIdx];

            internalCosts[moduleIdx] += graph[otherModuleIdx][moduleIdx];
        }

        modulePartition.setBlockForModule(module, targetBlock);
    }


    private void initializeGraph() {
        graph = new int[moduleCount][moduleCount];

        for (Net net : nets) {
            List<Module> modulesInNet = net.getModules();
            int numModulesInNet = modulesInNet.size();
            for (int i = 0; i < numModulesInNet; ++i) {
                Module module1 = modulesInNet.get(i);
                if (!moduleToIdx.containsKey(module1)) {
                    continue;
                }
                int module1Idx = moduleToIdx.get(module1);

                for (int j = i + 1; j < numModulesInNet; ++j) {
                    Module module2 = modulesInNet.get(j);
                    if (!moduleToIdx.containsKey(module2)) {
                        continue;
                    }
                    int module2Idx = moduleToIdx.get(module2);

                    ++graph[module1Idx][module2Idx];
                    ++graph[module2Idx][module1Idx];
                }
            }
        }
    }

    private void initializeCosts() {

        internalCosts = new int[moduleCount];
        externalCosts = new int[moduleCount];

        for (int i = 0; i < moduleCount; ++i) {
            PartitionBlock blockContainingModule = modulePartition.getBlockForModule(modules.get(i));

            for (int j = i + 1; j < moduleCount; ++j) {
                if (modulePartition.getBlockForModule(modules.get(j)) == blockContainingModule) {
                    internalCosts[i] += graph[i][j];
                    internalCosts[j] += graph[i][j];
                } else {
                    externalCosts[i] += graph[i][j];
                    externalCosts[j] += graph[i][j];
                }
            }
        }
    }

    private int dValue(int v) {
        return externalCosts[v] - internalCosts[v];
    }

    private int gainOfInterchange(int a, int b) {
        return dValue(a) + dValue(b) - 2 * graph[a][b];
    }


    private void initializeModuleToIdMapping(Collection<Module> modules) {
        moduleToIdx = new HashMap<>(modules.size());

        int idx = 0;

        for (Module module : modules) {
            moduleToIdx.put(module, idx);
            ++idx;
        }
    }

    private ModulePair getMaxGainPair(Collection<Module> firstPart, Collection<Module> secondPart, Collection<Module> lockedModules) {
        int maxGain = Integer.MIN_VALUE;
        ModulePair result = new ModulePair();

        ArrayList<Module> firstPartList = new ArrayList<>();
        ArrayList<Module> secondPartList = new ArrayList<>();
        firstPartList.addAll(firstPart);
        secondPartList.addAll(secondPart);

        Collections.sort(firstPartList, (o1, o2) -> {
            int i1 = moduleToIdx.get(o1);
            int i2 = moduleToIdx.get(o2);
            return dValue(i2) - dValue(i1);
        });

        Collections.sort(secondPartList, (o1, o2) -> {
            int i1 = moduleToIdx.get(o1);
            int i2 = moduleToIdx.get(o2);
            return dValue(i2) - dValue(i1);
        });

        for (Module moduleFromFirst : firstPartList) {
            if (lockedModules.contains(moduleFromFirst)) {
                continue;
            }

            int module1Idx = moduleToIdx.get(moduleFromFirst);
            for (Module moduleFromSecond : secondPartList) {
                if (lockedModules.contains(moduleFromSecond)) {
                    continue;
                }


                int module2Idx = moduleToIdx.get(moduleFromSecond);

                if (dValue(module1Idx) + dValue(module2Idx) < maxGain) {
                    break;
                }

                int curGain = gainOfInterchange(module1Idx, module2Idx);
                if (curGain > maxGain) {
                    maxGain = curGain;
                    result.first = (moduleFromFirst);
                    result.second = (moduleFromSecond);
                }
            }
        }

        return result;
    }

    private class ModulePair {
        private Module first;
        private Module second;
    }
}
