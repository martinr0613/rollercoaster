package rollercoaster.controller;

import java.util.List;
import java.util.Vector;

import rollercoaster.model.*;
import rollercoaster.util.GameHelper;

public class GuestService extends PeopleService {

	public static void decideNewTarget(Guest guest, Board board) {

		Vector<GameObject> possibleTargets = new Vector<>();
		GameObjectType filter = getGuestPreference(guest);
		for (GameObject obj : GameHelper.findAllBuilding(board, filter)) {
			if (GameHelper.isBuilding(obj)) {
				if (((Building) obj).getUseCost() <= guest.getMoney() && ((Building) obj).isTargetable()) {
					possibleTargets.add((obj));
				}
			} else {
				possibleTargets.add(obj);
			}
		}

		if (possibleTargets.size() == 0) {
			guest.setState(GuestState.LEFT);
			return;
		}
		List<Node> tmp = null;

		int index = 0;
		while (tmp == null && !possibleTargets.isEmpty()) {
			index = GameHelper.random(0, possibleTargets.size() - 1);
			tmp = findPathTo(guest, possibleTargets.get(index), board);
			if (tmp == null)
				possibleTargets.remove(possibleTargets.get((index)));
		}
		if (tmp == null) {
			guest.setState(GuestState.LEFT);
			return;
		}
		guest.setTarget(possibleTargets.get(index));
		guest.setState(GuestState.WALKING);
	}

	protected static GameObjectType getGuestPreference(Guest guest) {
		GameObjectType filter = null;
		if (guest.getHunger() > 50) {
			filter = GameObjectType.RESTAURANT;
		} else if (guest.getSpirit() < 50) {
			filter = GameObjectType.GAME;
		}
		if (guest.getHunger() >= 100 || guest.getSpirit() <= 0) {
			filter = GameObjectType.GATE;
		}
		return filter;
	}

}
