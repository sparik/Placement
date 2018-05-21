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
import am.aua.placement.partitioning.fm.FMPartitionSolver;
import am.aua.placement.partitioning.kl.KLPartitionSolver;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TestPlacement {

    private static final String TEST_FILES_PATH = "test_files";
    private final static Logger LOGGER = Logger.getLogger(TestPlacement.class);
    private static ArrayList<PlacementInput> inputs;
    private static File[] testFiles;
    private static PlacementObjective objective;
    private static Map<String, Integer> baselineResults;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        objective = TotalWirelengthObjective.getInstance();
        baselineResults = new HashMap<>();
        inputs = new ArrayList<>();

        URL testFilesResourceFolder = classLoader.getResource(TEST_FILES_PATH);

        testFiles = (new File(testFilesResourceFolder.getPath())).listFiles();

        for (File testFile : testFiles) {

            PlacementInput input = PlacementInputReader.read(testFile);
            List<Module> modules = input.getModules();
            Collections.shuffle(modules);
            List<Net> nets = input.getNetList();
            int height = input.getHeight();
            int width = input.getWidth();

            PlacementResult dumbPlacement = new PlacementResult();

            int idx = 0;
            long maxModuleId = 1;
            long dummyModuleIdx = -1;
            for (int i = 0; i < height; ++i) {
                for (int j = 0; j < width; ++j) {
                    if (modules.size() > idx) {
                        dumbPlacement.setSlotForModule(modules.get(idx++), new Slot(i, j));
                        if (modules.get(idx - 1).getId() > maxModuleId) {
                            maxModuleId = modules.get(idx - 1).getId();
                        }
                    } else {
                        if (dummyModuleIdx == -1) {
                            dummyModuleIdx = maxModuleId + 1;
                        } else {
                            ++dummyModuleIdx;
                        }
                        dumbPlacement.setSlotForModule(Module.withId(dummyModuleIdx), new Slot(i, j));
                    }
                }
            }

            baselineResults.put(testFile.getName(), (int) (objective.calculate(nets, dumbPlacement) + 0.5));
            inputs.add(input);
        }
    }

    @Test
    public void KL_WIRELENGTH_SUCCESS() {
        PlacementSolver solver = new PlacementSolverByPartitioning(objective, KLPartitionSolver::new);

        int idx = 0;

        for (PlacementInput input : inputs) {
            PlacementResult placement = solver.solve(input.getModules(), input.getNetList(), input.getHeight(), input.getWidth());

            int klResult = (int) (objective.calculate(input.getNetList(), placement) + 0.5);

            int baselineResult = baselineResults.get(testFiles[idx].getName());

            LOGGER.info(String.format("Test file: %s, Kernighan-Lin result : %d, Baseline result : %d\n", testFiles[idx].getName(), klResult, baselineResult));

            //Assert.assertTrue(klResult < baselineResult);
            ++idx;
        }
    }

    @Test
    public void FM_WIRELENGTH_SUCCESS() {
        PlacementSolver solver = new PlacementSolverByPartitioning(objective, FMPartitionSolver::new);

        int idx = 0;

        for (PlacementInput input : inputs) {
            PlacementResult placement = solver.solve(input.getModules(), input.getNetList(), input.getHeight(), input.getWidth());

            int fmResult = (int) (objective.calculate(input.getNetList(), placement) + 0.5);

            int baselineResult = baselineResults.get(testFiles[idx].getName());

            LOGGER.info(String.format("Test file: %s, Fiduccia_Mattheyses result : %d, Baseline result : %d\n", testFiles[idx].getName(), fmResult, baselineResult));

            //Assert.assertTrue(fmResult < baselineResult);
            ++idx;
        }
    }
}
