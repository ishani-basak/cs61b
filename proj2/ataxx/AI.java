/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.Random;

import static ataxx.PieceColor.*;
import static java.lang.Math.min;
import static java.lang.Math.max;
import java.util.ArrayList;

/** A Player that computes its own moves.
 *  @author Ishani Basak
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;
    /** A position magnitude indicating a win (for red if positive, blue
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. SEED is used to initialize
     *  a random-number generator for use in move computations.  Identical
     *  seeds produce identical behaviour. */
    AI(Game game, PieceColor myColor, long seed) {
        super(game, myColor);
        _random = new Random(seed);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getMove() {
        if (!getBoard().canMove(myColor())) {
            game().reportMove(Move.pass(), myColor());
            return "-";
        }
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        Main.reportTotalTimes();
        game().reportMove(move, myColor());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getBoard());
        _lastFoundMove = null;
        if (myColor() == RED) {
            minMax(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            minMax(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to the findMove method
     *  above. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove, int sense,
                       int alpha, int beta) {
        /* We use WINNING_VALUE + depth as the winning value so as to favor
         * wins that happen sooner rather than later (depth is larger the
         * fewer moves have been made. */
        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board, WINNING_VALUE + depth);
        }

        Move best = null;
        int bestScore;

        ArrayList<Move> possible = getColorMoves(board, board.whoseMove());
        if (possible.size() == 0) {
            possible.add(Move.pass());
        }

        if (sense == 1) {
            bestScore = -INFTY;
            for (Move m : possible) {
                Board copy = new Board(board);
                copy.makeMove(m);
                int score = minMax(copy, depth - 1, true, -1, alpha, beta);
                if (score > bestScore) {
                    best = m;
                    bestScore = score;
                    alpha = max(alpha, score);
                }
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            bestScore = INFTY;
            for (Move m : possible) {
                Board copy = new Board(board);
                copy.makeMove(m);
                int score = minMax(copy, depth - 1, true, 1, alpha, beta);
                if (score < bestScore) {
                    best = m;
                    bestScore = score;
                    beta = min(beta, score);
                }
                if (beta <= alpha) {
                    break;
                }
            }
        }
        if (best == null) {
            best = Move.PASS;
        }
        if (saveMove) {
            _lastFoundMove = best;
        }
        return bestScore;
    }

    /** Return a heuristic value for BOARD.  This value is +- WINNINGVALUE in
     *  won positions, and 0 for ties. */
    private int staticScore(Board board, int winningValue) {
        PieceColor winner = board.getWinner();
        if (winner != null) {
            return switch (winner) {
            case RED -> winningValue;
            case BLUE -> -winningValue;
            default -> 0;
            };
        }
        return board.numPieces(board.whoseMove())
                - board.numPieces(board.whoseMove().opposite());
    }

    private ArrayList<Move> getColorMoves(Board board, PieceColor color) {
        ArrayList<Move> colorMoves = new ArrayList<Move>();
        for (char col0 = 'a'; col0 <= 'g'; col0++) {
            for (char row0 = '1'; row0 <= '7'; row0++) {
                if (board.get(col0, row0) == color) {
                    for (int c = -2; c <= 2; c++) {
                        for (int r = -2; r <= 2; r++) {
                            if (!(c == 0 && r == 0)) {
                                Move m = Move.move(col0, row0,
                                        (char) (col0 + c), (char) (row0 + r));
                                if (board.legalMove(m)) {
                                    colorMoves.add(m);
                                }
                            }
                        }
                    }
                }
            }
        }
        return colorMoves;
    }

    /** Pseudo-random number generator for move computation. */
    private Random _random = new Random();
}
