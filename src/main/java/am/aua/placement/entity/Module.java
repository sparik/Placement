package am.aua.placement.entity;

/**
 * Created by sparik on 3/31/18.
 */
public class Module {
    public Module() {
    }

    private long id;

    public Module(long id) {
        this.id = id;
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
