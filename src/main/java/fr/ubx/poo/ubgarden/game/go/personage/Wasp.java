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

public class Wasp extends GameObject implements Movable, WalkVisitor {

    private Direction direction;
    private boolean moveRequested = false;

    private final int MOVE_INTERVAL = 1000; // Intervalle de 1 seconde

    public Wasp(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
    }


    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
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

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        setPosition(nextPos);
        return nextPos;
    }

    private long lastMoveTime = 0; // Dernière fois que la guêpe s'est déplacée
    @Override
    public void update(long now) {
        long elapsedTime = (now - lastMoveTime) / 1_000_000; // Convertir en millisecondes
        if (elapsedTime >= MOVE_INTERVAL) {
            Direction[] directions = Direction.values();
            Random rand = new Random();
            Direction randomDirection;

            // Boucle tant qu'une direction valide n'est pas trouvée
            boolean moved = false;
            while (!moved) {
                randomDirection = directions[rand.nextInt(directions.length)];
                if (canMove(randomDirection)) {
                    this.direction = randomDirection;
                    move(randomDirection); // Change la position dans une direction valide
                    moved = true; // Marque que le mouvement a été effectué
                }
            }

            lastMoveTime = now; // Met à jour le dernier moment de déplacement
        }

        moveRequested = false; // Réinitialise la demande de déplacement
    }




    public void hurt(int damage) {
    }

    public void hurt() {
        hurt(1);
    }

    public Direction getDirection() {
        return direction;
    }


}
