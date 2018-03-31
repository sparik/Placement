package am.aua.placement.partitioning.fm;

import am.aua.placement.entity.Module;

public class ModuleFM extends Module {
    private boolean isLocked;
    private int gain;
    private BlockType blockType;

    public ModuleFM(long id) {
        super(id);
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
}
