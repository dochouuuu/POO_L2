package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.Insecticide;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Grass;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;

import java.util.Random;


public class Nest extends Decor {
    private long lastInsect = 0;
    private int SPAWN_INTERVAL = 0; // Intervalle de 5 secondes

    public Nest(Position position) {
        super(position);
    }

    public void setSpawnInterval(int interval) {
        this.SPAWN_INTERVAL = interval;
    }

    @Override
    public void update(long now) {
        // Rien à faire ici pour le moment
    }

    // Méthode abstraite que les sous-classes devront implémenter pour créer le bon insecte
    public Insect createInsect(Game game, Position position) {
        return null;
    }

    public Insect spawnInsect(long now, Game game) {
        long inactiveTime = (now - lastInsect) / 1_000_000; // Convertir en millisecondes
        if (inactiveTime >= SPAWN_INTERVAL) {
            Position insectPosition = Position.randomPos(game, getPosition(), 2);

            lastInsect = now;
            return createInsect(game, insectPosition);
        }
        return null;
    }


}
