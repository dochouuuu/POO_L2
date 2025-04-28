package fr.ubx.poo.ubgarden.game;

import java.util.Random;

public record Position (int level, int x, int y) {
    public static Position randomPos(Game game, Position reference, int range) {
        Random rand = new Random();
        Position newPosition = null;

        while (newPosition == null || !game.world().getGrid().inside(newPosition)) {
            int offsetX = rand.nextInt(range * 2 + 1) - range; // Génère un décalage entre -range et +range
            int offsetY = rand.nextInt(range * 2 + 1) - range;

            int newX = reference.x() + offsetX;
            int newY = reference.y() + offsetY;

            newPosition = new Position(reference.level(), newX, newY);
        }

        return newPosition;
    }
}