package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;

public class CommandClass {

    /** current working directory. */
    static final File CWD = new File(".");

    /** .gitlet directory. */
    static final File GITLET_FOLDER =
            Utils.join(CWD, ".gitlet");

    /** commit directory. */
    static final File COMMIT_DIR =
            Utils.join(GITLET_FOLDER, "commits");

    /** staging area directory. */
    private static File _stagingArea =
            Utils.join(GITLET_FOLDER, "stagingArea");

    /** addition stage directory. */
    private static File _stagedForAddition =
            Utils.join(_stagingArea, "additions");

    /** removal stage directory. */
    private static File _stagedForRemoval =
            Utils.join(_stagingArea, "removals");

    /** directory of branches. */
    private static File _branches =
            Utils.join(GITLET_FOLDER, "branches");

    /** points to head commit. */
    private static File _head = Utils.join(GITLET_FOLDER, "head");

    /** points to original branch. */
    private static File _master = Utils.join(_branches, "master");

    /** points to active branch. */
    private static File _active = Utils.join(GITLET_FOLDER, "active");

    /** creates staging area object. */
    private static StagingArea stagingArea = new StagingArea();

    public static void init() throws IOException {
        if (GITLET_FOLDER.exists()) {
            Main.exitWithError("Gitlet version-control system "
                    + "already exists in the current directory.");
        } else {
            GITLET_FOLDER.mkdir();
            COMMIT_DIR.mkdir();
            _stagingArea.mkdir();
            _stagedForAddition.createNewFile();
            _stagedForRemoval.createNewFile();
            stagingArea.clearStagingArea();
            _branches.mkdir();
            _head.createNewFile();
            _master.createNewFile();
            _active.createNewFile();

            Utils.writeContents(_active, _master.getPath());
            Commit c = new Commit("initial commit");
            createCommit(c);
        }
    }

    @SuppressWarnings("unchecked")
    public static void add(String filename) throws IOException {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        File f = Utils.join(CWD, filename);
        if (!f.exists()) {
            Main.exitWithError("File does not exist.");
        }
        TreeMap<String, Blob> t =
                Utils.readObject(_stagedForAddition, TreeMap.class);
        Commit c = Utils.readObject(_master, Commit.class);
        if (t.containsKey(filename)) {
            Blob b = new Blob(filename);
            if (b.getContent().equals(c.getBlob(filename).getContent())) {
                stagingArea.removeFromStagingArea(filename);
            } else {
                stagingArea.removeFromStagingArea(filename);
                stagingArea.addFile(filename, b);
            }
        } else {
            Blob b = new Blob(filename);
            stagingArea.addFile(filename, b);
        }
    }

    @SuppressWarnings("unchecked")
    public static void commit(String message) throws IOException {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        TreeMap<String, Blob> a =
                Utils.readObject(_stagedForAddition, TreeMap.class);
        ArrayList<String> r =
                Utils.readObject(_stagedForRemoval, ArrayList.class);
        if (a.size() == 0 && r.size() == 0) {
            Main.exitWithError("No changes added to the commit.");
        } else if (message.equals("")) {
            Main.exitWithError("Please enter a commit message.");
        } else {
            Commit parent = Utils.readObject(activeBranch(), Commit.class);
            Commit thisCommit = new Commit(message, parent);
            thisCommit.trackFiles();
            createCommit(thisCommit);
        }
    }

    public static void rm(String filename) throws IOException {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        Commit headCommit = getHead();
        if (!headCommit.isTracked(filename)
                && (!stagingArea.toBeAdded(filename)
                && !stagingArea.toBeRemoved(filename))) {
            Main.exitWithError("No reason to remove the file.");
        } else {
            stagingArea.removeFile(filename);
            File f = Utils.join(GITLET_FOLDER, filename);
            f.delete();
        }
    }

    public static void log() {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        Commit log = getHead();
        while (log.getParent() != null) {
            log.history();
            log = log.getParent();
            System.out.println();
        }
        log.history();
    }

    public static void globalLog() {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        List<String> allCommitFiles = Utils.plainFilenamesIn(COMMIT_DIR);
        for (String s : allCommitFiles) {
            File filename = Utils.join(COMMIT_DIR, s);
            Commit c = Utils.readObject(filename, Commit.class);
            c.history();
        }
    }

