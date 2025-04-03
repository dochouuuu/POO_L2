/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Map;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.PickupVisitor;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.bonus.Insecticide;
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;

import java.util.ArrayList;

public class Gardener extends GameObject implements Movable, PickupVisitor, WalkVisitor {

    private int energy;
    private Direction direction;
    private boolean moveRequested = false;
    private int diseaseLevel = 1;
    private int insecticideCount = 0;

    public Gardener(Game game, Position position) {

        super(game, position);
        this.direction = Direction.DOWN;
        this.energy = game.configuration().gardenerEnergy();
    }

    public int getDiseaseLevel(){
        return diseaseLevel;
    }

    public int getInsecticideCount() {
        return insecticideCount;
    }

    @Override
    public void pickUp(EnergyBoost energyBoost) {
        if(energy + 50 > 100){
            energy = 100;
        } else {
            energy += 50;
        }
        System.out.println("I am taking the boost, I should do something ...");
    }

    public void pickUp(PoisonedApple poisoned) {
        diseaseLevel++;
    }

    public void pickUp(Insecticide insecticide) {
        insecticideCount++;
    }

    public int getEnergy() {
        return this.energy;
    }


    public void requestMove(Direction direction) {
        if (direction != this.direction && direction != null) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    @Override
    public final boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Map map = game.world().getGrid();
        if(!map.inside(nextPos)){
            return false;
        }
        Decor decor = map.get(nextPos);
        return decor == null || decor.walkableBy(this);
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if (nextPos.x() < 0 || nextPos.x() >= game.world().getGrid().width() ||
                nextPos.y() < 0 || nextPos.y() >= game.world().getGrid().height()) {
            System.out.println("Cannot move: Out of grid bounds!");
            return getPosition();
        }

        Decor next = game.world().getGrid().get(nextPos);
        setPosition(nextPos);

        if (next != null) {
            next.pickUpBy(this);
            energy -= next.energyConsumptionWalk() * getDiseaseLevel();
        }

        return nextPos;
    }



    private long lastMoveTime = 0;
    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                move(direction);
                lastMoveTime = now;
            }
        }
        moveRequested = false;
    }

    public void hurt(int damage) {
    }

    public void hurt() {
        hurt(1);
    }

    public Direction getDirection() {
        return direction;
    }

}