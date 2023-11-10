import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Ishani Basak
 */
public class ECHashStringSetTest  {

    @Test
    public void testAll() {
        List<String> expected = new ArrayList<String>();
        expected.add("Jeans");
        expected.add("Apple");
        expected.add("Bottom");
        expected.add("Boots");
        ECHashStringSet ec = new ECHashStringSet();
        ec.put("Apple");
        ec.put("Bottom");
        ec.put("Jeans");
        ec.put("Boots");
        assertEquals(expected, ec.asList());
        assertTrue(ec.contains("Apple"));
        assertFalse(ec.contains("Grape"));
        ec.put("A");
        ec.put("B");
        ec.put("C");
        ec.put("D");
        ec.put("E");
        ec.put("F");
        ec.put("G");
        ec.put("H");
        ec.put("I");
        ec.put("J");
        ec.put("K");
        ec.put("L");
        ec.put("M");
        ec.put("N");
        ec.put("O");
        ec.put("P");
        ec.put("Q");
        ec.put("R");
        ec.put("S");
        ec.put("T");
        ec.put("U");
        ec.put("V");
        ec.put("W");
        ec.put("X");
        ec.put("Y");
        ec.put("Z");
    }
}
