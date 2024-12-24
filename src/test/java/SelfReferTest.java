import lombok.val;
import org.junit.jupiter.api.Test;
import zank.lib.script_topo_sort.example.TestSortables;
import zank.lib.script_topo_sort.topo.TopoNotSolved;

/**
 * @author ZZZank
 */
public class SelfReferTest extends TopoTestBase {

    @Test
    void test1() throws TopoNotSolved {
        val sortables = new TestSortables()
            .add(0)
            .sortables;
        expectPreconditionFail(sortables);
    }

    @Test
    void test1inN() {
        expectPreconditionFail(
            new TestSortables()
                .add(1).add(3).add(0).add().add().add(1).add().add(7) //7
                .sortables
        );
    }

    @Test
    void testAll() {
        expectPreconditionFail(
            new TestSortables()
                .add(0, 1, 2, 3, 4).add(1).add(2).add(3).add(4).add(5, 1).add(6).add(7) //7
                .sortables
        );
    }
}
