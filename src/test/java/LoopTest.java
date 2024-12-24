import lombok.val;
import org.junit.jupiter.api.Test;

/**
 * @author ZZZank
 */
public class LoopTest extends TopoTestBase {

    @Test
    void loop2() {
        expectNotSolved(begin().add(1).add(0).sortables);
    }

    @Test
    void loopMany() {
        val testSortables = begin();
        int limit = 500;
        for (int i = 0; i < limit ; i++) {
            testSortables.add(i + 1);
        }
        testSortables.add(0);
        expectNotSolved(testSortables.sortables);
    }
}
