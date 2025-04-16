package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Map;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.WaspNest;

import java.util.Random;

public class Hornet extends Insects implements Movable, WalkVisitor {

    public Hornet (Game game, Position position){
        super(game, position);
        this.setMoveInterval(1000);
    }
    @Override
    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Map map = game.world().getGrid();
        if(!map.inside(nextPos)){
            return false;
        }
        Decor decor = map.get(nextPos);
        return decor == null || decor.walkableBy(this);
    }


}
