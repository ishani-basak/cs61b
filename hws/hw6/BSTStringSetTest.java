import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Test of a BST-based String Set.
 * @author Ishani Basak
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void testAll() {
        List<String> expected = new ArrayList<String>();
        expected.add("apple");
        expected.add("aztec");
        expected.add("brown");
        expected.add("water");
        BSTStringSet bst = new BSTStringSet();
        bst.put("apple");
        bst.put("aztec");
        bst.put("brown");
        bst.put("water");
        assertEquals(expected, bst.asList());
        assertTrue(bst.contains("apple"));
        assertFalse(bst.contains("grape"));
    }
}
