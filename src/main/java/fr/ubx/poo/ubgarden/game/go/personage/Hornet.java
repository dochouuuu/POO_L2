package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;

public class Hornet extends Insect implements Movable, WalkVisitor {

    public Hornet (Game game, Position position){
        super(game, position);
        this.setMoveInterval(1000);
        this.setLifePoints(2);
    }
}
