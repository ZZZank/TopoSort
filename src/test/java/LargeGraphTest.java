import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import zank.lib.script_topo_sort.example.TestSortables;
import zank.lib.script_topo_sort.topo.TopoSort;

/**
 * @author ZZZank
 */
public class LargeGraphTest extends TopoTestBase {

    private static TestSortables initLarge() {
        return begin()
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
            .add(2, 1);//23
    }

    @Test
    void midTest() {
        val sortables = initLarge().sortables;
        Assertions.assertDoesNotThrow(() -> TopoSort.sort(sortables));
    }

    @Test
    void largeTest() {
        val sortables = initLarge()
            .addBatch(100, 2, 7, 5, 23) //simulate 'priority' setting
            .addBatch(100, 2, 7, 5, 123)
            .addBatch(100, 2, 7, 5, 223)
            .addBatch(500, 2, 7, 5, 234, 245, 256, 267, 278, 289, 323)
            .sortables;
        Assertions.assertDoesNotThrow(() -> TopoSort.sort(sortables));
    }
}
