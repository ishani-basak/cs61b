package blocks;

import java.util.Random;

import static blocks.Utils.*;

/** A creator of random Blocks puzzles.
 *  @author Ishani Basak
 */
class PuzzleGenerator implements PuzzleSource {

    /** A new PuzzleGenerator whose random-number source is seeded
     *  with SEED. */
    PuzzleGenerator(long seed) {
        _random = new Random(seed);
    }

    /* By default, the convention is that overriding methods have the same
     * comment as on the method they override. See PuzzleSource. */
    @Override
    public boolean deal(Model model, int handSize) {
        assert handSize > 0;
        model.clearHand();
        int x = 0;
        while (x < handSize) {
            model.deal(PIECES[_random.nextInt(PIECES.length)]);
            x += 1;
        }
        return true;
    }

    @Override
    public void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** My PNRG. */
    private Random _random;

    /** Pieces available to be dealt to hand. */
    static final Piece[] PIECES = {
        new Piece("*** *** ***"),
        new Piece("** **"),
        new Piece("*"),

        new Piece("** ** **"),
        new Piece("*** ***"),

        new Piece("**"),
        new Piece("* *"),
        new Piece("***"),
        new Piece("* * *"),

        new Piece("** *."),
        new Piece("** .*"),
        new Piece("*. **"),
        new Piece(".* **"),

        new Piece("** *. **"),
        new Piece("** .* **"),
        new Piece("*.* ***"),
        new Piece("*** *.*"),

        new Piece("*.. *** *.."),
        new Piece("..* *** ..*"),
        new Piece(".*. .*. ***"),
        new Piece("*** .*. .*."),

        new Piece("*** ..* ..*"),
        new Piece("..* ..* ***"),
        new Piece("*** *.. *.."),
        new Piece("*.. *.. ***"),

        new Piece("** *. *."),
        new Piece("** .* .*"),
        new Piece("*.. ***"),
        new Piece("*** *.."),

        new Piece("*. .*"),
        new Piece(".* *."),

        new Piece("*.. .*. ..*"),
        new Piece("..* .*. *.."),

        new Piece("*.* .*. *.*"),
        new Piece(".*. *.* .*."),

        new Piece(".*. *** .*."),

        new Piece(".** **."),
        new Piece("**. .**"),
        new Piece("*. ** .*"),
        new Piece(".* ** *.")
    };

}