package am.aua.placement.partitioning;

import am.aua.placement.entity.Module;

import java.util.*;

public class ModulePartition {

    private HashMap<Module, PartitionBlock> partition;
    private HashMap<PartitionBlock, Set<Module>> blocks;

    public ModulePartition() {
        partition = new HashMap<>();
        blocks = new HashMap<>();
    }

    public PartitionBlock getBlockForModule(Module module) {
        return partition.get(module);
    }

    public Set<Module> getModulesInBlock(PartitionBlock block) {
        return blocks.get(block);
    }

    public void setBlockForModule(Module module, PartitionBlock block) {

        if (partition.containsKey(module)) {
            PartitionBlock currentBlock = partition.get(module);
            blocks.get(currentBlock).remove(module);
        }

        partition.put(module, block);
        if (!blocks.containsKey(block)) {
            blocks.put(block, new HashSet<>());
        }
        blocks.get(block).add(module);
    }

    public Set<PartitionBlock> getBlocks() {
        return blocks.keySet();
    }

    public boolean contains(Module module) {
        return partition.containsKey(module);
    }

    public Set<Module> getModules() {
        return partition.keySet();
    }

    public void setModulesForBlock(PartitionBlock block, Module... modules) {
        if (!blocks.containsKey(block)) {
            blocks.put(block, new HashSet<>(modules.length));
        }
        Collections.addAll(blocks.get(block), modules);

        for (Module module : modules) {
            partition.put(module, block);
        }
    }
}
