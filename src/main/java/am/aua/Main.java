package am.aua;

import am.aua.placement.PlacementOutput;
import am.aua.placement.PlacementSolver;
import am.aua.placement.PlacementSolverByPartitioning;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.objective.TotalWirelengthObjective;
import am.aua.placement.partitioning.PartitioningAlgorithm;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        if (args.length < 3) {
            System.out.printf("Usage: java -jar %s input_file output_file partitioning_algorithm\n", "place.jar");
            return;
        }

        // TODO add algorithm as 3rd argument

        String input_file_path = args[0];
        String output_file_path = args[1];

        String val = args[2];

        PartitioningAlgorithm algorithm = PartitioningAlgorithm.KERNIGHAN_LIN;

        if (val.equals("fm")) {
            algorithm = PartitioningAlgorithm.FIDUCCIA_MATTHEYSES;
        }
        else if (!val.equals("kl")) {
            System.out.println("algorithm should be one of 'kl', 'fm'");
            System.exit(0);
        }


        PlacementInput input = PlacementInputReader.read(input_file_path);

        PlacementSolver solver = new PlacementSolverByPartitioning(TotalWirelengthObjective.getInstance(), algorithm);

        PlacementResult result = solver.solve(input.getModules(), input.getNetList(), input.getHeight(), input.getWidth());

        try {
            writePlacementResultToFile(new PlacementOutput(input.getModules(), result), output_file_path);
        } catch (IOException ex) {
            ex.printStackTrace();
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
