package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Door extends Decor {
    private boolean isOpen;
    private boolean toNextLevel;

    public Door(Position position, boolean isOpen, boolean toNextLevel) {
        super(position);
        this.isOpen = isOpen;
        this.toNextLevel = toNextLevel;
    }

    public Door(Position position) {
        this(position, false, true);
    }

    public Door(Position position, boolean isOpen) {
        this(position, isOpen, true);
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

    /*public void open(Game game) {
        Position pos = this.getPosition();
        Door opened = new Door(pos, true, this.toNextLevel);
        game.world().getGrid().put(pos, opened);
        opened.setModified(true);

        System.out.println("Porte ouverte à (" + pos.x() + ", " + pos.y() + ")");
    }*/
}