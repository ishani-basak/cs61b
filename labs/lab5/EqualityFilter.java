/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen, Ishani Basak
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _input = input;
        _colName = colName;
        _match = match;
    }

    @Override
    protected boolean keep() {
        int index = _input.colNameToIndex(_colName);
        String val = candidateNext().getValue(index);
        if (val.equals(_match)) {
            return true;
        }
        return false;
    }

    private Table _input;
    private String _colName;
    private String _match;
}
