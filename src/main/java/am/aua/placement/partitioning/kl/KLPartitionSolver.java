package am.aua.placement.partitioning.kl;

import am.aua.placement.entity.Net;
import am.aua.placement.partitioning.PartitionSolver;
import javafx.util.Pair;

import java.util.Set;

/**
 * Created by sparik on 3/18/18.
 */
public class KLPartitionSolver implements PartitionSolver {
    @Override
    public Pair<Set<Long>, Set<Long>> partition(Iterable<Long> modules, Iterable<Net> nets, double balanceFactor) {
        return null;
    }
}
