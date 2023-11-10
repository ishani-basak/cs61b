import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/** A set of String values.
 *  @author Ishani Basak
 */
class ECHashStringSet implements StringSet {

    private LinkedList<String>[] buckets;
    private int defaultNumBuckets = 5;
    private int size;

    public ECHashStringSet() {
        buckets = (LinkedList<String>[]) new LinkedList[defaultNumBuckets];
        createNewBuckets();
        size = 0;
    }

    private void createNewBuckets() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<String>();
        }
    }

    @Override
    public void put(String s) {
        LinkedList l = buckets[whichBucket(s)];
        l.addLast(s);
        size++;
        if (size / buckets.length >= 5) {
            resize();
        }
    }

    private void resize() {
        List<String> words = asList();
        int oldLength = buckets.length;
        buckets = (LinkedList<String>[]) new LinkedList[oldLength*2];
        size = 0;
        createNewBuckets();
        for (String s : words) {
            put(s);
        }
    }

    @Override
    public boolean contains(String s) {
        int index = whichBucket(s);
        if (buckets[index].contains(s)) {
            return true;
        } else {
            return false;
        }
    }

    private int whichBucket(String s) {
        return (int) ((s.hashCode() & 0x7fffffff) % buckets.length);
    }

    @Override
    public List<String> asList() {
        List<String> words = new ArrayList<String>();
        for (LinkedList l : buckets) {
            for (int i = 0; i < l.size(); i++) {
                words.add(l.get(i).toString());
            }
        }
        return words;
    }
}
