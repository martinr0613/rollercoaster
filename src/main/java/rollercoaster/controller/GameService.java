package rollercoaster.controller;

import rollercoaster.model.*;

public class GameService {

	public static boolean canStartGame(Game game) {
		if (game.getStatus() != Status.AVAILABLE) {
			return false;
		}
		if (game.getQueue().size() < game.getMinPeopleToStart()) {
			return false;
		}
		if (!game.getHasPower()) {
			return false;
		}
		if (game.getNeedsMaintenance()) {
			return false;
		}
		return true;

	}

	public static void startGame(Game game, Long currentSecond) {
		while (game.getCapacity() > game.getCurrentUsing().size() && game.getQueue().size() > 0) {
			Guest guest = game.getQueue().remove(0);
			guest.setState(GuestState.PLAYING);
			BuildingService.payService(game, guest);
			game.getCurrentUsing().add(guest);
		}
		game.setStartActionAt(currentSecond);
		game.setStatus(Status.IN_USE);
	}

	public static void finishGame(Game game, Long currentSecond, Board board) {
		game.setStatus(Status.AVAILABLE);
		game.setStartActionAt(null);
		moveGuestsToClosestRoad(game, board);
		for (Guest guest : game.getCurrentUsing()) {
			guest.setState(GuestState.NONE);
		}
		game.getCurrentUsing().clear();
	}

	public static boolean isInBounds(int x, int y, Board board) {
		return x < board.getN() && x >= 0 && y < board.getM() && y >= 0;
	}

	public static void moveGuestsToClosestRoad(Game game, Board board) {
		Road foundRoad = null;
		GameObject a = null;
		if (isInBounds(game.getPosX() + 1, game.getPosY(), board)) {
			a = board.getBoard()[game.getPosX() + 1][game.getPosY()];
			if (a != null) {
				if (a.getType() == GameObjectType.ROAD) {
					foundRoad = (Road) board.getBoard()[game.getPosX() + 1][game.getPosY()];
				}
			}
		}
		if (isInBounds(game.getPosX() - 1, game.getPosY(), board)) {
			a = board.getBoard()[game.getPosX() - 1][game.getPosY()];
			if (a != null) {
				if (a.getType() == GameObjectType.ROAD) {
					foundRoad = (Road) board.getBoard()[game.getPosX() - 1][game.getPosY()];
				}
			}
		}
		if (isInBounds(game.getPosX(), game.getPosY() + 1, board)) {
			a = board.getBoard()[game.getPosX()][game.getPosY() + 1];
			if (a != null) {
				if (a.getType() == GameObjectType.ROAD) {
					foundRoad = (Road) board.getBoard()[game.getPosX()][game.getPosY() + 1];
				}
			}
		}
		if (isInBounds(game.getPosX(), game.getPosY() - 1, board)) {
			a = board.getBoard()[game.getPosX()][game.getPosY() - 1];
			if (a != null) {
				if (a.getType() == GameObjectType.ROAD) {
					foundRoad = (Road) board.getBoard()[game.getPosX()][game.getPosY() - 1];
				}
			}
		}
		for (Guest guest : game.getCurrentUsing()) {
			if (foundRoad != null) {
				guest.setPosX(foundRoad.getPosX());
				guest.setPosY(foundRoad.getPosY());
			}
		}
	}
}
