package rollercoaster.controller;

import rollercoaster.model.Building;
import rollercoaster.model.Guest;

public class BuildingService {

	public static void payService(Building building, Guest guest) {
		guest.spendMoney(building.getUseCost());
		building.increaseGuestsServed(1);
		building.increaseMoneyMade(building.getUseCost());
		building.setLocalMoney(building.getLocalMoney() + building.getUseCost());
	}

}
