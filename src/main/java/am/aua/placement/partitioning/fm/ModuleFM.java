package am.aua.placement.partitioning.fm;

import am.aua.placement.entity.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleFM extends Module {
    private boolean isLocked;
    private int gain;
    private BlockType blockType;
    private List<NetFM> nets;

    public ModuleFM(long id) {
        super(id);
        this.nets = new ArrayList<>();
    }

    public ModuleFM(Module module) {
        super(module.getId());
        this.nets = new ArrayList<>();
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public int getGain() {
        return gain;
    }

    public void setGain(int gain) {
        this.gain = gain;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public List<NetFM> getNets() {
        return nets;
    }

    public void setNets(List<NetFM> nets) {
        this.nets = nets;
    }
}
