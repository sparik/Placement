package am.aua.placement.partitioning;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Set;

public interface PartitionSolver {
    // TODO rad anel pair@
    ModulePartition partition(Collection<Module> modules, Iterable<Net> nets);
    void setInitialPartition(ModulePartition initialPartition);
}
