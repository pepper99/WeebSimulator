package main;

import firstMode.AudioUtil;
import firstMode.GameController;
import firstMode.GraphicsUtil;
import firstMode.Randomizer;
import firstMode.SpriteController;
import firstMode.sprite.Landmine;
import firstMode.sprite.Player;
import firstMode.sprite.Target;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application implements Commons {

	private ArrayList<Target> targets;
	private Player player;
	private Landmine landmine;
	
	private GraphicsContext graphicsContext;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		GameController.initController();
		SpriteController.initContorller();
		GraphicsUtil.init();
		AudioUtil.init();
		
		StackPane root = new StackPane();		
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);		
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		graphicsContext = canvas.getGraphicsContext2D();
		
		root.getChildren().add(canvas);

		player = new Player();
		targetInit(player.getTrueX(), player.getTrueY());
		
		scene.setOnKeyPressed(
				new EventHandler<KeyEvent>()
	            {
	                public void handle(KeyEvent e)
	                {
	        			player.keyPressed(e);
	                }
	            });
		scene.setOnKeyReleased(
				new EventHandler<KeyEvent>() {
					public void handle(KeyEvent e) {
						player.keyReleased(e);
					};
				}
		);
		
		new AnimationTimer()
		{
		    public void handle(long currentNanoTime)
		    {
				update();
				GraphicsUtil.doDrawing(graphicsContext, targets, player, landmine);
		    }
		}.start();
		
		primaryStage.setTitle("Weeb Simulator 2020");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void update() {

		// timer
		if (GameController.getCurrentTime() == 0) {
			GameController.setInGame(false);
			AudioUtil.musicStop();
		}
		else {
			GameController.decreaseTime();
			GameController.checkBoostBan();
			SpriteController.increaseTime();
			
			if(GameController.isBoostTrying() && GameController.canBoost()) {
				GameController.decreasePlayerBoostGauge();
			}
			else if(!GameController.boostFull()) {
				GameController.increasePlayerBoostGauge();
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
					}
					else {
						AudioUtil.playSFX(AudioUtil.SFX_BRUH);
					}
					player.setType(Randomizer.getPlayerType());
					targetInit(player.getTrueX(), player.getTrueY());
				}
			}
			if(GameController.isLandminePhase()) {
				if(landmine.isVisible() && GameController.playerCollideCheck(player, landmine)) {
					GameController.triggerSlow();
					AudioUtil.playSFX(AudioUtil.SFX_KHALED);
					landmine.setVisible(false);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	private void targetInit(int playerX, int playerY) {
		targets = new ArrayList<>();
		int[][] coordinates = Randomizer.coordinatesRandomizer(playerX, playerY);
		for(int i = 0; i < 3; i++) {
			targets.add(new Target(coordinates[i+1][0], coordinates[i+1][1], i));
		}
		if(GameController.isLandminePhase()) {
			landmine = new Landmine(coordinates[4][0], coordinates[4][1]);
		}
	}
}
