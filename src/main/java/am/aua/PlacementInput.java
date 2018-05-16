package am.aua;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public List<Module> getModules() {
        List<Module> modules = new ArrayList<>();
        for (int i = 1; i <= moduleCount; i++) {
            modules.add(Module.withId(i));
        }
        return modules;
    }

    public List<Net> getNetList() {
        List<Net> nets = new ArrayList<>();
        for (int[] netSubstitute : this.nets) {
            List<Module> modules = new ArrayList<>();
            for (int moduleId : netSubstitute) {
                modules.add(Module.withId(moduleId));
            }
            Net net = new Net(modules);
            nets.add(net);
        }
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
