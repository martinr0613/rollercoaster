package rollercoaster.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rollercoaster.model.GameObject;
import rollercoaster.model.GameObjectType;
import rollercoaster.model.Guest;
import rollercoaster.model.Node;
import rollercoaster.model.People;

public class PeopleService {

	public static List<Node> findPathTo(People people, GameObject target, Board board) {
		List<Node> open = new ArrayList<>();
		List<Node> closed = new ArrayList<>();
		Node start = new Node(people.getPosX(), people.getPosY(), null,
				(int) calcDistance(people.getPosX(), target.getPosX(), people.getPosY(), target.getPosY()), 0,
				(int) calcDistance(people.getPosX(), target.getPosX(), people.getPosY(), target.getPosY()));
		open.add(start);
		while (true) {
			Node cur = open.get(0);
			double lowest = cur.getFcost();
			for (int i = 1; i < open.size(); i++) {
				if (open.get(i).getFcost() < lowest) {
					lowest = open.get(i).getFcost();
					cur = open.get(i);
				}
			}
			open.remove(cur);
			closed.add(cur);
			if (cur.getHcost() < 1) {
				// path has been found,backtrace through parent
				return backtracePath(people, cur);
			}

			// checks the neighbour to the right
			checkNeigbour(open, start, cur, cur.getCurx() + 1, cur.getCury(), board, target, closed);
			// checks neighbour to the left
			checkNeigbour(open, start, cur, cur.getCurx() - 1, cur.getCury(), board, target, closed);
			// checks neighbour to the north
			checkNeigbour(open, start, cur, cur.getCurx(), cur.getCury() + 1, board, target, closed);
			// checks neighbour to the south
			checkNeigbour(open, start, cur, cur.getCurx(), cur.getCury() - 1, board, target, closed);
			if (open.isEmpty()) {
				return null;
			}
		}
	}

	public static void checkNeigbour(List<Node> open, Node start, Node cur, int neighbourX, int neighbourY, Board board,
			GameObject target, List<Node> closed) {
		if (checkWalkableNeighbour(neighbourX, neighbourY, board, target)
				&& !neighbourInClosed(neighbourX, neighbourY, closed)) {
			if (!checkIfInOpen(open, cur, start, neighbourX, neighbourY, target)) {
				addNewNode(open, cur, neighbourX, neighbourY, start, target);
			}
		}
	}

	public static boolean checkWalkableNeighbour(int posx, int posy, Board board, GameObject target) {
		if (posx < board.getN() && posx >= 0 && posy < board.getM() && posy >= 0) {
			GameObject a = board.getBoard()[posx][posy];
			if (a == null) {
				return false;
			}
			return a.getType() == GameObjectType.ROAD || a == target;
		}
		return false;
	}

	public static List<Node> backtracePath(People people, Node node) {
		Node curNode = node;
		List<Node> path = new ArrayList<>();
		path.add(curNode);
		while (curNode.getParent() != null) {
			curNode = curNode.getParent();
			path.add(curNode);
		}
		Collections.reverse(path);
		people.setRemainingPath(path);
		return path;
	}

	public static double calcDistance(int x1, int x2, int y1, int y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public static void addNewNode(List<Node> open, Node cur, int testx, int testy, Node start, GameObject target) {
		Node potential = new Node();
		potential.setFcost(calcDistance(testx, start.getCurx(), testy, start.getCury())
				+ calcDistance(testx, target.getPosX(), testy, target.getPosY()));
		potential.setHcost(calcDistance(testx, target.getPosX(), testy, target.getPosY()));
		potential.setParent(cur);
		potential.setCurx(testx);
		potential.setCury(testy);
		open.add(potential);
	}

	public static boolean checkIfInOpen(List<Node> open, Node cur, Node start, int testx, int testy,
			GameObject target) {
		boolean found = false;
		for (int i = 0; i < open.size(); i++) {
			if (open.get(i).getCurx() == testx && open.get(i).getCury() == testy) {
				found = true;
				if (calcDistance(testx, start.getCurx(), testy, start.getCury())
						+ calcDistance(testx, target.getPosX(), testy, target.getPosY()) > open.get(i).getFcost()) {
					open.get(i).setFcost(calcDistance(testx, start.getCurx(), testy, start.getCury())
							+ calcDistance(testx, target.getPosX(), testy, target.getPosY()));
					open.get(i).setHcost(calcDistance(testx, target.getPosX(), testy, target.getPosY()));
					open.get(i).setParent(cur);
				}
			}
		}
		return found;
	}

	public static boolean neighbourInClosed(int posx, int posy, List<Node> closed) {
		// if given x,y coordinates are inside of closed list, return true
		for (Node node : closed) {
			if (node.getCurx() == posx && node.getCury() == posy) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkArrived(People people, GameObject target) {
		return (calcDistance(people.getPosX(), target.getPosX(), people.getPosY(), target.getPosY()) < 1);
	}

	public static void moveCloserToTarget(People people) {
		List<Node> pathToTake = people.getRemainingPath();
		if (pathToTake.size() > 0) {
			List<Node> remainingPathToTake = new ArrayList<>();
			Node a = pathToTake.get(0);
			people.setPosX(a.getCurx());
			people.setPosY(a.getCury());
			for (int i = 1; i < pathToTake.size(); i++) {
				remainingPathToTake.add(pathToTake.get(i));
			}
			people.setRemainingPath(remainingPathToTake);
		}
	}

	public static boolean isInPath(GameObject element, People person) {
		for (Node n : person.getRemainingPath()) {
			if (n.getCurx() == element.getPosX() && n.getCury() == element.getPosY()) {
				return true;
			}
		}
		return false;
	}

}
