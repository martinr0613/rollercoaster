package rollercoaster.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import rollercoaster.model.Building;
import rollercoaster.model.Electricity;
import rollercoaster.model.Game;
import rollercoaster.model.GameObject;
import rollercoaster.model.GameObjectType;
import rollercoaster.model.Guest;
import rollercoaster.model.GuestState;
import rollercoaster.model.Maintainer;
import rollercoaster.model.MaintainerState;
import rollercoaster.model.People;
import rollercoaster.model.Plant;
import rollercoaster.model.Restaurant;
import rollercoaster.model.Status;
import rollercoaster.util.GameHelper;

public class Logic implements LogicHandler {

	private static final long PERIOD_LENGTH_IN_SEC = 24;

	private final int N;
	private final int M;
	private final Board board;
	private final List<Guest> guests = new ArrayList<>();
	private final List<Maintainer> maintainers = new ArrayList<>();
	private int currentMoney;
	private long elapsedTime = 0;
	private long elapsedTimeInSecond = 0;
	private long lastTime;
	private boolean running = true;

	private int lastPeriodIncome = 0;
	private int currentPeriodIncome = 0;
	private long lastDay = 0;
	private long lastSec = 0;

	private int gameSpeed = 1;

	private boolean parkOpened = false;

	public Logic(int N, int M, Board board, int currentMoney) {
		this.N = N;
		this.M = M;
		this.board = board;
		this.currentMoney = currentMoney;
		board.setLogic(this);
		// maintainers.add(new Maintainer(board.getBoardElement(N / 2, 0).get()));
	}

	public void tick() {
		if (!parkOpened) {
			return;
		}
		long now = getCurrentTime();
		if (!running) {
			lastTime = now;
			return;
		}
		elapsedTime += (now - lastTime) * gameSpeed;
		// ide jön minden logika
		elapsedTimeInSecond = elapsedTime / 1_000_000_000;
		long day = elapsedTimeInSecond / PERIOD_LENGTH_IN_SEC;

		endOfPeriod(day);

		tickBoard();
		generateGuest();
		tickPeoples();

		// ez az utolsó sor kötelezően!!
		lastTime = now;
		lastSec = elapsedTimeInSecond;
	}

	protected long getCurrentTime() {
		return System.nanoTime();
	}

	protected void endOfPeriod(long day) {
		if (day == lastDay) {
			return;
		}
		lastPeriodIncome = currentPeriodIncome;
		currentPeriodIncome = 0;
		lastDay = day;
		payMaintainers();
	}

	protected void payMaintainers() {
		for (Maintainer m : maintainers) {
			currentMoney -= m.getSalary();
		}
	}

	public void addMaintainer(Maintainer m) {
		maintainers.add(m);
	}

	protected void generateGuest() {
		if (elapsedTimeInSecond != lastSec && elapsedTimeInSecond % 3 == 0) {
			Guest guestToBeAdded = createNewGuest();
			final int entranceFee = 10;
			currentMoney += guestToBeAdded.payEntranceFee(entranceFee);
			guests.add(guestToBeAdded);
		}
	}

	protected Guest createNewGuest() {
		final float moneyRatio = 0.0028f;
		final float maxMoneyMultiplier = 100f;
		float timeFactor = elapsedTimeInSecond * moneyRatio;
		float moneyMultiplier = Math.min(maxMoneyMultiplier, 1.0f + timeFactor);
		int lowerBound = Math.round(40 * moneyMultiplier);
		int upperBound = Math.round(50 * moneyMultiplier);
		return new Guest(GameHelper.random(lowerBound, upperBound), GameHelper.random(20, 40), elapsedTimeInSecond,
				board.getBoardElement(N / 2, 0).orElseThrow(() -> new AssertionError("The gate is missing!")));
	}

	protected void tickBoard() {
		for (GameObject obj : board) {
			if (obj == null) {
				continue;
			}
			obj.tick(this, elapsedTimeInSecond);
			if (GameHelper.isBuilding(obj)) {
				int buildingMoney = ((Building) obj).collectMoney();
				currentMoney += buildingMoney;
				currentPeriodIncome += buildingMoney;
			}
		}
	}

	protected void tickPeoples() {
		for (Guest g : guests) {
			g.tick(this, elapsedTimeInSecond);
		}
		for (Maintainer m : maintainers) {
			m.tick(this, elapsedTimeInSecond);
		}
		guests.removeIf(g -> g.getState() == GuestState.LEFT);
	}

	/**
	 * Checks if a given element has access to electricity, by first finding all
	 * electricity boxes, then seeing if any of them are in range, if so true is
	 * returned, otherwise false is
	 *
	 * @param b the building placed
	 */

