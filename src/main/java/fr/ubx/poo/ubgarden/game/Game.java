package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.DoorClosed;
import fr.ubx.poo.ubgarden.game.go.decor.DoorOpened;
import fr.ubx.poo.ubgarden.game.go.decor.Hedgehog;
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

    public Position getHedgehogPosition(){
        return hedgehogPostion;
    }

    public void setHedgehogPosition() {
        if (this.world == null || this.world.getGrid() == null) {
            return;
        }

        for (var decor : this.world.getGrid().values()) {
            if (decor instanceof Hedgehog) {
                this.hedgehogPostion = new Position (world.currentLevel(), decor.getPosition().x(), decor.getPosition().y());
            }
        }
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

    public void openDoors() {
        for (Decor decor : world().getGrid().values()) {
            if (decor instanceof DoorClosed) {
                Position pos = decor.getPosition();
                world().getGrid().put(pos, new DoorOpened(pos));
            }
        }
    }

    public int totalCarrots() {
        int cpt = 0;
        for (Decor decor : world().getGrid().values()) {
            if (decor != null && decor.getBonus() instanceof Carrots) {
                cpt++;
            }
        }
        return cpt;
    }

}