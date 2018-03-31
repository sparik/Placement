package am.aua.placement.partitioning.fm;

//TODO this should extend global Module
public class ModuleFM {
    private long id;
    private boolean isLocked;
    private int gain;
    private BlockType blockType;

    public ModuleFM(long id) {
        this.id = id;
        this.isLocked = false; //TODO default?

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
