package am.aua.placement.partitioning;

public class PartitionBlock {
    private int id;

    public PartitionBlock(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartitionBlock that = (PartitionBlock) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
