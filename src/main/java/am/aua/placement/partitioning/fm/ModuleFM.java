package am.aua.placement.partitioning.fm;

public class ModuleFM {
    private boolean isLocked;
    private int gain;

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
}
