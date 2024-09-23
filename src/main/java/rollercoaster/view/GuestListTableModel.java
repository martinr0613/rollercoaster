package rollercoaster.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import rollercoaster.model.Guest;

public class GuestListTableModel extends AbstractTableModel {
	private List<Guest> data = new ArrayList<>();
	private Class[] types;
	private static String[] COLUMN_NAMES = { "Hunger", "Spirit", "$", "Status", "" };

	public GuestListTableModel() {
		// define column types
		types = this.types = new Class[] { String.class, String.class, String.class, String.class, String.class };
	}

	public void setData(List<Guest> data) {
		this.data = data;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public Class getColumnClass(int columnIndex) {
		return types[columnIndex];
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return types.length;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= data.size()) {
			return "";
		}
		Guest guest = data.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return guest.getHunger();
			case 1:
				return guest.getSpirit();
			case 2:
				return guest.getMoney();
			case 3:
				return guest.getState();
			case 4:
				return "[" + guest.getPosX() + "," + guest.getPosY() + "]";
			default:
				return null;
		}
	}
}
