package am.aua.placement;

import am.aua.PlacementInput;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class PlacementInputReader {
    public PlacementInputReader() {
    }

    public PlacementInput read() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("../16_16_256_1000_2.json");
        PlacementInput input = null;
        try {
            input = mapper.readValue(file, PlacementInput.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
}
