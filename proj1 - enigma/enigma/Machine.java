package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Ishani Basak
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        iterRotor = allRotors.iterator();
        _availRotors = new HashMap<String, Rotor>(allRotors.size());
        for (int i = 0; i < allRotors.size(); i++) {
            Rotor r = iterRotor.next();
            _availRotors.put(r.name(), r);
        }
        _rotors = new HashMap<Integer, Rotor>(numRotors);
        _plugboard = new Permutation("", alphabet());
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _rotors.get(k);
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rotors.clear();
        int i = 0;
        while (i < _numRotors) {
            if (_availRotors.containsKey(rotors[i])) {
                _rotors.put(i, _availRotors.get(rotors[i]));
                i++;
            } else {
                throw new EnigmaException("rotor not available");
            }

        }
        int r = 0;
        if (!_rotors.get(r).reflecting() || _rotors.get(r).rotates()) {
            throw new EnigmaException("not a reflector");
        }
        r++;
        while (r < _numRotors - _pawls) {
            if (_rotors.get(r).reflecting() || _rotors.get(r).rotates()) {
                throw new EnigmaException("not a fixed rotor");
            }
            r++;
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            _rotors.get(i + 1).set(setting.charAt(i));
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {
        HashMap<Rotor, Boolean> toAdvance = new HashMap<Rotor, Boolean>();
        for (int i = 0; i < _rotors.size(); i++) {
            toAdvance.put(_rotors.get(i), false);
        }
        toAdvance.replace(_rotors.get(_rotors.size() - 1), true);
        int r = _rotors.size() - 1;
        while (r > 0) {
            Rotor curr = _rotors.get(r);
            Rotor prev = _rotors.get(r - 1);
            if (curr.atNotch() && prev.rotates()) {
                toAdvance.replace(curr, true);
                toAdvance.replace(prev, true);
            }
            r--;
        }
        for (int j = 0; j < toAdvance.size(); j++) {
            if (toAdvance.get(_rotors.get(j))) {
                _rotors.get(j).advance();
            }
        }
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    int applyRotors(int c) {
        for (int r = _rotors.size() - 1; r >= 0; r--) {
            c = _rotors.get(r).convertForward(c);
        }
        for (int r = 1; r < _rotors.size(); r++) {
            c = _rotors.get(r).convertBackward(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (int s = 0; s < msg.length(); s++) {
            char initialChar = msg.charAt(s);
            int initialInt = alphabet().toInt(initialChar);
            int finalInt = convert(initialInt);
            result += Character.toString(alphabet().toChar(finalInt));
        }
        return result;
    }

    /** Set ring settings.
     * @param settings string of ring settings */
    void setRings(String settings) {
        for (int i = 1; i < _numRotors; i++) {
            Rotor r = _rotors.get(i);
            r.setRing(settings.charAt(i - 1));
        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors in machine. */
    private final int _numRotors;

    /** Number of pawls in machine. */
    private final int _pawls;

    /** Collection of rotors as an iterator. */
    private Iterator<Rotor> iterRotor;

    /** Hashmap of all available rotors. */
    private final HashMap<String, Rotor> _availRotors;

    /** Hashmap of machine's rotors. */
    private HashMap<Integer, Rotor> _rotors;

    /** Plugboard permutation. */
    private Permutation _plugboard;
}
