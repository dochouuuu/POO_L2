package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.DoorClosed;
import fr.ubx.poo.ubgarden.game.go.decor.DoorOpened;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;


public class Game {

    private final Configuration configuration;
    private final World world;
    private final Gardener gardener;
    private Position hedgehogPostion;
    private boolean switchLevelRequested = false;
    private int switchLevel;
    public Game(World world, Configuration configuration, Position gardenerPosition) {
        this.configuration = configuration;
        this.world = world;
        gardener = new Gardener(this, gardenerPosition);
    }

    public Position getHedgehogPostion(){
        return hedgehogPostion;
    }

    public void setHedgehogPostion(Position position){
        this.hedgehogPostion = position;
    }

    public Configuration configuration() {
        return configuration;
    }

    public Gardener getGardener() {
        return this.gardener;
    }

    public World world() {
        return world;
    }

    public boolean isSwitchLevelRequested() {
        return switchLevelRequested;
    }

    public int getSwitchLevel() {
        return switchLevel;
    }

    public void requestSwitchLevel(int level) {
        this.switchLevel = level;
        switchLevelRequested = true;
    }

    public void clearSwitchLevel() {
        switchLevelRequested = false;
    }

    public void openAllDoors() {
        for (Decor decor : world().getGrid().values()) {
            if (decor instanceof DoorClosed) {
                Position p = decor.getPosition();
                world().getGrid().get(p).remove();
            }
        }
    }

    public int totalCarrots() {
        int cpt = 0;
        for (Decor decor : world().getGrid().values()) {
            if (decor.getBonus() instanceof Carrots) {
                cpt++;
            }
        }
        return cpt;
    }

}