	public void checkElectricity(Building b) {
		boolean hasPower = false;
		for (GameObject obj : board) {
			if (GameHelper.isElectricity(obj)) {
				Electricity e = (Electricity) obj;
				hasPower = hasPower || GameHelper.isInElectricityRange(e, b);
			}
		}
		b.setHasPower(hasPower);
	}

	/**
	 * Usage: call when electricity is placed or deleted sets nearby buildings'
	 * hasPower attribute
	 *
	 * @param e            the element added or removed
	 * @param isPlacement: call with false when removing, with true when adding
	 *                     electricity
	 */
	public void checkNearbyBuildings(Electricity e, boolean isPlacement) {
		for (GameObject obj : board) {
			if (GameHelper.isBuilding(obj) && GameHelper.isInElectricityRange(e, obj)) {
				if (isPlacement)
					((Building) obj).setHasPower(true);
				else {
					if (timesPowered((Building) obj) < 2) {
						((Building) obj).setHasPower(false);
					}
				}
			}
		}
	}

	public int timesPowered(Building b) {
		int ret = 0;

		for (GameObject obj : board) {
			if (GameHelper.isElectricity(obj) && GameHelper.isInElectricityRange((Electricity) obj, b)) {
				++ret;
			}

		}
		return ret;
	}

	/**
	 * canPlaceElement - Used for checking if element is placeable on the selected
	 * square returns true if square is free and enough money is posessed return
	 * false otherwise
	 *
	 * @param x       coord of rows in board
	 * @param y       coord of cols in board
	 * @param element GameObject to be placed
	 * @return boolean
	 */

	public boolean canPlaceElement(int x, int y, GameObject element) {
		if (board.getBoard()[x][y] != null) {
			JOptionPane.showMessageDialog(null, "A választott mező nem üres!");
			return false;
		}
		if (currentMoney < element.getBuildPrice()) {
			JOptionPane.showMessageDialog(null, "Nincs elegendő pénzed az építéshez!");
			return false;
		}
		return true;
	}

	public void placeElement(int x, int y, GameObject element) {
		currentMoney -= element.getBuildPrice();
		element.setPosX(x);
		element.setPosY(y);
		if (GameHelper.isElectricity(element)) {
			checkNearbyBuildings((Electricity) element, true);
		}
		if (GameHelper.isBuilding(element)) {
			((Building) element).setCreationTime(elapsedTimeInSecond);
			checkElectricity((Building) element);
		}

		if (parkOpened) {
			element.setStartActionAt(elapsedTimeInSecond);
		} else {
			element.setStatus(Status.AVAILABLE);
		}
	}

	public void openPark(boolean parkOpened) {
		lastTime = System.nanoTime();
		this.parkOpened = parkOpened;
	}

	/**
	 * method findAdjacent()
	 *
	 * @param x coord x
	 * @param y coord y
	 * @return a neighbouring board element, if there are none, it returns the gate
	 */
	public GameObject findAdjacent(int x, int y) {
		if (isInBounds(x - 1, y) && board.getBoardElement(x - 1, y).isPresent()
				&& isValidPosition(board.getBoardElement(x - 1, y).get()))
			return board.getBoardElement(x - 1, y).get();
		if (isInBounds(x, y - 1) && board.getBoardElement(x, y - 1).isPresent()
				&& isValidPosition(board.getBoardElement(x, y - 1).get()))
			return board.getBoardElement(x, y - 1).get();
		if (isInBounds(x + 1, y) && board.getBoardElement(x + 1, y).isPresent()
				&& isValidPosition(board.getBoardElement(x + 1, y).get()))
			return board.getBoardElement(x + 1, y).get();
		if (isInBounds(x, y + 1) && board.getBoardElement(x, y + 1).isPresent()
				&& isValidPosition(board.getBoardElement(x, y + 1).get()))
			return board.getBoardElement(x, y + 1).get();
		return board.getBoardElement(board.getN() / 2, 0).orElseThrow(() -> new AssertionError("The gate is missing!"));
	}

	public boolean isInBounds(int x, int y) {
		return x < board.getN() && x >= 0 && y < board.getM() && y >= 0;
	}

	private boolean isValidPosition(GameObject pos) {
		return pos != null && pos.getType() != GameObjectType.ELECTRICITY && pos.getType() != GameObjectType.PLANT;
	}

