package rollercoaster.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Image;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import rollercoaster.model.*;

public class BoardTest {

	@Test
	void setSelectedGameObjectCantPlace() {
		final Logic logic = mock(Logic.class);
		Board board = spy(new Board(2, 2));
		board.setLogic(logic);

		when(logic.canPlaceElement(anyInt(), anyInt(), any(GameObject.class))).thenReturn(false);
		board.setSelectedGameObject(0, 0);
		verify(logic).canPlaceElement(anyInt(), anyInt(), any(GameObject.class));
		assertNull(board.getBoard()[0][0]);
	}

	@Test
	void setSelectedGameObjectCanPlace() {
		final Logic logic = mock(Logic.class);
		Board board = spy(new Board(2, 2));
		board.setLogic(logic);

		when(board.getSelectedObject()).thenReturn(new Plant(null, 0, 0, 0));
		when(logic.canPlaceElement(anyInt(), anyInt(), any(GameObject.class))).thenReturn(true);
		board.setSelectedGameObject(0, 0);
		verify(logic).canPlaceElement(anyInt(), anyInt(), any(GameObject.class));
		verify(logic).placeElement(anyInt(), anyInt(), any(GameObject.class));

		assertNotNull(board.getBoard()[0][0]);
	}

	@Test
	void addSelectedElementToGridPlacePlantCantCalledNeighbourUpdate() {
		final Logic logic = mock(Logic.class);
		Board board = spy(new Board(2, 2));
		board.setLogic(logic);

		when(board.getSelectedObject()).thenReturn(new Plant(null, 0, 0, 0));
		when(logic.canPlaceElement(anyInt(), anyInt(), any(GameObject.class))).thenReturn(true);
		board.addSelectedElementToGrid(0, 0);
		verify(board, times(0)).calculateNeighbourRoadsTextureUponPlacement(anyInt(), anyInt(), any(GameObject.class));
	}

	@Test
	void addSelectedElementToGridPlaceElectricityCantCalledNeighbourUpdate() {
		final Logic logic = mock(Logic.class);
		Board board = spy(new Board(2, 2));
		board.setLogic(logic);

		when(board.getSelectedObject()).thenReturn(new Electricity(null, 0, 0));
		when(logic.canPlaceElement(anyInt(), anyInt(), any(GameObject.class))).thenReturn(true);
		board.addSelectedElementToGrid(0, 0);
		verify(board, times(0)).calculateNeighbourRoadsTextureUponPlacement(anyInt(), anyInt(), any(GameObject.class));
	}

	@Test
	void addSelectedElementToGridPlaceOtherCalledNeighbourUpdate() {
		final Logic logic = mock(Logic.class);
		Board board = spy(new Board(2, 2));
		board.setLogic(logic);

		when(board.getSelectedObject()).thenReturn(new Restaurant(null, 0, 0, 0, 0));
		when(logic.canPlaceElement(anyInt(), anyInt(), any(GameObject.class))).thenReturn(true);
		board.addSelectedElementToGrid(0, 0);
		verify(board).calculateNeighbourRoadsTextureUponPlacement(anyInt(), anyInt(), any(GameObject.class));
	}

	@Test
	void setSelectedGameObjectCanRemove() {
		try (final MockedStatic<JOptionPane> mockedJOptionPane = mockStatic(JOptionPane.class)) {
			final Logic logic = mock(Logic.class);
			Board board = spy(new Board(2, 2));
			board.setLogic(logic);
			board.getBoard()[0][0] = new Plant(null, 0, 0, 0);

			when(board.getSelectedObject()).thenReturn(new Bin(null));
			when(logic.canPlaceElement(anyInt(), anyInt(), any(GameObject.class))).thenReturn(true);
			board.setSelectedGameObject(0, 0);
			verify(logic).removeElement(anyInt(), anyInt(), any(GameObject.class));

			assertNull(board.getBoard()[0][0]);
		}
	}

	@ParameterizedTest(name = "{index} => x={0}, y={1}")
	@CsvSource({ "-1, 0", "0, -1", "3, 0", "0, 3", })
	void setConnectableNeighbourOutOfDeimensionReturn0(int x, int y) {
		Board board = spy(new Board(2, 2));
		assertEquals(0, board.setConnectableNeighbour(x, y, 0, 0));
	}

	@Test
	void setConnectableNeighbourNullInPosReturn0() {
		Board board = spy(new Board(2, 2));
		assertEquals(0, board.setConnectableNeighbour(0, 0, 0, 0));
	}

	@Test
	void setConnectableNeighbourNotRoadReturn0() {
		Board board = spy(new Board(2, 2));
		Plant p = new Plant(null, 0, 0, 0);
		board.getBoard()[0][0] = p;
		assertEquals(0, board.setConnectableNeighbour(0, 0, 0, 0));
	}

	@Test
	void setConnectableNeighbourRoadCalledSetRoadTexture() {
		Board board = spy(new Board(2, 2));
		doNothing().when(board).setRoadTexture(any(Road.class), anyInt());
		Road p = new Road(null, 0);
		board.getBoard()[0][0] = p;

		board.setConnectableNeighbour(0, 0, 0, 100);
		verify(board).setRoadTexture(p, 100);
	}

	@Test
	void setConnectableNeighbourRoadReturnDir() {
		Board board = spy(new Board(2, 2));
		doNothing().when(board).setRoadTexture(any(Road.class), anyInt());
		Road p = new Road(null, 0);
		board.getBoard()[0][0] = p;

		assertEquals(100, board.setConnectableNeighbour(0, 0, 100, 0));
	}

	@Test
	void setConnectableNeighbourGateNotCalledSetRoadTexture() {
		Board board = spy(new Board(2, 2));
		doNothing().when(board).setRoadTexture(any(Road.class), anyInt());
		Gate p = new Gate(null);
		board.getBoard()[0][0] = p;

		board.setConnectableNeighbour(0, 0, 0, 100);
		verify(board, times(0)).setRoadTexture(any(Road.class), anyInt());
	}

	@Test
	void setConnectableNeighbourGateReturnDir() {
		Board board = spy(new Board(2, 2));
		doNothing().when(board).setRoadTexture(any(Road.class), anyInt());
		Gate p = new Gate(null);
		board.getBoard()[0][0] = p;

		assertEquals(8, board.setConnectableNeighbour(0, 0, 8, 0));
		assertEquals(0, board.setConnectableNeighbour(0, 0, 2, 0));
		assertEquals(0, board.setConnectableNeighbour(0, 0, 5, 0));
		assertEquals(0, board.setConnectableNeighbour(0, 0, 0, 0));
	}

	@Test
	void removeGameObjectNull() {
		Board board = spy(new Board(2, 2));

		board.removeGameObject(0, 0);
		assertNull(board.getBoard()[0][0]);
	}

	@Test
	void removeGameObjectNotNull() {
		Board board = spy(new Board(2, 2));
		Plant p = new Plant(null, 0, 0, 0);
		board.getBoard()[0][0] = p;

		board.removeGameObject(0, 0);
		assertNull(board.getBoard()[0][0]);
	}

	@Test
	void getBoardElementTextureNull()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Board board = spy(new Board(2, 2));

		assertNull(board.getBoardElementTexture(0, 0));
	}

	@Test
	void getBoardElementTextureNotNull() {
		Image mockImage = mock(Image.class);
		Board board = spy(new Board(2, 2));
		Plant p = new Plant(mockImage, 0, 0, 0);
		board.getBoard()[0][0] = p;

		assertEquals(mockImage, board.getBoardElementTexture(0, 0));
	}
}
