package zank.lib.script_topo_sort.topo;

import java.util.Collection;

/**
 * @author ZZZank
 */
public interface TopoSortable<T extends TopoSortable<T>> {

    Collection<T> getDependencies();
}
