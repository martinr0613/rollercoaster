package rollercoaster.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.util.Collection;
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

public class PeopleServiceTest {

    @Test
    public void backTracePath() {
        Node n1 = new Node(0, 1, null, 0, 0, 0);
        Node n2 = new Node(0, 2, n1, 0, 0, 0);
        Node n3 = new Node(0, 3, n2, 0, 0, 0);
        List<Node> nodes = new ArrayList<>();
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);

        Guest g = mock(Guest.class);

        assertEquals(nodes, PeopleService.backtracePath(g, n3));
    }

    @Test
    public void addNewNode() {
        Node cur = new Node(0, 1, null, 0, 0, 0);
        Node start = new Node(0, 2, null, 0, 0, 0);
        List<Node> nodes = new ArrayList<>();

        Road r = mock(Road.class);
        doReturn(0).when(r).getPosX();
        doReturn(0).when(r).getPosY();

        PeopleService.addNewNode(nodes, cur, 0, 4, start, r);

        assertEquals(PeopleService.calcDistance(0, r.getPosX(), 4, r.getPosY()), nodes.get(0).getHcost());
        assertEquals(cur, nodes.get(0).getParent());
        assertEquals(0, nodes.get(0).getCurx());
        assertEquals(4, nodes.get(0).getCury());
    }

    @Test
    public void checkWalkableNeighbourNotRoad() {
        final Board mockedBoard = mock(Board.class);
        doReturn(11).when(mockedBoard).getN();
        doReturn(15).when(mockedBoard).getM();

        GameObject g = new Game(null, 0, 0, 0, 0, 100);
        GameObject target = new Game(null, 0, 0, 0, 0, 100);
        GameObject[][] board = new GameObject[11][15];
        board[2][2] = g;
        board[2][2].setPosX(2);
        board[2][2].setPosY(2);
        doReturn(board).when(mockedBoard).getBoard();

        assertEquals(false, PeopleService.checkWalkableNeighbour(g.getPosX(), g.getPosY(), mockedBoard, target));
    }

    @Test
    public void checkWalkableNeighbourRoad() {
        final Board mockedBoard = mock(Board.class);
        doReturn(11).when(mockedBoard).getN();
        doReturn(15).when(mockedBoard).getM();

        Road r = new Road(null, 0);
        r.setPosX(2);
        r.setPosY(2);
        GameObject[][] board = new GameObject[11][15];
        board[2][2] = r;

        doReturn(board).when(mockedBoard).getBoard();

        assertEquals(true, PeopleService.checkWalkableNeighbour(r.getPosX(), r.getPosY(), mockedBoard, r));

    }

    @Test
    public void neighbourInClosedTrue() {
        Node n1 = new Node(0, 1, null, 0, 0, 0);
        Node n2 = new Node(0, 2, n1, 0, 0, 0);
        Node n3 = new Node(0, 3, n2, 0, 0, 0);
        List<Node> nodes = new ArrayList<>();
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);

        assertEquals(true, PeopleService.neighbourInClosed(0, 3, nodes));
    }

    @Test
    public void neighbourInClosedFalse() {
        Node n1 = new Node(0, 1, null, 0, 0, 0);
        Node n2 = new Node(0, 2, n1, 0, 0, 0);
        Node n3 = new Node(0, 3, n2, 0, 0, 0);
        List<Node> nodes = new ArrayList<>();
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);

        assertEquals(false, PeopleService.neighbourInClosed(0, 4, nodes));
    }

    @Test
    public void isInPathTrue() {
        final Board mockedBoard = mock(Board.class);
        doReturn(11).when(mockedBoard).getN();
        doReturn(15).when(mockedBoard).getM();

        Node n1 = new Node(0, 1, null, 0, 0, 0);
        Node n2 = new Node(0, 2, n1, 0, 0, 0);
        Node n3 = new Node(0, 3, n2, 0, 0, 0);
        List<Node> nodes = new ArrayList<>();
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);

        Game game = mock(Game.class);
        doReturn(0).when(game).getPosX();
        doReturn(2).when(game).getPosY();

        Guest guest = new Guest(0, 0, 0, game);
        guest.setRemainingPath(nodes);

        assertEquals(true, PeopleService.isInPath(game, guest));

    }

    @Test
    public void isInPathFalse() {
        final Board mockedBoard = mock(Board.class);
        doReturn(11).when(mockedBoard).getN();
        doReturn(15).when(mockedBoard).getM();

        Node n1 = new Node(0, 1, null, 0, 0, 0);
        Node n2 = new Node(0, 2, n1, 0, 0, 0);
        Node n3 = new Node(0, 3, n2, 0, 0, 0);
        List<Node> nodes = new ArrayList<>();
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);

        Game game = mock(Game.class);
        doReturn(0).when(game).getPosX();
        doReturn(4).when(game).getPosY();

        Guest guest = new Guest(0, 0, 0, game);
        guest.setRemainingPath(nodes);

        assertEquals(false, PeopleService.isInPath(game, guest));

    }

}
