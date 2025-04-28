package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;

public class Door extends Decor {
    private boolean isOpen;
    private final boolean toNextLevel;

    public Door(Position position, boolean isOpen, boolean toNextLevel) {
        super(position);
        this.isOpen = isOpen;
        this.toNextLevel = toNextLevel;
    }

    public boolean isToNextLevel() {
        return toNextLevel;
    }

    public void setIsOpen (Boolean isOpen){
        this.isOpen = isOpen;
    }

    public boolean getIsOpen (){
        return this.isOpen;
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return this.isOpen;
    }

    public boolean walkableBy (Insect insect) { return false;}
}