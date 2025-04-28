/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.bonus.*;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.decor.ground.*;
import fr.ubx.poo.ubgarden.game.go.personage.*;

import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubgarden.game.view.ImageResource.*;

public final class SpriteFactory {

    public static Sprite create(Pane layer, GameObject gameObject) {
        ImageResourceFactory factory = ImageResourceFactory.getInstance();
        if (gameObject instanceof Grass)
            return new Sprite(layer, factory.get(GRASS), gameObject);
        if (gameObject instanceof Tree)
            return new Sprite(layer, factory.get(TREE), gameObject);
        if (gameObject instanceof Flowers)
            return new Sprite(layer, factory.get(FLOWERS), gameObject);
        if (gameObject instanceof Land)
            return new Sprite(layer, factory.get(LAND), gameObject);
        if (gameObject instanceof EnergyBoost)
            return new Sprite(layer, factory.get(APPLE), gameObject);
        if (gameObject instanceof Insecticide)
            return new Sprite(layer, factory.get(INSECTICIDE), gameObject);
        if (gameObject instanceof PoisonedApple)
            return new Sprite(layer, factory.get(POISONED_APPLE), gameObject);
        if (gameObject instanceof Carrots)
            return new Sprite(layer, factory.get(CARROTS), gameObject);
        if (gameObject instanceof Hedgehog)
            return new Sprite(layer, factory.get(HEDGEHOG), gameObject);
        if (gameObject instanceof WaspNest)
            return new Sprite(layer, factory.get(NESTWASP), gameObject);
        if (gameObject instanceof HornetNest)
            return new Sprite(layer, factory.get(NESTHORNET), gameObject);
        if (gameObject instanceof Wasp)
            return new Sprite(layer, factory.get(WASP_LEFT), gameObject);
        if (gameObject instanceof Hornet)
            return new Sprite(layer, factory.get(HORNET_LEFT), gameObject);
        if (gameObject instanceof Door){
            if (((Door) gameObject).getIsOpen()){
                return new Sprite(layer, factory.get(DOOR_OPENED), gameObject);
            }
            return new Sprite(layer, factory.get(DOOR_CLOSED), gameObject);
        }

        throw new RuntimeException("Unsupported sprite for decor " + gameObject);
    }
}
