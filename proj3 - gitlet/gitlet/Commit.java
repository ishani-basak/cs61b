package gitlet;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeMap;
import java.io.File;

public class Commit implements Serializable {

    /** stores commit message. */
    private String _message;

    /** stores date. */
    private Date _date;

    /** stores parent commit. */
    private Commit _parent;

    /** stores other parent commit. */
    private Commit _otherParent;

    /** stores hashcode id. */
    private String _hash;

    /** stores all tracked files. */
    private TreeMap<String, Blob> _fileTracker;

    public Commit(String message) {
        _message = message;
        _parent = null;
        _otherParent = null;
        _date = new Date(0);
        _hash = Utils.sha1(Utils.serialize(this));
        _fileTracker = new TreeMap<String, Blob>();

    }

    public Commit(String message, Commit parent) {
        _message = message;
        _parent = parent;
        _otherParent = null;
        _date = new Date();
        _hash = Utils.sha1(Utils.serialize(this));
        _fileTracker = copyParentFileTracker();
    }

    public Commit(String message, Commit parent, Commit otherParent) {
        _message = message;
        _parent = parent;
        _otherParent = otherParent;
        _date = new Date();
        _hash = Utils.sha1(Utils.serialize(this));
        _fileTracker = copyParentFileTracker();
    }

    /** @param date
     * @return string of formatted date. */
    public String formatDate(Date date) {
        DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        return format.format(date);
    }

    /** Returns message. */
    public String getMessage() {
        return _message;
    }

    /** Returns timestamp. */
    public String getTimestamp() {
        return formatDate(_date);
    }

    /** Returns Date. */
    public Date getDate() {
        return _date;
    }

    /** Returns parent. */
    public Commit getParent() {
        return _parent;
    }

    /** Returns filetracker. */
    public TreeMap<String, Blob> getFileTracker() {
        return _fileTracker;
    }

    /** @param filename
     * Returns true if a file is tracked. */
    public boolean isTracked(String filename) {
        return _fileTracker.containsKey(filename);
    }

    /** Tracks files. */
    public void trackFiles() {
        TreeMap<String, Blob> additions =
                CommandClass.getStagingArea().getAdditions();
        Set<String> addFiles = additions.keySet();
        for (String s : addFiles) {
            if (_fileTracker.containsKey(s)) {
                _fileTracker.remove(s);
            }
            _fileTracker.put(s, additions.get(s));
        }
        ArrayList<String> removeFiles =
                CommandClass.getStagingArea().getRemovals();
        for (String s : removeFiles) {
            if (_fileTracker.containsKey(s)) {
                _fileTracker.remove(s);
            }
        }
    }

    /** @param filename
     * Returns certain blob. */
    public Blob getBlob(String filename) {
        if (_fileTracker.get(filename) != null) {
            return _fileTracker.get(filename);
        } else {
            File f = Utils.join(CommandClass.CWD, "empty");
            Utils.writeContents(f, "");
            return new Blob("empty");
        }

    }

    /** Returns other parent. */
    public Commit getOtherParent() {
        return _otherParent;
    }

    /** Returns hashcode. */
    public String getHash() {
        return _hash;
    }

    /** Prints history of one commit. */
    public void history() {
        System.out.println("===");
        System.out.println("commit " + this.getHash());
        System.out.println("Date: " + this.getTimestamp());
        System.out.println(this.getMessage());
    }

    /** @param second
     * @param name
     * Returns true if a file has been modified. */
    public boolean modified(Commit second, String name) {
        return changed(second, name) || removed(second, name);
    }

    /** @param second
     * @param name
     * Returns true if a file has been changed. */
    public boolean changed(Commit second, String name) {
        if (this.isTracked(name) && second.isTracked(name)) {
            return !this.getBlob(name).getContent().equals(
                    second.getBlob(name).getContent());
        }
        return false;
    }

    /** @param second
     * @param name
     * Returns true if a file has been removed. */
    public boolean removed(Commit second, String name) {
        return this.isTracked(name) && !second.isTracked(name);
    }

    /** @param current
     * @param given
     * @param name
     * Returns true if two commits modified a file in the same way. */
    public boolean modifiedSame(Commit current, Commit given, String name) {
        if (this.modified(current, name) && this.modified(given, name)) {
            if (current.isTracked(name) && given.isTracked(name)
                && current.getBlob(name).getContent().equals(
                        given.getBlob(name).getContent())) {
                return true;
            } else if (!current.isTracked(name) && !given.isTracked(name)) {
                return true;
            }
        }
        return false;
    }

    /** @return copied parent filetracker. */
    public TreeMap<String, Blob> copyParentFileTracker() {
        TreeMap<String, Blob> newFileTracker = new TreeMap<>();
        for (String s : _parent._fileTracker.keySet()) {
            newFileTracker.put(s, _parent._fileTracker.get(s));
        }
        return newFileTracker;
    }
}
