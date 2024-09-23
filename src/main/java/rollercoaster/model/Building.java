package rollercoaster.model;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public abstract class Building extends GameObject {
    protected int useCost;
    protected int localMoney = 0;
    protected int capacity;
    protected boolean targetable;
    protected int useTime;
    protected boolean hasPower;
    protected boolean needsMaintenance;
    protected int useCostDefault;
    protected long creationTime;
    protected int moneyMade;
    protected int guestsServed;
    protected List<Guest> queue = new ArrayList<>(); // possibly make public to access queue methods easily
    protected List<Guest> currentUsing = new ArrayList<>();
    protected boolean waitingForMaintenance;

    public Building(Image texture, int useCost, int capacity, int useTime, int buildPrice, GameObjectType type) {
        super(texture, buildPrice, type);
        this.hasPower = false;
        this.needsMaintenance = false;
        this.useCost = useCost;
        this.useCostDefault = useCost;
        this.useTime = useTime;
        this.capacity = capacity;
        this.moneyMade = 0;
        this.guestsServed = 0;
    }

    public int collectMoney() {
        int tmp = localMoney;
        localMoney = 0;
        return tmp;
    }

    public void setNeedsMaintenance(boolean needsMaintenance) {
        this.needsMaintenance = needsMaintenance;
    }

    public void setHasPower(boolean hasPower) {
        this.hasPower = hasPower;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setUseCost(int useCost) {
        this.useCost = useCost;
    }

    public int getLocalMoney() {
        return localMoney;
    }

    public int getMoneyMade() {
        return moneyMade;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public int getGuestsServed() {
        return guestsServed;
    }

    public void increaseMoneyMade(int money) {
        this.moneyMade += money;
    }

    public void increaseGuestsServed(int count) {
        this.guestsServed += count;
    }

    public int getUseCostDefault() {
        return useCostDefault;
    }

    public void setLocalMoney(int localMoney) {
        this.localMoney = localMoney;
    }

    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }

    public void setQueue(List<Guest> queue) {
        this.queue = queue;
    }

    public void setCurrentUsing(List<Guest> currentUsing) {
        this.currentUsing = currentUsing;
    }

    protected void addToCurrentUsing(Guest newGuest) {
        this.currentUsing.add(newGuest);
    }

    public void addToQueue(Guest guest) {
        this.queue.add(guest);
    }

    public void removeFromQueue(Guest guest) {
        this.queue.remove(guest);
    }

    public boolean getHasPower() {
        return hasPower;
    }

    public boolean getNeedsMaintenance() {
        return needsMaintenance;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getUseTime() {
        return useTime;
    }

    public int getUseCost() {
        return useCost;
    }

    public List<Guest> getQueue() {
        return queue;
    }

    public boolean isTargetable() {
        return status != Status.UNDER_CONSTRUCTION && !needsMaintenance && hasPower;
    }

    public List<Guest> getCurrentUsing() {
        return currentUsing;
    }

    public boolean getWaitingForMaintenance() {
        return waitingForMaintenance;
    }

    public void setWaitingForMaintenance(boolean waiting) {
        waitingForMaintenance = waiting;
    }

}
