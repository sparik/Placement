package am.aua.placement;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;

/**
 * Created by sparik on 3/18/18.
 */
public interface PlacementSolver {
    PlacementResult solve(Iterable<Module> modules, Iterable<Net> nets, int H, int W);
}
