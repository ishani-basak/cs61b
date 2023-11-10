/** HW #7, Two-sum problem.
 * @author Ishani Basak
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        for (int a = 0; a < A.length; a++) {
            for (int b = 0; b < B.length; b++) {
                if (A[a] + B[b] == m) {
                    return true;
                }
            }
        }
        return false;
    }

}
