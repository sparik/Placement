package am.aua.placement.partitioning.fm;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.partitioning.ModulePartition;
import am.aua.placement.partitioning.PartitionSolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FMPartitionSolver implements PartitionSolver {
    private List<ModuleFM> block1 = new ArrayList<>();
    private List<ModuleFM> block2 = new ArrayList<>();
    private List<ModuleFM> modules = new ArrayList<>();
    private List<NetFM> nets = new ArrayList<>(); //TODO tirumer
    private double balanceFactor;

    public FMPartitionSolver(double balanceFactor) {
        this.balanceFactor = balanceFactor;
    }

    public FMPartitionSolver(double balanceFactor, Collection<Module> block1, Collection<Module> block2, Collection<Net> nets, Collection<Module> modules) {
        this(balanceFactor);
        //TODO add balance check
        if (block1.size() + block2.size() != modules.size()) {
            throw new IllegalArgumentException("Sizes of blocks do not add up to the number of modules");
        }
        for (Net net :
                nets) {
            NetFM netFM = new NetFM();
            for (Module module :
                    net.getModules()) {
                ModuleFM moduleFM = new ModuleFM(module);
                if (block1.contains(module)) {
                    moduleFM.setBlockType(BlockType.BLOCK_1);
                    this.block1.add(moduleFM);
                } else {
                    moduleFM.setBlockType(BlockType.BLOCK_2);
                    this.block2.add(moduleFM);
                }
                moduleFM.getNets().add(netFM);
                this.modules.add(moduleFM);
            }
            this.nets.add(netFM);
        }
    }

    public ModulePartition partition(Collection<Module> modules, Iterable<Net> nets) {
        //TODO i dont need modules
        return partitionFM(this.modules, nets);
    }

    @Override
    public void setInitialPartition(ModulePartition initialPartition) {

    }

    private ModulePartition partitionFM(Collection<ModuleFM> modules, Iterable<Net> nets) {
        initialPartition(modules);
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

    private ModuleFM whichMaxGain(Iterable<ModuleFM> modules) {
        ModuleFM moduleFM = modules.iterator().next();
        ModuleFM maxGainModule = null;
        while (moduleFM.isLocked()){
            maxGainModule = modules.iterator().next();
        }
        for (ModuleFM module :
                modules) {
            if (module.getGain() > maxGainModule.getGain()) {
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
