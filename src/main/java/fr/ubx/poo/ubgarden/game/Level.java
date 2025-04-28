package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.bonus.*;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.decor.ground.*;
import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import fr.ubx.poo.ubgarden.game.launcher.MapLevel;

import java.util.Collection;
import java.util.HashMap;

public class Level implements Map {

    private final int level;
    private final int width;

    private final int height;

    private final java.util.Map<Position, Decor> decors = new HashMap<>();

    public Level(Game game, int level, MapLevel entities) {
        this.level = level;
        this.width = entities.width();
        this.height = entities.height();

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Position position = new Position(level, i, j);
                MapEntity mapEntity = entities.get(i, j);
                switch (mapEntity) {
                    case Grass:
                        decors.put(position, new Grass(position));
                        break;
                    case Tree:
                        decors.put(position, new Tree(position));
                        break;
                    case Flowers:
                        decors.put(position, new Flowers(position));
                        break;
                    case Land:
                        decors.put(position, new Land(position));
                        break;
                    case Apple: {
                        Decor grass = new Grass(position);
                        grass.setBonus(new EnergyBoost(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case Carrots:  {
                        Decor land = new Land(position);
                        land.setBonus(new Carrots(position, land));
                        decors.put(position, land);
                        break;
                    }
                    case PoisonedApple:  {
                        Decor grass = new Grass(position);
                        grass.setBonus(new PoisonedApple(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case Insecticide:  {
                        Decor grass = new Grass(position);
                        grass.setBonus(new Insecticide(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case Hedgehog:
                        Hedgehog hedgehog = new Hedgehog(position);
                        decors.put(position, hedgehog);
                        hedgehog.setPosition(position);
                        break;
                    case NestWasp:
                        decors.put(position, new WaspNest(position));
                        break;
                    case NestHornet:
                        decors.put(position, new HornetNest(position));
                        break;
                    case DoorNextOpened: {
                        Door door = new Door(position, true, true); // porte ouverte vers le niveau suivant
                        decors.put(position, door);
                        break;
                    }
                    case DoorPrevOpened: {
                        Door door = new Door(position, true, false); // porte ouverte vers le niveau précédent
                        decors.put(position, door);
                        break;
                    }
                    case DoorNextClosed: {
                        Door door = new Door(position, false, true); // porte FERMÉE vers le niveau suivant
                        decors.put(position, door);
                        break;
                    }
                    default:
                        throw new RuntimeException("EntityCode " + mapEntity.name() + " not processed");
                }
            }
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    public Decor get(Position position) {
        return decors.get(position);
    }

    public Collection<Decor> values() {
        return decors.values();
    }

    @Override
    public boolean inside(Position position) {
        if ((position.x() < 0) || (position.x() >= width()) ||
                (position.y() < 0) || (position.y() >= height())){
            return false;
        }
        return true;
    }

    @Override
    public void put(Position position, Decor decor) {
        decors.put(position, decor);
    }
}
