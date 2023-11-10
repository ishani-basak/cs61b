import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/** HW #7, Count inversions.
 *  @author Ishani Basak
 */
public class Inversions {

    /** A main program for testing purposes.  Prints the number of inversions
     *  in the sequence ARGS. */
    public static void main(String[] args) {
        System.out.println(inversions(Arrays.asList(args)));
    }

    /** Return the number of inversions of T objects in ARGS. */
    public static <T extends Comparable<? super T>> int inversions(List<T> args) {
        int count = 0;
        for (int i = 0; i < args.size(); i++) {
            for (int j = i + 1; j < args.size(); j++) {
                if (args.get(i).compareTo(args.get(j)) > 0) {
                    count++;
                }
            }
        }
        return count;
    }
}
