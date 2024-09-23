package rollercoaster.model;

import java.awt.Image;

public class Electricity extends GameObject {
	private int range;

	public Electricity(Image texture, int range, int buildingPrice) {
		super(texture, buildingPrice, GameObjectType.ELECTRICITY);
		this.range = range;
	}

	public int getRange() {
		return range;
	}

	public Object clone() {
		Electricity b = new Electricity(getTexture(), getRange(), getBuildPrice());
		return b;
	}
}