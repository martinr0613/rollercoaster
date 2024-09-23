package rollercoaster.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import rollercoaster.controller.Board;
import rollercoaster.model.*;

public class GameHelper {

	private static EnumSet<GameObjectType> buildings = EnumSet.of(GameObjectType.GAME, GameObjectType.RESTAURANT);

	public static int random(int low, int high) {
		Random r = new Random();
		return r.nextInt(high + 1 - low) + low;
	}

	public static boolean isElectricity(GameObject object) {
		if (object == null) {
			return false;
		}
		return GameObjectType.ELECTRICITY == object.getType();
	}

	public static boolean isBuilding(GameObject object) {
		if (object == null) {
			return false;
		}
		return buildings.contains(object.getType());
	}

	public static boolean isGame(GameObject object) {
		if (object == null) {
			return false;
		}
		return object.getType() == GameObjectType.GAME;
	}

	public static boolean isInElectricityRange(Electricity e, Position object) {
		return e.getRange() >= distance(e, object);
	}

	public static boolean isInPlantRange(Plant p, Guest pos) {
		return p.getRange() >= distance(p, pos);
	}

	public static int distance(Position a, Position b) {
		return Math.abs(a.getPosX() - b.getPosX()) + Math.abs(a.getPosY() - b.getPosY());
	}

	public static int distance(Position a, Guest b) {
		return Math.abs(a.getPosX() - b.getPosX()) + Math.abs(a.getPosY() - b.getPosY());
	}

	public static List<GameObject> findAllBuilding(Board board, GameObjectType filter) {
		List<GameObject> buildings = new ArrayList<>();
		if (filter == GameObjectType.GATE) {
			buildings.add(board.getGate());
		} else {
			if (board != null) {
				for (GameObject gameObject : board) {
					if (!GameHelper.isBuilding(gameObject)) {
						continue;
					}
					Building obj = (Building) gameObject;
					if (filter == null || obj.getType() == filter) {
						buildings.add(obj);
					}
				}
			}
		}
		return buildings;
	}

}
