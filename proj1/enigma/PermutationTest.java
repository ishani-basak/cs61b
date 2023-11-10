package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.ArrayList;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Ishani Basak
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkAddCycle() {
        perm = new Permutation("(ABCDEFG) (HIJK)", new Alphabet());
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("ABCDEFG");
        strings.add("HIJK");

        perm.addCycle("(LMNOP)");
        strings.add("LMNOP");
        assertEquals(perm.cycles(), strings);
        perm.addCycle("(QRSTUV)");
        strings.add("QRSTUV");
        assertEquals(perm.cycles(), strings);
        perm.addCycle("(WXYZ)");
        strings.add("WXYZ");
        assertEquals(perm.cycles(), strings);
    }

    @Test
    public void checkPermute() {
        perm = new Permutation("(ABCDEFG) (HIJK)", new Alphabet());
        perm.addCycle("(LMNOP)");

        assertEquals('N', perm.permute('M'));
        assertEquals('A', perm.permute('G'));
        assertEquals('S', perm.permute('S'));

        assertEquals(13, perm.permute(12));
        assertEquals(0, perm.permute(6));
        assertEquals(18, perm.permute(18));
    }

    @Test
    public void checkInvert() {
        perm = new Permutation("(ABCDEFG) (HIJK)", new Alphabet());
        perm.addCycle("(LMNOP)");

        assertEquals('M', perm.invert('N'));
        assertEquals('G', perm.invert('A'));
        assertEquals('S', perm.invert('S'));

        assertEquals(12, perm.invert(13));
        assertEquals(6, perm.invert(0));
        assertEquals(18, perm.invert(18));
    }

    @Test
    public void checkDerangement() {
        perm = new Permutation("(ABCDEFG) (HIJK) (LMNOP) (QRSTUV) (WXYZ)",
                new Alphabet());
        assertTrue(perm.derangement());

        perm = new Permutation("(ABCDEFG) (LMNOP)",
                new Alphabet("ABCDEFGLMNOP"));
        assertTrue(perm.derangement());

        perm = new Permutation("(ABCDEFG) (LMNOP) (S) (V)", new Alphabet());
        assertFalse(perm.derangement());
    }

    @Test
    public void checkSize() {
        Alphabet alpha1 = new Alphabet();
        Permutation perm1 = new Permutation("(ABCDEFG) (HIJK) (LMNOP)", alpha1);
        assertEquals(26, perm1.size());

        Alphabet alpha2 = new Alphabet("ABCDEFGHIJKLMNOPQRSTUV");
        Permutation perm2 = new Permutation("(ABCDEFG) (HIJK) (LMNOP)", alpha2);
        assertEquals(22, perm2.size());
    }
}
