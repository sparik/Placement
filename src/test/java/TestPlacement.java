import am.aua.PlacementInput;
import am.aua.PlacementInputReader;
import am.aua.placement.PlacementSolver;
import am.aua.placement.PlacementSolverByPartitioning;
import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.entity.Slot;
import am.aua.placement.objective.PlacementObjective;
import am.aua.placement.objective.TotalWirelengthObjective;
import am.aua.placement.partitioning.PartitioningAlgorithm;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class TestPlacement {

    // TODO maybe test for all test files
    private static PlacementInput input;
    private static List<Module> modules;
    private static List<Net> nets;
    private static int height;
    private static int width;
    private static int baselineResult;
    private static PlacementObjective objective;

    @BeforeClass
    public static void setUpBeforeClass() {
        ClassLoader classLoader = TestPlacement.class.getClassLoader();
        String testFilePath = classLoader.getResource("16_16_256_250_1.json").getFile();

        input = PlacementInputReader.read(testFilePath);
        modules = input.getModules();
        nets = input.getNetList();
        height = input.getHeight();
        width = input.getWidth();
        objective = TotalWirelengthObjective.getInstance();

        PlacementResult dumbPlacement = new PlacementResult();

        int moduleId = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                dumbPlacement.setSlotForModule(Module.withId(++moduleId), new Slot(i, j));
            }
        }

        baselineResult = (int) (objective.calculate(nets, dumbPlacement) + 0.5);
    }

    @Test
    public void KL_WIRELENGTH_SUCCESS() {
        PartitioningAlgorithm algorithm = PartitioningAlgorithm.KERNIGHAN_LIN;

        PlacementSolver solver = new PlacementSolverByPartitioning(objective, algorithm);
        PlacementResult placement = solver.solve(modules, nets, height, width);

        int klResult = (int) (objective.calculate(nets, placement) + 0.5);

        System.out.printf("Kernighan-Lin result : %d, Baseline result : %d\n", klResult, baselineResult);

        Assert.assertTrue(klResult < baselineResult);
    }

    @Test
    public void FM_WIRELENGTH_SUCCESS() {
        PartitioningAlgorithm algorithm = PartitioningAlgorithm.FIDUCCIA_MATTHEYSES;

        PlacementSolver solver = new PlacementSolverByPartitioning(objective, algorithm);
        PlacementResult placement = solver.solve(modules, nets, height, width);

        int fmResult = (int) (objective.calculate(nets, placement) + 0.5);

        System.out.printf("Fiduccia_Mattheyses result : %d, Baseline result : %d\n", fmResult, baselineResult);

        Assert.assertTrue(fmResult < baselineResult);
    }
}
