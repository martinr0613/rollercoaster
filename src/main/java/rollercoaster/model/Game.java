package rollercoaster.model;

import java.awt.Image;

import rollercoaster.controller.LogicHandler;

public class Game extends Building {

	private int repairTime;
	private int minPeopleToStart;
	private int dailyPrice;

	public Game(Image texture, int minPeopleToStart, int useCost, int useTime, int capacity, int buildPrice) {
		super(texture, useCost, capacity, useTime, buildPrice, GameObjectType.GAME);
		this.minPeopleToStart = minPeopleToStart;
		this.repairTime = 5;
		this.dailyPrice = 10;
	}

	public int getMinPeopleToStart() {
		return minPeopleToStart;
	}

	public void setMinPeopleToStart(int value) {
		this.minPeopleToStart = value;
	}

	public Object clone() {
		Game b = new Game(getTexture(), getMinPeopleToStart(), getUseCost(), getUseTime(), getCapacity(),
				getBuildPrice());
		return b;
	}

	public int getRepairTime() {
		return repairTime;
	}

	public int getDailyPrice() {
		return dailyPrice;
	}

	public void setDailyPrice(int newDaily) {
		dailyPrice = newDaily;
	}

	public void tick(LogicHandler handler, long currentSecond) {
		handler.handleGameTick(currentSecond, this);
	}

}
