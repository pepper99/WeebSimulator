package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sprite.Landmine;
import sprite.Player;
import sprite.Target;

import java.util.ArrayList;

import base.Commons;
import control.AudioUtil;
import control.FloatingTextController;
import control.GameController;
import control.GraphicsUtil;
import control.Randomizer;
import control.SceneUtil;

public class Main extends Application implements Commons {
	
	private ArrayList<Target> targets;
	private ArrayList<Landmine> landmines;
	private Player player;
	private FloatingTextController floatingTextController;
	
	private GraphicsContext graphicsContext;
	private static AnimationTimer gameAnim;
	private Scene scene;
	
	@Override
	public void start(Stage stage) throws Exception {
		StackPane root = new StackPane();
		scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);		
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		graphicsContext = canvas.getGraphicsContext2D();
		floatingTextController = new FloatingTextController();

		root.getChildren().add(canvas);
		
		gameAnim = new AnimationTimer(){			
		    public void handle(long currentNanoTime)
		    {
		    	if(GameController.getGameState() == GameController.STATE_INGAME) {
					update();
					GraphicsUtil.doGameDrawing(graphicsContext, targets, player, landmines, floatingTextController);
		    	}
		    	else if(GameController.getGameState() == GameController.STATE_RESTART) {
		    		gameInit();
		    		GameController.setGameState(GameController.STATE_INGAME);
		    	}
		    	else {
		    		GameController.setGameState(GameController.STATE_GAMEOVER);
		    	}
		    }
		};
		
		GraphicsUtil.init();
		AudioUtil.init();
		SceneUtil.init(stage, scene, gameAnim);
		
		stage.setTitle("Weeb Simulator 2020");
		stage.getIcons().add(new Image(ClassLoader.getSystemResource("images/icon.png").toString()));
		SceneUtil.switchTo(SceneUtil.MENU);
		stage.setResizable(false);
		stage.show();
	}
	
	private void gameInit() {
		player = new Player();
		targetInit(player.getTrueX(), player.getTrueY());
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				player.keyPressed(e);
			}
	    });
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				player.keyReleased(e);
			};
		});
		
		GameController.init();
	}

	private void update() {

		// timer
		if (GameController.getGameTime() == 0) {
    		GameController.setGameState(GameController.STATE_GAMEOVER);
			SceneUtil.switchTo(SceneUtil.GAMEOVER);
		}
		else {
			GameController.gameTick();
			
			if(!floatingTextController.isEmpty()) {
				floatingTextController.updateTimer();
			}
		}
    	
    	// player slowed check
    	if(GameController.isSlowed()) {
        	GameController.setPlayerState(GameController.PLAYER_SLOW);
    	}
    	else {
        	GameController.setPlayerState(GameController.PLAYER_NORMAL);
    	}

		// player
		player.act();

		// player collide check
		if (player.isVisible()) {
			for (Target target : targets) {
				if(GameController.playerCollideCheck(player, target)) {
					if(player.getType() == target.getType()) {
						GameController.increaseTime();
						GameController.increaseScore();
						AudioUtil.playSFX(AudioUtil.SFX_WOW);
						floatingTextController.addText("WOW", player.getX(), player.getY());
					}
					else {
						AudioUtil.playSFX(AudioUtil.SFX_BRUH);
						floatingTextController.addText("BRUH", player.getX(), player.getY());
					}
					player.setType(Randomizer.getPlayerType());
					targetInit(player.getTrueX(), player.getTrueY());
				}
			}
			if(GameController.hasLandmine()) {
				for (Landmine landmine : landmines) {
					if(landmine.isVisible() && GameController.playerCollideCheck(player, landmine)) {
						GameController.triggerSlow();
						AudioUtil.playSFX(AudioUtil.SFX_KHALED);
						floatingTextController.addText("LMAO REKT", player.getX(), player.getY());
						landmine.setVisible(false);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	private void targetInit(int playerX, int playerY) {
		targets = new ArrayList<Target>();
		int[][] coordinates = Randomizer.coordinatesRandomizer(playerX, playerY, GameController.getLandmineCount());
		for(int i = 0; i < 3; i++) {
			targets.add(new Target(coordinates[i+1][0], coordinates[i+1][1], i));
		}
		if(GameController.hasLandmine()) {
			landmines = new ArrayList<Landmine>();
			for(int i = 0; i < GameController.getLandmineCount(); i++) {
				landmines.add(new Landmine(coordinates[i+4][0], coordinates[i+4][1]));
			}
		}
	}
}