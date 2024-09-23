package rollercoaster.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;

import rollercoaster.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import rollercoaster.util.GameHelper;

class LogicTest {

	@Mock
	private Board board;

	@ParameterizedTest(name = "{index} => x={0}, y={1}, hasPower={2}")
	@CsvSource({ //
			"1, 1, true", //
			"0, 5, true", //
			"5, 0, true", //
			"0, 6, false",//
	})
	void checkElectricity(int x, int y, boolean hasPower) {
		final Board mockedBoard = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, mockedBoard, 1000));

		List<GameObject> boardAsList = new ArrayList<>();
		Electricity e = new Electricity(null, 5, 0);
		boardAsList.add(e);

		doReturn(boardAsList.iterator()).when(mockedBoard).iterator();

		Restaurant r1 = new Restaurant(null, 0, 0, 0, 0);
		r1.setPosX(x);
		r1.setPosY(y);
		r1.setHasPower(true);

		logic.checkElectricity(r1);

		assertEquals(hasPower, r1.getHasPower());

	}

	@ParameterizedTest(name = "{index} => x={0}, y={1}, hasPower={2}")
	@CsvSource({ //
			"1, 1, true", //
			"0, 5, true", //
			"5, 0, true", //
			"0, 6, false",//
	})
	void checkNearbyBuildings(int x, int y, boolean hasPower) {
		final Board mockedBoard = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, mockedBoard, 1000));

		List<GameObject> boardAsList = new ArrayList<>();
		Restaurant r1 = new Restaurant(null, 0, 0, 0, 0);
		r1.setPosX(x);
		r1.setPosY(y);
		boardAsList.add(r1);

		doReturn(boardAsList.iterator()).when(mockedBoard).iterator();

		Electricity e = new Electricity(null, 5, 0);

		logic.checkNearbyBuildings(e, true);

		assertEquals(hasPower, r1.getHasPower());
	}

	@Test
	void canPlaceElementNotEmptyPlace() {
		try (final MockedStatic<JOptionPane> mockedJOptionPane = mockStatic(JOptionPane.class)) {
			final Board mockedBoard = spy(new Board(11, 15));
			mockedBoard.getBoard()[4][6] = new Game(null, 0, 0, 0, 0, 0);
			Logic logic = spy(new Logic(11, 15, mockedBoard, 100));

			assertFalse(logic.canPlaceElement(4, 6, new Game(null, 0, 0, 0, 0, 0)));
		}
	}

	@Test
	void canPlaceElementNotEnoughMoney() {
		try (final MockedStatic<JOptionPane> mockedJOptionPane = mockStatic(JOptionPane.class)) {
			final Board mockedBoard = spy(new Board(11, 15));
			Logic logic = spy(new Logic(11, 15, mockedBoard, 100));

			assertFalse(logic.canPlaceElement(4, 6, new Game(null, 0, 0, 0, 0, 101)));
		}
	}

	@Test
	void canPlaceElementOK() {
		try (final MockedStatic<JOptionPane> mockedJOptionPane = mockStatic(JOptionPane.class)) {
			final Board mockedBoard = spy(new Board(11, 15));
			Logic logic = spy(new Logic(11, 15, mockedBoard, 100));

			assertTrue(logic.canPlaceElement(4, 6, new Game(null, 0, 0, 0, 0, 0)));
		}
	}

	@Test
	void placeElement() {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));
		doNothing().when(logic).checkElectricity(any(Building.class));
		doNothing().when(logic).checkNearbyBuildings(any(Electricity.class), Mockito.anyBoolean());

		GameObject g = new Game(null, 0, 0, 0, 0, 100);
		logic.placeElement(0, 1, g);

		assertEquals(0, g.getPosX());
		assertEquals(1, g.getPosY());
		assertEquals(900, logic.getCurrentMoney());
	}

	@Test
	void testGameObjectBuildFinished() {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));

		Road r = new Road(null, 0);
		r.setStartActionAt(0L);
		r.setBuildTime(10);

		assertEquals(Status.UNDER_CONSTRUCTION, r.getStatus());

		logic.handleGameObjectTick(5, r);
		assertEquals(Status.UNDER_CONSTRUCTION, r.getStatus());

		logic.handleGameObjectTick(11, r);
		assertEquals(Status.AVAILABLE, r.getStatus());
		assertNull(r.getStartActionAt());
	}

	@Test
	void testGameBuildFinished() {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));

		Game r = new Game(null, 0, 0, 0, 0, 0);
		r.setStartActionAt(0L);
		r.setBuildTime(10);

		assertEquals(Status.UNDER_CONSTRUCTION, r.getStatus());

		logic.handleGameTick(5, r);
		assertEquals(Status.UNDER_CONSTRUCTION, r.getStatus());

		logic.handleGameTick(11, r);
		assertEquals(Status.AVAILABLE, r.getStatus());
		assertNull(r.getStartActionAt());
	}

	@Test
	void testGameCallGameFinished() {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));

		try (MockedStatic<GameService> gameLogic = mockStatic(GameService.class)) {

			Game game = spy(new Game(null, 0, 0, 5, 0, 0));
			game.setStartActionAt(0L);
			game.setStatus(Status.IN_USE);

			logic.handleGameTick(5, game);

			gameLogic.verify(() -> GameService.finishGame(any(Game.class), any(), any(Board.class)));
		}
	}

	@Test
	void tickParkClosed() {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));
		logic.tick();
		verify(logic, times(0)).getCurrentTime();
	}

	@Test
	void tickNotRunning() {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));
		doNothing().when(logic).endOfPeriod(anyLong());
		logic.openPark(true);
		logic.setRunning(false);

		logic.tick();
		verify(logic, times(0)).endOfPeriod(anyLong());
	}

	@Test
	void tickNotCallAllFunction() {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));
		doNothing().when(logic).endOfPeriod(anyLong());
		doNothing().when(logic).tickBoard();
		doNothing().when(logic).generateGuest();
		doNothing().when(logic).tickPeoples();

		logic.openPark(true);
		logic.tick();
		verify(logic).endOfPeriod(anyLong());
		verify(logic).tickBoard();
		verify(logic).generateGuest();
		verify(logic).tickPeoples();
	}

	@Test
	void endOfPeriodSameDay() throws Exception {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));

		Field f1 = Logic.class.getDeclaredField("currentPeriodIncome");
		f1.setAccessible(true);
		f1.set(logic, 2);

		logic.endOfPeriod(0);

		assertEquals(0, logic.getLastPeriodIncome());
	}

	@Test
	void endOfPeriodNewDay() throws Exception {
		final Board board = mock(Board.class);
		doNothing().when(board).setLogic(any(Logic.class));
		Logic logic = spy(new Logic(11, 15, board, 1000));

		Field f1 = Logic.class.getDeclaredField("currentPeriodIncome");
		f1.setAccessible(true);
		f1.set(logic, 2);

		logic.endOfPeriod(1);

		assertEquals(2, logic.getLastPeriodIncome());
		assertEquals(1, logic.getLastDay());
		verify(logic).payMaintainers();
	}

	@Test
	void payMaintainer() {
		final Board board = mock(Board.class);
		doNothing().when(board).setLogic(any(Logic.class));
		Logic logic = spy(new Logic(11, 15, board, 1000));
		final Maintainer maintainer = mock(Maintainer.class);
		logic.addMaintainer(maintainer);
		assertEquals(logic.getMaintainers().size(), 1);
		doReturn(5).when(maintainer).getSalary();
		logic.payMaintainers();
		assertEquals(logic.getCurrentMoney(), 995);
	}

	@Test
	void generateGuestWrongTime() {
		final Board board = mock(Board.class);
		doNothing().when(board).setLogic(any(Logic.class));
		Logic logic = spy(new Logic(11, 15, board, 1000));

		logic.generateGuest();
		assertEquals(0, logic.getGuests().size());
	}

	@Test
	void generateGuestCorrectTime() throws Exception {
		final Board board = mock(Board.class);
		doNothing().when(board).setLogic(any(Logic.class));
		Logic logic = spy(new Logic(11, 15, board, 1000));
		doReturn(mock(Guest.class)).when(logic).createNewGuest();

		Field f1 = Logic.class.getDeclaredField("elapsedTimeInSecond");
		f1.setAccessible(true);
		f1.set(logic, 3);

		logic.generateGuest();
		assertEquals(1, logic.getGuests().size());
	}

	@Test
	void createNewGuestTest() {
		int randomValue = 10;
		try (MockedStatic<GameHelper> gameHelper = mockStatic(GameHelper.class)) {
			gameHelper.when(() -> GameHelper.random(anyInt(), anyInt())).thenReturn(randomValue);

			GameObject g = mock(GameObject.class);
			final Board board = mock(Board.class);
			doNothing().when(board).setLogic(any(Logic.class));
			doReturn(Optional.of(g)).when(board).getBoardElement(anyInt(), anyInt());
			Logic logic = spy(new Logic(11, 15, board, 1000));

			Guest guest = logic.createNewGuest();

			assertEquals(randomValue, guest.getMoney());
			assertEquals(randomValue, guest.getMaxWaitTime());
			assertEquals(g, guest.getPosition());
		}
	}

	@Test
	void findAdjacent() {
		Board mockedBoard = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, mockedBoard, 1000));

		GameObject[][] board = new GameObject[11][15];
		doReturn(true).when(logic).isInBounds(anyInt(), anyInt());
		board[2][2] = new Game(null, 0, 0, 0, 0, 0);
		board[2][2].setPosX(2);
		board[2][2].setPosY(2);

		board[11 / 2][0] = new Gate(null);
		board[11 / 2][0].setPosX(11 / 2);
		board[11 / 2][0].setPosY(0);
		doReturn(11).when(mockedBoard).getN();
		doReturn(15).when(mockedBoard).getM();
		Mockito.when(mockedBoard.getBoardElement(2, 2)).thenReturn(Optional.ofNullable(board[2][2]));

		assertThrows(AssertionError.class, () -> logic.findAdjacent(2, 2));

		Mockito.when(mockedBoard.getBoardElement(11 / 2, 0)).thenReturn(Optional.ofNullable(board[11 / 2][0]));
		assertEquals(board[11 / 2][0], logic.findAdjacent(2, 2));

		board[2][3] = new Game(null, 0, 0, 0, 0, 0);
		board[2][3].setPosX(2);
		board[2][3].setPosY(3);

		Mockito.when(mockedBoard.getBoardElement(2, 3)).thenReturn(Optional.ofNullable(board[2][3]));
		assertEquals(board[2][3], logic.findAdjacent(2, 2));

		board[1][2] = new Game(null, 0, 0, 0, 0, 0);
		board[1][2].setPosX(1);
		board[1][2].setPosY(2);
		Mockito.when(mockedBoard.getBoardElement(1, 2)).thenReturn(Optional.ofNullable(board[1][2]));
		assertEquals(board[1][2], logic.findAdjacent(2, 2));

	}

	@Test
	void tickBoard() {
		Board board = new Board(11, 15);
		Logic logic = spy(new Logic(11, 15, board, 1000));

		Building b = spy(new Restaurant(null, 0, 0, 0, 0));
		doNothing().when(b).tick(any(Logic.class), anyLong());
		doReturn(99).when(b).collectMoney();

		board.getBoard()[1][1] = b;
		logic.tickBoard();

		verify(b).collectMoney();

		assertEquals(1099, logic.getCurrentMoney());
	}

	@Test
	public void handleGuestTickInQueue() {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));

		Building b = mock(Building.class);
		when(b.getPosX()).thenReturn(0);
		when(b.getPosY()).thenReturn(0);

		Guest g = spy(new Guest(0, 0, 0, b));
		g.setState(GuestState.IN_QUEUE);

		when(logic.getElapsedTimeInSecond()).thenReturn(1L);
		logic.handleGuestTick(5L, g);
		assertEquals(95, g.getSpirit());
		assertEquals(2, g.getHunger());

	}

	@Test
	public void handleGuestTickPlaying() {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));

		Building b = mock(Building.class);
		when(b.getPosX()).thenReturn(0);
		when(b.getPosY()).thenReturn(0);

		Guest g = spy(new Guest(0, 0, 0, b));
		g.setState(GuestState.PLAYING);
		g.setSpirit(50);

		when(logic.getElapsedTimeInSecond()).thenReturn(1L);
		logic.handleGuestTick(5L, g);
		assertEquals(57, g.getSpirit());
		assertEquals(2, g.getHunger());
	}

	@Test
	public void handleGuestTickEating() {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));

		Building b = mock(Building.class);
		when(b.getPosX()).thenReturn(0);
		when(b.getPosY()).thenReturn(0);

		Guest g = spy(new Guest(0, 0, 0, b));
		g.setState(GuestState.EATING);
		g.setHunger(50);

		when(logic.getElapsedTimeInSecond()).thenReturn(1L);
		logic.handleGuestTick(5L, g);
		assertEquals(10, g.getHunger());
	}

	@Test
	public void handleGuestTickLeaving() {
		Board board = spy(new Board(11, 15));
		Logic logic = spy(new Logic(11, 15, board, 1000));
		Gate gate = mock(Gate.class);
		when(gate.getPosX()).thenReturn(11 / 2);
		when(gate.getPosY()).thenReturn(0);
		Building b = mock(Building.class);
		when(b.getPosX()).thenReturn(0);
		when(b.getPosY()).thenReturn(0);
		doNothing().when(b).removeFromQueue(any(Guest.class));

		doReturn(GameObjectType.GATE).when(gate).getType();
		doReturn(true).when(logic).isInBounds(anyInt(), anyInt());

		Guest g = new Guest(0, 0, 0, gate);
		g.setState(GuestState.IN_QUEUE);
		g.setSpirit(0);
		g.setPosX(11 / 2);
		g.setPosY(0);

		GuestService.decideNewTarget(g, board);

		try (MockedStatic<PeopleService> peopleService = mockStatic(PeopleService.class)) {
			when(logic.getElapsedTimeInSecond()).thenReturn(1L);
			logic.handleGuestTick(5L, g);
			assertEquals(GuestState.WALKING, g.getState());
			assertEquals(g.getTarget().getType(), GameObjectType.GATE);
			when(logic.getElapsedTimeInSecond()).thenReturn(2L);
			g.setPosition(gate);
			peopleService.when(() -> PeopleService.checkArrived(any(People.class), any(GameObject.class)))
					.thenReturn(true);
			logic.handleGuestTick(10L, g);
			assertEquals(GuestState.LEFT, g.getState());
		}
	}

	@Test
	public void handleMaintainerTickRepair() throws Exception {
		final Board board = mock(Board.class);
		Logic logic = spy(new Logic(11, 15, board, 1000));
		when(board.getBoard()).thenReturn(new GameObject[11][15]);

		Field f1 = Logic.class.getDeclaredField("elapsedTimeInSecond");
		f1.setAccessible(true);
		f1.set(logic, 10);

		Game game = spy(new Game(null, 0, 0, 0, 0, 0));
		Maintainer m = spy(new Maintainer(game));
		when(m.getStartRepairAt()).thenReturn(0L);
		m.setTarget(game);
		m.setState(MaintainerState.REPAIR);

		logic.handleMaintainerTick(0L, m);

		assertEquals(game.getStatus(), Status.AVAILABLE);
		assertEquals(game.getNeedsMaintenance(), false);

		assertEquals(m.getTarget(), null);
		assertEquals(m.getPosition(), null);
		assertEquals(m.getState(), MaintainerState.NONE);

	}

}