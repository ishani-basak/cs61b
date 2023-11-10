package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  @author Ishani Basak
 */

public class ListsTest {

    @Test
    public void basicRunsTest() {
        IntList input = IntList.list(1, 2, 3, 1, 2);
        IntList run1 = IntList.list(1, 2, 3);
        IntList run2 = IntList.list(1, 2);
        IntListList result = IntListList.list(run1, run2);
        assertEquals(result, Lists.naturalRuns(input));
    }

    @Test
    public void testNaturalRuns() {
        IntList i1 = IntList.list(1, 3, 8, 2, 7, 5, 3, 8, 9);
        IntListList o1 = IntListList.list(
                IntList.list(1, 3, 8),
                IntList.list(2, 7),
                IntList.list(5),
                IntList.list(3, 8, 9));
        assertEquals(o1, Lists.naturalRuns(i1));

        IntList i2 = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        IntListList o2 = IntListList.list(
                IntList.list(1, 3, 7),
                IntList.list(5),
                IntList.list(4, 6, 9, 10),
                IntList.list(10, 11));
        assertEquals(o2, Lists.naturalRuns(i2));

        IntList i3 = IntList.list(1, 1, 1);
        IntListList o3 = IntListList.list(
                IntList.list(1),
                IntList.list(1),
                IntList.list(1));
        assertEquals(o3, Lists.naturalRuns(i3));

        IntList i4 = IntList.list(1, 11, 111);
        IntListList o4 = IntListList.list(IntList.list(1, 11, 111));
        assertEquals(o4, Lists.naturalRuns(i4));

        IntList i5 = IntList.list(123, 12, 1);
        IntListList o5 = IntListList.list(
                IntList.list(123),
                IntList.list(12),
                IntList.list(1));
        assertEquals(o5, Lists.naturalRuns(i5));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
