package zank.lib.script_topo_sort;

import lombok.val;
import zank.lib.script_topo_sort.example.TestSortables;
import zank.lib.script_topo_sort.topo.TopoPreconditionFailed;
import zank.lib.script_topo_sort.topo.TopoSort;
import zank.lib.script_topo_sort.topo.TopoNotSolved;

/**
 * @author ZZZank
 */
public class Main {
    public static void main(String[] args) {
        val sortables = new TestSortables()
            .add()//0
            .add(0, 3)//1
            .add(1)//2
            .add()//3
            .add(0)//4
            .add(2, 3, 4)//5
            .add(2, 3, 4)//6
            .add(2, 3, 5)//7
            .add(2, 3, 5, 11)//8
            .add(2, 3, 5, 7)//9
            .add(1, 2, 4, 9)//10
            .add(2, 3, 5, 7, 17, 23)//11
            .add(2, 3, 5, 7)//12
            .add(2, 3, 5, 7, 17, 23)//13
            .add(2, 3, 5, 7, 0)//14
            .add(2, 3, 5, 7, 23)//15
            .add(1, 14, 7, 15)//16
            .add(2, 3, 5, 7, 16)//17
            .add(2, 8, 5, 17)//18
            .add(2, 3, 5, 7, 17)//19
            .add(2, 19, 22, 23)//20
            .add(2, 3, 5, 7, 17, 23)//21
            .add(2, 19, 11, 23)//22
            .add(2, 1)//23
            .addBatch(100, 2, 7, 5, 23) //simulate 'priority' setting
            .addBatch(100, 2, 7, 5, 123)
            .addBatch(100, 2, 7, 5, 223)
            .addBatch(500, 2, 7, 5, 234, 245, 256, 267, 278, 289, 323)
            .addBatch(500)
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
            for (int i = 0; i < 20; i++) {
                TopoSort.sort(sortables); //warming up
            }
            var nano = System.nanoTime();
            TopoSort.sort(sortables);
            out.printf("topo sort used %s\n", System.nanoTime() - nano);

            for (int i = 0; i < 20; i++) {
                TopoSort.sortDense(sortables); //warming up
            }
            nano = System.nanoTime();
            TopoSort.sortDense(sortables);
            out.printf("dense topo sort used %s\n", System.nanoTime() - nano);

            val sorted = TopoSort.sortDense(sortables);
            out.println("Sorted:");
            out.println(sorted);
        } catch (TopoNotSolved e) {
            out.println("Problem not solved:");
            out.println(e.getFullMessage());
        } catch (TopoPreconditionFailed e) {
            out.println("pre-condition failed");
            out.println(e.getMessage());
        }
    }
}