/*
* Copyright (c) 2020. Laurent Réveillère
*/

package fr.ubx.poo.ubgarden.game.engine;

import fr.ubx.poo.ubgarden.game.*;
import fr.ubx.poo.ubgarden.game.go.bonus.Insecticide;
import fr.ubx.poo.ubgarden.game.go.decor.*;
import fr.ubx.poo.ubgarden.game.go.decor.ground.*;
import fr.ubx.poo.ubgarden.game.go.personage.*;
import fr.ubx.poo.ubgarden.game.view.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.util.*;


 public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final Game game;
    private final Gardener gardener;
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final List<Wasp> wasps = new LinkedList<>();
    private final List<Hornet> hornets = new LinkedList<>();

    private final Scene scene;

    private StatusBar statusBar;

    private final Pane rootPane = new Pane();
    private final Group root = new Group();
    private final Pane layer = new Pane();
    private Input input;

    public GameEngine(Game game, Scene scene) {
        this.game = game;
        this.scene = scene;
        this.gardener = game.getGardener();
        initialize();
        buildAndSetGameLoop();
    }

    public Pane getRoot() {
        return rootPane;
    }

    private void initialize() {
        sprites.clear();
        layer.getChildren().clear();

        int height = game.world().getGrid().height();
        int width = game.world().getGrid().width();
        int sceneWidth = width * ImageResource.size;
        int sceneHeight = height * ImageResource.size;
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        input = new Input(scene);

        root.getChildren().clear();
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight);

        rootPane.getChildren().clear();
        rootPane.setPrefSize(sceneWidth, sceneHeight + StatusBar.height);
        rootPane.getChildren().add(root);

        // Create sprites
        int currentLevel = game.world().currentLevel();

        for (var decor : game.world().getGrid().values()) {
            sprites.add(SpriteFactory.create(layer, decor));
            decor.setModified(true);
            var bonus = decor.getBonus();
            if (bonus != null) {
                sprites.add(SpriteFactory.create(layer, bonus));
                bonus.setModified(true);
            }
        }

        sprites.add(new SpriteGardener(layer, gardener));
        resizeScene(sceneWidth, sceneHeight);
    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                checkLevel();

                // Check keyboard actions
                processInput();

                // Do actions
                update(now);
                checkCollision();

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

     private void checkLevel() {
         if (game.isSwitchLevelRequested()) {
             int newLevel = game.getSwitchLevel();

             // Vérifie que le niveau demandé existe
             if (newLevel >= 1 && newLevel <= game.world().maxLevel()) {
                 int previousLevel = game.world().currentLevel();

                 // Change le niveau courant
                 game.world().setCurrentLevel(newLevel);

                 Position targetPosition = null;
                 boolean movingUp = newLevel > previousLevel;

                 // Cherche la porte qui correspond au bon sens
                 for (Decor decor : game.world().getGrid().values()) {
                     if (decor instanceof Door door && door.getIsOpen()) {
                         // Si on monte, on cherche une porte qui ne mène pas au niveau suivant (côté entrée)
                         if (movingUp && !door.isToNextLevel()) {
                             targetPosition = door.getPosition();
                             break;
                         }
                         // Si on descend, on cherche une porte qui mène au niveau suivant (côté sortie)
                         else if (!movingUp && door.isToNextLevel()) {
                             targetPosition = door.getPosition();
                             break;
                         }
                     }
                 }

                 // Si on n'a pas trouvé de porte dans le bon sens, on prend n'importe quelle porte ouverte
                 if (targetPosition == null) {
                     for (Decor decor : game.world().getGrid().values()) {
                         if (decor instanceof Door door && door.getIsOpen()) {
                             targetPosition = door.getPosition();
                             break;
                         }
                     }
                 }

                 // Place le jardinier à la position choisie
                 gardener.setPosition(targetPosition);
                 gardener.setDirection(Direction.DOWN);
                 gardener.setJustArrived(true);

                 // Met à jour le nombre de carottes à collecter
                 game.setTotalCarrots(game.calculTotalCarrots());

                 // Réinitialise le compteur de carottes
                 game.getGardener().resetCarrots();

                 // Oublie la demande de changement de niveau
                 game.clearSwitchLevel();

                 // Ré-initialise toute la scène graphique
                 initialize();
             }
         }
     }

     private void checkCollision() {
         // Check si collision entre le jardiner et un insecte
         Iterator<Wasp> waspIterator = wasps.iterator();
         while (waspIterator.hasNext()) {
             Wasp wasp = waspIterator.next();
             if (gardener.getPosition().equals(wasp.getPosition())) {
                 if (gardener.getInsecticideCount() >= 1) {
                     gardener.useInsecticide(1);
                     wasp.hurt(1);
                 } else {
                     gardener.hurt(20);
                     wasp.hurt(1);
                 }

                 if (wasp.getLifePoints() == 0) {
                     wasp.remove();
                     waspIterator.remove();
                     cleanupSprites();
                 }
             }
         }

         Iterator<Hornet> hornetIterator = hornets.iterator();
         while (hornetIterator.hasNext()) {
             Hornet hornet = hornetIterator.next();
             if (gardener.getPosition().equals(hornet.getPosition())) {
                 if (gardener.getInsecticideCount() >= 2) {
                     gardener.useInsecticide(2);
                     hornet.hurt(2);
                 } else {
                     gardener.hurt(30);
                     hornet.hurt(1);
                 }

                 if (hornet.getLifePoints() <= 0) {
                     hornet.remove();
                     hornetIterator.remove();
                     cleanupSprites();
                 }
             }
         }
     }


     private void processInput() {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            gardener.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            gardener.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            gardener.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            gardener.requestMove(Direction.UP);
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text message = new Text(msg);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setFont(new Font(60));
        message.setFill(color);

        StackPane pane = new StackPane(message);
        pane.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
        rootPane.getChildren().clear();
        rootPane.getChildren().add(pane);

        new AnimationTimer() {
            public void handle(long now) {
                processInput();
            }
        }.start();
    }

     private void spawnInsecticide(int quantity) {
         // Fait apparaître des instectides à des positions aléatoires
         for (int i = 0; i < quantity; i++) {
             Position insecticidePosition = Position.randomPos(game, new Position(game.world().currentLevel(), 0, 0), game.world().getGrid().width()); // Générer une position aléatoire dans toute la carte
             Decor decor = game.world().getGrid().get(insecticidePosition);

             while (!(decor instanceof Ground) || decor.getBonus() != null){
                 insecticidePosition = Position.randomPos(game, new Position(game.world().currentLevel(), 0, 0), game.world().getGrid().width());
                 decor = game.world().getGrid().get(insecticidePosition);
             }

             Insecticide insecticide = new Insecticide(insecticidePosition, decor);
             decor.setBonus(insecticide);
             sprites.add(SpriteFactory.create(layer, insecticide));
         }
     }

     private void update(long now) {
         // Mise à jour des décors et des nids d'insectes
         game.world().getGrid().values().forEach(decor -> {
             decor.update(now);
             checkCollision();
             if (decor instanceof WaspNest) {
                 WaspNest waspNest = (WaspNest) decor;
                 Wasp wasp = waspNest.spawnWasp(now, game);
                 if (wasp != null) {
                     wasps.add(wasp);
                     sprites.add(new SpriteWasp(layer, wasp));
                     spawnInsecticide(1);
                 }
             }
             if (decor instanceof HornetNest) {
                 HornetNest hornetNest = (HornetNest) decor;
                 Hornet hornet = hornetNest.spawnHornet(now, game);
                 if (hornet != null) {
                     hornets.add(hornet);
                     sprites.add(new SpriteHornet(layer, hornet));
                     spawnInsecticide(2);
                 }
             }
         });

         // Mise à jour des insectes
         Iterator<Wasp> waspIterator = wasps.iterator();
         while (waspIterator.hasNext()) {
             Wasp wasp = waspIterator.next();
             if (wasp.getLifePoints() <= 0) {
                 wasp.remove();
                 waspIterator.remove();
                 cleanupSprites();
             } else {
                 wasp.update(now);
             }
         }

         Iterator<Hornet> hornetIterator = hornets.iterator();
         while (hornetIterator.hasNext()) {
             Hornet hornet = hornetIterator.next();
             if (hornet.getLifePoints() <= 0) {
                 hornet.remove();
                 hornetIterator.remove();
                 cleanupSprites();
             } else {
                 hornet.update(now);
             }
         }

         // Mise à jour du jardinier
         gardener.update(now);

         // Vérification des conditions de fin de jeu
         if (gardener.getEnergy() < 0) {
             gameLoop.stop();
             showMessage("Perdu!", Color.RED);
         } else if (gardener.getPosition().equals(game.getHedgehogPosition())) {
             gameLoop.stop();
             showMessage("Gagné !", Color.GREEN);
         }
     }


     public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
        cleanupInsects();
    }

    public void cleanupInsects() {
        // Supprime tous les insectes marqués comme supprimées
        wasps.removeIf(Wasp::isDeleted);
        hornets.removeIf(Hornet::isDeleted);
    }

    private void render() {
        sprites.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }

    private void resizeScene(int width, int height) {
        rootPane.setPrefSize(width, height + StatusBar.height);
        layer.setPrefSize(width, height);
        Platform.runLater(() -> scene.getWindow().sizeToScene());
    }
}