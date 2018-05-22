package am.aua;

import am.aua.placement.PlacementSolver;
import am.aua.placement.PlacementSolverByPartitioning;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.objective.TotalWirelengthObjective;
import am.aua.placement.partitioning.PartitionSolverFactory;
import am.aua.placement.partitioning.fm.FMPartitionSolver;
import am.aua.placement.partitioning.kl.KLPartitionSolver;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        if (args.length < 3) {
            LOGGER.info(String.format("Usage: java -jar %s input_file output_file partitioning_algorithm\n", "place.jar"));
            return;
        }

        String inputFilePath = args[0];
        String outputFilePath = args[1];

        String val = args[2];

        PartitionSolverFactory partitionSolverFactory = KLPartitionSolver::new;

        if (val.equals("fm")) {
            partitionSolverFactory = FMPartitionSolver::new;
        } else if (!val.equals("kl")) {
            LOGGER.warn("algorithm should be one of 'kl', 'fm'");
            System.exit(0);
        }


        PlacementInput input = null;

        try {
            input = PlacementInputReader.read(new File(inputFilePath));
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            System.exit(1);
        }

        PlacementSolver solver = new PlacementSolverByPartitioning(TotalWirelengthObjective.getInstance(), partitionSolverFactory);

        PlacementResult result = solver.solve(input.getModules(), input.getNetList(), input.getHeight(), input.getWidth());

        try {
            writePlacementResultToFile(new PlacementOutput(input.getModules(), result), outputFilePath);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            System.exit(1);
        }
    }

    private static void writePlacementResultToFile(PlacementOutput output, String filepath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        File file = new File(filepath);

        mapper.writeValue(file, output);
    }
}
