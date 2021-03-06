/**
 * modified by Herbert Roider <herbert@roider.at>
 */
package draganbjedov.netbeans.tooldata.view;

import draganbjedov.netbeans.tooldata.view.ccp.DropResult;
import draganbjedov.netbeans.tooldata.view.ccp.Pair;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/*
 * BACnetObjectsTableModel.java
 *
 * Created on 29.03.2012., 16.00.35
 *
 * @author Dragan Bjedov
 */
public class TOOLDATATableModel extends AbstractTableModel {

    private List<List<String>> values;
    private List<String> headers;
//    private UndoRedo.Manager undoRedoManager;

    public TOOLDATATableModel() {
        headers = new ArrayList<String>();

        //List<String> header = new ArrayList<>();
        //header.add("Channel");
        headers.add("T");
        headers.add("D");
        headers.add("Typ"); // $TC_DP1
        headers.add("Edge pos");
        headers.add("L1");
        headers.add("L2");
        headers.add("L3");
        headers.add("R1");
        headers.add("R2");
        headers.add("8");
        headers.add("9");
        headers.add("10");
        headers.add("11");
        headers.add("L1 Verschl.");
        headers.add("L2 Verschl.");
        headers.add("L3 Verschl.");
        headers.add("R1 Verschl.");
        headers.add("R2 Verschl.");
        headers.add("17");
        headers.add("18");
        headers.add("19");
        headers.add("20");
        headers.add("Base L1");
        headers.add("Base L2");
        headers.add("Base L3");
        headers.add("Tool clearance angle");
        headers.add("Use of tool inverse");

        values = new ArrayList<List<String>>();
    }

//    public TOOLDATATableModel(List<String> headers) {
//        this.headers = headers;
//        values = new ArrayList<List<String>>();
//    }
//
//    public TOOLDATATableModel(List<List<String>> values, List<String> headers) {
//        this.values = values;
//        this.headers = headers;
//    }
    /**
     * Adds empty row at end of table
     */
    public void addRow() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < headers.size(); i++) {
            try {
                data.add("0");
            } catch (Exception ex) {
                System.err.println("Add row Exception: " + ex);
            }
        }
        data.set(0, "1");
        data.set(1, "1");
        data.set(2, "510");
        data.set(3, "9");
        values.add(data);
        fireTableRowsInserted(values.size() - 1, values.size() - 1);
    }

    /**
     * Adds row at end of table
     *
     * @param data Row data
     */
    public void addRow(List<String> data) {
        while (data.size() < headers.size()) {
            data.add("0");
        }
        values.add(data);
        fireTableRowsInserted(values.size() - 1, values.size() - 1);
    }

    /**
     * Adds empty row to specific row
     *
     * @param row Row index
     */
    public void insertRow(int row) {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < headers.size(); i++) {
            try {
                data.add("0");
            } catch (Exception ex) {
                System.err.println("Add row Exception: " + ex);
            }
        }
        data.set(0, "1");
        data.set(1, "1");
        data.set(2, "510");
        data.set(3, "9");

        try {
            values.add(row, data);
        } catch (Exception e) {
            addRow(data);
            return;
        }
        fireTableRowsInserted(row, row);
    }

    /**
     * Adds row to specific row
     *
     * @param row Row index
     * @param data Row data
     */
    public void insertRow(int row, List<String> data) {
        while (data.size() < headers.size()) {
            data.add("0");
        }
        values.add(row, data);
        fireTableRowsInserted(row, row);
    }

    /**
     * Removes specific row
     *
     * @param row Row index
     */
    public void removeRow(int row) {
        if (row >= 0 && row < values.size()) {
            values.remove(row);
            if (values.size() > 0) {
                fireTableRowsDeleted(0, values.size() - 1);
            } else {
                fireTableDataChanged();
            }
        }
    }

    public void removeRows(int[] rows) {
        for (int i = 0; i < rows.length; i++) {
            values.remove(rows[i] - i);
        }
        if (values.size() > 0) {
            fireTableRowsDeleted(0, values.size() - 1);
        } else {
            fireTableDataChanged();
        }
    }

    public void addRows(int row, List<List<String>> rows) {
        try {
            values.addAll(row, rows);
            fireTableRowsInserted(row, row + rows.size() - 1);
        } catch (Exception e) {
            addRows(rows);
        }
    }

    public void addRows(List<List<String>> rows) {
        int row = values.size();
        values.addAll(rows);
        fireTableRowsInserted(row, row + rows.size() - 1);
    }

    private ArrayList<String> getClonedTableRowObject(final ArrayList<String> value) {
        ArrayList<String> cloned = new ArrayList<String>(value.size());
        cloned.addAll(value);
        return cloned;
    }

    public void paste(List<Pair<Integer, ArrayList<String>>> data, boolean isCopy) {
        int row = this.values.size() - 1;
        for (Pair<Integer, ArrayList<String>> pair : data) {
            final ArrayList<String> value = pair.second();
            if (isCopy) {//Copy+Paste
                values.add(getClonedTableRowObject(value));
            } else {//Cut+Paste
                values.add(value);
            }
        }
        fireTableRowsInserted(row, row + data.size() - 1);
    }

    public void paste(List<Pair<Integer, ArrayList<String>>> data, int afterIndex, boolean isCopy) {
        if (afterIndex == values.size() - 1) {
            int row = values.size() - 1;
            for (Pair<Integer, ArrayList<String>> pair : data) {
                final ArrayList<String> value = pair.second();
                if (isCopy) {//Copy+Paste
                    values.add(getClonedTableRowObject(value));
                } else {//Cut+Paste
                    values.add(value);
                }
            }
            fireTableRowsInserted(row, row + data.size() - 1);
        } else {
            for (int i = 0; i < data.size(); i++) {
                int index = afterIndex + i + 1;
                final ArrayList<String> value = data.get(i).second();
                if (isCopy) {//Copy+Paste
                    values.add(index, getClonedTableRowObject(value));
                } else {//Cut+Paste
                    values.add(index, value);
                }
            }
            fireTableRowsInserted(afterIndex + 1, afterIndex + data.size());
        }
    }

    public DropResult dropInsert(List<Pair<Integer, ArrayList<String>>> data, int index) {
        int firstIndex = data.get(0).first();
        if (firstIndex == index) {
            return DropResult.INVALID;
        }
        if (data.size() > 1) {
            int lastIndex = data.get(data.size() - 1).first();
            if (index > firstIndex && index < lastIndex) {
                return DropResult.INVALID;
            }
        }
        DropResult result;
        if (index < firstIndex) {//Move up
            for (int i = 0; i < data.size(); i++) {
                moveRow(data.get(i).first(), index + i);
            }
            result = DropResult.MOVED_UP;
        } else { //Move down
            for (int i = 0; i < data.size(); i++) {
                moveRow(data.get(i).first() - i, index);
            }
            result = DropResult.MOVED_DOWN;
        }
        fireTableDataChanged();
        return result;
    }

    public void dropOn(int index, ArrayList<String> data) {
        values.set(index, getClonedTableRowObject(data));
        fireTableRowsUpdated(index, index);
    }

    @Override
    public int getRowCount() {
        return values.size();
    }

    @Override
    public int getColumnCount() {
        return headers.size();
    }

    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= values.size() || columnIndex >= headers.size()) {
            return null;
        }
        final List<String> data = values.get(rowIndex);
        return data.get(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return headers.get(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (row >= values.size() || column >= headers.size()) {
            return;
        }

        List<String> data = values.get(row);

        data.set(column, (String) value);
        fireTableCellUpdated(row, column);

    }

    protected void moveRow(int from, int to) {
        if (from > to) {
            List<String> row = values.remove(from);
            values.add(to, row);
        } else {
            List<String> row = values.remove(from);
            if (to - 1 < getRowCount()) {
                values.add(to - 1, row);
            } else {
                values.add(row);
            }
        }
    }

    public void setValues(List<List<String>> values) {
        this.values = values;
    }

//    public List<String> getHeaders() {
//        return headers;
//    }
//    public void setHeaders(List<String> headers) {
//        this.headers = headers;
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.values != null ? this.values.hashCode() : 0);
        hash = 67 * hash + (this.headers != null ? this.headers.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TOOLDATATableModel other = (TOOLDATATableModel) obj;
        if (this.values != other.values && (this.values == null || !this.values.equals(other.values))) {
            return false;
        }
//        if (this.headers != other.headers && (this.headers == null || !this.headers.equals(other.headers))) {
//            return false;
//        }
        return true;
    }

    @Override
    public TOOLDATATableModel clone() {
        TOOLDATATableModel model = new TOOLDATATableModel();
        if (headers != null) {
            List<String> cloned = new ArrayList<String>(headers.size());
            for (String header : headers) {
                cloned.add(header);
            }
            model.headers = cloned;
//model.setHeaders(cloned);
        }
        if (values != null) {
            List<List<String>> cloned = new ArrayList<List<String>>(values.size());
            for (List<String> row : values) {
                List<String> clonedRow = new ArrayList<String>(row.size());
                for (String s : row) {
                    clonedRow.add(s);
                }
                cloned.add(clonedRow);
            }
            model.values = cloned;
        }
        return model;
    }
}
