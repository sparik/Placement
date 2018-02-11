package am.aua.placement.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sparik on 2/11/18.
 */
public class Net {
    private Set<Module> modules;

    public Net(Iterable<Module> modules) {
        this.modules = new HashSet<>();

        for (Module module : modules) {
            this.modules.add(module);
        }
    }

    public Net(Module ... modules) {
        this.modules = new HashSet<>();

        Collections.addAll(this.modules, modules);
    }

    public Set<Module> getModules() {
        return new HashSet<>(modules);
    }

}
