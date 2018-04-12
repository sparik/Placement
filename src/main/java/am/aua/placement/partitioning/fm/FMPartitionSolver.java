package am.aua.placement.partitioning.fm;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.partitioning.ModulePartition;
import am.aua.placement.partitioning.PartitionBlock;
import am.aua.placement.partitioning.PartitionSolver;
import com.sun.istack.internal.NotNull;

import java.util.*;

public class FMPartitionSolver implements PartitionSolver {
    private List<Module> modules = new ArrayList<>();
    private Map<Module, Set<Net>> moduleNetMap;
    private List<Module> lockedModules = new ArrayList<>();
    private ModulePartition currentPartition;
    private Map<Module, Integer> gainMap;


    public FMPartitionSolver() {
    }

    // assume equal parts

    @Override
    public ModulePartition partition(Collection<Module> modules, Collection<Net> nets) {
        return partition(modules, nets, modules.size() / 2, modules.size() / 2);
    }
    // assume random initial partition

    @Override
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

    @Override
    public ModulePartition partition(ModulePartition initialPartition, Collection<Net> nets) {
        if (initialPartition.getBlocks().size() != 2) {
            throw new IllegalArgumentException("Only two-way partitioning is implemented.");
        }

        this.currentPartition = initialPartition;
        this.modules = new ArrayList<>(initialPartition.getModules());
        this.moduleNetMap = new HashMap<>();
        initializeModuleNetMapping(nets);

        gainMap = mapGainsIfUnlocked(modules);
        int maxGainSum = gainMap.values().stream().reduce(0, Integer::sum);
        ModulePartition bestPartition = initialPartition;
        //step 4
        while (lockedModules.size() < modules.size()) {
            //step 1
            int currentGainSum = moveModuleFromBlock(PartitionBlock.withId(1))
                    + moveModuleFromBlock(PartitionBlock.withId(2));

            if (currentGainSum > maxGainSum) {
                bestPartition = currentPartition;
            }

        }
        return bestPartition;
    }

    private int moveModuleFromBlock(PartitionBlock currentBlock) {
        //step 2
        List<Module> modulesWithGainsSorted = sortByGains(gainMap);
        Module baseModule = null;
        for (Module aModuleWithGainsSorted : modulesWithGainsSorted) {
            if (currentPartition.getBlockForModule(aModuleWithGainsSorted).equals(currentBlock)) {
                baseModule = aModuleWithGainsSorted;
            }
        }
        //step 3
        lockedModules.add(baseModule);
        changeBlock(baseModule);
        gainMap = mapGainsIfUnlocked(modules);

        return gainMap.values().stream().reduce(0, Integer::sum);
    }

    //step 3
    private void changeBlock(Module module) {
        int oldId = currentPartition.getBlockForModule(module).getId();
        int newId = oldId % 2 + 1;
        PartitionBlock newBlock = PartitionBlock.withId(newId);
        currentPartition.setBlockForModule(module, newBlock);
    }

    //step 2.1
    private List<Module> sortByGains(@NotNull Map<Module, Integer> gainMap) {
        List<Map.Entry<Module, Integer>> entryList = new ArrayList<>(gainMap.entrySet());
        entryList.sort(Comparator.comparing(Map.Entry::getValue));

        List<Module> sortedModules = new ArrayList<>();
        for (Map.Entry<Module, Integer> entry : entryList) {
            sortedModules.add(entry.getKey());
        }
        return sortedModules;
    }

    private void initializeModuleNetMapping(@NotNull Collection<Net> nets) {
        for (Net net : nets) {
            for (Module module : net.getModules()) {
                if (!moduleNetMap.containsKey(module)) {
                    moduleNetMap.put(module, new HashSet<>());
                }
                moduleNetMap.get(module).add(net);
            }
        }
    }

    //step 1
    private Map<Module, Integer> mapGainsIfUnlocked(@NotNull Collection<Module> modules) {
        Map<Module, Integer> gainMap = new HashMap<>();
        for (Module module : modules) {
            Set<Net> nets = moduleNetMap.get(module);
            if (!lockedModules.contains(module)) {
                int gain = getFS(module, nets) - getTE(module, nets);
                gainMap.put(module, gain);
            }
        }
        return gainMap;
    }

    //step 1.1
    private int getFS(Module module, Set<Net> nets) {
        PartitionBlock currentBlock = currentPartition.getBlockForModule(module);
        int counter = 0;
        outer:
        for (Net net : nets) {
            for (Module m : net.getModules()) {
                if (m.equals(module) || currentPartition.getModulesInBlock(currentBlock).contains(m))
                    continue outer;
            }
            ++counter;
        }
        return counter;
    }

    //step 1.2
    private int getTE(Module module, Set<Net> nets) {
        PartitionBlock currentBlock = currentPartition.getBlockForModule(module);
        int counter = 0;
        outer:
        for (Net net : nets) {
            for (Module m : net.getModules()) {
                if (!currentPartition.getModulesInBlock(currentBlock).contains(m))
                    continue outer;
            }
            ++counter;
        }
        return counter;
    }
}