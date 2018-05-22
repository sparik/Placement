package am.aua.placement.objective;

import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.entity.PlacementResult;
import am.aua.placement.entity.Slot;
import am.aua.placement.objective.ObjectiveCalculationException;
import am.aua.placement.objective.PlacementObjective;
import am.aua.placement.objective.TotalWirelengthObjective;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sparik on 4/6/18.
 */
public class TestTotalWirelengthObjective {

    private final PlacementObjective objective = TotalWirelengthObjective.getInstance();

    @Test(expected = ObjectiveCalculationException.class)
    public void testWirelengthCalculation1() {
        Module module1 = Module.withId(0);
        Module module2 = Module.withId(1);
        Module module3 = Module.withId(2);

        List<Net> netList = new ArrayList<>();
        netList.add(new Net(module1, module2, module3));

        PlacementResult placement = new PlacementResult();
        placement.setSlotForModule(module1, new Slot(1, 2));
        placement.setSlotForModule(module2, new Slot(2, 3));

        double result = objective.calculate(netList, placement);
    }

    @Test
    public void testWirelengthCalculation2() {
        Module module1 = Module.withId(0);
        Module module2 = Module.withId(1);
        Module module3 = Module.withId(2);

        List<Net> netList = new ArrayList<>();
        netList.add(new Net(module1, module2, module3));

        PlacementResult placement = new PlacementResult();
        placement.setSlotForModule(module1, new Slot(1, 2));
        placement.setSlotForModule(module2, new Slot(2, 3));
        placement.setSlotForModule(module3, new Slot(10, 7));

        double expectedResult = 14;

        double result = objective.calculate(netList, placement);

        Assert.assertEquals(result, expectedResult, 1E-6);
    }

    @Test
    public void testWirelengthCalculation3() {

        double expectedResult = 0;

        double result = objective.calculate(new ArrayList<>(), new PlacementResult());

        Assert.assertEquals(result, expectedResult, 1E-6);
    }
}