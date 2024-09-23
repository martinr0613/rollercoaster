package rollercoaster.model;

import java.awt.Image;

import rollercoaster.controller.LogicHandler;

public class Restaurant extends Building {

	public Restaurant(Image texture, int useCost, int capacity, int useTime, int buildPrice) {
		super(texture, useCost, capacity, useTime, buildPrice, GameObjectType.RESTAURANT);
	}

	public Object clone() {
		Restaurant b = new Restaurant(getTexture(), getUseCost(), getCapacity(), getUseTime(), getBuildPrice());
		return b;
	}

	public void tick(LogicHandler handler, long currentSecond) {
		handler.handleRestaurantTick(currentSecond, this);
	}

}
