package zank.lib.script_topo_sort.example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZZZank
 */
public class IntSortables {
    public final List<IntSortable> sortables = new ArrayList<>();
    private int currentSize = 0;

    public IntSortables add(int... dependencies) {
        this.sortables.add(new IntSortable(currentSize++, dependencies));
        return this;
    }

    public int getCurrentSize() {
        return currentSize;
    }
}
