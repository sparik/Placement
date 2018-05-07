import am.aua.PlacementInput;
import am.aua.placement.PlacementInputReader;
import am.aua.placement.PlacementSolver;
import am.aua.placement.PlacementSolverByPartitioning;
import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.objective.PlacementObjective;
import am.aua.placement.objective.TotalWirelengthObjective;
import am.aua.placement.partitioning.PartitioningAlgorithm;
import org.junit.Test;

import java.util.List;

public class TestPlacement {

    private PlacementInputReader reader = new PlacementInputReader();
    private PlacementInput input = reader.read();
    private List<Module> modules = input.getModules();
    private List<Net> nets = input.getNetList();
    private int height = input.getHeight();
    private int width = input.getWidth();

    @Test
    public void KL_WIRELENGTH_SUCCESS() {
        PlacementObjective objective = TotalWirelengthObjective.getInstance();
        PartitioningAlgorithm algorithm = PartitioningAlgorithm.KERNIGHAN_LEE;

        PlacementSolver solver = new PlacementSolverByPartitioning(objective, algorithm);
        PlacementResult result = solver.solve(modules, nets, height, width);
        System.out.println(objective.calculate(nets, result));

    }

    @Test
    public void FM_WIRELENGTH_SUCCESS() {
        PlacementObjective objective = TotalWirelengthObjective.getInstance();
        PartitioningAlgorithm algorithm = PartitioningAlgorithm.FIDUCCIA_MATTHEYSES;

        PlacementSolver solver = new PlacementSolverByPartitioning(objective, algorithm);
        PlacementResult result = solver.solve(modules, nets, height, width);
        System.out.println(objective.calculate(nets, result));
    }
}
