package gitlet;

import java.io.Serializable;

public class Blob implements Serializable {

    /** Stores the content of a Blob. */
    private final String _content;

    /** Stores the sha1 id of the content. */
    private final String _id;

    public Blob(String filename) {
        _content = Utils.readContentsAsString(
                Utils.join(CommandClass.CWD, filename));
        _id = Utils.sha1(Utils.serialize(_content));
    }

    public String getContent() {
        return _content;
    }
}
