package am.aua.placement.entity;

/**
 * Created by sparik on 2/11/18.
 */

public class Slot {
    private double x;
    private double y;

    public Slot(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
