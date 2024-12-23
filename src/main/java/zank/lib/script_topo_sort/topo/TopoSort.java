package zank.lib.script_topo_sort.topo;

import lombok.val;

import java.util.*;

/**
 * @author ZZZank
 */
public final class TopoSort {

    @SuppressWarnings({"unchecked", "unused"})
    public static <T extends TopoSortable<T>> List<T> sort(Collection<T> input)
        throws TopoException, IllegalArgumentException {
        return sort(input instanceof List<?> l ? (List<T>) l : new ArrayList<>(input));
    }

    private static <T extends TopoSortable<T>> void indexSortableDependencies(
        Map<T, Integer> indexes,
        Map<Integer, Set<Integer>> requiredBy,
        Map<Integer, Set<Integer>> requires
    ) throws IllegalArgumentException {
        for (val e : indexes.entrySet()) {
            val sortable = e.getKey();
            val index = e.getValue();
            val dependencies = sortable.getDependencies();

            val dependencyIndexes = new HashSet<Integer>();
            for (val dependency : dependencies) {
                val depIndex = indexes.get(dependency);
                dependencyIndexes.add(depIndex);
                if (depIndex == null) {
                    throw new IllegalArgumentException("%s (dependency of %s) not in input".formatted(
                        dependency,
                        sortable
                    ));
                } else if (depIndex.equals(index)) {
                    throw new IllegalArgumentException("%s claimed itself as its dependency".formatted(sortable));
                }
                requiredBy.computeIfAbsent(depIndex, (k) -> new HashSet<>()).add(index);
            }

            requires.put(index, dependencyIndexes);
        }
    }

    private static <T extends TopoSortable<T>> HashMap<T, Integer> indexSortables(List<T> input)
        throws IllegalArgumentException {
        val toIndexes = new HashMap<T, Integer>();
        for (int i = 0, size = input.size(); i < size; i++) {
            val sortable = input.get(i);
            val old = toIndexes.put(sortable, i);
            if (old != null) {
                throw new IllegalArgumentException("values in index %s and %s are same values".formatted(i, old));
            }
        }
        return toIndexes;
    }

    public static <T extends TopoSortable<T>> List<T> sort(List<T> input) throws TopoException, IllegalArgumentException {
        //indexing
        val indexes = indexSortables(input);

        //indexing dependencies
        val requiredBy = new HashMap<Integer, Set<Integer>>();
        val requires = new HashMap<Integer, Set<Integer>>();
        indexSortableDependencies(indexes, requiredBy, requires);

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
    ) throws TopoException {
        for (val require : requires.values()) {
            if (!require.isEmpty()) {
                val unsolved = requires.entrySet()
                    .stream()
                    .filter(e -> !e.getValue().isEmpty())
                    .toList();
                throw new TopoException(unsolved, input);
            }
        }
    }

    public static <T extends TopoSortable<T>> List<T> sortDense(List<T> input)
        throws TopoException, IllegalArgumentException {
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
                    throw new IllegalArgumentException(String.format(
                        "%s (dependency of %s) not in input",
                        dependency,
                        sortable
                    ));
                } else if (depIndex.equals(index)) {
                    throw new IllegalArgumentException("%s claimed itself as its dependency".formatted(sortable));
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
                throw new TopoException(new ArrayList<>(), new ArrayList<>());
            }
        }
        return sorted;
    }
}
