package am.aua.placement.partitioning.fm;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.partitioning.ModulePartition;
import am.aua.placement.partitioning.PartitionBlock;
import am.aua.placement.partitioning.PartitionSolver;

import java.util.*;

public class FMPartitionSolver implements PartitionSolver {
    private List<Module> modules;
    private Map<Module, Set<Net>> moduleNetMap;
    private List<Module> lockedModules;
    private ModulePartition currentPartition;
    private List<Module> movedModules;

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
        lockedModules = new ArrayList<>();
        movedModules = new ArrayList<>();
        initializeModuleNetMapping(nets);

        int currentGainSum = 0;
        Map<Integer, Integer> moves = new HashMap<>();
        int seed = 0;
        //step 4
        while (lockedModules.size() < modules.size()) {
            //step 1
            Integer moveGain = moveBaseModule();
            if (moveGain == null)
                break;
            currentGainSum += moveGain;
            seed++;
            moveGain = moveBaseModule();
            if (moveGain == null) {
                Module lastModule = movedModules.get(movedModules.size() - 1);
                movedModules.remove(lastModule);
                changeBlock(lastModule);
                break;
            }
            currentGainSum += moveGain;
            moves.put(seed++, currentGainSum);
        }

        if (moves.isEmpty())
            return currentPartition;

        int bestSeed = Collections.max(moves.entrySet(), Comparator.comparing(Map.Entry::getValue)).getKey();
        int i = movedModules.size() - 1;
        while (i > bestSeed) {
            changeBlock(movedModules.get(i--));
        }
        return currentPartition;
    }

    private Integer moveBaseModule() {
        Map<Module, Integer> gainMap = mapGainsIfUnlocked();
        Module baseModule = null;
        for (int i = 1; i <= gainMap.size(); i++) {
            Module tempBaseModule = getNthBaseModule(i, gainMap);
            PartitionBlock otherBlock = getOtherBlock(currentPartition.getBlockForModule(tempBaseModule));
            if (satisfiesBalanceCriterion(otherBlock)) {
                baseModule = tempBaseModule;
                currentPartition.setBlockForModule(baseModule, otherBlock);
                lockedModules.add(baseModule);
                break;
            }
        }
        if (baseModule == null) {
            return null;
        }

        movedModules.add(baseModule);
        return gainMap.get(baseModule);
    }

    private boolean satisfiesBalanceCriterion(PartitionBlock otherBlock) {
        int potentialBlockSize = currentPartition.getBlockSize(otherBlock) + 1;
        double BALANCE_FACTOR = 0.5;
        int MODULE_WEIGHT = 1;
        boolean notTooBig = BALANCE_FACTOR * modules.size() - MODULE_WEIGHT <=
                potentialBlockSize;
        boolean notTooSmall = potentialBlockSize <=
                BALANCE_FACTOR * modules.size() + MODULE_WEIGHT;
        return notTooBig && notTooSmall;
    }

    private Module getNthBaseModule(int n, Map<Module, Integer> gainMap) {
        List<Module> maxGainModules = new ArrayList<>();

        List<Map.Entry<Module, Integer>> gainMapEntryList = new ArrayList<>(gainMap.entrySet());

        for (int i = 0; i < n; i++) {
            Map.Entry<Module, Integer> currentMaxEntry = gainMapEntryList.get(0);
            for (Map.Entry<Module, Integer> gainMapEntry : gainMapEntryList) {
                if (gainMapEntry.getValue() >= currentMaxEntry.getValue() && !maxGainModules.contains(gainMapEntry.getKey())) {
                    currentMaxEntry = gainMapEntry;
                }
            }
            maxGainModules.add(currentMaxEntry.getKey());
        }
        return maxGainModules.get(n - 1);
    }


    //step 3
    private void changeBlock(Module module) {
        int oldId = currentPartition.getBlockForModule(module).getId();
        int newId = oldId % 2 + 1;
        PartitionBlock newBlock = PartitionBlock.withId(newId);
        currentPartition.setBlockForModule(module, newBlock);
    }

    private PartitionBlock getOtherBlock(PartitionBlock oldBlock) {
        int newId = oldBlock.getId() % 2 + 1;
        return PartitionBlock.withId(newId);
    }


    private void initializeModuleNetMapping(Collection<Net> nets) {
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
    private Map<Module, Integer> mapGainsIfUnlocked() {
        Map<Module, Integer> gainMap = new HashMap<>();
        for (Module module : modules) {
            Set<Net> nets = moduleNetMap.containsKey(module) ? moduleNetMap.get(module) : new HashSet<>();
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
                if (m.equals(module))
                    continue;
                if (currentPartition.getModulesInBlock(currentBlock).contains(m))
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