    public static void find(String message) {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        boolean messageExists = false;
        List<String> allCommitFiles = Utils.plainFilenamesIn(COMMIT_DIR);
        for (String s : allCommitFiles) {
            File filename = Utils.join(COMMIT_DIR, s);
            Commit c = Utils.readObject(filename, Commit.class);
            if (c.getMessage().equals(message)) {
                System.out.println(c.getHash());
                messageExists = true;
            }
        }
        if (!messageExists) {
            Main.exitWithError("Found no commit with that message.");
        }
    }

    @SuppressWarnings("unchecked")
    public static void status() {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        printBranches();
        printStagedFiles();
        printRemovedFiles();
        printModifications();
        printUntrackedFiles();
    }

    public static void checkout1(String fill,
                                 String filename) throws IOException {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        if (!fill.equals("--")) {
            Main.exitWithError("Incorrect operands.");
        }
        Commit headCommit = Utils.readObject(_master, Commit.class);
        if (!headCommit.isTracked(filename)) {
            Main.exitWithError("File does not exist in that commit.");
        }
        File f = Utils.join(CWD, filename);
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        Blob newF = headCommit.getBlob(filename);
        Utils.writeContents(f, newF.getContent());
    }

    public static void checkout2(String id, String fill,
                                 String filename) throws IOException {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        if (!fill.equals("--")) {
            Main.exitWithError("Incorrect operands.");
        }
        Commit c = getCommitFromId(id);
        if (!c.isTracked(filename)) {
            Main.exitWithError("File does not exist in that commit.");
        }
        File f = Utils.join(CWD, filename);
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        Blob newF = c.getBlob(filename);
        Utils.writeContents(f, newF.getContent());
    }

