package rollercoaster.view;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import rollercoaster.controller.Logic;

public class GuestList extends JScrollPane {

	private final JTable table;
	private Logic logic;

	private GuestListTableModel model = new GuestListTableModel();

	public GuestList(Logic logic) {
		this.logic = logic;
		this.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(1, 0, 1)));
		this.setPreferredSize(new Dimension(300, 400));

		table = new JTable(model);
		// table.setFillsViewportHeight(true);
		table.setPreferredSize(new Dimension(300, 400));
		setViewportView(table);

		// table.setPreferredScrollableViewportSize(getPreferredSize());
		// add(table);
	}

	public void tick() {
		model.setData(logic.getGuests());
	}
}
