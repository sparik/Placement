package am.aua.placement;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;

import java.util.Collection;

/**
 * Created by sparik on 3/18/18.
 */
public interface PlacementSolver {
    PlacementResult solve(Collection<Module> modules, Collection<Net> nets, int H, int W);
}
