package am.aua.placement.objective;

import am.aua.placement.entity.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sparik on 3/18/18.
 */
public class TotalWirelengthObjective implements PlacementObjective {

    private static final TotalWirelengthObjective instance = new TotalWirelengthObjective();

    public static TotalWirelengthObjective getInstance() {
        return instance;
    }

    public double calculate(Collection<Net> nets, PlacementResult result) {

        double totalWirelength = 0.0;

        for (Net net : nets) {
            List<Module> modules = net.getModules();
            List<Point2d> moduleSlots = new ArrayList<>();

            for (Module module : modules) {
                Slot slotForModule = result.getSlotForModule(module);
                if (slotForModule == null) {
                    throw new ObjectiveCalculationException(String.format("Module with id %d is not placed.", module.getId()));
                }
                moduleSlots.add(slotForModule);
            }

            totalWirelength += minimumBoundingRectangleSemiPerimeter(moduleSlots);
        }

        return totalWirelength;
    }

    private double minimumBoundingRectangleSemiPerimeter(List<Point2d> points) {
        double minX, maxX, minY, maxY;

        int n = points.size();

        if (n == 0) {
            return 0;
        }

        minX = points.get(0).getX();
        maxX = points.get(0).getX();
        minY = points.get(0).getY();
        maxY = points.get(0).getY();

        for (Point2d p : points) {
            double curX = p.getX();
            double curY = p.getY();

            if (curX > maxX) {
                maxX = curX;
            }
            if (curX < minX) {
                minX = curX;
            }

            if (curY > maxY) {
                maxY = curY;
            }
            if (curY < minY) {
                minY = curY;
            }
        }

        return (maxX - minX + maxY - minY);
    }
}
