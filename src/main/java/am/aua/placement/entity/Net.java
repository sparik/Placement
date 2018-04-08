package am.aua.placement.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sparik on 2/11/18.
 */
public class Net {
    private List<Module> modules;

    public Net(Iterable<Module> modules) {
        this.modules = new ArrayList<>();

        for (Module module : modules) {
            this.modules.add(module);
        }
    }

    public Net(Module... modules) {
        this.modules = new ArrayList<>();

        Collections.addAll(this.modules, modules);
    }

    public List<Module> getModules() {
        return new ArrayList<>(modules);
    }

}
