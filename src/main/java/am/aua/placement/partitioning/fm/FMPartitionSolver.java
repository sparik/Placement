package am.aua.placement.partitioning.fm;

import am.aua.placement.entity.Net;
import am.aua.placement.partitioning.PartitionSolver;
import javafx.util.Pair;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//TODO change iterable to collection
public class FMPartitionSolver implements PartitionSolver {
    @Override
    public Pair<Set<Long>, Set<Long>> partition(Collection<Long> modules, Iterable<Net> nets, double balanceFactor) {
        Collection<ModuleFM> fmModules = new HashSet<>();
        for (Long module :
                modules) {
            fmModules.add(new ModuleFM(module));
        }
        return partitionFM(fmModules, nets, balanceFactor);
    }

    private Pair<Set<Long>, Set<Long>> partitionFM(Collection<ModuleFM> modules, Iterable<Net> nets, double balanceFactor) {
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
        for (int i = 0; i < modules.size(); i += 2) {
            modules.iterator().next().setBlockType(BlockType.FROM_BLOCK);
            modules.iterator().next().setBlockType(BlockType.TO_BLOCK);
        }
    }

    private ModuleFM getMaxGain(Iterable<ModuleFM> modules) {
        ModuleFM maxGainModule = modules.iterator().next();
        for (ModuleFM module :
                modules) {
            if (module.getGain() > maxGainModule.getGain()) {
                maxGainModule = module; //TODO reference type probs check
            }
        }
        return maxGainModule;
    }


    //do initial partition
    //compute gains
    //compute FS
    //compute TE
    //check balance criterion


    private int getFS(ModuleFM module) {

    }

    private int getTE(ModuleFM module) {
    }


}
