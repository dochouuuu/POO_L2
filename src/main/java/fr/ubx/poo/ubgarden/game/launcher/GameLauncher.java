package fr.ubx.poo.ubgarden.game.launcher;

import fr.ubx.poo.ubgarden.game.*;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.Hedgehog;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import fr.ubx.poo.ubgarden.game.Map;


public class GameLauncher {

    private GameLauncher() {
    }

    public static GameLauncher getInstance() {
        return LoadSingleton.INSTANCE;
    }

    private int integerProperty(Properties properties, String name, int defaultValue) {
        return Integer.parseInt(properties.getProperty(name, Integer.toString(defaultValue)));
    }

    private boolean booleanProperty(Properties properties, String name, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(name, Boolean.toString(defaultValue)));
    }

    private Configuration getConfiguration(Properties properties) {

        // Load parameters
        int waspMoveFrequency = integerProperty(properties, "waspMoveFrequency", 2);
        int hornetMoveFrequency = integerProperty(properties, "hornetMoveFrequency", 1);

        int gardenerEnergy = integerProperty(properties, "gardenerEnergy", 100);
        int energyBoost = integerProperty(properties, "energyBoost", 50);
        long energyRecoverDuration = integerProperty(properties, "energyRecoverDuration", 1_000);
        long diseaseDuration = integerProperty(properties, "diseaseDuration", 5_000);

        return new Configuration(gardenerEnergy, energyBoost, energyRecoverDuration, diseaseDuration, waspMoveFrequency, hornetMoveFrequency);
    }

    public Game load(File file) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + file, e);
        }

        Configuration configuration = getConfiguration(props);

        // Lire les niveaux depuis le fichier
        int levels = Integer.parseInt(props.getProperty("levels", "1"));
        boolean compressed = Boolean.parseBoolean(props.getProperty("compression", "false"));
        java.util.Map<Integer, MapLevel> allLevels = new java.util.HashMap<>();

        for (int i = 1; i <= levels; i++) {
            String key = "level" + i;
            String mapData = props.getProperty(key);
            if (mapData == null)
                throw new RuntimeException("Missing data for level " + i);
            if (compressed)
                mapData = decompress(mapData);
            allLevels.put(i, loadFromString(mapData));
        }

        // Trouver la position du jardinier dans le niveau 1
        Position gardenerPosition = allLevels.get(1).getGardenerPosition();
        if (gardenerPosition == null)
            throw new RuntimeException("Gardener not found");

        // Créer le monde
        World world = new World(levels);
        Game game = new Game(world, configuration, gardenerPosition);

        // Créer chaque niveau
        for (int i = 1; i <= levels; i++) {
            MapLevel mapLevel = allLevels.get(i);
            Map level = new Level(game, i, mapLevel);
            world.put(i, level);
        }

        game.setTotalCarrots(game.calculeTotalCarrots());
        game.setHedgehogPosition();

        return game;
    }

    public Game load() {
        Properties emptyConfig = new Properties();
        MapLevel mapLevel = new MapLevelDefaultStart();
        Position gardenerPosition = mapLevel.getGardenerPosition();
        if (gardenerPosition == null)
            throw new RuntimeException("Gardener not found");
        Configuration configuration = getConfiguration(emptyConfig);
        World world = new World(1);
        Game game = new Game(world, configuration, gardenerPosition);
        Map level = new Level(game, 1, mapLevel);
        world.put(1, level);
        game.setTotalCarrots(game.calculeTotalCarrots());
        game.setHedgehogPosition();
        return game;
    }

    private static class LoadSingleton {
        static final GameLauncher INSTANCE = new GameLauncher();
    }

    private MapLevel loadFromString(String mapString) {
        String[] lines = mapString.trim().split("x");
        int height = lines.length;
        int width = lines[0].length();

        MapLevel level = new MapLevel(width, height);

        for (int y = 0; y < height; y++) {
            String row = lines[y];
            if (row.length() != width) {
                throw new RuntimeException("Line " + (y + 1) + " has incorrect width");
            }

            for (int x = 0; x < width; x++) {
                char code = row.charAt(x);
                MapEntity entity = MapEntity.fromCode(code);
                level.set(x, y, entity);
            }
        }

        return level;
    }

    private String decompress(String data) {
        StringBuilder result = new StringBuilder();
        char prev = 0;
        int cpt = 0;

        for (char c : data.toCharArray()) {
            if (Character.isDigit(c)) {
                cpt = cpt * 10 + (c - '0');
            } else {
                if (cpt > 0) {
                    for (int i = 1; i < cpt; i++) {
                        result.append(prev);
                    }
                    cpt = 0;
                }
                result.append(c);
                prev = c;
            }
        }

        if (cpt > 0) {
            for (int i = 1; i < cpt; i++) {
                result.append(prev);
            }
        }

        return result.toString();
    }
}