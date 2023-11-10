import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        assertEquals(9, MultiArr.maxValue(new int[][]{{1, 6}, {9, 1}, {8, 6}}));
        assertEquals(4, MultiArr.maxValue(new int[][]{{-3, -10, 2, 2}, {4, -6, -6, -7}}));
        assertEquals(0, MultiArr.maxValue(new int[][]{{-5}, {-6}, {-10}, {0}, {-4}}));
    }

    @Test
    public void testAllRowSums() {
        assertArrayEquals(new int[]{7, 10, 14},
                MultiArr.allRowSums(new int[][]{{1, 6}, {9, 1}, {8, 6}}));
        assertArrayEquals(new int[]{-9, -15},
                MultiArr.allRowSums(new int[][]{{-3, -10, 2, 2}, {4, -6, -6, -7}}));
        assertArrayEquals(new int[]{-5, -6, -10, 0, -4},
                MultiArr.allRowSums(new int[][]{{-5}, {-6}, {-10}, {0}, {-4}}));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
