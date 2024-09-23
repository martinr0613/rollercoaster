package rollercoaster.controller;

import rollercoaster.model.*;
import rollercoaster.util.GameHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MaintainerService extends PeopleService {

    public static void decideOnMaintainer(Game object, List<Maintainer> maintainers, Board board) {
        if (maintainers.isEmpty()) {
            return;
        }
        int closestIndex = 0;
        for (int i = 1; i < maintainers.size(); i++) {
            if (findPathTo(maintainers.get(i), object, board)
                    .size() < findPathTo(maintainers.get(closestIndex), object, board).size()) {
                closestIndex = i;
            }
        }
        List<Node> tmp = null;
        tmp = findPathTo(maintainers.get(closestIndex), object, board);
        if (tmp == null) {
            maintainers.get(closestIndex).setState(MaintainerState.NONE);
            return;
        } else {
            maintainers.get(closestIndex).setState(MaintainerState.WALKING);
            maintainers.get(closestIndex).setTarget(object);
            object.setWaitingForMaintenance(true);
        }
        return;
    }

    public static void walkAround(Maintainer maintainer, Board board) {
        List<Road> roadList = new ArrayList<>();
        addToRoadListIfRoad(roadList, board, maintainer.getPosX() - 1, maintainer.getPosY());
        addToRoadListIfRoad(roadList, board, maintainer.getPosX() + 1, maintainer.getPosY());
        addToRoadListIfRoad(roadList, board, maintainer.getPosX(), maintainer.getPosY() - 1);
        addToRoadListIfRoad(roadList, board, maintainer.getPosX(), maintainer.getPosY() + 1);
        if (!roadList.isEmpty()) {
            int ind = GameHelper.random(0, roadList.size() - 1);
            maintainer.setPosX(roadList.get(ind).getPosX());
            maintainer.setPosY(roadList.get(ind).getPosY());
        }
    }

    public static void addToRoadListIfRoad(List<Road> roadList, Board board, int posx, int posy) {
        GameObject a;
        if (GameService.isInBounds(posx, posy, board)) {
            a = board.getBoard()[posx][posy];
            if (a != null) {
                if (a.getType() == GameObjectType.ROAD) {
                    roadList.add((Road) a);
                }
            }
        }
    }
}
