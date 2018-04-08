package am.aua.placement.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sparik on 2/11/18.
 */
public class PlacementResult {

    private Map<Module, Slot> placement;

    public PlacementResult() {
        placement = new HashMap<>();
    }

    public void setSlotForModule(Module module, Slot slot) {
        placement.put(module, slot);
    }

    public Slot getSlotForModule(Module module) {
        return placement.get(module);
    }
}
