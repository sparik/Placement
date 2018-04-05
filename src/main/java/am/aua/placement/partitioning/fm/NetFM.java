package am.aua.placement.partitioning.fm;

import java.util.ArrayList;
import java.util.List;

public class NetFM {
    private List<ModuleFM> modules;

    public NetFM() {
        this.modules = new ArrayList<>();
    }

    public List<ModuleFM> getModules() {
        return modules;
    }

    public void setModules(List<ModuleFM> modules) {
        this.modules = modules;
    }
}
