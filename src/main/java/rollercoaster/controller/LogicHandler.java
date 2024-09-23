package rollercoaster.controller;

import rollercoaster.model.Game;
import rollercoaster.model.GameObject;
import rollercoaster.model.Guest;
import rollercoaster.model.Maintainer;
import rollercoaster.model.Plant;
import rollercoaster.model.Restaurant;

public interface LogicHandler {

	public void handleGameObjectTick(long currentSecond, GameObject object);

	public void handleGameTick(long currentSecond, Game object);

	public void handlePlantTick(long currentSecond, Plant object);

	public void handleRestaurantTick(long currentSecond, Restaurant object);

	public void handleGuestTick(long currentSecond, Guest object);

	public void handleMaintainerTick(long currentSecond, Maintainer object);
}
