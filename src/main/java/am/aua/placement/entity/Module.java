package am.aua.placement.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sparik on 3/31/18.
 */
public class Module {

    private static final Map<Long, Module> instances = new HashMap<>();
    private long id;

    private Module() {
    }

    public static Module withId(long id) {
        if (instances.containsKey(id)) {
            return instances.get(id);
        }

        Module result = new Module();
        result.id = id;

        instances.put(id, result);

        return result;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Module module = (Module) o;

        return id == module.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
