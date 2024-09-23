package rollercoaster.model;

import java.awt.Image;

public class Road extends GameObject {

	private int neigbours;

	public Road(Image texture, int buildPrice) {
		super(texture, buildPrice, GameObjectType.ROAD);
		this.neigbours = 0;
		this.buildTime = 1;
	}

	public int getNeigbours() {
		return neigbours;
	}

	public void setNeigbours(int neigbours) {
		this.neigbours = neigbours;
	}

	public Object clone() {
		Road b = new Road(getTexture(), getBuildPrice());
		return b;
	}

}
