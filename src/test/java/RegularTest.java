import org.junit.jupiter.api.Test;

/**
 * @author ZZZank
 */
public class RegularTest extends TopoTestBase {

    @Test
    void test1() {
        expectPass(
            begin().add(2).add(0).add().sortables,
            2, 0, 1
        );
    }

    @Test
    void testNoDep() {
        expectPass(
            begin().add().add().add().sortables,
            0, 1, 2
        );
    }

    @Test
    void testSubLayer() {
        expectPass(
            begin().add(3).add(3).add(3).add().add(3).add(3).add(3).add(3).add(3).add(3).add(3).add(3).add(3).sortables,
            3, 0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12
        );
    }

    @Test
    void testManyLayers() {
        expectPass(
            begin().add(3).add(3).add(3).add().add(3).add(3).add(3, 5).add(3).add(6).add(6).add(6).add(3).add(3).sortables,
            3, 0, 1, 2, 4, 5, 7, 11, 12, 6, 8, 9, 10
        );
    }
}
