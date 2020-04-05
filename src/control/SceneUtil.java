package control;

import base.Commons;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class SceneUtil implements Commons {
	
	public static final int MENU = 0;
	public static final int GAME = 1;
	public static final int GAMEOVER = 2;
	public static final int HELP = 3;
	public static final int NONE = -1;
	
	private static int previousSceneType;
	private static Stage stage;
	private static Scene gameScene;
	private static Scene menuScene;
	private static Scene helpScene;
	private static Scene gameOverScene;
	private static AnimationTimer gameAnim;
	private static AnimationTimer menuAnim;
	private static MediaPlayer gameOverPlayer;
	private static MediaPlayer introPlayer;
	private static MediaPlayer teamIntroPlayer;
  
	public static void init(Stage stage, Scene gameScene, AnimationTimer gameAnim) {
		SceneUtil.stage = stage;
		SceneUtil.gameScene = gameScene;
		SceneUtil.gameAnim = gameAnim;
		menuScene = setMenuScene();
		helpScene = setHelpScene();
		gameOverScene = setGameOverScene();
		previousSceneType = NONE;
	}

	private static Scene setMenuScene() {
		Pane root = new Pane();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		GraphicsContext g = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		
		menuAnim = new AnimationTimer() {
			double x = 242;
			double y = 0;
			
		    public void handle(long currentNanoTime)
		    {
				GraphicsUtil.drawMenu(g, MENU_BG_WIDTH - (int) x - 1, MENU_BG_HEIGHT - (int) y - 1);
				x = (x + 0.5) % MENU_BG_WIDTH;
				y = (y + 0.5) % MENU_BG_HEIGHT;
		    }
		};
		
		Node[][] buttons = new Node[3][2];
		buttons[0] = getMenuButton(61, 400, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				introPlayer.stop();
				menuAnim.stop();
				AudioUtil.playSFX(AudioUtil.SFX_CLICK);
				setScene(stage, GAME);
			}
		});
		buttons[1] = getMenuButton(61, 475, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				menuAnim.stop();
				AudioUtil.playSFX(AudioUtil.SFX_CLICK);
				setScene(stage, HELP);
			}
		});
		buttons[2] = getMenuButton(61, 550, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				AudioUtil.playSFX(AudioUtil.SFX_CLICK);
		        Platform.exit();
		        System.exit(0);
			}
		});
		
		teamIntroPlayer = new MediaPlayer(new Media(ClassLoader.getSystemResource("videos/teamintro.mp4").toString()));
		introPlayer = new MediaPlayer(new Media(ClassLoader.getSystemResource("videos/intro.mp4").toString()));
		MediaView mediaView = new MediaView(teamIntroPlayer);
		teamIntroPlayer.setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	mediaView.setMediaPlayer(introPlayer);
	        	introPlayer.play();
	        	root.getChildren().addAll(buttons[0]);
	        	root.getChildren().addAll(buttons[1]);
	        	root.getChildren().addAll(buttons[2]);
	        }
		});
		introPlayer.setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	mediaView.setVisible(false);
	        	menuAnim.start();
	        }
		});
		introPlayer.setOnStopped(new Runnable() {
	        @Override
	        public void run() {
	        	mediaView.setVisible(false);
	        }
		});
		introPlayer.setOnPlaying(new Runnable() {
	        @Override
	        public void run() {
	        	mediaView.setVisible(true);
	        }
		});
		
		root.getChildren().add(mediaView);
		return scene;
	}

	public static Scene setHelpScene() {
		return null;
	}
	
	private static Scene setGameOverScene() {
		Pane root = new Pane();
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		GraphicsContext g = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		
		Node[][] button = new Node[2][3];
		button[0] = getGameOverButton(530, 515, 90, 40, "Retry", new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				setScene(stage, GAME);
			}
		});
		button[1] = getGameOverButton(750, 515, 90, 40, "Menu", new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				setScene(stage, MENU);
			}
		});
		root.getChildren().addAll(button[0]);
		root.getChildren().addAll(button[1]);
		
		gameOverPlayer = new MediaPlayer(new Media(ClassLoader.getSystemResource("videos/gameover.mp4").toString()));
		MediaView mediaView = new MediaView(gameOverPlayer);
		
		gameOverPlayer.setOnPlaying(new Runnable() {
	        @Override
	        public void run() {
	        	mediaView.setVisible(true);
	        }
	    });		
		gameOverPlayer.setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	mediaView.setVisible(false);
	        	GraphicsUtil.drawGameOver(g);
	        }
	    });
		
		root.getChildren().add(mediaView);
		
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				mediaView.setVisible(false);
				gameOverPlayer.stop();
		        GraphicsUtil.drawGameOver(g);
			}
		});
		
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mediaView.setVisible(false);
				gameOverPlayer.stop();
				GraphicsUtil.drawGameOver(g);
			}
		});
		
		return scene;
	}


	private static Node[] getGameOverButton(int x, int y, int width , int height, String message, 
			EventHandler<MouseEvent> eventHandler)
	{
		Ellipse ellipse = new Ellipse(x, y, 80, 25);
		GaussianBlur guassianBlur = new GaussianBlur(35);
		ellipse.setFill(Color.rgb(245, 135, 35));
		ellipse.setVisible(false);
		ellipse.setEffect(guassianBlur);
		
		Rectangle rect = getButton(x - 35, y - 20, width, height, eventHandler, new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				AudioUtil.playSFX(AudioUtil.SFX_SELECT);
				ellipse.setVisible(newValue);
			}
		});
		
		Text text = new Text(x - 45, y + 15, message);
		text.setFill(Color.WHITE);
		text.setFont(Font.loadFont(ClassLoader.getSystemResource("fonts/OptimusPrinceps.ttf").toString(), 35.16));
    
		Node[] nodes = {ellipse, text, rect} ;
		return nodes;
	}
  
	private static Node[] getMenuButton(int x, int y, int width, int height, EventHandler<MouseEvent> eventHandler) {
		Circle circle = new Circle(x - 14, y + (height / 2), 10, Color.WHITE);
		circle.setStroke(MENU_COLOR);
		circle.setStrokeWidth(5);
		circle.setVisible(false);

		Rectangle rect = getButton(x, y, width, height, eventHandler, new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                circle.setVisible(newValue);
                if(newValue) AudioUtil.playSFX(AudioUtil.SFX_HOVER);
            }
        });
		
		Node[] nodes = {rect, circle};
		return nodes;
	}
	
	private static Rectangle getButton(int x, int y, int width, int height, EventHandler<MouseEvent> eventHandler, 
			ChangeListener<Boolean> changeListener) {
		Rectangle rect = new Rectangle(x, y, width, height);
		rect.setOpacity(0);
		rect.setOnMouseClicked(eventHandler);
		rect.hoverProperty().addListener(changeListener);
		return rect;
	}
	
	public static void setScene(Stage stage, int type) {
		switch(type) {
		case MENU:
			if(previousSceneType == NONE) {
				teamIntroPlayer.play();
			}
			else {
				System.out.println("test");
				introPlayer.seek(Duration.ZERO);
				introPlayer.play();
			}
			stage.setScene(menuScene);
			AudioUtil.playMusic(AudioUtil.MUSIC_MENU);
			break;
		case GAME:
			if(previousSceneType == MENU) menuAnim.stop();
			stage.setScene(gameScene);
			gameAnim.start();
			AudioUtil.playMusic(AudioUtil.MUSIC_GAME);
			break;
		case GAMEOVER:
			gameAnim.stop();
			AudioUtil.stopAudio();
			gameOverPlayer.seek(Duration.ZERO);
			gameOverPlayer.play();
			stage.setScene(gameOverScene);
			break;
		case HELP:
			stage.setScene(helpScene);
			break;
		}
		previousSceneType = type;
	}
}