    public static void checkout3(String branch) throws IOException {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        File f = Utils.join(_branches, branch);
        if (!f.exists()) {
            Main.exitWithError("No such branch exists.");
        }
        if (f.getPath().equals(activeBranch().getPath())) {
            Main.exitWithError("No need to checkout the current branch.");
        }
        List<String> filenames = Utils.plainFilenamesIn(CWD);
        for (String name : filenames) {
            if (!stagingArea.toBeAdded(name)
                    && !getHead().isTracked(name)) {
                Main.exitWithError("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
            }
        }
        Utils.writeContents(_active, f.getPath());
        Commit c = Utils.readObject(activeBranch(), Commit.class);
        for (String filename : filenames) {
            if (!c.isTracked(filename)) {
                if (getHead().isTracked(filename)) {
                    File n = new File(CWD, filename);
                    n.delete();
                }
            } else {
                checkout2(c.getHash(), "--", filename);
            }
        }
        List<String> trackedFiles =
                new ArrayList<>(c.getFileTracker().keySet());
        for (String name : trackedFiles) {
            if (!filenames.contains(name)) {
                checkout2(c.getHash(), "--", name);
            }
        }
        Utils.writeObject(_head, Utils.readObject(activeBranch(),
                Commit.class));
    }

    public static void branch(String branch) throws IOException {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        File b = Utils.join(_branches, branch);
        if (b.exists()) {
            Main.exitWithError("A branch with that name already exists.");
        } else {
            b.createNewFile();
            Utils.writeObject(b, getHead());
        }
    }

    public static void rmBranch(String branch) {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        File f = Utils.join(_branches, branch);
        if (!f.exists()) {
            Main.exitWithError("A branch with that name does not exist.");
        } else if (f.getPath().equals(activeBranch().getPath())) {
            Main.exitWithError("Cannot remove the current branch.");
        } else {
            f.delete();
        }
    }

    public static void reset(String hash) throws IOException {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        Commit c = getCommitFromId(hash);
        List<String> filenames = Utils.plainFilenamesIn(CWD);
        for (String name : filenames) {
            if (!stagingArea.toBeAdded(name)
                    && !getHead().isTracked(name)) {
                Main.exitWithError("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
            }
        }
        List<String> trackedFiles =
                new ArrayList<>(c.getFileTracker().keySet());
        for (String name : trackedFiles) {
            checkout2(hash, "--", name);
        }

        for (String name : filenames) {
            File n = new File(CWD, name);
            n.delete();
            if (c.isTracked(name)) {
                checkout2(hash, "--", name);
            } else if (stagingArea.toBeAdded(name)) {
                rm(name);
            }
        }
        Utils.writeObject(activeBranch(), getCommitFromId(hash));
        stagingArea.clearStagingArea();
        Utils.writeObject(_head, Utils.readObject(activeBranch(),
                Commit.class));
    }

    public static void merge(String branch) throws IOException {
        mergeErrors(branch);
        Commit current = getHead(), given = commitFromBranch(branch);
        Commit sp = getSplitPoint(current, given);
        if (sp.getHash().equals(given.getHash())) {
            Main.exitWithError("Given branch is an ancestor "
                    + "of the current branch.");
        }
        if (sp.getHash().equals(current.getHash())) {
            checkout3(branch);
            Main.exitWithError("Current branch fast-forwarded.");
        }
        ArrayList<String> allFiles = addToAllFiles(sp, current, given);
        String message = "Merged " + branch + " into "
                + activeBranch().getName() + ".";
        Commit result = new Commit(message, current, given);
        result.getFileTracker().clear();
        for (String filename : Utils.plainFilenamesIn(CWD)) {
            Utils.restrictedDelete(filename);
        }
        for (String name : allFiles) {
            if (case1(sp, current, given, name)) {
                makeFile(sp, given, result, name);
            } else if (case2(sp, current, given, name)) {
                makeFile(sp, current, result, name);
            } else if (case3(sp, current, given, name)) {
                makeFile(sp, current, result, name);
            } else if (case4(sp, current, given, name)) {
                makeFile(sp, current, result, name);
            } else if (case5(sp, current, given, name)) {
                makeFile(sp, given, result, name);
            } else if (case6(sp, current, given, name)) {
                Utils.restrictedDelete(name);
            } else if (case7(sp, current, given, name)) {
                continue;
            } else if (case8a(sp, current, given, name)) {
                String text = "<<<<<<< HEAD\n"
                        + current.getBlob(name).getContent() + "=======\n"
                        + given.getBlob(name).getContent() + ">>>>>>>\n";
                makeConflict(name, text, result);
            } else if (case8b(sp, current, given, name)) {
                String text = "<<<<<<< HEAD\n" + current.getBlob(name).
                        getContent() + "=======\n" + ">>>>>>>\n";
                makeConflict(name, text, result);
            } else if (case8c(sp, current, given, name)) {
                String text = "<<<<<<< HEAD\n" + "=======\n"
                        + given.getBlob(name).getContent() + ">>>>>>>\n";
                makeConflict(name, text, result);
            }
        }
        createCommit(result);
    }

    public static void createCommit(Commit c) throws IOException {
        File commit = Utils.join(COMMIT_DIR, c.getHash());
        commit.createNewFile();
        Utils.writeObject(commit, c);
        Utils.writeObject(_head, c);
        Utils.writeObject(activeBranch(), c);
        stagingArea.clearStagingArea();
    }

    public static Commit getCommitFromId(String id) {
        for (String h : Utils.plainFilenamesIn(COMMIT_DIR)) {
            if (h.substring(0, id.length()).equals(id)) {
                id = h;
            }
        }
        File f = Utils.join(COMMIT_DIR, id);
        if (!f.exists()) {
            Main.exitWithError("No commit with that id exists.");
            return null;
        } else {
            return Utils.readObject(f, Commit.class);
        }
    }

    public static Commit getHead() {
        return Utils.readObject(_head, Commit.class);
    }

    public static Commit commitFromBranch(String branch) {
        File f = Utils.join(_branches, branch);
        return Utils.readObject(f, Commit.class);
    }

    public static File activeBranch() {
        return new File(Utils.readContentsAsString(_active));
    }

    public static Commit getSplitPoint(Commit current, Commit given) {
        Commit a = current;
        Commit b = given;
        Commit splitPoint = null;
        ArrayList<String> givenBranch = new ArrayList<>();
        while (b != null) {
            givenBranch.add(b.getHash());
            b = b.getParent();
        }
        while (splitPoint == null && a != null) {
            if (givenBranch.contains(a.getHash())) {
                splitPoint = getCommitFromId(a.getHash());
            } else if (a.getOtherParent() != null) {
                if (givenBranch.contains(a.getOtherParent().getHash())) {
                    a = a.getOtherParent();
                }
            } else {
                a = a.getParent();
            }

        }
        return splitPoint;
    }

    public static ArrayList<String> addToAllFiles(
            Commit a, Commit b, Commit c) {
        ArrayList<String> allFiles = new ArrayList<>();
        for (String s : a.getFileTracker().keySet()) {
            if (!allFiles.contains(s)) {
                allFiles.add(s);
            }
        }
        for (String s : b.getFileTracker().keySet()) {
            if (!allFiles.contains(s)) {
                allFiles.add(s);
            }
        }
        for (String s : c.getFileTracker().keySet()) {
            if (!allFiles.contains(s)) {
                allFiles.add(s);
            }
        }
        return allFiles;
    }

    public static File getStagedForAddition() {
        return _stagedForAddition;
    }

    public static File getStagedForRemoval() {
        return _stagedForRemoval;
    }

    public static File getBranches() {
        return _branches;
    }

    /** @param sp
     *@param current
     * @param given
     * @param name
     * @return true if modified in GIVEN but not in CURRENT
     * result: GIVEN */
    public static boolean case1(Commit sp, Commit current, Commit given,
                                String name) {
        return sp.modified(given, name) && !sp.modified(current, name);
    }

    /** @param sp
     *@param current
     * @param given
     * @param name
     * @return true if modified in CURRENT but not in GIVEN
     * result: CURRENT */
    public static boolean case2(Commit sp, Commit current, Commit given,
                                String name) {
        return sp.modified(current, name) && !sp.modified(given, name);
    }

    /** @param sp
     *@param current
     * @param given
     * @param name
     * @return true if modified in GIVEN and CURRENT in the same way
     * result: CURRENT */
    public static boolean case3(Commit sp, Commit current,
                                Commit given, String name) {
        return sp.modified(given, name) && sp.modified(current, name)
                && sp.modifiedSame(current, given, name);
    }

    /** @param sp
     *@param current
     * @param given
     * @param name
     * @return true if not present in SP and GIVEN but present in CURRENT
     * result: CURRENT */
    public static boolean case4(Commit sp, Commit current,
                                Commit given, String name) {
        return !sp.isTracked(name) && current.isTracked(name)
                && !given.isTracked(name);
    }

    /** @param sp
     *@param current
     * @param given
     * @param name
     * @return true if not present in SP and CURRENT but present in GIVEN
     * result: GIVEN */
    public static boolean case5(Commit sp, Commit current,
                                Commit given, String name) {
        return !sp.isTracked(name) && given.isTracked(name)
                && !current.isTracked(name);
    }

    /** @param sp
     *@param current
     * @param given
     * @param name
     * @return true if unmodified in CURRENT but deleted in GIVEN
     * result: remove file */
    public static boolean case6(Commit sp, Commit current,
                                Commit given, String name) {
        return !sp.modified(current, name) && !given.isTracked(name);
    }

    /** @param sp
     *@param current
     * @param given
     * @param name
     * @return true if unmodified in GIVEN but deleted in CURRENT
     * result: stay removed */
    public static boolean case7(Commit sp, Commit current,
                                Commit given, String name) {
        return !sp.modified(given, name) && !current.isTracked(name);
    }

    /** @param sp
     *@param current
     * @param given
     * @param name
     * @return true if modified in GIVEN and CURRENT in different ways:
     * both are still text files
     * result: combination */
    public static boolean case8a(Commit sp, Commit current,
                                 Commit given, String name) {
        return sp.modified(given, name) && sp.modified(current, name)
                && !sp.modifiedSame(current, given, name)
                && sp.changed(current, name) && sp.changed(given, name);
    }

    /** @param sp
     *@param current
     * @param given
     * @param name
     * @return true if modified in GIVEN and CURRENT in different ways:
     * current is text, given is deleted
     * result: combination */
    public static boolean case8b(Commit sp, Commit current,
                                 Commit given, String name) {
        return sp.modified(given, name) && sp.modified(current, name)
                && !sp.modifiedSame(current, given, name)
                && sp.changed(current, name) && sp.removed(given, name);
    }

    /** @param sp
     *@param current
     * @param given
     * @param name
     * @return true if modified in GIVEN and CURRENT in different ways:
     * current is deleted, given is text
     * result: combination */
    public static boolean case8c(Commit sp, Commit current,
                                 Commit given, String name) {
        return sp.modified(given, name) && sp.modified(current, name)
                && !sp.modifiedSame(current, given, name)
                && sp.removed(current, name) && sp.changed(given, name);
    }

    public static StagingArea getStagingArea() {
        return stagingArea;
    }

    public static void makeFile(Commit sp, Commit c,
                                Commit result, String name) {
        if (!sp.removed(c, name)) {
            result.getFileTracker().put(name, c.getBlob(name));
            File f = Utils.join(CWD, name);
            Utils.writeContents(f, result.getFileTracker().
                    get(name).getContent());
        } else {
            Utils.restrictedDelete(name);
        }
    }

    public static void makeConflict(String name, String text, Commit result) {
        File f = Utils.join(CWD, name);
        Utils.writeContents(f, text);
        result.getFileTracker().put(name, new Blob(name));
        System.out.println("Encountered a merge conflict.");
    }

    public static void mergeErrors(String branch) {
        if (!GITLET_FOLDER.exists()) {
            Main.exitWithError("Not in an initialized Gitlet directory.");
        }
        if (stagingArea.getAdditions().size() > 0
                || stagingArea.getRemovals().size() > 0) {
            Main.exitWithError("You have uncommitted changes.");
        }
        if (!Utils.join(_branches, branch).exists()) {
            Main.exitWithError("A branch with that name does not exist.");
        }
        if (activeBranch().getPath().equals(_branches.getPath()
                + "/" + branch)) {
            Main.exitWithError("Cannot merge a branch with itself.");
        }
        for (String name : Utils.plainFilenamesIn(CWD)) {
            if (!stagingArea.toBeAdded(name)
                    && !getHead().isTracked(name)) {
                Main.exitWithError("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
            }
        }
    }

    public static void printBranches() {
        System.out.println("=== Branches ===");
        List<String> branches = Utils.plainFilenamesIn(_branches);
        Collections.sort(branches);
        for (String branch : branches) {
            if (Utils.readObject(Utils.join(_branches, branch),
                    Commit.class).getHash().equals(getHead().getHash())) {
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printStagedFiles() {
        System.out.println("=== Staged Files ===");
        TreeMap<String, Blob> adds =
                Utils.readObject(_stagedForAddition, TreeMap.class);
        List<String> stagedFiles = new ArrayList<>(adds.keySet());
        Collections.sort(stagedFiles);
        for (String file : stagedFiles) {
            System.out.println(file);
        }
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printRemovedFiles() {
        System.out.println("=== Removed Files ===");
        ArrayList<String> removals =
                Utils.readObject(_stagedForRemoval, ArrayList.class);
        Collections.sort(removals);
        for (String file : removals) {
            System.out.println(file);
        }
        System.out.println();
    }

    public static void printModifications() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String name : Utils.plainFilenamesIn(CWD)) {
            if (getHead().isTracked(name)
                    && !getHead().getBlob(name).getContent().equals(
                    Utils.readContentsAsString(Utils.join(CWD, name)))
                    && !stagingArea.toBeAdded(name)) {
                System.out.println(name + " (modified)");
            } else if (stagingArea.toBeAdded(name)
                    && !stagingArea.getAdditions().get(name).
                    getContent().equals(Utils.readContentsAsString(
                            Utils.join(CWD, name)))) {
                System.out.println(name + " (modified)");
            } else if (stagingArea.toBeAdded(name)
                    && !Utils.join(CWD, name).exists()) {
                System.out.println(name + " (deleted)");
            } else if (!stagingArea.toBeRemoved(name)
                    && getHead().isTracked(name)
                    && !Utils.join(CWD, name).exists()) {
                System.out.println(name + " (deleted)");
            }
        }
        Set<String> allFiles = new HashSet<>();
        allFiles.addAll(getHead().getFileTracker().keySet());
        allFiles.addAll(stagingArea.getAdditions().keySet());
        for (String name : allFiles) {
            if (!Utils.join(CWD, name).exists()
                    && !stagingArea.toBeRemoved(name)) {
                System.out.println(name + " (deleted)");
            }
        }
        System.out.println();
    }

    public static void printUntrackedFiles() {
        System.out.println("=== Untracked Files ===");
        for (String name : Utils.plainFilenamesIn(CWD)) {
            if (Utils.join(CWD, name).exists()
                    && !stagingArea.toBeAdded(name)
                    && !stagingArea.toBeRemoved(name)
                    && !getHead().isTracked(name)) {
                System.out.println(name);
            }
        }
    }
}
