package rollercoaster.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import rollercoaster.model.Game;
import rollercoaster.model.Guest;
import rollercoaster.model.GuestState;
import rollercoaster.model.Status;

public class GameLogicTest {

	@Test
	void canStartGame() {
		Game game = new Game(null, 1, 0, 0, 1, 0);
		game.setHasPower(true);
		game.setNeedsMaintenance(false);

		game.setStatus(Status.IN_USE);
		assertEquals(false, GameService.canStartGame(game));

		game.setStatus(Status.UNDER_CONSTRUCTION);
		assertEquals(false, GameService.canStartGame(game));

		game.setStatus(Status.AVAILABLE);
		assertEquals(false, GameService.canStartGame(game));

		game.addToQueue(new Guest(0, 0, 0, game));
		assertEquals(true, GameService.canStartGame(game));

		game.setNeedsMaintenance(true);
		assertEquals(false, GameService.canStartGame(game));
		game.setNeedsMaintenance(false);

		game.setHasPower(false);
		assertEquals(false, GameService.canStartGame(game));
		game.setHasPower(true);

		game.addToQueue(new Guest(0, 0, 0, game));
		assertEquals(true, GameService.canStartGame(game));
	}

	@Test
	void startGame1() {
		Game game = new Game(null, 1, 0, 0, 1, 0);
		Guest g1 = new Guest(0, 0, 0, game);
		game.addToQueue(g1);
		Guest g2 = new Guest(0, 0, 0, game);
		game.addToQueue(g2);

		GameService.startGame(game, 5L);
		assertEquals(1, game.getQueue().size());
		assertEquals(1, game.getCurrentUsing().size());
		assertEquals(GuestState.PLAYING, g1.getState());
		assertNotEquals(GuestState.PLAYING, g2.getState());
		assertEquals(5L, game.getStartActionAt());
		assertEquals(Status.IN_USE, game.getStatus());
	}

	@Test
	void startGame2() {
		Game game = new Game(null, 1, 0, 0, 2, 0);
		Guest g1 = new Guest(0, 0, 0, game);
		game.addToQueue(g1);

		GameService.startGame(game, 5L);
		assertEquals(0, game.getQueue().size());
		assertEquals(1, game.getCurrentUsing().size());
		assertEquals(GuestState.PLAYING, g1.getState());
		assertEquals(5L, game.getStartActionAt());
		assertEquals(Status.IN_USE, game.getStatus());
	}

	@Test
	void finishGame() {
		Game game = new Game(null, 1, 0, 0, 1, 0);
		Guest g1 = new Guest(0, 0, 0, game);
		final Board board = mock(Board.class);
		g1.setState(GuestState.PLAYING);
		game.getCurrentUsing().add(g1);
		game.setStartActionAt(0L);
		game.setStatus(Status.IN_USE);

		GameService.finishGame(game, 5L, board);

		assertEquals(null, game.getStartActionAt());
		assertEquals(Status.AVAILABLE, game.getStatus());
		assertEquals(0, game.getCurrentUsing().size());
		assertEquals(GuestState.NONE, g1.getState());

	}
}
