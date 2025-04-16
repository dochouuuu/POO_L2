package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.view.ImageResourceFactory;
import javafx.scene.image.Image;

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
        Door opened = new Door(pos, true);

        for (Decor decor1 : game.world().getGrid().values()) {
            if (decor1 != null && decor1.getPosition().equals(pos)) {
                decor1.remove();
            }
        }
        game.world().getGrid().put(pos, opened);
        //game.world().getGrid().values().add(opened);
        System.out.println(game.world().getGrid().values());
        opened.setModified(true);
        System.out.println("🚪 Porte ouverte à " + pos.x() + ";" + pos.y());

    }
}
