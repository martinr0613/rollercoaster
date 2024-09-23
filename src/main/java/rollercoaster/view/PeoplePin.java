package rollercoaster.view;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import rollercoaster.model.People;
import rollercoaster.model.PeopleType;
import rollercoaster.util.GameHelper;

class PeoplePin extends JButton {

	private People people;
	private int offsetX;
	private int offsetY;

	public PeoplePin(int offsetX, int posX, int offsetY, int posY, People people, String texturePreffix,
			int textureCount) {
		this.people = people;
		this.offsetX = offsetX;
		this.offsetY = offsetY;

		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		if (people.getType() == PeopleType.GUEST) {
			setIcon(new ImageIcon(
					ResourceLoader.getTexture(Resource.valueOf(texturePreffix + GameHelper.random(1, textureCount)))
							.getScaledInstance(20, 42, Image.SCALE_SMOOTH)));
		} else {
			setIcon(new ImageIcon(
					ResourceLoader.getTexture(Resource.valueOf(texturePreffix))
							.getScaledInstance(20, 42, Image.SCALE_SMOOTH)));
		}

		setBounds((int) offsetX + 25 + posX * 59, (int) offsetY + 11 + posY * 59, 20, 42);
	}

	public void tick(int posX, int posY) {
		setBounds((int) offsetX + 25 + posX * 59, (int) offsetY + 11 + posY * 59, 20, 42);
	}

	public People getPeople() {
		return people;
	}

}