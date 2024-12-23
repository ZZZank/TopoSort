package zank.lib.script_topo_sort;

import lombok.val;
import zank.lib.script_topo_sort.example.TestSortables;
import zank.lib.script_topo_sort.topo.TopoSort;
import zank.lib.script_topo_sort.topo.TopoException;

/**
 * @author ZZZank
 */
public class Main {
    public static void main(String[] args) {
        val sortables = new TestSortables()
            .add(1)//0
            .add(0, 3)//1
            .add(1)//2
            .add()//3
            .add(0)//4
            .add(2, 3, 4)//5
            .sortables;

        val out = System.out;
        out.println("data:");
        out.println(sortables.stream()
            .map(s -> {
                val builder = new StringBuilder();
                s.append(builder, true);
                builder.append('\n');
                return builder.toString();
            })
            .toList()
        );

        try {
            val sorted = TopoSort.sort(sortables);
            out.println("Sorted:");
            out.println(sorted);
        } catch (TopoException e) {
            out.println("Error happened:");
            out.println(e.getFullMessage());
        }
    }
}