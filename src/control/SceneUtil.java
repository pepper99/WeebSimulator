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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SceneUtil implements Commons {
	
	private static Stage stage;
	private static Scene gameScene;
	private static Scene menuScene;
	private static Scene helpScene;
	private static Scene gameOverScene;
	private static AnimationTimer animationTimer;
	private static AnimationTimer menuAnim;
	
	public static void init(Stage stage, Scene gameScene, AnimationTimer animationTimer) {
		SceneUtil.stage = stage;
		SceneUtil.gameScene = gameScene;
		SceneUtil.animationTimer = animationTimer;
		
		menuScene = setMenuScene();
		helpScene = setHelpScene();
		gameOverScene = setGameOverScene();
	}

	private static Scene setMenuScene() {
		Pane root = new Pane();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		GraphicsContext g = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		
		menuAnim = new AnimationTimer(){
			double x = 242;
			double y = 0;
			
		    public void handle(long currentNanoTime)
		    {
				GraphicsUtil.drawMenu(g, MENU_BG_WIDTH - (int) x - 1, MENU_BG_HEIGHT - (int) y - 1);
				x = (x + 0.5) % MENU_BG_WIDTH;
				y = (y + 0.5) % MENU_BG_HEIGHT;
		    }
		};
		
		root.getChildren().addAll(getMenuButton(61, 400, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				menuAnim.stop();
				stage.setScene(gameScene);
				stage.show();
				animationTimer.start();
				AudioUtil.playMusic(AudioUtil.BGM_GAME);
			}
		}));
		
		root.getChildren().addAll(getMenuButton(61, 475, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				menuAnim.stop();
				stage.setScene(helpScene);
				stage.show();
			}
		}));
		
		root.getChildren().addAll(getMenuButton(61, 550, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
		        Platform.exit();
		        System.exit(0);
			}
		}));
		
		MediaPlayer mediaPlayer = new MediaPlayer(new Media(ClassLoader.getSystemResource("videos/intro.mp4").toString()));
		mediaPlayer.setAutoPlay(true);
		MediaView mediaView = new MediaView(mediaPlayer);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	mediaView.setVisible(false);
	    		menuAnim.start();
	        }
	    });
		
		root.getChildren().add(mediaView);
		return scene;
	}

	public static Scene setHelpScene() {
		return null;
	}
	
	public static Scene setGameOverScene() {
		Pane root = new Pane();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		GraphicsContext g = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		
		root.getChildren().add(getButton(61, 400, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stage.setScene(gameScene);
				stage.show();
				animationTimer.start();
				AudioUtil.playMusic(AudioUtil.BGM_GAME);
			}
		}));
		
		root.getChildren().add(getButton(61, 475, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stage.setScene(menuScene);
				stage.show();
				menuAnim.start();
				AudioUtil.playMusic(AudioUtil.BGM_MENU);
			}
		}));
		
		GraphicsUtil.drawGameOver(g);
		return scene;
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
            }
        });
		
		Node[] nodes = new Node[2];
		nodes[0] = rect;
		nodes[1] = circle;
		return nodes;
	}
	
	private static Rectangle getButton(int x, int y, int width, int height, EventHandler<MouseEvent> eventHandler) {
		Rectangle rect = new Rectangle(x, y, width, height);
		rect.setOpacity(1);
		rect.setFill(Color.RED);
		rect.setOnMouseClicked(eventHandler);
		return rect;
	}
	
	private static Rectangle getButton(int x, int y, int width, int height, EventHandler<MouseEvent> eventHandler, 
			ChangeListener<Boolean> changeListener) {
		Rectangle rect = new Rectangle(x, y, width, height);
		rect.setOpacity(0);
		rect.setOnMouseClicked(eventHandler);
		rect.hoverProperty().addListener(changeListener);
		return rect;
	}
	
	public static Scene getMenuScene() {
		return menuScene;
	}
	
	public static Scene getHelpScene() {
		return helpScene;
	}
	
	public static Scene getGameOverScene() {
		return gameOverScene;
	}
}
