import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/** HW #7, Sorting ranges.
 *  @author Ishani
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        ArrayList<int[]> ranges = new ArrayList<int[]>();
        if (intervals.size() == 0) {
            return 0;
        }
        for (int i = 0; i < intervals.size(); i++) {
            ranges.add(intervals.get(i));
            checkRanges(ranges);
        }
        int sum = 0;
        for (int s = 0; s < ranges.size(); s++) {
            sum += (ranges.get(s)[1] - ranges.get(s)[0]);
        }
        return sum;
    }

    public static ArrayList<int[]> checkRanges(ArrayList<int[]> ranges) {
        for (int i = 0; i < ranges.size(); i++) {
            int low = ranges.get(i)[0];
            int high = ranges.get(i)[1];
            int thisLow = ranges.get(ranges.size()-1)[0];
            int thisHigh = ranges.get(ranges.size()-1)[1];
            if ((thisLow > low && thisLow < high) || (thisHigh > low && thisHigh < high)) {
                ranges.set(i, new int[]{Math.min(low, thisLow), Math.max(high, thisHigh)});
                ranges.remove(ranges.size()-1);
            }
        }
        return ranges;
    }

    /** Test intervals. */
    static final int[][] INTERVALS1 = {
        {19, 30}, {8, 15}, {3, 10}, {6, 12}, {4, 5}
    };

    static final int[][] INTERVALS2 = {
            {0, 10}, {15, 25}, {30, 40}
    };

    static final int[][] INTERVALS3 = {
            {0, 15}, {10, 25}, {20, 40}
    };

    /** Covered length of INTERVALS. */
    static final int CORRECT1 = 23;
    static final int CORRECT2 = 30;
    static final int CORRECT3 = 40;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT1, coveredLength(Arrays.asList(INTERVALS1)));
        assertEquals(CORRECT2, coveredLength(Arrays.asList(INTERVALS2)));
        assertEquals(CORRECT3, coveredLength(Arrays.asList(INTERVALS3)));
    }

    @Test
    public void emptyTest() {
        assertEquals(0, coveredLength(Arrays.asList(new int[][]{})));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
