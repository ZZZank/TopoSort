import lombok.val;
import org.junit.jupiter.api.Assertions;
import zank.lib.script_topo_sort.example.TestSortable;
import zank.lib.script_topo_sort.example.TestSortables;
import zank.lib.script_topo_sort.topo.TopoNotSolved;
import zank.lib.script_topo_sort.topo.TopoPreconditionFailed;
import zank.lib.script_topo_sort.topo.TopoSort;
import zank.lib.script_topo_sort.topo.TopoSortable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZZank
 */
public abstract class TopoTestBase {
    static TestSortables begin() {
        return new TestSortables();
    }

    static <T extends TopoSortable<T>> void expectPreconditionFail(List<T> sortables) {
        Assertions.assertThrows(
            TopoPreconditionFailed.class,
            () -> TopoSort.sort(sortables)
        );
        Assertions.assertThrows(
            TopoPreconditionFailed.class,
            () -> TopoSort.sortDense(sortables)
        );
    }

    static <T extends TopoSortable<T>> void expectNotSolved(List<T> sortables) {
        Assertions.assertThrows(
            TopoNotSolved.class,
            () -> TopoSort.sort(sortables)
        );
        Assertions.assertThrows(
            TopoNotSolved.class,
            () -> TopoSort.sortDense(sortables)
        );
    }

    static <T extends TopoSortable<T>> void expectRuntimeEx(List<T> sortables) {
        Assertions.assertThrows(
            RuntimeException.class,
            () -> TopoSort.sort(sortables)
        );
        Assertions.assertThrows(
            RuntimeException.class,
            () -> TopoSort.sortDense(sortables)
        );
    }

    static <T extends TopoSortable<T>> void expectPass(List<T> sortables, List<T> expected) {
        Assertions.assertEquals(expected, TopoSort.sort(sortables));
        Assertions.assertEquals(expected, TopoSort.sortDense(sortables));
    }

    static void expectPass(List<TestSortable> sortables, int... expected) {
        val expectedResult = Arrays.stream(expected)
            .mapToObj(TestSortable::new)
            .collect(Collectors.toList());
        Assertions.assertEquals(expectedResult, TopoSort.sort(sortables));
        Assertions.assertEquals(expectedResult, TopoSort.sortDense(sortables));
    }
}
