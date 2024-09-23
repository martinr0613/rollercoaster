package rollercoaster.model;

import java.util.ArrayList;
import java.util.List;

public abstract class People {
	protected PeopleType type;
	protected GameObject target;
	protected GameObject position;
	public List<Node> remainingPathToTarget = new ArrayList<>();
	protected Integer posX;
	protected Integer posY;

	public People() {
		target = null;
	}

	public People(GameObject target) {
		this.target = target;
		this.position = target;
	}

	// texture?
	public Integer getPosX() {
		return posX;
	}

	public Integer getPosY() {
		return posY;
	}

	public GameObject getTarget() {
		return target;
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}

	public GameObject getPosition() {
		return position;
	}

	public void setPosition(GameObject position) {
		this.position = position;
	}

	public void setPosX(Integer posX) {
		this.posX = posX;
	}

	public void setPosY(Integer posY) {
		this.posY = posY;
	}

	public List<Node> getRemainingPath() {
		return remainingPathToTarget;
	}

	public void emptyRemainingPath() {
		List<Node> pathCopy = new ArrayList<>(remainingPathToTarget);
		for (Node n : remainingPathToTarget) {
			pathCopy.remove(n);
		}
		remainingPathToTarget = pathCopy;
	}

	public void setRemainingPath(List<Node> newPath) {
		remainingPathToTarget = newPath;
	}

	public PeopleType getType() {
		return type;
	}

}
