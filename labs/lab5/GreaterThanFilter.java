/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen, Ishani Basak
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _input = input;
        _colName = colName;
        _ref = ref;
    }

    @Override
    protected boolean keep() {
        int index = _input.colNameToIndex(_colName);
        String val = candidateNext().getValue(index);
        if (val.compareTo(_ref) > 0) {
            return true;
        }
        return false;
    }

    private Table _input;
    private String _colName;
    private String _ref;
}
