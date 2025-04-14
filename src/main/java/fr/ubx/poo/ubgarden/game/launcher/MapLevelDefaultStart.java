package fr.ubx.poo.ubgarden.game.launcher;


import fr.ubx.poo.ubgarden.game.go.decor.*;

import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.*;

public class MapLevelDefaultStart extends MapLevel {


    private final static int width = 18;
    private final static int height = 8;
        private final MapEntity[][] level1 = {
                {Carrots, Carrots, Carrots, Grass, Hedgehog, DoorNextOpened, DoorNextClosed, DoorPrevOpened, Flowers, Tree, Tree, Tree, Grass, Tree, Grass, Tree, Grass, NestHornet},
                {Carrots, Gardener, Grass, Grass, Grass, Grass, Grass, Grass, Flowers, Tree, Grass, Grass, Grass, Tree, Tree, Tree, Grass, Grass},
                {Carrots, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Carrots, Tree, Tree, Tree, Grass, Tree, PoisonedApple, Tree, Grass, Grass},
                {Tree, Grass, Tree, Tree, Grass, Tree, Tree, Tree, Carrots, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Apple, Grass},
                {Tree, Grass, Tree, Grass, Grass, Tree, Grass, Tree, Flowers, Tree, Tree, Tree, Grass, Tree, Grass, Tree, Grass, Grass},
                {Tree, Grass, Tree, Tree, Grass, Tree, Tree, Tree, Flowers, Tree, Grass, Tree, Grass, Tree, Grass, Tree, Grass, Grass},
                {Tree, Apple, Tree, Grass, Grass, Tree, Grass, Tree, Flowers, Tree, Tree, Tree, Grass, Tree, Insecticide, Tree, Grass, Grass},
                {Tree, Tree, Tree, Tree, Grass, Tree, Grass, Tree, Flowers, Tree, Grass, Tree, Grass, Tree, Tree, Tree, Grass, NestWasp}
        };
        /*private final MapEntity[][] level1 = {
                {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
                {Grass, Gardener, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Tree, Grass, Grass, Tree, Tree, Grass, Grass, Grass, Grass},
                {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Tree, Grass, Grass, Grass, Grass},
                {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Tree, Grass, Grass, Grass, Grass},
                {Grass, Tree, Grass, Tree, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Tree, Grass, Grass, Apple, Grass},
                {Grass, Tree, Tree, Tree, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
                {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
                {Grass, Tree, Grass, Tree, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass}
        };*/
    public MapLevelDefaultStart() {
        super(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, level1[j][i]);
    }
}
