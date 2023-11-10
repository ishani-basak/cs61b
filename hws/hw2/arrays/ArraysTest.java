package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  @author Ishani Basak
 */

public class ArraysTest {

    @Test
    public void testCatenate() {
        int[] a = new int[]{1, 2, 3};
        int[] b = new int[0];
        int[] c = new int[]{4, 4};
        assertArrayEquals(a, Arrays.catenate(a, b));
        assertArrayEquals(new int[]{1, 2, 3, 4, 4}, Arrays.catenate(a, c));
        assertArrayEquals(a, Arrays.catenate(b, a));
        assertArrayEquals(c, Arrays.catenate(b, c));
        assertArrayEquals(new int[]{4, 4, 1, 2, 3}, Arrays.catenate(c, a));
        assertArrayEquals(c, Arrays.catenate(c, b));
    }

    @Test
    public void testRemove() {
        int[] a = new int[]{0, 1, 2, 3, 4};
        int[] b = new int[]{5, 6, 7, 8, 9};
        int[] c = new int[]{1, 3, 5, 7, 9, 0, 2, 4, 6, 8};
        assertArrayEquals(new int[]{1, 2, 3, 4}, Arrays.remove(a, 0, 1));
        assertArrayEquals(new int[]{5, 9}, Arrays.remove(b, 1, 3));
        assertArrayEquals(new int[]{1, 3, 5, 7, 9}, Arrays.remove(c, 5, 5));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
