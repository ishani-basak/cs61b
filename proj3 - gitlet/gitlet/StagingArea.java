package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class StagingArea implements Serializable {

    /** Stores the filenames and blobs staged for add. */
    private TreeMap<String, Blob> _toAdd = new TreeMap<String, Blob>();

    /** Stores the filenames staged for removal. */
    private ArrayList<String> _toRemove = new ArrayList<>();

    public void addFile(String filename, Blob b) {
        Commit head = CommandClass.getHead();
        File f = Utils.join(CommandClass.CWD, filename);
        if (!head.isTracked(filename)
                || !head.getBlob(filename).getContent().equals(
                        Utils.readContentsAsString(f))) {
            if (toBeAdded(filename)) {
                removeFromStagingArea(filename);
            }
            _toAdd = getAdditions();
            _toAdd.put(filename, b);
            Utils.writeObject(CommandClass.getStagedForAddition(), _toAdd);
        }
        if (toBeRemoved(filename)) {
            _toRemove = getRemovals();
            _toRemove.remove(filename);
            Utils.writeObject(CommandClass.getStagedForRemoval(), _toRemove);
        }
    }

    public void removeFile(String filename) {
        if (toBeAdded(filename)) {
            _toAdd = getAdditions();
            _toAdd.remove(filename);
            Utils.writeObject(CommandClass.getStagedForAddition(), _toAdd);
        } else if (CommandClass.getHead().isTracked(filename)) {
            File f = Utils.join(CommandClass.CWD, filename);
            if (f.exists()) {
                f.delete();
            }
            _toRemove = getRemovals();
            _toRemove.add(filename);
            Utils.writeObject(CommandClass.getStagedForRemoval(), _toRemove);
        }
    }

    @SuppressWarnings("unchecked")
    public TreeMap<String, Blob> getAdditions() {
        return Utils.readObject(CommandClass.getStagedForAddition(),
                TreeMap.class);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getRemovals() {
        return Utils.readObject(CommandClass.getStagedForRemoval(),
                ArrayList.class);
    }

    public boolean toBeAdded(String filename) {
        return getAdditions().containsKey(filename);
    }

    public boolean toBeRemoved(String filename) {
        return getRemovals().contains(filename);
    }

    public void removeFromStagingArea(String filename) {
        if (toBeAdded(filename)) {
            getAdditions().remove(filename);
        }
        if (toBeRemoved(filename)) {
            getRemovals().remove(filename);
        }
    }

    public void clearStagingArea() {
        Utils.writeObject(CommandClass.getStagedForAddition(),
                new TreeMap<String, Blob>());
        Utils.writeObject(CommandClass.getStagedForRemoval(),
                new ArrayList<>());

        _toAdd.clear();
        _toRemove.clear();
    }
}
