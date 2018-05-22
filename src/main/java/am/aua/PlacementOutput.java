package am.aua;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.entity.Slot;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sparik on 5/16/18.
 */
public class PlacementOutput {

    @JsonIgnore
    private Map<OutputModule, Slot> placement;

    public PlacementOutput(Collection<Module> modules, PlacementResult placement) {
        this.placement = new HashMap<>(modules.size());

        for (Module module : modules) {
            this.placement.put(new OutputModule(module), placement.getSlotForModule(module));
        }
    }

    @JsonAnyGetter
    public Map<OutputModule, Slot> getPlacement() {
        return placement;
    }

    private class OutputModule {
        private Module module;

        OutputModule(Module module) {
            this.module = module;
        }

        @Override
        public String toString() {
            return String.valueOf(module.getId());
        }
    }
}
