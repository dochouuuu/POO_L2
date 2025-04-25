package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
import fr.ubx.poo.ubgarden.game.go.personage.Insect;


public class HornetNest extends Nest {
    public HornetNest(Position position) {
        super(position);
        this.setSpawnInterval(10000); // Intervalle de 10 secondes
    }

    @Override
    public Hornet createInsect(Game game, Position position) {
        return new Hornet(game, position);
    }

    public Hornet spawnHornet(long now, Game game) {
        Insect insect = spawnInsect(now, game);
        return (Hornet) insect;
    }
}
