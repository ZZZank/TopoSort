package zank.lib.script_topo_sort.example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZZZank
 */
public class TestSortables {
    public final List<TestSortable> sortables = new ArrayList<>();
    private int currentSize = 0;

    public TestSortables add(int... dependencies) {
        this.sortables.add(new TestSortable(currentSize++, dependencies));
        return this;
    }

    public TestSortables addBatch(int batch, int... dependencies) {
        for (int i = 0; i < batch; i++) {
            add(dependencies);
        }
        return this;
    }

    public int getCurrentSize() {
        return currentSize;
    }
}
