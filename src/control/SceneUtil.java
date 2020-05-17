package control;

import base.Commons;
import exception.SceneUtilException;
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
import javafx.scene.layout.StackPane;
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
	
	private static int helpStage;
	
	private static int previousSceneType;
	private static Stage stage;
	private static Scene gameScene;
	private static Scene menuScene;
	private static Scene helpScene;
	private static Scene gameOverScene;
	
	private static AnimationTimer gameAnim;
	private static AnimationTimer menuAnim;
	private static AnimationTimer helpAnim;
	
	private static MediaPlayer gameOverPlayer;
	private static MediaPlayer introPlayer;
	private static MediaPlayer teamIntroPlayer;
	private static MediaPlayer countdownPlayer;
  
	public static void init(Stage stage, Scene gameScene, AnimationTimer gameAnim) {
		SceneUtil.stage = stage;
		SceneUtil.gameScene = gameScene;
		SceneUtil.gameAnim = gameAnim;
		menuScene = setMenuScene();
		gameOverScene = setGameOverScene();
		helpScene = setHelpScene();
		previousSceneType = NONE;
		setGameCountdown();
	}
	
	private static void setGameCountdown() {
		StackPane root = (StackPane) gameScene.getRoot();
		countdownPlayer = new MediaPlayer(new Media(ClassLoader.getSystemResource("videos/countdown.mp4").toString()));
		MediaView mediaView = new MediaView(countdownPlayer);
		
		countdownPlayer.setOnPlaying(new Runnable() {
	        @Override
	        public void run() {
	        	mediaView.setVisible(true);
	        }
	    });
		countdownPlayer.setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	mediaView.setVisible(false);
	        	countdownPlayer.stop();
				gameAnim.start();
	    		GameController.setGameState(GameController.STATE_RESTART);
	        }
		});
		
		root.getChildren().add(mediaView);
	}

	private static Scene setMenuScene() {
		Pane root = new Pane();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		GraphicsContext g = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);

		menuAnim = new AnimationTimer() {
		    public void handle(long currentNanoTime)
		    {
				GraphicsUtil.drawMenu(g);
		    }
		};
		
		Node[][] buttons = new Node[3][2];
		buttons[0] = getMenuButton(61, 400, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				introPlayer.stop();
				menuAnim.stop();
				AudioUtil.playSFX(AudioUtil.SFX_CLICK);
				switchTo(GAME);
			}
		});
		buttons[1] = getMenuButton(61, 475, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				menuAnim.stop();
				AudioUtil.playSFX(AudioUtil.SFX_CLICK);
				switchTo(HELP);
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
	        	introPlayer.stop();
	        	mediaView.setVisible(false);
	        	GraphicsUtil.resetMenuCoordinates();
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
	
	private static Scene setHelpScene() {
		StackPane root = new StackPane();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		GraphicsContext g = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		
		int stageCount = GraphicsUtil.HELP_STAGECOUNT;
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if(helpStage < stageCount) {
					helpStage++;
				}
				else {
					helpAnim.stop();
					menuAnim.stop();
					switchTo(MENU);
				}
			}
		});
		
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(helpStage < stageCount) {
					helpStage++;
				}
				else {
					helpAnim.stop();
					menuAnim.stop();
					switchTo(MENU);
				}
			}
		});
		
		helpAnim = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				GraphicsUtil.drawHelp(g, helpStage);
			}
		};
		
		return scene;
	}
	
	private static Scene setGameOverScene() {
		Pane root = new Pane();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		GraphicsContext g = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		
		Node[][] button = new Node[2][3];
		button[0] = getGameOverButton(530, 515, 90, 40, "Retry", new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				switchTo(GAME);
			}
		});
		button[1] = getGameOverButton(750, 515, 90, 40, "Menu", new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				switchTo(MENU);
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
		gameOverPlayer.setOnStopped(new Runnable() {
	        @Override
	        public void run() {
	        	gameOverPlayer.seek(gameOverPlayer.getStopTime());
	        	mediaView.setVisible(false);
	        	GraphicsUtil.drawGameOver(g);
	        }
	    });
		
		root.getChildren().add(mediaView);
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				gameOverPlayer.stop();
			}
		});
		
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				gameOverPlayer.stop();
			}
		});
		
		return scene;
	}

	private static Node[] getGameOverButton(int x, int y, int width , int height, String message, 
			EventHandler<MouseEvent> eventHandler)
	{
		Ellipse ellipse = new Ellipse(x, y, 80, 14);
		ellipse.setFill(Color.rgb(245, 135, 35));
		ellipse.setVisible(false);
		ellipse.setEffect(new GaussianBlur(50));
		
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
	
	private static void setScene(int type) throws SceneUtilException {
		switch(type) {
		case MENU:
			if(previousSceneType == NONE) {
				teamIntroPlayer.play();
			}
			else {
				introPlayer.seek(Duration.ZERO);
				introPlayer.play();
			}
			stage.setScene(menuScene);
			AudioUtil.playMusic(AudioUtil.MUSIC_MENU);
			break;
			
		case GAME:
        	countdownPlayer.seek(Duration.ZERO);
			if(previousSceneType == MENU) {
				menuAnim.stop();
			}
			else if(previousSceneType == GAMEOVER) {
	    		GameController.setGameState(GameController.STATE_RESTART);
			}
			countdownPlayer.play();
			stage.setScene(gameScene);
			AudioUtil.playMusic(AudioUtil.MUSIC_GAME0);
			break;
			
		case GAMEOVER:
			gameAnim.stop();
			AudioUtil.stopAudio();
			gameOverPlayer.seek(Duration.ZERO);
			gameOverPlayer.play();
			stage.setScene(gameOverScene);
			break;
			
		case HELP:
			helpStage = 0;
			helpAnim.start();
			stage.setScene(helpScene);
			AudioUtil.playMusic(AudioUtil.MUSIC_HELP);
			break;
			
		default:
			throw new SceneUtilException("Invalid scene type");
		}
		previousSceneType = type;
	}
	
	public static void switchTo(int type) {
		try {
			setScene(type);
		} catch (SceneUtilException e) {
			e.printStackTrace();
			try {
				setScene(MENU);
			} catch (SceneUtilException e1) {
				e1.printStackTrace();
			}
		}
	}
}