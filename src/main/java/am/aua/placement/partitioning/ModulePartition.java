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

    // TODO make random
    // TODO handle more blocks
    public static ModulePartition getRandomPartition(Collection<Module> modules, int firstPartSize) {
        ModulePartition result = new ModulePartition();
        PartitionBlock firstPart = PartitionBlock.withId(1);
        PartitionBlock secondPart = PartitionBlock.withId(2);
        Random random = new Random();

        Set<Integer> firstPartIndices = new HashSet<>();
        while (firstPartIndices.size() < firstPartSize) {
            //0 to modules.size()
            int index = random.nextInt(modules.size());
            firstPartIndices.add(index);
        }

        int i = 0;
        for (Module module : modules) {
            if (firstPartIndices.contains(i)) {
                result.setBlockForModule(module, firstPart);
            } else result.setBlockForModule(module, secondPart);
            ++i;
        }
        return result;
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
