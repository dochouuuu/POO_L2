package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Map;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.PickupVisitor;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.bonus.Insecticide;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Grass;

import java.util.Random;

public abstract class Insect extends GameObject implements Movable, PickupVisitor, WalkVisitor {
    private Direction direction;
    private boolean moveRequested = false;
    private int lifePoints = 1;


    private int MOVE_INTERVAL = 1; // Intervalle de 1 seconde

    public Insect(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
    }

    public void setMoveInterval (int interval){
        this.MOVE_INTERVAL = interval;
    }

    public int getLifePoints () { return this.lifePoints; }

    public void setLifePoints (int lifePoints) { this.lifePoints = lifePoints; }

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

    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);

        // Si l'insecte marche sur une bombe insecticide, il perd un point de vie
        if (next instanceof Grass && ((Grass) next).getBonus() instanceof Insecticide) {
            this.hurt(1);
            ((Grass) next).getBonus().remove(); // On supprime l'insecticide pour éviter qu'il y en ait trop
        }

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

            boolean moved = false;
            int attempts = 0;

            // Essaie au plus autant de directions qu'il y en a (pour éviter une boucle infinie)
            while (!moved && attempts < directions.length) {
                Direction randomDirection = directions[rand.nextInt(directions.length)];
                if (canMove(randomDirection)) {
                    this.direction = randomDirection;
                    move(randomDirection); // Change la position dans une direction valide
                    moved = true; // Marque que le mouvement a été effectué
                }
                attempts++;
            }
            lastMoveTime = now; // Met à jour le dernier moment de déplacement
        }

        moveRequested = false ; // Réinitialise la demande de déplacement
    }


    public void hurt(int damage) { this.lifePoints = this.lifePoints - damage; }

    public Direction getDirection() {
        return direction;
    }

}
