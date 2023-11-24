package sqlite.viewer;

import javax.swing.table.AbstractTableModel;
import java.util.Map;

public class DataTableModel extends AbstractTableModel {
    private final String[] columns;
    private final Map<Integer, Object[]> data;

    public DataTableModel(String[] columns, Map<Integer, Object[]> data) {
        this.columns = columns;
        this.data = data;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }
}
