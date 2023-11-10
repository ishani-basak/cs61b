package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;


import java.util.ArrayList;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Ishani Basak
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void testInvertChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('A', p.invert('C'));
        assertEquals('C', p.invert('D'));
        assertEquals('D', p.invert('B'));
    }

    @Test
    public void checkPermute() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("(ABCDEFG) (HIJK) (LMNOP)", alpha);

        assertEquals('N', perm.permute('M'));
        assertEquals('A', perm.permute('G'));
        assertEquals('S', perm.permute('S'));

        assertEquals(13, perm.permute(12));
        assertEquals(0, perm.permute(6));
        assertEquals(18, perm.permute(18));
    }

    @Test
    public void checkInvert() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("(ABCDEFG) (HIJK) (LMNOP)", alpha);

        assertEquals('M', perm.invert('N'));
        assertEquals('G', perm.invert('A'));
        assertEquals('S', perm.invert('S'));

        assertEquals(12, perm.invert(13));
        assertEquals(6, perm.invert(0));
        assertEquals(18, perm.invert(18));
    }

    @Test
    public void checkDerangement() {
        Alphabet alpha = getNewAlphabet();
        Permutation p1 = getNewPermutation("(ABCDEFG) (HIJK) (LMNOP) (QRSTUV) (WXYZ)", alpha);
        Permutation p2 = getNewPermutation("(ABCDEFG) (HIJK) (LMNOP)", alpha);
        Permutation p3 = getNewPermutation("(ABCDEFG) (LMNOP) (S) (V)", alpha);

        assertTrue(p1.derangement());
        assertFalse(p2.derangement());
        assertFalse(p3.derangement());
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.invert('F');
    }

    @Test
    public void checkSize() {
        Alphabet alpha1 = getNewAlphabet();
        Permutation perm1 = getNewPermutation("(ABCDEFG) (HIJK) (LMNOP)", alpha1);
        assertEquals(26, perm1.size());

        Alphabet alpha2 = getNewAlphabet("ABCDEFGHIJKLMNOPQRSTUV");
        Permutation perm2 = getNewPermutation("(ABCDEFG) (HIJK) (LMNOP)", alpha2);
        assertEquals(22, perm2.size());
    }
}
