import am.aua.placement.entity.Module;
import am.aua.placement.entity.Net;
import am.aua.placement.partitioning.ModulePartition;
import am.aua.placement.partitioning.PartitionBlock;
import am.aua.placement.partitioning.PartitionSolver;
import am.aua.placement.partitioning.fm.FMPartitionSolver;
import am.aua.placement.partitioning.kl.KLPartitionSolver;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sparik on 4/6/18.
 */
public class TestKernighanLin {

    private final PartitionSolver kernighanLin = new FMPartitionSolver();

    @Test
    public void testKernighanLinWithInitialPartition() {
        ModulePartition initialPartition = new ModulePartition();
        PartitionBlock firstBlock = PartitionBlock.withId(1);
        PartitionBlock secondBlock = PartitionBlock.withId(2);

        initialPartition.setModulesForBlock(firstBlock, Module.withId(2), Module.withId(3), Module.withId(4));
        initialPartition.setModulesForBlock(secondBlock, Module.withId(1), Module.withId(5), Module.withId(6));

        List<Net> netList = new ArrayList<>();
        netList.add(new Net(Module.withId(1), Module.withId(2)));
        netList.add(new Net(Module.withId(2), Module.withId(3)));
        netList.add(new Net(Module.withId(2), Module.withId(4)));
        netList.add(new Net(Module.withId(4), Module.withId(5), Module.withId(6)));

        ModulePartition result = kernighanLin.partition(initialPartition, netList);

        Assert.assertEquals(result.getBlocks().size(), 2);
        Assert.assertEquals(result.getModules().size(), 6);
        Assert.assertTrue(result.getModulesInBlock(firstBlock).contains(Module.withId(1)));
        Assert.assertTrue(result.getModulesInBlock(firstBlock).contains(Module.withId(2)));
        Assert.assertTrue(result.getModulesInBlock(firstBlock).contains(Module.withId(3)));
        Assert.assertTrue(result.getModulesInBlock(secondBlock).contains(Module.withId(4)));
        Assert.assertTrue(result.getModulesInBlock(secondBlock).contains(Module.withId(5)));
        Assert.assertTrue(result.getModulesInBlock(secondBlock).contains(Module.withId(6)));
    }
}
