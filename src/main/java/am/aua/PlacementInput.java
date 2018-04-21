package am.aua;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class PlacementInput {
    @JsonProperty("height")
    private int height;

    @JsonProperty("width")
    private int width;

    @JsonProperty("moduleCount")
    private int moduleCount;

    @JsonProperty("nets")
    private int[][] nets;

    private PlacementInput() {

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getModuleCount() {
        return moduleCount;
    }

    public int[][] getNets() {
        return nets;
    }

    @Override
    public String toString() {
        return "PlacementInput{" +
                "height=" + height +
                ", width=" + width +
                ", moduleCount=" + moduleCount +
                ", nets=" + Arrays.deepToString(nets) +
                '}';
    }
}
