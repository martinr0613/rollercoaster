package rollercoaster.controller;

import java.awt.Image;
import java.util.Iterator;
import java.util.Optional;

import javax.swing.JOptionPane;

import rollercoaster.model.GameObject;
import rollercoaster.model.GameObjectType;
import rollercoaster.model.Gate;
import rollercoaster.model.Road;
import rollercoaster.view.Resource;
import rollercoaster.view.ResourceLoader;

public class Board implements Iterable<GameObject> {

	private int N, M;
	private GameObject[][] board;
	private Gate gate;

	private GameObject selectedObject;
	private Logic logic;

	public Board(int N, int M) {
		this.N = N;
		this.M = M;
		this.board = new GameObject[N][M];
		gate = new Gate(ResourceLoader.getTexture(Resource.GATE));
		gate.setPosX(N / 2);
		gate.setPosY(0);
		board[N / 2][0] = gate;
		selectedObject = new Road(ResourceLoader.getTexture(Resource.ROAD_0), 0);
	}

	public void setSelectedGameObject(int x, int y) {
		if (getSelectedObject().getType() == GameObjectType.BIN) {
			removeSelectedElementFromGrid(x, y);
		} else {
			addSelectedElementToGrid(x, y);
		}
	}

	protected void addSelectedElementToGrid(int x, int y) {
		if (!logic.canPlaceElement(x, y, selectedObject)) {
			return;
		}
		GameObject placeableElement = (GameObject) getSelectedObject().clone();
		if (placeableElement.getType() != GameObjectType.PLANT
				&& placeableElement.getType() != GameObjectType.ELECTRICITY) {
			calculateNeighbourRoadsTextureUponPlacement(x, y, placeableElement);
		}
		logic.placeElement(x, y, placeableElement);
		board[x][y] = placeableElement;
	}

	protected void removeSelectedElementFromGrid(int x, int y) {
		if (board[x][y] == null) {
			JOptionPane.showMessageDialog(null, "Üres mező");
		} else if (board[x][y].getType() == GameObjectType.GATE) {
			JOptionPane.showMessageDialog(null, "A kaput nem lehet törölni!");
		} else {
			int confirm = JOptionPane.showConfirmDialog(null, "Biztosan törölni szeretnéd ezt az elemet?", "delete?",
					JOptionPane.OK_CANCEL_OPTION);
			if (confirm == 0) {
				if (board[x][y].getType() != GameObjectType.PLANT
						&& board[x][y].getType() != GameObjectType.ELECTRICITY) {
					calculateNeighbourRoadsTextureUponDeletion(x, y);
				}
				GameObject tmp = board[x][y];
				board[x][y] = null;
				logic.removeElement(x, y, tmp);

			}
		}

	}

	protected void calculateNeighbourRoadsTextureUponPlacement(int x, int y, GameObject placeableElement) {
		int neighbours = 0;
		neighbours += setConnectableNeighbour(x - 1, y, 1, 4);
		neighbours += setConnectableNeighbour(x + 1, y, 4, 1);
		neighbours += setConnectableNeighbour(x, y - 1, 8, 2);
		neighbours += setConnectableNeighbour(x, y + 1, 2, 8);
		if (placeableElement.getType() == GameObjectType.ROAD) {
			setRoadTexture((Road) placeableElement, neighbours);
		}
	}

	protected void calculateNeighbourRoadsTextureUponDeletion(int x, int y) {
		setConnectableNeighbour(x - 1, y, 1, -4);
		setConnectableNeighbour(x + 1, y, 4, -1);
		setConnectableNeighbour(x, y - 1, 8, -2);
		setConnectableNeighbour(x, y + 1, 2, -8);
	}

	protected int setConnectableNeighbour(int x, int y, int dir, int neighbourDir) {
		if (x < 0 || x >= N || y < 0 || y >= M) {
			return 0;
		}
		if (board[x][y] == null) {
			return 0;
		}
		GameObject object = board[x][y];
		if (GameObjectType.PLANT == object.getType()) {
			return 0;
		}
		if (GameObjectType.GATE == object.getType() && dir != 8) {
			return 0;
		}
		if (GameObjectType.ROAD == object.getType()) {
			setRoadTexture((Road) object, neighbourDir);
			return dir;
		}
		return dir;
	}

	protected void setRoadTexture(Road r, int roadDirection) {
		r.setNeigbours(r.getNeigbours() + roadDirection);
		r.setTexture(ResourceLoader.getTexture(Resource.valueOf("ROAD_" + r.getNeigbours())));
	}

	public void removeGameObject(int x, int y) {
		board[x][y] = null;
	}

	public void selectPlaceableObject(GameObject object) {
		this.selectedObject = object;
	}

	public int getN() {
		return N;
	}

	public int getM() {
		return M;
	}

	public Gate getGate() {
		return gate;
	}

	public GameObject[][] getBoard() {
		return board;
	}

	protected GameObject getSelectedObject() {
		return selectedObject;
	}

	public Optional<GameObject> getBoardElement(int x, int y) {
		return Optional.ofNullable(board[x][y]);
	}

	public Image getBoardElementTexture(int x, int y) {
		if (board[x][y] == null) {
			return null;
		}
		return board[x][y].getTexture();
	}

	public void setLogic(Logic logic) {
		this.logic = logic;
	}

	@Override
	public Iterator<GameObject> iterator() {
		return new Iterator<>() {
			private int row = 0;
			private int col = 0;

			@Override
			public boolean hasNext() {
				if (row >= board.length)
					return false;
				return col < board[row].length || row != board.length - 1;
			}

			@Override
			public GameObject next() {
				if (!hasNext()) {
					throw new ArrayIndexOutOfBoundsException(String.format("x: %s, y: %s", row, col));
				}
				if (col >= board[row].length) {
					row++;
					col = 0;
				}
				return board[row][col++];
			}
		};
	}

}
