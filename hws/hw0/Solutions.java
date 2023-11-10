/** Solutions to the HW0 Java101 exercises.
 *  @author Allyson Park and Ishani Basak
 */
public class Solutions {

    /** Returns whether or not the input x is even.
     */
    public static boolean isEven(int x) {
        return (x % 2 == 0);
    }

    public static int max(int[] a) {
        int m = a[0];
        for (int i : a) {
            if (i > m) {
                m = i;
            }
        }
        return m;
    }

    public static boolean wordBank(String word, String[] bank) {
        for (String s : bank) {
            if (word.equals(s)) {
                return true;
            }
        } 
        return false;
    }

    public static boolean threeSum(int[] a) {
        for (int x : a) {
            for (int y : a) {
                for (int z : a) {
                    if (x + y + z == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // TODO: Fill in the method signatures for the other exercises
    // Your methods should be static for this HW. DO NOT worry about what this means.
    // Note that "static" is not necessarily a default, it just happens to be what
    // we want for THIS homework. In the future, do not assume all methods should be
    // static.

}
