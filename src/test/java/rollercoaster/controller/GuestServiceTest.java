package rollercoaster.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import rollercoaster.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import rollercoaster.util.GameHelper;

public class GuestServiceTest {

	@Test
	void getGuestPreferenceNoPreference() {
		Gate gate = mock(Gate.class);
		Guest g = new Guest(0, 0, 0, gate);

		assertNull(GuestService.getGuestPreference(g));
	}

	@Test
	void getGuestPreferenceHungry() {
		GameObject gate = mock(GameObject.class);
		Guest g = new Guest(0, 0, 0, gate);

		g.setHunger(0);
		assertNull(GuestService.getGuestPreference(g));

		g.setHunger(51);
		assertEquals(GameObjectType.RESTAURANT, GuestService.getGuestPreference(g));

		g.setHunger(100);
		assertEquals(GameObjectType.GATE, GuestService.getGuestPreference(g));
	}

	@Test
	void getGuestPreferenceSad() {
		GameObject gate = mock(GameObject.class);
		Guest g = new Guest(0, 0, 0, gate);

		g.setSpirit(100);
		assertNull(GuestService.getGuestPreference(g));

		g.setSpirit(49);
		assertEquals(GameObjectType.GAME, GuestService.getGuestPreference(g));

		g.setSpirit(0);
		assertEquals(GameObjectType.GATE, GuestService.getGuestPreference(g));
	}

	@Test
	void decideNewTargetTooSad() {
		try (MockedStatic<GameHelper> gameHelper = mockStatic(GameHelper.class)) {
			Gate gate = mock(Gate.class);
			when(gate.getType()).thenReturn(GameObjectType.GATE);
			Building b = spy(new Restaurant(null, 10, 0, 0, 0));
			when(b.getType()).thenReturn(GameObjectType.RESTAURANT);
			when(b.isTargetable()).thenReturn(true);
			List<GameObject> list = new ArrayList<>();
			list.add(b);
			list.add(gate);

			gameHelper.when(() -> GameHelper.findAllBuilding(any(), any())).thenReturn(new ArrayList<>(list));
			gameHelper.when(() -> GameHelper.isBuilding(b)).thenReturn(true);
			Board board = mock(Board.class);

			Guest g = new Guest(0, 0, 0, gate);
			g.setSpirit(0);

			GuestService.decideNewTarget(g, board);
			assertEquals(g.getTarget().getType(), GameObjectType.GATE);
		}
	}

	@Test
	void decideNewTargetHomeless() {
		try (MockedStatic<GameHelper> gameHelper = mockStatic(GameHelper.class)) {
			Gate gate = mock(Gate.class);
			when(gate.getType()).thenReturn(GameObjectType.GATE);
			Building b = spy(new Restaurant(null, 10, 0, 0, 0));
			List<GameObject> list = new ArrayList<>();
			list.add(b);
			list.add(gate);

			gameHelper.when(() -> GameHelper.findAllBuilding(any(), any())).thenReturn(new ArrayList<>(list));
			gameHelper.when(() -> GameHelper.isBuilding(b)).thenReturn(true);
			Board board = mock(Board.class);

			Guest g = new Guest(0, 0, 0, gate);
			g.setMoney(0);

			GuestService.decideNewTarget(g, board);
			assertEquals(g.getTarget().getType(), GameObjectType.GATE);
		}
	}

	@Test
	void decideNewTargetNotFoundTarget() {
		try (MockedStatic<GameHelper> gameHelper = mockStatic(GameHelper.class)) {

			Gate gate = mock(Gate.class);
			Board board = mock(Board.class);
			Guest g = new Guest(0, 0, 0, gate);
			g.setMoney(100);

			List<GameObject> list = new ArrayList<>();

			gameHelper.when(() -> GameHelper.findAllBuilding(any(), any())).thenReturn(new ArrayList<>(list));

			GuestService.decideNewTarget(g, board);
			assertEquals(GuestState.LEFT, g.getState());
		}
	}

	@Test
	void decideNewTargetFoundTarget() {
		try (MockedStatic<GameHelper> gameHelper = mockStatic(GameHelper.class)) {
			List<Game> games = new ArrayList<>();
			Game game = new Game(null, 0, 0, 0, 0, 0);
			game.setStatus(Status.AVAILABLE);
			game.setHasPower(true);
			games.add(game);
			gameHelper.when(() -> GameHelper.findAllBuilding(any(Board.class), any())).thenReturn(games);
			gameHelper.when(() -> GameHelper.random(anyInt(), anyInt())).thenReturn(0);

			try (MockedStatic<GuestService> guestService = mockStatic(GuestService.class)) {
				guestService.when(() -> GuestService.decideNewTarget(any(Guest.class), any(Board.class)))
						.thenCallRealMethod();
				guestService.when(() -> GuestService.getGuestPreference(any())).thenReturn(null);

				Board board = mock(Board.class);
				Gate gate = mock(Gate.class);

				Guest g = new Guest(0, 0, 0, gate);
				g.setMoney(100);
				g.setSpirit(100);

				GuestService.decideNewTarget(g, board);
			}
		}
	}

}