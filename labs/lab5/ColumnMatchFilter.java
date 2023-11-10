import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen, Ishani Basak
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _input = input;
        _colName1 = colName1;
        _colName2 = colName2;
    }

    @Override
    protected boolean keep() {
        int index1 = _input.colNameToIndex(_colName1);
        int index2 = _input.colNameToIndex(_colName2);
        String val1 = candidateNext().getValue(index1);
        String val2 = candidateNext().getValue(index2);
        if (val1.equals(val2)) {
            return true;
        }
        return false;
    }

    private Table _input;
    private String _colName1;
    private String _colName2;
}
