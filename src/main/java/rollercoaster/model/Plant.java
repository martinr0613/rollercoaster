package rollercoaster.model;

import java.awt.Image;

import rollercoaster.controller.LogicHandler;

public class Plant extends GameObject {
	private int range;
	private int spiritIncrease;

	public Plant(Image texture, int range, int spiritIncrease, int buildPrice) {
		super(texture, buildPrice, GameObjectType.PLANT);
		this.range = range;
		this.spiritIncrease = spiritIncrease;
		this.buildTime = 2;
	}

	public int getRange() {
		return range;
	}

	public int getSpiritIncrease() {
		return spiritIncrease;
	}

	public void tick(LogicHandler handler, long currentSecond) {
		handler.handlePlantTick(currentSecond, this);
	}

	public Object clone() {
		Plant b = new Plant(getTexture(), getRange(), getSpiritIncrease(), getBuildPrice());
		return b;
	}
}
