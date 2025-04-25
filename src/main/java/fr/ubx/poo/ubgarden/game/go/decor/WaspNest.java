package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;

import java.util.Random;


public class WaspNest extends Nest {
    public WaspNest(Position position) {
        super(position);
        this.setSpawnInterval(5000); // Intervalle de 5 secondes
    }

    @Override
    public Wasp createInsect(Game game, Position position) {
        return new Wasp(game, position);
    }

    public Wasp spawnWasp(long now, Game game) {
        Insect insect = spawnInsect(now, game);
        return (Wasp) insect;
    }
}
