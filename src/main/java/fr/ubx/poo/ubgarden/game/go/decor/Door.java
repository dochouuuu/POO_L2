package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Door extends Decor {
    private boolean isOpen;
    public Door(Position position) {
        super(position);
        this.isOpen = false;
    }

    public Door(Position position, boolean isOpen) {
        super(position);
        this.isOpen = isOpen;
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

    public void openDoors(Game game, Decor decor) {
        Position pos = decor.getPosition();
        Door openedDoor = new Door(pos, true);

        // Remplace dans la grille
        game.world().getGrid().put(pos, openedDoor);

        // Signale que la porte doit être redessinée
        openedDoor.setModified(true);

        System.out.println("Porte ouverte à (" + pos.x() + ", " + pos.y() + ")");
    }
}