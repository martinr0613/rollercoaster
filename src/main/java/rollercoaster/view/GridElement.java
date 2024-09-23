package rollercoaster.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GridElement extends JButton {

	private int row;
	private int col;

	public GridElement(int row, int col, int size, Image texture) {
		this.row = row;
		this.col = col;
		if (texture != null) {
			setIcon(new StretchIcon(texture));
		}
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setPreferredSize(new Dimension(size, size));

		final Border raisedBevelBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);
		final Border emptyBorder = BorderFactory.createEmptyBorder();
		this.setBorder(emptyBorder);
		GridElement _this = this;

		setOpaque(false);
		setContentAreaFilled(false);

		this.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					_this.setBorder(raisedBevelBorder);
				} else {
					_this.setBorder(emptyBorder);
				}
			}
		});
	}

	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	public void setTexture(Image texture) {
		if (texture != null) {
			setIcon(new StretchIcon(texture));
		} else {
			setIcon(null);
		}
	}

}
