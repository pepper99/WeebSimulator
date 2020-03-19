package firstMode;

import main.Commons;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application implements Commons {

	private ArrayList<Target> targets;
	private Player player;
	
	private String message = "Game Over";
	
	private GraphicsContext graphicsContext;
	private MediaPlayer mediaPlayer;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		GameController.initController();
		musicInit();
		
		StackPane root = new StackPane();
		
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		graphicsContext = canvas.getGraphicsContext2D();
		
		graphicsContext.setFill(Color.rgb(21,24,31));
		graphicsContext.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		root.getChildren().add(canvas);

		player = new Player();
		targetInit(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
		
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
				doDrawing();
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
		}

		// player
		player.act();

		// player collide check
		if (player.isVisible()) {

			int playerX = player.getX();
			int playerY = player.getY();

			for (Target target : targets) {

				int targetX = target.getX();
				int targetY = target.getY();

				if (target.isVisible() && player.isVisible()) {
					if (playerX + 80 >= (targetX) && playerX + PLAYER_WIDTH - 80 <= (targetX + TARGET_WIDTH)
							&& playerY + 80 >= (targetY) && playerY + PLAYER_HEIGHT - 80 <= (targetY + TARGET_HEIGHT)) {
						if(player.getType() == target.getType()) {
							GameController.increaseTime();
							GameController.increaseScore();
						}
						
						Random random = new Random();
						targetInit(playerX, playerY);
						player.setType(random.nextInt(3));
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	private void targetInit(int playerX, int playerY) {
		targets = Randomizer.targetsRandomizer(playerX, playerY);
	}

	private void drawTargets(GraphicsContext g) {

		for (Target target : targets) {

			if (target.isVisible()) {
				g.drawImage(target.getImage(), target.getX(), target.getY());
			}
		}
	}

	private void drawPlayer(GraphicsContext g) {

		if (player.isVisible()) {
			g.drawImage(player.getImage(), player.getX(), player.getY());
		}
	}

	private void doDrawing() {
		
		graphicsContext.setFill(Color.rgb(21,24,31));
		graphicsContext.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

		if (GameController.isInGame()) {
			graphicsContext.setFill(Color.GREEN);
			graphicsContext.fillRect((WINDOW_WIDTH - GameController.getCurrentTime()) / 2, TIMER_Y, GameController.getCurrentTime(), TIMER_HEIGHT);
			drawTargets(graphicsContext);
			drawPlayer(graphicsContext);
			drawScore(graphicsContext);
		}
		else {
			mediaPlayer.stop();
			gameOver(graphicsContext);
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
		mediaPlayer.play();
	}
}
