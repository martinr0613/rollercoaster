package rollercoaster.model;

import rollercoaster.controller.LogicHandler;

public class Maintainer extends People {
	private Long startRepairAt = null;
	private MaintainerState state;
	private int salary;

	public Maintainer(GameObject position) {
		type = PeopleType.MAINTAINER;
		state = MaintainerState.NONE;
		salary = 5;
		this.position = position;
		this.posX = position.getPosX();
		this.posY = position.getPosY();
	}

	public void tick(LogicHandler handler, long currentSecond) {
		handler.handleMaintainerTick(currentSecond, this);
	}

	public int getSalary() {
		return salary;
	}

	public Long getStartRepairAt() {
		return startRepairAt;
	}

	public void setStartRepairAt(Long startRepairAt) {
		this.startRepairAt = startRepairAt;
	}

	public MaintainerState getState() {
		return state;
	}

	public void setState(MaintainerState state) {
		this.state = state;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}
}
