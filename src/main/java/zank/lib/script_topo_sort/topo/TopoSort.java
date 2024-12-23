package zank.lib.script_topo_sort.topo;

import lombok.val;

import java.util.*;

/**
 * @author ZZZank
 */
public final class TopoSort {

    public static <T extends TopoSortable<T>> List<T> sort(Collection<T> input) {
        return sort(input instanceof List<?> l ? (List<T>) l : new ArrayList<>(input));
    }

    public static <T extends TopoSortable<T>> List<T> sort(List<T> input) {
        //indexing
        val indexes = new HashMap<T, Integer>();
        for (int i = 0, size = input.size(); i < size; i++) {
            val sortable = input.get(i);
            val old = indexes.put(sortable, i);
            if (old != null) {
                throw new IllegalArgumentException("values in index %s and %s are same values".formatted(i, old));
            }
        }
        //indexing dependencies
        val requiredBy = new HashMap<Integer, Set<Integer>>();
        val dependencyCounts = new HashMap<Integer, Integer>();
        for (val sortable : input) {
            val index = indexes.get(sortable);
            val dependencies = sortable.getDependencies();
            for (val dependency : dependencies) {
                val depIndex = indexes.get(dependency);
                if (depIndex == null) {
                    throw new IllegalArgumentException("%s (dependency of %s) not in input".formatted(dependency, sortable));
                }
                requiredBy.computeIfAbsent(depIndex, (k) -> new HashSet<>())
                    .add(index);
            }
            dependencyCounts.put(index, dependencies.size());
        }
        //sort
        val sorted = new ArrayList<T>();
        var sortableIndexes = indexes.values()
            .stream()
            .filter(i -> !requiredBy.containsKey(i))
            .toList();
        while (!sortableIndexes.isEmpty()) {
            val newlyFree = new ArrayList<Integer>();
            for (val free : sortableIndexes) {
                sorted.add(input.get(free));
                val depends = requiredBy.get(free);
                for (val depend : depends) {
                    val modified = dependencyCounts.get(depend) - 1;
                    dependencyCounts.put(depend, modified);
                    if (modified < 0) {
                        throw new IllegalStateException("what");
                    }
                    if (modified == 0) {
                        newlyFree.add(depend);
                    }
                }
            }
            sortableIndexes = newlyFree;
        }
        for (val depCount : dependencyCounts.values()) {
            if (depCount != 0) {
                throw new IllegalStateException("there are un-solved inputs");
            }
        }
        return sorted;
    }
}
