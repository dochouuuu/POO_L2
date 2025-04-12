package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;

import java.util.Random;


public class HornetNest extends Decor {
    private long lastHornet = 0;
    private static final int SPAWN_INTERVAL = 10000; // Intervalle de 5 secondes

    public HornetNest(Position position) {
        super(position);
    }

    @Override
    public void update(long now) {
        // Rien à faire ici pour le moment
    }

    public Hornet spawnHornet(long now, Game game) {
        long inactiveTime = (now - lastHornet) / 1_000_000; // Convertir en millisecondes
        if (inactiveTime >= SPAWN_INTERVAL) {
            Random rand = new Random();
            Position newPosition = null;

            // Répéter tant qu'une position valide n'est pas trouvée
            while (newPosition == null || !game.world().getGrid().inside(newPosition)) {
                int offsetX = rand.nextInt(5) - 2;
                int offsetY = rand.nextInt(5) - 2;

                int x = getPosition().x() + offsetX;
                int y = getPosition().y() + offsetY;

                newPosition = new Position(game.world().currentLevel(), x, y);
            }

            lastHornet = now;
            return new Hornet(game, newPosition);
        }
        return null;
    }


}
