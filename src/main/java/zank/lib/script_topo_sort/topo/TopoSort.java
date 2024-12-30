package zank.lib.script_topo_sort.topo;

import lombok.val;

import java.util.*;

/**
 * @author ZZZank
 */
public final class TopoSort {

    @SuppressWarnings({"unchecked", "unused"})
    public static <T extends TopoSortable<T>> List<T> sort(Collection<T> input)
        throws TopoNotSolved, TopoPreconditionFailed {
        return sort(input instanceof List<?> l ? (List<T>) l : new ArrayList<>(input));
    }

    private static <T extends TopoSortable<T>> HashMap<T, Integer> indexSortables(Collection<T> input)
        throws TopoPreconditionFailed {
        val toIndexes = new HashMap<T, Integer>();
        var i = 0;
        for (val sortable : input) {
            val old = toIndexes.put(sortable, i++);
            if (old != null) {
                throw new TopoPreconditionFailed("values in index %s and %s are same values", i, old);
            }
        }
        return toIndexes;
    }

    public static <T extends TopoSortable<T>> List<T> sort(List<T> input)
        throws TopoNotSolved, TopoPreconditionFailed {
        //construct object->index map, sorting will only use index for better generalization
        val indexes = indexSortables(input);

        //indexing dependencies
        val requiredBy = new HashMap<Integer, Set<Integer>>();
        val requires = new HashMap<Integer, Set<Integer>>();
        for (val e : indexes.entrySet()) {
            val sortable = e.getKey();
            val index = e.getValue();

            val dependencyIndexes = new HashSet<Integer>();
            for (T dependency : sortable.getDependencies()) {
                val depIndex = indexes.get(dependency);
                if (depIndex == null) {
                    throw new TopoPreconditionFailed("%s (dependency of %s) not in input", dependency, sortable);
                } else if (depIndex.equals(index)) {
                    throw new TopoPreconditionFailed("%s claimed itself as its dependency", sortable);
                }
                dependencyIndexes.add(depIndex);
                requiredBy.computeIfAbsent(depIndex, (k) -> new HashSet<>()).add(index);
            }

            requires.put(index, dependencyIndexes);
        }

        var avaliables = new ArrayList<Integer>();
        for (val e : indexes.entrySet()) {
            val dependencyCount = e.getKey().getDependencies().size();
            val index = e.getValue();
            if (dependencyCount == 0) {
                avaliables.add(index);
            }
        }

        //sort
        val sorted = new ArrayList<T>();
        while (!avaliables.isEmpty()) {
            val newlyFree = new ArrayList<Integer>();

            for (val free : avaliables) {
                sorted.add(input.get(free));
                val dependents = requiredBy.getOrDefault(free, Collections.emptySet());
                for (val dependent : dependents) {
                    val require = requires.get(dependent);
                    require.remove(free);
                    if (require.isEmpty()) {
                        newlyFree.add(dependent);
                    }
                }
            }

            avaliables = newlyFree;
        }
        validateResult(requires, input);
        return sorted;
    }

    private static <T extends TopoSortable<T>> void validateResult(
        Map<Integer, Set<Integer>> requires,
        List<T> input
    ) throws TopoNotSolved {
        for (val require : requires.values()) {
            if (!require.isEmpty()) {
                val unsolved = requires.entrySet()
                    .stream()
                    .filter(e -> !e.getValue().isEmpty())
                    .toList();
                throw new TopoNotSolved(unsolved, input);
            }
        }
    }

    public static <T extends TopoSortable<T>> List<T> sortDense(List<T> input)
        throws TopoNotSolved, TopoPreconditionFailed {
        val size = input.size();
        // construct object->index map, sorting will only use index for better generalization
        val indexed = indexSortables(input);

        val dependencies = new boolean[size][size];
        val dependencyCounts = new int[size];

        for (val e : indexed.entrySet()) {
            val sortable = e.getKey();
            val index = e.getValue();
            for (val dependency : sortable.getDependencies()) {
                val depIndex = indexed.get(dependency);
                if (depIndex == null) {
                    throw new TopoPreconditionFailed(
                        "%s (dependency of %s) not in input",
                        dependency,
                        sortable
                    );
                } else if (depIndex.equals(index)) {
                    throw new TopoPreconditionFailed("%s claimed itself as its dependency", sortable);
                }
                dependencies[index][depIndex] = true;
            }
            dependencyCounts[index] = sortable.getDependencies().size();
        }

        var avaliables = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            if (dependencyCounts[i] == 0) {
                avaliables.add(i);
            }
        }

        val sorted = new ArrayList<T>();
        while (!avaliables.isEmpty()) {
            val newlyFree = new ArrayList<Integer>();

            for (val free : avaliables) {
                sorted.add(input.get(free));
                for (int i = 0; i < size; i++) {
                    if (dependencies[i][free]) {
                        dependencies[i][free] = false;
                        val newCount = --dependencyCounts[i];
                        if (newCount == 0) {
                            newlyFree.add(i);
                        }
                    }
                }
            }

            avaliables = newlyFree;
        }

        for (int dependencyCount : dependencyCounts) {
            if (dependencyCount != 0) {
                throw new TopoNotSolved(new ArrayList<>(), new ArrayList<>());
            }
        }
        return sorted;
    }
}
