package enigma;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ishani Basak
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String[] c = cycles.split(" ");
        _cycles = new ArrayList<String>();
        for (String s : c) {
            addCycle(s);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    void addCycle(String cycle) {
        if (cycle.length() >= 3) {
            _cycles.add(cycle.substring(1, cycle.length() - 1));
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char letter = _alphabet.toChar(wrap(p));
        char c = permute(letter);
        return _alphabet.toInt(c);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char letter = _alphabet.toChar(wrap(c));
        char p = invert(letter);
        return _alphabet.toInt(p);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        for (String s : _cycles) {
            if (s.contains(Character.toString(p))) {
                int index = s.indexOf(p);
                if (index == s.length() - 1) {
                    return s.charAt(0);
                } else {
                    return s.charAt(index + 1);
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (String s : _cycles) {
            if (s.contains(Character.toString(c))) {
                int index = s.indexOf(c);
                if (index == 0) {
                    return s.charAt(s.length() - 1);
                } else {
                    return s.charAt(index - 1);
                }
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    ArrayList<String> cycles() {
        return _cycles;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int letters = 0;
        for (String s : _cycles) {
            letters += s.length();
        }
        if (letters != _alphabet.size()) {
            return false;
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Permutation cycles. */
    private ArrayList<String> _cycles;
}
