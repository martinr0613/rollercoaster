package rollercoaster.model;

import java.awt.Image;

public class Gate extends GameObject {

	public Gate(Image texture) {
		super(texture, 0, GameObjectType.GATE);
		buildTime = 0;
		status = Status.AVAILABLE;
	}

	public Object clone() {
		Gate b = new Gate(getTexture());
		return b;
	}

}
