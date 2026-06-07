package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.json.JSONObject;

public class LawsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] _columns = { "Key", "Value", "Description" };

	private List<LawsInfo> _rows;

	LawsTableModel() {
		_rows = new ArrayList<LawsInfo>();
	}

	public void updateTable(JSONObject data) {
		_rows.clear();

		for (String key : data.keySet()) _rows.add(new LawsInfo(key, "", data.getString(key)));

		fireTableStructureChanged();
	}

	public void clear() {
		_rows.clear();
	}

	@Override
	public String getColumnName(int column) { return _columns[column]; }
	@Override
	public int getColumnCount() { return _columns.length; }
	@Override
	public int getRowCount() { return _rows.size(); }
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LawsInfo li = _rows.get(rowIndex);
		String s = "";
		switch(columnIndex) {
		case 0:
			s = li.getKey();
			break;
		case 1:
			s = li.getValue();
			break;
		case 2:
			s = li.getDescription();
			break;
		}
		return s;
	}  
	@Override
	public void setValueAt(Object o,int rowIndex,int columnIndex) {
		LawsInfo li = _rows.get(rowIndex);
		li.setValue(o.toString());
	}
	@Override
	public boolean isCellEditable(int rowIndex,int columnIndex) { return columnIndex == 1; }

	public void addEmptyForce(String key,String description) {
		_rows.add(new LawsInfo(key,"",description));
		this.fireTableStructureChanged();
	}

	public String toString(){
		String s = "";
		for (int i = 0; i < _rows.size(); i++)
			s = s + _rows.get(i) + "\n";
		return s;
	}
}