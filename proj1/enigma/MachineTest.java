package enigma;

import java.util.HashMap;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Ishani Basak
 */
public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTS ***** */

    private static final Alphabet AZ = new Alphabet(TestUtils.UPPER_STRING);

    private static final HashMap<String, Rotor> ROTORS = new HashMap<>();

    static {
        HashMap<String, String> nav = TestUtils.NAVALA;

        ROTORS.put("B", new Reflector("B", new Permutation(nav.get("B"), AZ)));
        ROTORS.put("C", new Reflector("C", new Permutation(nav.get("C"), AZ)));
        ROTORS.put("Beta",
                new FixedRotor("Beta",
                        new Permutation(nav.get("Beta"), AZ)));
        ROTORS.put("Gamma",
                new FixedRotor("Gamma",
                        new Permutation(nav.get("Gamma"), AZ)));
        ROTORS.put("I",
                new MovingRotor("I",
                        new Permutation(nav.get("I"), AZ), "Q"));
        ROTORS.put("II",
                new MovingRotor("II",
                        new Permutation(nav.get("II"), AZ), "E"));
        ROTORS.put("III",
                new MovingRotor("III",
                        new Permutation(nav.get("III"), AZ), "V"));
        ROTORS.put("IV",
                new MovingRotor("IV", new Permutation(nav.get("IV"), AZ),
                        "J"));
        ROTORS.put("V",
                new MovingRotor("V", new Permutation(nav.get("V"), AZ),
                        "Z"));
        ROTORS.put("VI",
                new MovingRotor("VI", new Permutation(nav.get("VI"), AZ),
                        "ZM"));
        ROTORS.put("VII",
                new MovingRotor("VII", new Permutation(nav.get("VII"), AZ),
                        "ZM"));
        ROTORS.put("VIII",
                new MovingRotor("VIII", new Permutation(nav.get("VIII"), AZ),
                        "ZM"));
    }

    private static final String[] ROTORS1 = { "B", "Beta", "III", "IV", "I" };
    private static final String SETTING1 = "AXLE";

    private Machine mach1() {
        Machine mach = new Machine(AZ, 5, 3, ROTORS.values());
        mach.insertRotors(ROTORS1);
        mach.setRotors(SETTING1);
        return mach;
    }

    @Test
    public void testInsertRotors() {
        Machine mach = new Machine(AZ, 5, 3, ROTORS.values());
        mach.insertRotors(ROTORS1);
        assertEquals(5, mach.numRotors());
        assertEquals(3, mach.numPawls());
        assertEquals(AZ, mach.alphabet());
        assertEquals(ROTORS.get("B"), mach.getRotor(0));
        assertEquals(ROTORS.get("Beta"), mach.getRotor(1));
        assertEquals(ROTORS.get("III"), mach.getRotor(2));
        assertEquals(ROTORS.get("IV"), mach.getRotor(3));
        assertEquals(ROTORS.get("I"), mach.getRotor(4));
    }

    @Test(expected = EnigmaException.class)
    public void testBadRotors() {
        Machine mach = new Machine(AZ, 5, 3, ROTORS.values());
        String[] rotors2 = { "B", "C", "III", "IX", "I" };
        mach.insertRotors(rotors2);
    }

    @Test
    public void testConvertChar() {
        Machine mach = mach1();
        mach.setPlugboard(new Permutation("(YF) (HZ)", AZ));
        assertEquals(25, mach.convert(24));
    }

    @Test
    public void testConvertMsg() {
        Machine mach = mach1();
        mach.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)", AZ));
        assertEquals("QVPQSOKOILPUBKJZPISFXDW",
                mach.convert("FROMHISSHOULDERHIAWATHA"));
    }
}
