package am.aua.placement.partitioning;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;

import java.util.Collection;

public interface PartitionSolver {
    ModulePartition partition(ModulePartition initialPartition, Collection<Net> nets);

    ModulePartition partition(Collection<Module> modules, Collection<Net> nets);

    ModulePartition partition(Collection<Module> modules, Collection<Net> nets, int... partSizes);
}
