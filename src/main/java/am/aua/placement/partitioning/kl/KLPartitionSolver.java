package am.aua.placement.partitioning.kl;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.partitioning.PartitionSolver;
import com.sun.tools.jdeps.Graph;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by sparik on 3/18/18.
 */
public class KLPartitionSolver implements PartitionSolver {

    private ArrayList<Module> part1;
    private ArrayList<Module> part2;
    private ArrayList<Net> nets;
    private int[][] interchangeGain;
    private int[] internalCost;
    private int[] externalCost;



    public KLPartitionSolver(Collection<Module> modules, Collection<Net> nets, int firstPartSize, int secondPartSize) {
        if (firstPartSize + secondPartSize != modules.size()) {
            throw new IllegalArgumentException("Sizes of blocks do not add up to the number of modules");
        }
        initializePartition(modules, firstPartSize);
    }

    public KLPartitionSolver(Collection<Module> modules, Collection<Net> nets) {
        this(modules, nets, modules.size() / 2, modules.size() / 2);
    }

    public KLPartitionSolver(Collection<Module> block1, Collection<Module> block2, Collection<Net> nets) {
        this.part1 = new ArrayList<>(block1);
        this.part2 = new ArrayList<>(block2);
        this.nets = new ArrayList<>(nets);
    }

    public Pair<Set<Module>, Set<Module>> partition() {

        boolean can_improve = false;



        while (can_improve) {
            HashSet<Module> X = new HashSet<>();
            HashSet<Module> Y = new HashSet<>();
        }

        return null;
    }

    private void initializePartition(Collection<Module> modules, int firstPartSize) {
        this.part1 = new ArrayList<>();
        this.part2 = new ArrayList<>();

        int idx = 0;
        for (Module module : modules) {
            if (idx < firstPartSize) {
                this.part1.add(module);
            }
            else {
                this.part2.add(module);
            }
            ++idx;
        }
    }

    private void initializeGainsOfInterchanges() {

        int sizeA = this.part1.size();
        int sizeB = this.part2.size();

        interchangeGain = new int[sizeA][sizeB];


        for (int i = 0; i < sizeA; ++i) {

        }
    }

    private int dValue(int v) {
        return this.externalCost[v] - this.internalCost[v];
    }

    private int gainOfInterchange(int a, int b) {
        return this.dValue(a) + this.dValue(b); // TODO change
    }
}
