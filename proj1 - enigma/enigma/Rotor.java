package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Ishani Basak
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = _permutation.wrap(posn);
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = alphabet().toInt(cposn);
    }

    /** Set ring setting.
     * @param c ring setting  */
    void setRing(char c) {
        _ringSetting = alphabet().toInt(c);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int inR = p + setting() - _ringSetting;
        int result = permutation().permute(inR);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(p + setting()));
        }
        int outR = result - setting() + _ringSetting;
        return permutation().wrap(outR);
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int inR = e + setting() - _ringSetting;
        int result = permutation().invert(inR);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(e + setting()));
        }
        int outR = result - setting() + _ringSetting;
        return permutation().wrap(outR);
    }

    /** Returns the positions of the notches, as a string giving the letters
     *  on the ring at which they occur. */
    String notches() {
        return "";
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        for (int s = 0; s < notches().length(); s++) {
            if (notches().charAt(s) == alphabet().toChar(_setting)) {
                return true;
            }
        }
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The current setting of each rotor. */
    private int _setting;

    /** Ring setting of current rotor. */
    private int _ringSetting = 0;
}
