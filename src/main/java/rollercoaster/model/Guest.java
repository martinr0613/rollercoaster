package rollercoaster.model;

import rollercoaster.controller.LogicHandler;

public class Guest extends People {
	private int spirit;
	private int hunger;
	private int money;
	private int maxWaitTime;
	private int timeSinceLastGame;
	private long timeInQueue;
	private long queueStartTime;
	private GuestState state;
	private long lastCheckTime;

	public Guest(int money, int maxWaitTime, long creationTime, GameObject position) {
		this.type = PeopleType.GUEST;
		this.money = money;
		this.maxWaitTime = maxWaitTime;
		this.lastCheckTime = creationTime;
		this.position = position;
		this.posX = position.getPosX();
		this.posY = position.getPosY();
		spirit = 100;
		hunger = 0; // 0-100 0: not hungry, 100: died of starvation
		timeSinceLastGame = 0;
		timeInQueue = 0;
		state = GuestState.ARRIVED;
	}

	public void tick(LogicHandler handler, long currentSecond) {
		handler.handleGuestTick(currentSecond, this);
	}

	public void modifySpirit(long value) {
		spirit += value;
		if (spirit < 0) {
			spirit = 0;
		}
		if (spirit > 100) {
			spirit = 100;
		}
	}

	public void modifyHunger(long value) {
		hunger += value;
		if (hunger > 100) {
			hunger = 100;
		}
		if (hunger < 0) {
			hunger = 0;
		}
	}

	public void spendMoney(int price) {
		money -= price;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}

	public void setSpirit(int spirit) {
		this.spirit = spirit;
	}

	public void setTimeSinceLastGame(int time) {
		this.timeSinceLastGame = time;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public void setMaxWaitTime(int time) {
		this.maxWaitTime = time;
	}

	public void setState(GuestState state) {
		this.state = state;
	}

	public int getHunger() {
		return this.hunger;
	}

	public int getSpirit() {
		return this.spirit;
	}

	public GameObject getPosition() {
		return position;
	}

	public int getTimeSinceLastGame() {
		return this.timeSinceLastGame;
	}

	public int getMoney() {
		return this.money;
	}

	public int getMaxWaitTime() {
		return this.maxWaitTime;
	}

	public GuestState getState() {
		return this.state;
	}

	public long getTimeInQueue() {
		return timeInQueue;
	}

	public void setTimeInQueue(long timeInQueue) {
		this.timeInQueue = timeInQueue;
	}

	public long getQueueStartTime() {
		return queueStartTime;
	}

	public int payEntranceFee(int fee) {
		this.money -= fee;
		return fee;
	}

	public void setQueueStartTime(long queueStartTime) {
		this.queueStartTime = queueStartTime;
	}

	public long getLastCheckTime() {
		return lastCheckTime;
	}

	public void setLastCheckTime(long lastCheckTime) {
		this.lastCheckTime = lastCheckTime;
	}

}
