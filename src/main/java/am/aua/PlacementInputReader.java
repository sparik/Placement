package am.aua;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class PlacementInputReader {

    private PlacementInputReader() {

    }

    public static PlacementInput read(String path) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);
        PlacementInput input = null;
        try {
            input = mapper.readValue(file, PlacementInput.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
}
