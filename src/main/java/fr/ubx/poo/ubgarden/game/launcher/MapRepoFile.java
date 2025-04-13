package fr.ubx.poo.ubgarden.game.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MapRepoFile implements MapRepo {

    public MapLevel loadFromString(String mapString) {
        String[] lines = mapString.trim().split("x");
        int height = lines.length;
        int width = lines[0].length();

        MapLevel level = new MapLevel(width, height);
        for (int y = 0; y < height; y++) {
            String row = lines[y];
            if (row.length() != width) {
                throw new MapException("Line " + y + " has incorrect width");
            }

            for (int x = 0; x < width; x++) {
                MapEntity entity = MapEntity.fromCode(row.charAt(x));
                level.set(x, y, entity);
            }
        }
        return level;
    }

    public MapLevel load(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(fis);
            String map = props.getProperty("map");
            if (map == null) throw new MapException("Missing 'map' property in file");
            return loadFromString(map);
        } catch (IOException e) {
            throw new MapException("Cannot load map from file: " + file.getName());
        }
    }

    @Override
    public MapLevel load(String name) {
        return load(new File(name));
    }
}