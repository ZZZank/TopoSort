package zank.lib.script_topo_sort.example;

import lombok.val;
import zank.lib.script_topo_sort.topo.TopoSortable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author ZZZank
 */
public class TestSortable implements TopoSortable<TestSortable> {
    public final int index;
    public final List<TestSortable> dependencies;

    public TestSortable(int index, int... dependencies) {
        this.index = index;
        this.dependencies = Arrays.stream(dependencies).mapToObj(TestSortable::new).toList();
    }

    @Override
    public Collection<TestSortable> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TestSortable that && index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(index);
    }

    @Override
    public String toString() {
        val builder = new StringBuilder();
        append(builder, false);
        return builder.toString();
    }

    public void append(StringBuilder builder, boolean includeDependencies) {
        builder.append("Test{").append(index);
        if (includeDependencies) {
            builder.append(", dependencies=").append(dependencies.stream().map(d -> d.index).toList());
        }
        builder.append('}');
    }
}
