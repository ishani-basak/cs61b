package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collection;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ishani Basak
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        String currLine = _input.nextLine();
        if (currLine.charAt(0) != '*') {
            throw new EnigmaException("not starting with "
                    + "configuration settings");
        }
        while (currLine != null) {
            if (currLine != "" && currLine.charAt(0) == '*') {
                setUp(m, currLine);
            } else {
                String input = "";
                String[] line = currLine.split("\s");
                for (String s : line) {
                    input += s;
                }
                String output = m.convert(input);
                printMessageLine(output);
            }
            if (_input.hasNextLine()) {
                currLine = _input.nextLine();
            } else {
                currLine = null;
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        String currToken = _config.next();
        if (currToken.matches("\\S{3,}")) {
            _alphabet = new Alphabet(currToken);
        } else {
            throw new EnigmaException("no alphabet");
        }

        currToken = _config.next();
        int numRotors;
        int pawls;
        if (currToken.matches("\\d")) {
            numRotors = Integer.parseInt(currToken);
        } else {
            throw new EnigmaException("not a number");
        }
        currToken = _config.next();
        if (currToken.matches("\\d")) {
            pawls = Integer.parseInt(currToken);
        } else {
            throw new EnigmaException("not a number");
        }

        Collection<Rotor> allRotors = new ArrayList<Rotor>();
        while (_config.hasNext()) {
            allRotors.add(readRotor());
        }
        return new Machine(_alphabet, numRotors, pawls, allRotors);
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        String name = _config.next();
        String mn = _config.next();
        String cycles = "";
        String nextToken;
        while (_config.hasNext("(\\((\\w|\\.)+\\))+")) {
            nextToken = _config.next();
            if (nextToken.matches(".*\\)\\(.*")) {
                int index = nextToken.substring(1).indexOf('(');
                nextToken = nextToken.substring(0, index + 1)
                        + " " + nextToken.substring(index + 1);
            }
            cycles += nextToken + " ";
        }
        if (_config.hasNext("\\(\\w+")) {
            throw new EnigmaException("bad cycle");
        }
        if (mn.charAt(0) == 'M') {
            return new MovingRotor(name, new Permutation(cycles, _alphabet),
                    mn.substring(1));
        } else if (mn.charAt(0) == 'N') {
            return new FixedRotor(name, new Permutation(cycles, _alphabet));
        } else {
            return new Reflector(name, new Permutation(cycles, _alphabet));
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] set = settings.split(" ");
        String[] rotors = new String[M.numRotors()];
        System.arraycopy(set, 1, rotors, 0,
                M.numRotors());
        if (rotors.length < M.numRotors()) {
            throw new EnigmaException("incorrect number of rotors");
        }
        M.insertRotors(rotors);
        M.setRotors(set[M.numRotors() + 1]);
        if (set.length > M.numRotors() + 2) {
            if (set[M.numRotors() + 2].charAt(0) != '(') {
                String rings = set[M.numRotors() + 2];
                M.setRings(rings);
                if (set.length > M.numRotors() + 3) {
                    String cycles = "";
                    for (int s = M.numRotors() + 3; s < set.length; s++) {
                        cycles += set[s] + " ";
                    }
                    M.setPlugboard(new Permutation(cycles, _alphabet));
                }
            } else {
                String cycles = "";
                for (int s = M.numRotors() + 2; s < set.length; s++) {
                    cycles += set[s] + " ";
                }
                M.setPlugboard(new Permutation(cycles, _alphabet));
            }
        }
    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        while (msg.length() > 5) {
            for (int i = 0; i < 5; i++) {
                _output.print(msg.charAt(i));
            }
            _output.print(' ');
            msg = msg.substring(5);
        }
        for (int i = 0; i < msg.length(); i++) {
            _output.print(msg.charAt(i));
        }
        _output.println();
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}
