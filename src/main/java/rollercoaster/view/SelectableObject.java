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

import rollercoaster.model.GameObject;

public class SelectableObject extends JButton {

	private final Border raisedBevelBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);
	private final Border emptyBorder = BorderFactory.createEmptyBorder();

	private GameObject gameObject;
	private boolean selected = false;

	public SelectableObject(GameObject gameObject, int size, Image texture) {
		this.gameObject = gameObject;
		setIcon(new StretchIcon(texture));
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setPreferredSize(new Dimension(size, size));
		setText("$" + gameObject.getBuildPrice());

		setBackground(Color.WHITE);

		this.setBorder(emptyBorder);
		SelectableObject _this = this;

		this.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (!selected) {
					_this.drawBorder(model.isRollover());
				}
			}
		});
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		drawBorder(selected);
	}

	private void drawBorder(boolean selected) {
		if (selected) {
			setBorder(raisedBevelBorder);
		} else {
			setBorder(emptyBorder);
		}
	}

	public GameObject getGameObject() {
		return gameObject;
	}

	public void setTexture(Image texture) {
		setIcon(new StretchIcon(texture));
	}

}
