package am.aua.placement.partitioning.kl;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.partitioning.PartitionSolver;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Set;

/**
 * Created by sparik on 3/18/18.
 */
public class KLPartitionSolver implements PartitionSolver {

    public KLPartitionSolver() {
    }

    public KLPartitionSolver(Collection<Module> block1, Collection<Module> block2) {

    }

    public Pair<Set<Long>, Set<Long>> partition(Collection<Module> modules, Iterable<Net> nets) {
        return null;
    }
}
