package am.aua.placement.entity;

import java.util.*;

/**
 * Created by sparik on 2/11/18.
 */
public class Net {
    private List<Long> modules;

    public Net(Iterable<Long> modules) {
        this.modules = new ArrayList<>();

        for (Long module : modules) {
            this.modules.add(module);
        }
    }

    public Net(Long ... modules) {
        this.modules = new ArrayList<>();

        Collections.addAll(this.modules, modules);
    }

    public List<Long> getModules() {
        return new ArrayList<>(modules);
    }

}
