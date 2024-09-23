package rollercoaster.view;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLayeredPane;

import rollercoaster.controller.Board;
import rollercoaster.controller.Logic;
import rollercoaster.model.Guest;
import rollercoaster.model.Maintainer;
import rollercoaster.model.PeopleType;

public class GridContainer extends JLayeredPane {

	private final Logic logic;
	private final Grid grid;

	public GridContainer(int N, int M, Logic logic, Board board) {
		this.logic = logic;

		grid = new Grid(N, M, board);
		grid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		grid.setBounds(0, 0, M * 60, N * 60);
		add(grid, Integer.valueOf(1));

		// double gridX = grid.getBounds().getX();
		// double gridY = grid.getBounds().getY();
		// add(new GuestPin((int) gridY, 10, (int) gridX, 10, new Guest(0, 0, 0, new
		// Restaurant(null, 0, 0, 0, 0))),
		// Integer.valueOf(3));
	}

	public void tick() {
		grid.tick();
		renderGuests();
		renderMaintainers();
	}

	private void renderGuests() {
		Set<Guest> guestSet = new HashSet<>(logic.getGuests());
		for (Component c : getComponentsInLayer(2)) {
			PeoplePin p = (PeoplePin) c;
			if (p.getPeople().getType() == PeopleType.GUEST) {
				GuestPin gp = (GuestPin) p;
				Guest g = gp.getGuest();
				if (!guestSet.contains(g)) {
					remove(c);
				}
				guestSet.remove(g);
				gp.tick(g.getPosY(), g.getPosX());
			}
		}
		double gridX = grid.getBounds().getX();
		double gridY = grid.getBounds().getY();
		for (Guest g : guestSet) {
			if (g.getPosX() == null || g.getPosY() == null) {
				continue;
			}
			add(new GuestPin((int) gridY, g.getPosY(), (int) gridX, g.getPosX(), g), Integer.valueOf(2));
		}
	}

	private void renderMaintainers() {
		Set<Maintainer> maintainerSet = new HashSet<>(logic.getMaintainers());
		for (Component c : getComponentsInLayer(2)) {
			PeoplePin p = (PeoplePin) c;
			if (p.getPeople().getType() == PeopleType.MAINTAINER) {
				MaintainerPin gp = (MaintainerPin) p;
				Maintainer g = gp.getMaintainer();
				if (!maintainerSet.contains(g)) {
					remove(c);
				}
				maintainerSet.remove(g);
				gp.tick(g.getPosY(), g.getPosX());
			}
		}
		double gridX = grid.getBounds().getX();
		double gridY = grid.getBounds().getY();
		for (Maintainer m : maintainerSet) {
			if (m.getPosX() == null || m.getPosY() == null) {
				continue;
			}
			add(new MaintainerPin((int) gridY, m.getPosY(), (int) gridX, m.getPosX(), m), Integer.valueOf(2));
		}
	}
}
