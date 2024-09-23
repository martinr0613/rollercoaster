package rollercoaster.model;

import java.util.List;

public class Node {
    private double fcost;
    private double gcost;
    private double hcost;
    private int curx;
    private int cury;
    private Node parent;

    public Node() {
    }

    public Node(int curx, int cury, Node parent, int fcost, int gcost, int hcost) {
        this.curx = curx;
        this.cury = cury;
        this.hcost = hcost;
        this.gcost = gcost;
        this.fcost = fcost;
        this.parent = parent;
    }

    public int getCurx() {
        return curx;
    }

    public int getCury() {
        return cury;
    }

    public double getFcost() {
        return fcost;
    }

    public double getGcost() {
        return gcost;
    }

    public double getHcost() {
        return hcost;
    }

    public Node getParent() {
        return parent;
    }

    public void setFcost(double fcost) {
        this.fcost = fcost;
    }

    public void setGcost(double gcost) {
        this.gcost = gcost;
    }

    public void setHcost(double hcost) {
        this.hcost = hcost;
    }

    public void setCurx(int curx) {
        this.curx = curx;
    }

    public void setCury(int cury) {
        this.cury = cury;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean checkIfRoadInNodeList(Road road, List<Node> nodeList) {
        boolean found = false;
        boolean end = false;
        int counter = 0;
        while (!found && !end) {
            if (nodeList.size() <= counter) {
                end = true;
            } else {
                if (nodeList.get(counter).curx == road.getPosX() && nodeList.get(counter).cury == road.getPosY()) {
                    found = true;
                }
            }

        }
        return found;
    }
}
