package am.aua.placement.entity;

/**
 * Created by sparik on 2/11/18.
 */
public class Module {

    private long id;
    private int height;
    private int width;

    public Module(int id, int height, int width) {
        this.id = id;
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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
