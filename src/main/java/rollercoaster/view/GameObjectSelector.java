package rollercoaster.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import rollercoaster.controller.Board;
import rollercoaster.model.*;

public class GameObjectSelector extends JPanel {

	private static final List<GameObject> PLACEABLE_OBJECTS = new ArrayList<>();

	private final ActionListener actionListener = new ObjectSelectorActionListener();

	private Board board;
	private SelectableObject selected = null;

	public GameObjectSelector(Board board) {
		this.board = board;
		initPlaceables();
		initPanel();
	}

	private void initPanel() {
		this.setLayout(new FlowLayout());
		this.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(1, 0, 1)));
		// this.setBounds(1000, 0, 120, 720);
		this.setPreferredSize(new Dimension(300, 300));

		int size = 50;
		for (GameObject go : PLACEABLE_OBJECTS) {
			final JButton btn = new SelectableObject(go, size, go.getTexture());
			btn.addActionListener(actionListener);
			add(btn);
		}
	}

	private void initPlaceables() {
		PLACEABLE_OBJECTS.add(new Game(ResourceLoader.getTexture(Resource.CAROUSEL_2), 1, 5, 10, 5, 200));
		PLACEABLE_OBJECTS.add(new Game(ResourceLoader.getTexture(Resource.HAMMER), 4, 15, 15, 20, 950));
		PLACEABLE_OBJECTS.add(new Game(ResourceLoader.getTexture(Resource.CAROUSEL_1), 1, 95, 10, 5, 2500));
		PLACEABLE_OBJECTS.add(new Game(ResourceLoader.getTexture(Resource.OBSERVATION_WHEEL), 5, 170, 10, 30, 10000));
		PLACEABLE_OBJECTS.add(new Game(ResourceLoader.getTexture(Resource.DODGEM), 1, 1827, 10, 5, 99999));

		PLACEABLE_OBJECTS.add(new Road(ResourceLoader.getTexture(Resource.ROAD_0), 15));

		PLACEABLE_OBJECTS.add(new Plant(ResourceLoader.getTexture(Resource.GRASS), 6, 1, 10));
		PLACEABLE_OBJECTS.add(new Plant(ResourceLoader.getTexture(Resource.TREE_1), 12, 1, 100));
		PLACEABLE_OBJECTS.add(new Plant(ResourceLoader.getTexture(Resource.TREE_2), 12, 4, 1200));
		PLACEABLE_OBJECTS.add(new Plant(ResourceLoader.getTexture(Resource.FLOWER), 7, 1, 50));

		PLACEABLE_OBJECTS.add(new Restaurant(ResourceLoader.getTexture(Resource.RESTAURANT), 50, 1, 1, 2500));
		PLACEABLE_OBJECTS.add(new Restaurant(ResourceLoader.getTexture(Resource.HOTDOG), 12, 1, 1, 500));

		PLACEABLE_OBJECTS.add(new Electricity(ResourceLoader.getTexture(Resource.ELECTRICITY), 9, 500));
		PLACEABLE_OBJECTS.add(new Bin(ResourceLoader.getTexture(Resource.BIN)));
	}

	private class ObjectSelectorActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if (selected != null) {
				selected.setSelected(false);
			}
			SelectableObject gameButton = (SelectableObject) event.getSource();
			selected = gameButton;
			selected.setSelected(true);
			board.selectPlaceableObject(selected.getGameObject());
		}

	}
}
