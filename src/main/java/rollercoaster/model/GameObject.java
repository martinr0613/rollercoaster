package rollercoaster.model;

import java.awt.Image;

import rollercoaster.controller.LogicHandler;

/**
 * <h1>Class GameObject</h1> base class that all placeable elements extend
 * texture - texture to be displayed buildPrice - cost to build posX, posY -
 * current place in board
 *
 */
public abstract class GameObject implements Position {

	protected final GameObjectType type;

	protected Long startActionAt = null;
	protected long buildTime = 5;
	private int buildPrice;
	private Image texture;
	protected Status status;

	private int neigbours = 0;

	private int posX;
	private int posY;

	public GameObject(Image texture, int buildPrice, GameObjectType type) {
		this.type = type;
		this.texture = texture;
		this.buildPrice = buildPrice;
		this.status = Status.UNDER_CONSTRUCTION;
	}

	public Image getTexture() {
		return texture;
	}

	public int getBuildPrice() {
		return buildPrice;
	}

	@Override
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	@Override
	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setBuildPrice(int buildPrice) {
		this.buildPrice = buildPrice;
	}

	public void setTexture(Image texture) {
		this.texture = texture;
	}

	public int getNeigbours() {
		return neigbours;
	}

	public void setNeigbours(int neigbours) {
		this.neigbours = neigbours;
	}

	public Long getStartActionAt() {
		return this.startActionAt;
	}

	public void setStartActionAt(Long startActionAt) {
		this.startActionAt = startActionAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status newStatus) {
		this.status = newStatus;
	}

	public long getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(long buildTime) {
		this.buildTime = buildTime;
	}

	public GameObjectType getType() {
		return type;
	}

	public void tick(LogicHandler handler, long currentSecond) {
		handler.handleGameObjectTick(currentSecond, this);
	}

	public abstract Object clone();

}