	public void removeElement(int x, int y, GameObject element) {
		if (!parkOpened) {
			currentMoney += element.getBuildPrice();
		} else
			currentMoney += Math.floor(element.getBuildPrice() * 0.75);
		if (element.getType() == GameObjectType.ELECTRICITY)
			checkNearbyBuildings((Electricity) element, false);
		GameObject neighbour = findAdjacent(x, y);
		for (Guest g : guests) {
			if (isPersonInGameObject(g, element)) {

				g.setState(GuestState.NONE);
				g.setTarget(null);
				g.setPosX(neighbour.getPosX());
				g.setPosY(neighbour.getPosY());
			}
			if (PeopleService.isInPath(element, g) || g.getTarget() == element) {
				g.emptyRemainingPath();
				board.getBoard()[x][y] = null;
				g.setTarget(null);
				g.setState(GuestState.NONE);
				GuestService.decideNewTarget(g, this.board);
			}
		}
	}

	public boolean isPersonInGameObject(People g, GameObject b) {
		return g.getPosX() == b.getPosX() && g.getPosY() == b.getPosY();
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public long getElapsedTimeInSecond() {
		return elapsedTimeInSecond;
	}

	public int getCurrentMoney() {
		return currentMoney;
	}

	public int getLastPeriodIncome() {
		return lastPeriodIncome;
	}

	public long getLastDay() {
		return lastDay;
	}

	public int getGameSpeed() {
		return gameSpeed;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setGameSpeed(int speed) {
		this.gameSpeed = speed;
	}

	public List<Guest> getGuests() {
		return guests;
	}

	public List<Maintainer> getMaintainers() {
		return maintainers;
	}

	public int getGuestCount() {
		return this.guests.size();
	}

	public Board getBoard() {
		return board;
	}

	@Override
	public void handleGameObjectTick(long currentSecond, GameObject object) {
		if (object.getStartActionAt() == null) {
			return;
		}
		if (object.getStatus() == Status.UNDER_CONSTRUCTION
				&& object.getStartActionAt() + object.getBuildTime() < currentSecond) {
			object.setStatus(Status.AVAILABLE);
			object.setStartActionAt(null);
		}
	}

	@Override
	public void handleGameTick(long currentSecond, Game object) {
		// checks if game is finished building
		if (object.getStatus() == Status.UNDER_CONSTRUCTION
				&& object.getStartActionAt() + object.getBuildTime() < currentSecond) {
			object.setStatus(Status.AVAILABLE);
			object.setStartActionAt(null);
		}
		// checks if game needs maintenance, and calls for maintainer
		if (object.getNeedsMaintenance() && !object.getWaitingForMaintenance()) {
			List<Maintainer> availableMaintainers = new ArrayList<>();
			for (Maintainer maintainer : maintainers) {
				if (maintainer.getState() == MaintainerState.NONE) {
					availableMaintainers.add(maintainer);
				}
			}
			MaintainerService.decideOnMaintainer(object, availableMaintainers, board);
		}
		// checks if game round is over
		if (object.getStatus() == Status.IN_USE && currentSecond - object.getStartActionAt() >= object.getUseTime()) {
			GameService.finishGame(object, currentSecond, board);
		}
		if (object.getStatus() == Status.AVAILABLE && Math.random() < 0.0001) { // game has 0.5% chance to need
			// maintenance
			object.setNeedsMaintenance(true);
			for (Guest g : object.getQueue()) {
				g.setState(GuestState.NONE);
			}
			object.getQueue().clear();
		}
		if (GameService.canStartGame(object)) { // starts game if possible
			GameService.startGame(object, currentSecond);
		}
	}

	@Override
	public void handlePlantTick(long currentSecond, Plant object) {
		if (object.getStatus() == Status.UNDER_CONSTRUCTION
				&& object.getStartActionAt() + object.getBuildTime() < currentSecond) {
			object.setStatus(Status.AVAILABLE);
			object.setStartActionAt(null);
		}
		long deltaTime = elapsedTimeInSecond - lastSec;

		if (deltaTime == 0) {
			return;
		}
		if (object.getStatus() != Status.AVAILABLE) {
			return;
		}
		for (Guest g : guests) {
			if (GameHelper.isInPlantRange(object, g))
				g.modifySpirit(object.getSpiritIncrease());
		}
	}

	@Override
	public void handleRestaurantTick(long currentSecond, Restaurant object) {
		// checks if finished building
		if (object.getStatus() == Status.UNDER_CONSTRUCTION
				&& object.getStartActionAt() + object.getBuildTime() < currentSecond) {
			object.setStatus(Status.AVAILABLE);
			object.setStartActionAt(null);
		}
		// checks if round is over
		if (object.getStatus() == Status.IN_USE && currentSecond - object.getStartActionAt() >= object.getUseTime()) {
			RestaurantService.finishRound(object, currentSecond);
		}
		if (object.getStatus() == Status.AVAILABLE && object.getQueue().size() > 0) {
			RestaurantService.startRound(object, currentSecond);
		}
	}

	@Override
	public void handleGuestTick(long currentSecond, Guest object) {
		long deltaTime = getElapsedTimeInSecond() - lastSec;
		if (deltaTime == 0) {
			return;
		}
		switch (object.getState()) {
			case ARRIVED:
				object.setState(GuestState.NONE);
				break;
			case NONE:
				GuestService.decideNewTarget(object, board);
				break;
			case IN_QUEUE:
				object.modifySpirit(-5);
				object.modifyHunger(2);
				object.setTimeInQueue(object.getTimeInQueue() + elapsedTimeInSecond - object.getQueueStartTime());
				if (object.getTimeInQueue() > object.getMaxWaitTime() / 2) {
					object.modifySpirit(-2);
				}
				if (object.getTimeInQueue() > object.getMaxWaitTime()) {
					object.modifySpirit(-10);
					object.setState(GuestState.NONE);
					GameObject neighbour = findAdjacent(object.getPosX(), object.getPosY());
					object.setPosX(neighbour.getPosX());
					object.setPosY(neighbour.getPosY());
				}
				if (object.getSpirit() <= 0 || object.getHunger() >= 100) {
					GuestService.decideNewTarget(object, board);
				}
				break;
			case PLAYING:
				object.modifySpirit(7);
				object.modifyHunger(2);
				break;
			case EATING:
				// modifySpirit(-5);
				object.modifyHunger(-40);
				break;
			case WALKING:
				object.modifySpirit(-3);
				object.modifyHunger(2);
				boolean arrivedToTarget = GuestService.checkArrived(object, object.getTarget());
				if (arrivedToTarget) {
					if (object.getTarget().getType() == GameObjectType.GATE) {
						object.setState(GuestState.LEFT);
					} else {
						object.setState(GuestState.IN_QUEUE);
					}
					if (object.getTarget().getType() == GameObjectType.RESTAURANT
							|| object.getTarget().getType() == GameObjectType.GAME) {
						object.setState(GuestState.IN_QUEUE);
						if (GameHelper.isBuilding(object.getTarget())) {
							((Building) object.getTarget()).addToQueue(object);
						}
					}
				} else if (!verifyTargetAvailability(object.getTarget())) {
					GuestService.decideNewTarget(object, board);
				} else {
					GuestService.moveCloserToTarget(object);
				}
				break;
			default:
				break;
		}
		if (object.getHunger() >= 25 && object.getHunger() < 50) {
			object.modifySpirit(-2);
		} else if (object.getHunger() >= 50 && object.getHunger() < 75) {
			object.modifySpirit(-5);
		} else if (object.getHunger() >= 75) {
			object.modifySpirit(-10);
		}
	}

	@Override
	public void handleMaintainerTick(long currentSecond, Maintainer object) {
		long deltaTime = elapsedTimeInSecond - lastSec;
		if (deltaTime == 0) {
			return;
		}

		switch (object.getState()) {
			case REPAIR:
				if (object.getStartRepairAt() + ((Game) object.getTarget()).getRepairTime() < elapsedTimeInSecond) {
					((Game) object.getTarget()).setWaitingForMaintenance(false);
					((Game) object.getTarget()).setStatus(Status.AVAILABLE);
					((Game) object.getTarget()).setNeedsMaintenance(false);
					object.setTarget(null);
					object.setPosition(null);
					object.setState(MaintainerState.NONE);
					object.setStartRepairAt(null);
				}
				break;
			case WALKING:
				boolean arrivedToTarget = MaintainerService.checkArrived(object, object.getTarget());
				if (arrivedToTarget) {
					object.setState(MaintainerState.REPAIR);
					object.setStartRepairAt(currentSecond);
				} else {
					MaintainerService.moveCloserToTarget(object);
				}
				break;
			case NONE:
				MaintainerService.walkAround(object, board);
				break;
		}
	}

	private boolean verifyTargetAvailability(GameObject target) {
		return (GameHelper.isBuilding(target) && ((Building) target).isTargetable());
	}

	private Game findRepairableGame() {
		List<Game> possibleTargets = new ArrayList<>();
		GameObject[][] b = board.getBoard();

		for (GameObject[] gameObjects : b) {
			for (int j = 0; j < b[0].length; j++) {
				if (gameObjects[j] != null && gameObjects[j].getType() == GameObjectType.GAME) {
					Game game = (Game) gameObjects[j];
					if (game.getNeedsMaintenance()) {
						possibleTargets.add(game);
					}
				}
			}
		}
		if (possibleTargets.isEmpty()) {
			return null;
		}
		return possibleTargets.get(GameHelper.random(0, possibleTargets.size() - 1));
	}
}
