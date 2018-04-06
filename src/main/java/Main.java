import am.aua.placement.PlacementSolver;
import am.aua.placement.PlacementSolverByPartitioning;
import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.objective.PlacementObjective;
import am.aua.placement.objective.TotalWirelengthObjective;
import am.aua.placement.partitioning.PartitionSolver;
import am.aua.placement.partitioning.PartitioningAlgorithm;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        PlacementObjective objective = TotalWirelengthObjective.getInstance();
        PartitioningAlgorithm algorithm = PartitioningAlgorithm.KERNIGHAN_LEE;

        PlacementSolver solver = new PlacementSolverByPartitioning(objective, algorithm);

        int H = 4;
        int W = 4;
        List<Module> modules = new ArrayList<>();
        List<Net> nets = new ArrayList<>();

        PlacementResult result = solver.solve(modules, nets, H, W);
    }
}
