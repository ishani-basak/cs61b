package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ishani Basak
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> ....
     *  java gitlet.Main add hello.txt */
    public static void main(String... args) throws IOException {
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        } else if (args.length == 1) {
            if (args[0].equals("init")) {
                CommandClass.init();
            } else if (args[0].equals("log")) {
                CommandClass.log();
            } else if (args[0].equals("global-log")) {
                CommandClass.globalLog();
            } else if (args[0].equals("status")) {
                CommandClass.status();
            }
        } else if (args.length == 2) {
            if (args[0].equals("add")) {
                CommandClass.add(args[1]);
            } else if (args[0].equals("commit")) {
                CommandClass.commit(args[1]);
            } else if (args[0].equals("rm")) {
                CommandClass.rm(args[1]);
            } else if (args[0].equals("find")) {
                CommandClass.find(args[1]);
            } else if (args[0].equals("branch")) {
                CommandClass.branch(args[1]);
            } else if (args[0].equals("rm-branch")) {
                CommandClass.rmBranch(args[1]);
            } else if (args[0].equals("reset")) {
                CommandClass.reset(args[1]);
            } else if (args[0].equals("merge")) {
                CommandClass.merge(args[1]);
            } else if (args[0].equals("checkout")) {
                CommandClass.checkout3(args[1]);
            } else {
                exitWithError("No command with that name exists.");
            }
        } else if (args.length == 3 && args[0].equals("checkout")) {
            CommandClass.checkout1(args[1], args[2]);
        } else if (args.length == 4 && args[0].equals("checkout")) {
            CommandClass.checkout2(args[1], args[2], args[3]);
        } else {
            exitWithError("No command with that name exists.");
        }
    }

    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(0);
    }
}
