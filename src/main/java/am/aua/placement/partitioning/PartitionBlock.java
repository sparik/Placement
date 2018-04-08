package am.aua.placement.partitioning;

import java.util.HashMap;
import java.util.Map;

public class PartitionBlock {

    private static final Map<Integer, PartitionBlock> instances = new HashMap<>();
    private int id;

    private PartitionBlock() {
    }

    public static PartitionBlock withId(int id) {
        if (instances.containsKey(id)) {
            return instances.get(id);
        }

        PartitionBlock result = new PartitionBlock();
        result.id = id;

        instances.put(id, result);

        return result;
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
