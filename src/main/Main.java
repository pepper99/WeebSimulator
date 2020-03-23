package main;

import firstMode.GameController;
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
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class Main extends Application implements Commons {

	private ArrayList<Target> targets;
	private Player player;
	private Landmine landmine;
	
	private String message = "Game Over";
	
	private GraphicsContext graphicsContext;
	private MediaPlayer mediaPlayer;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		GameController.initController();
		SpriteController.initContorller();
		musicInit();
		
		StackPane root = new StackPane();		
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);		
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		graphicsContext = canvas.getGraphicsContext2D();
		
		graphicsContext.setFill(Color.rgb(21,24,31));
		graphicsContext.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
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
				doDrawing(graphicsContext);
		    }
		}.start();
		
		primaryStage.setTitle("Weeb Simulator 2020");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		mediaPlayer.play();
	}

	private void update() {

		// timer
		if (GameController.getCurrentTime() == 0) {
			GameController.setInGame(false);
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
					}
					player.setType(Randomizer.getPlayerType());
					targetInit(player.getTrueX(), player.getTrueY());
				}
			}
			if(GameController.isLandminePhase()) {
				if(landmine.isVisible() && GameController.playerCollideCheck(player, landmine)) {
					GameController.triggerSlow();
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

	private void doDrawing(GraphicsContext g) {
		
		if(GameController.boost()) {
			g.setEffect(new InnerShadow(100, Color.WHITE));
        }
		g.setFill(Color.rgb(21,24,31));
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

		if (GameController.isInGame()) {
			g.setEffect(new DropShadow(20, 0, 30, Color.BLACK));
			if(GameController.isLandminePhase()) drawLandmine(graphicsContext);
			drawPlayer(graphicsContext);
			drawTargets(graphicsContext);
			g.setEffect(null);
			drawTimeBar(graphicsContext);
			drawBoostBar(graphicsContext);
			drawScore(graphicsContext);
		}
		else {
			mediaPlayer.stop();
			gameOver(graphicsContext);
		}
	}

	private void drawTargets(GraphicsContext g) {
		for (Target target : targets) {
			if (target.isVisible()) {
				SpriteController.switchSprite(target);
				g.drawImage(target.getImage(), target.getX(), target.getY());
			}
		}
	}

	private void drawPlayer(GraphicsContext g) {
		if (player.isVisible()) {
			DropShadow e = (DropShadow) g.getEffect(null);
			SpriteController.switchSprite(player);
			if(GameController.isSlowed() && SpriteController.flash()) {
	        	g.setGlobalAlpha(0.5);
	        }
			else if(GameController.boost()) {
				DropShadow c1 = (DropShadow) g.getEffect(null);
				Bloom c2 = new Bloom(0);
				c2.setInput(new Glow(1));
				c1.setInput(c2);
				g.setEffect(c1);
	        }
			g.drawImage(player.getImage(), player.getX(), player.getY());
			g.setEffect(e);
	        g.setGlobalAlpha(1);
		}
	}

	private void drawLandmine(GraphicsContext g) {
		if (landmine.isVisible()) {
			SpriteController.switchSprite(landmine);
			g.drawImage(landmine.getImage(), landmine.getX(), landmine.getY());
		}
	}
	
	private void drawScore(GraphicsContext g) {
		String scoreMessage = Integer.toString(GameController.getScore());
		Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );
	    g.setFont( theFont );
	    g.setLineWidth(1);
		g.setFill(Color.WHITE);
		g.setTextAlign(TextAlignment.CENTER);
		g.fillText(scoreMessage, WINDOW_WIDTH / 2, 100 );
	}
	
	private void drawTimeBar(GraphicsContext g) {
		graphicsContext.setFill(Color.GREEN);
		graphicsContext.fillRect((WINDOW_WIDTH - GameController.getCurrentTime()) / 2, TIMER_Y, GameController.getCurrentTime(), TIMER_HEIGHT);
	}
	
	private void drawBoostBar(GraphicsContext g) {
		graphicsContext.setFill(Color.DEEPSKYBLUE);
		graphicsContext.fillRect(BOOSTBAR_X, BOOSTBAR_Y, BOOSTBAR_WIDTH, GameController.getPlayerBoostGauge());
	}

	private void gameOver(GraphicsContext g) {
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 64 );
	    g.setFont( theFont );
	    g.setStroke( Color.WHITE );
	    g.setLineWidth(1);		
		g.setFill( Color.RED );
		g.setTextAlign(TextAlignment.CENTER);
        g.fillText( message, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 );
        g.strokeText( message, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 );
	}
	
	private void musicInit() {
		Media h = new Media(ClassLoader.getSystemResource("musics/bgm.mp3").toString());
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	mediaPlayer.seek(Duration.ZERO);
	        	mediaPlayer.play();
	        }
	    });
		mediaPlayer.play();
	}
}
