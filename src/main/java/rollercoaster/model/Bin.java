package rollercoaster.model;

import java.awt.Image;

public class Bin extends GameObject {

	public Bin(Image texture) {
		super(texture, 0, GameObjectType.BIN);
	}

	public Object clone() {
		Bin b = new Bin(getTexture());
		return b;
	}

}
