/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Map;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.PickupVisitor;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.bonus.Insecticide;
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.Door;

import java.util.ArrayList;
import java.util.List;

public class Gardener extends GameObject implements Movable, PickupVisitor, WalkVisitor {

    private int energy;
    private Direction direction;
    private boolean moveRequested = false;
    private int diseaseLevel = 1;
    private int insecticideCount = 0;
    private List<Timer> poisonTimers = new ArrayList<>();
    private int carrots = 0;
    private boolean justArrived = false;

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
    public void pickUp(EnergyBoost apple) {
        energy = Math.min(energy + game.configuration().energyBoost(), game.configuration().gardenerEnergy());
        diseaseLevel = 1;
    }

    public void pickUp(PoisonedApple poisoned) {
        diseaseLevel++;
        Timer t = new Timer(game.configuration().diseaseDuration());
        t.start();
        poisonTimers.add(t);
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
        System.out.println("MOVE: justArrived=" + justArrived + ", pos=" + getPosition());
        if (isJustArrived()) {
            setJustArrived(false);
            System.out.println("MOVE => setJustArrived(FALSE)");
        }
        Position nextPos = direction.nextPosition(getPosition());

        Map map = game.world().getGrid();
        if (!map.inside(nextPos)){
            return getPosition();
        }

        Decor next = game.world().getGrid().get(nextPos);
        setPosition(nextPos);

        if (next != null) {
            int cost = next.energyConsumptionWalk() * getDiseaseLevel();
            hurt(cost);
            next.pickUpBy(this);
        }

        return nextPos;
    }

    private long lastMoveTime = 0;
    public void update(long now) {
        System.out.println("UPDATE: justArrived=" + justArrived + ", pos=" + getPosition());
        if (moveRequested) {
            if (canMove(direction)) {
                move(direction);
                lastMoveTime = now;
            }
        }
        Decor decor = game.world().getGrid().get(this.getPosition());
        if (!this.isJustArrived() && decor instanceof Door door && door.getIsOpen()) {
            int currentLevel = game.world().currentLevel();
            int targetLevel = door.isToNextLevel() ? currentLevel + 1 : currentLevel - 1;

            System.out.println("Passage au niveau " + targetLevel + " via une porte " + (door.isToNextLevel() ? "vers le niveau suivant" : "vers le niveau précédent"));

            game.requestSwitchLevel(targetLevel);
        }
        else {
            long inactive = (now - lastMoveTime) / 1_000_000;
            if (inactive >= game.configuration().energyRecoverDuration()) {
                if (energy < game.configuration().gardenerEnergy()) {
                    energy++;
                    lastMoveTime = now;
                }
            }
        }

        poisonTimers.forEach(t -> t.update(now)); // Met à jour les timers de maladie
        poisonTimers.removeIf(t -> !t.isRunning()); // Supprime ceux qui sont terminés
        diseaseLevel = 1 + poisonTimers.size();
        moveRequested = false;
    }

    public void hurt(int damage) {
        this.energy -= damage;
    }

    public void hurt() {
        hurt(1);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        setModified(true);
    }

    public void resetCarrots() {
        this.carrots = 0;
    }

    public void collectCarrot() {
        this.carrots++;

        if (this.carrots == game.getTotalCarrots()) {
            game.openDoors();
        }
        System.out.println("Carottes collectées : " + this.carrots + "/" + game.getTotalCarrots());
    }

    public boolean isJustArrived() {
        return justArrived;
    }
    public void setJustArrived(boolean justArrived) {
        this.justArrived = justArrived;
    }
}