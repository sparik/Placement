package am.aua.placement.partitioning;

import am.aua.placement.entity.Net;
import javafx.util.Pair;

import java.util.Set;

public interface PartitionSolver {
    Pair<Set<Long>, Set<Long>> partition(Iterable<Long> modules, Iterable<Net> nets, double balanceFactor);
}
