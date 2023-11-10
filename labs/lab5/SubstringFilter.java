/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen, Ishani Basak
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _input = input;
        _colName = colName;
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
        int index = _input.colNameToIndex(_colName);
        String val = candidateNext().getValue(index);
        if (val.contains(_subStr)) {
            return true;
        }
        return false;
    }

    private Table _input;
    private String _colName;
    private String _subStr;
}
