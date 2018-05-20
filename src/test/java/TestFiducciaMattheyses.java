import am.aua.placement.PlacementSolver;
import am.aua.placement.PlacementSolverByPartitioning;
import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.objective.TotalWirelengthObjective;
import am.aua.placement.partitioning.PartitionSolver;
import am.aua.placement.partitioning.PartitionSolverFactory;
import am.aua.placement.partitioning.PartitioningAlgorithm;
import am.aua.placement.partitioning.fm.FMPartitionSolver;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sparik on 5/20/18.
 */
public class TestFiducciaMattheyses {
    @Test
    public void testFM_8dim_success() {
        List<Net> nets = new ArrayList<>();
        List<Module> modules = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            modules.add(Module.withId(i));
            modules.add(Module.withId(i+8));
            nets.add(new Net(Module.withId(i), Module.withId(i+8)));
        }

        PlacementSolver solver = new PlacementSolverByPartitioning(TotalWirelengthObjective.getInstance(), FMPartitionSolver::new);

        PlacementResult result = solver.solve(modules, nets, 4, 4);

        int actualWirelength = (int)(TotalWirelengthObjective.getInstance().calculate(nets, result) + 0.5);
        int expectedWirelength = 8;

        Assert.assertEquals(expectedWirelength, actualWirelength);

    }
}
