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
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.printf("Usage: %s input_file output_file", "java -jar place.jar");
            return;
        }

        // TODO add algorithm as 3rd argument

        String input_file_path = args[0];
        String output_file_path = args[1];

        PlacementInput input = PlacementInputReader.read(input_file_path);

        PlacementSolver solver = new PlacementSolverByPartitioning(TotalWirelengthObjective.getInstance(), PartitioningAlgorithm.KERNIGHAN_LIN);

        PlacementResult result = solver.solve(input.getModules(), input.getNetList(), input.getHeight(), input.getWidth());

        Path outfile_path = Paths.get(output_file_path);

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
