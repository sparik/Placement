import am.aua.placement.PlacementSolver;
import am.aua.placement.PlacementSolverByPartitioning;
import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.objective.TotalWirelengthObjective;
import am.aua.placement.partitioning.PartitionSolver;
import am.aua.placement.partitioning.PartitioningAlgorithm;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Set;

public class Main {
    public static void main(String[] args) {



        PlacementSolver solver;
        solver = new PlacementSolverByPartitioning(TotalWirelengthObjective.getInstance(), PartitioningAlgorithm.KERNIGHAN_LEE);
    }
}
