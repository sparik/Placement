package am.aua.placement.partitioning.fm;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.partitioning.ModulePartition;
import am.aua.placement.partitioning.PartitionSolver;

import java.util.*;

public class FMPartitionSolver implements PartitionSolver {
    private List<Module> modules = new ArrayList<>();
    private List<Net> nets = new ArrayList<>();
    private Map<Module, Set<Net>> moduleNetMap;

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
        initializeModuleNetMapping(nets);

        this.nets = new ArrayList<>(nets);
        this.modules = new ArrayList<>(initialPartition.getModules());

        return null;
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

    private ModulePartition partitionFM(Collection<ModuleFM> modules, Collection<Net> nets) {
        initialPartition(modules);
        while (true) {
            setGainsIfUnlocked(modules);
            ModuleFM baseModule = whichMaxGain(modules);
            //TODO BALANCE CRITERION
            baseModule.setLocked(true);
            setGainsIfAffected(baseModule);
        }

    }

    private void setGainsIfAffected(ModuleFM baseModule) {
        for (NetFM netFM :
                baseModule.getNets()) {
            for (ModuleFM moduleFM :
                    netFM.getModules()) {
                if (!moduleFM.isLocked()) {
                    int gain = getFS(moduleFM) - getTE(moduleFM);
                    moduleFM.setGain(gain);
                }
            }
        }
    }

    private void setGainsIfUnlocked(Collection<ModuleFM> modules) {
        for (ModuleFM module :
                modules) {
            if (!module.isLocked()) {
                int gain = getFS(module) - getTE(module);
                module.setGain(gain);
            }
        }
    }

    private void initialPartition(Collection<ModuleFM> modules) {
        while (modules.iterator().hasNext()) {

            modules.iterator().next().setBlockType(BlockType.BLOCK_1);
            modules.iterator().next().setBlockType(BlockType.BLOCK_2);
        }
    }

    private ModuleFM whichMaxGain(Collection<ModuleFM> modules) {
        ModuleFM maxGainModule = modules.iterator().next();
        try {
            while (maxGainModule.isLocked()) {
                maxGainModule = modules.iterator().next();
            }
        } catch (NoSuchElementException e) {
            return null;
        }
        for (ModuleFM module :
                modules) {
            if (!module.isLocked() && module.getGain() > maxGainModule.getGain()) {
                maxGainModule = module; //TODO reference type probs check
            }
        }
        return maxGainModule;
    }

    private int getFS(ModuleFM module) {
//        //TODO only BLOCK_1's modules should be scanned
//        //TODO for that make a module->moduleFM converter
//        int counter = 0;
//        for (ModuleFM m :
//                modules) {
//            if (m.equals(module)) {
//                continue;
//            }
//            if (m.getBlockType() == BlockType.BLOCK_1 && !Collections.disjoint(m.getNets(), module.getNets()))
//                counter++;
//        }
//        return counter;

        int counter = 0;

        for (Net net :
                nets) {

        }
        outer:
        for (NetFM netFM :
                module.getNets()) {
            for (ModuleFM m :
                    netFM.getModules()) {
                if (m.equals(module) || m.getBlockType() == BlockType.BLOCK_1)
                    continue outer;
                ++counter;
            }
        }
        return counter;
    }

    private int getTE(ModuleFM module) {
        int counter = 0;
        outer:
        for (NetFM netFM :
                module.getNets()) {
            for (ModuleFM m :
                    netFM.getModules()) {
                if (m.getBlockType() == BlockType.BLOCK_2)
                    continue outer;
                ++counter;
            }
        }
        return counter;
    }


}
