package am.aua.placement.objective;

import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;

import java.util.Collection;

/**
 * Created by sparik on 3/18/18.
 */
public interface PlacementObjective {
    double calculate(Collection<Net> nets, PlacementResult result);
}
