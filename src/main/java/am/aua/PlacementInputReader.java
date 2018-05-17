package am.aua;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class PlacementInputReader {

    private PlacementInputReader() {

    }

    public static PlacementInput read(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, PlacementInput.class);
    }
}
