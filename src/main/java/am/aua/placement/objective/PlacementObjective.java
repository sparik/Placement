package am.aua.placement.objective;

import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;

/**
 * Created by sparik on 3/18/18.
 */
public interface PlacementObjective {
    Double calculate(Iterable<Net> nets, PlacementResult result);
}
