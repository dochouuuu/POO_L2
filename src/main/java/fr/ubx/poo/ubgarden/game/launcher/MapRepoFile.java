package fr.ubx.poo.ubgarden.game.launcher;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MapRepoFile implements MapRepo {

    @Override
    public MapLevel load(String filename) {
        return load(new File(filename));
    }

    public MapLevel load(File file) {
        Map<Integer, MapLevel> allLevels = loadAllLevels(file);
        if (!allLevels.containsKey(1)) {
            throw new MapException("Missing level1 data");
        }
        return allLevels.get(1);
    }

    public MapLevel loadFromString(String mapString) {
        String[] lines = mapString.trim().split("x");
        int height = lines.length;
        int width = lines[0].length();

        MapLevel level = new MapLevel(width, height);

        for (int y = 0; y < height; y++) {
            String row = lines[y];
            if (row.length() != width) {
                throw new MapException("Line " + (y + 1) + " has incorrect width");
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

    public Map<Integer, MapLevel> loadAllLevels(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(fis);

            int levels = Integer.parseInt(props.getProperty("levels", "1"));
            boolean compressed = Boolean.parseBoolean(props.getProperty("compression", "false"));

            Map<Integer, MapLevel> allLevels = new HashMap<>();

            for (int i = 1; i <= levels; i++) {
                String key = "level" + i;
                String mapData = props.getProperty(key);

                if (mapData == null) {
                    throw new MapException("Missing data for " + key);
                }

                if (compressed) {
                    mapData = decompress(mapData);
                }

                MapLevel level = loadFromString(mapData);
                allLevels.put(i, level);
            }

            return allLevels;
        } catch (IOException e) {
            throw new MapException("Cannot load map from file: " + file.getName());
        }
    }
}