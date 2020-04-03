package control;

import java.io.File;

import base.Commons;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
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

public class SceneUtil implements Commons {
	
	private static Stage stage;
	private static Scene gameScene;
	private static Scene menuScene;
	private static Scene helpScene;
	private static Scene gameOverScene;
	private static AnimationTimer animationTimer;
	
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
		
		root.getChildren().addAll(getMenuButton(61, 400, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stage.setScene(gameScene);
				stage.show();
				animationTimer.start();
				AudioUtil.playMusic(AudioUtil.BGM_GAME);
			}
		}));
		
		root.getChildren().addAll(getMenuButton(61, 475, 140, 75, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
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
		
		GraphicsUtil.drawMenu(g);
		
		return scene;
	}

	public static Scene setHelpScene() {
		return null;
	}

	public static Scene gameOver()
	{
		String path = "res/musics/3.mp4";  

        Media media = new Media(new File(path).toURI().toString());  

        MediaPlayer mediaPlayer = new MediaPlayer(media);   
        MediaView mediaView = new MediaView(mediaPlayer);  
        mediaPlayer.setAutoPlay(true);   
        Button btn1 = new Button("go to result");
        btn1.setOnAction(e -> stage.setScene(getGameOverScene()));
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        Group root = new Group();  
        GraphicsContext g = canvas.getGraphicsContext2D();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	root.getChildren().addAll(getDarkSoulsButton(WINDOW_WIDTH/2 - 110, 515, 90, 40,"Retry", new EventHandler<MouseEvent>() {
	    			@Override
	    			public void handle(MouseEvent t) {
	    				
	    				stage.setScene(gameScene);
	    				stage.show();
	    				animationTimer.start();
	    				AudioUtil.playMusic(AudioUtil.BGM_GAME);
	    			}
	    		}));
	    		
	    		root.getChildren().addAll(getDarkSoulsButton(WINDOW_WIDTH/2 + 110, 515, 90, 40,"Menu", new EventHandler<MouseEvent>() {
	    			@Override
	    			public void handle(MouseEvent t) {
	    				stage.setScene(menuScene);
	    				stage.show();
	    				AudioUtil.playMusic(AudioUtil.BGM_MENU);
	    			}
	    		}));
	    		
	    		GraphicsUtil.drawGameOver(g);
	        }
	        
	    });
    	
        root.getChildren().add(mediaView);  
        root.getChildren().add(canvas);
        
       
        Scene scene = new Scene(root,1280,720);  
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event)
			{
					mediaPlayer.stop();
					stage.setScene(gameOverScene);
					stage.show();
			}
        });
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				mediaPlayer.stop();
				stage.setScene(gameOverScene);
				stage.show();
				
			}
			
        });
        return scene;
        

	}
	public static Scene setGameOverScene() {
		Pane root = new Pane();
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		GraphicsContext g = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		
		root.getChildren().addAll(getDarkSoulsButton(WINDOW_WIDTH/2 - 110, 515, 90, 40,"Retry", new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				
				stage.setScene(gameScene);
				stage.show();
				animationTimer.start();
				AudioUtil.playMusic(AudioUtil.BGM_GAME);
			}
		}));
		
		root.getChildren().addAll(getDarkSoulsButton(WINDOW_WIDTH/2 + 110,515, 90, 40,"Menu", new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				stage.setScene(menuScene);
				stage.show();
				AudioUtil.playMusic(AudioUtil.BGM_MENU);
			}
		}));
		
		GraphicsUtil.drawGameOver(g);
		
		return scene;
	}
	private static Node[] getDarkSoulsButton(int x, int y, int width , int height,String text, EventHandler<MouseEvent> eventHandler)
	{
		Ellipse ellipse = new Ellipse(x,y,160/2,50/2);
		GaussianBlur guassianBlur = new GaussianBlur(35);
		ellipse.setFill(Color.rgb(245, 135, 35));
		ellipse.setVisible(false);
		ellipse.setEffect(guassianBlur);
		
		Rectangle rct = getButton(x-35,y -20 ,width,height,eventHandler,new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,Boolean newValue)
			{
				AudioUtil.play();// change tp AudioUtil.playsfx(5);
				ellipse.setVisible(newValue);
			}
		});
		Node[] nodes = new Node[3];
		Text txt = new Text();
		
		txt.setText(text);
		txt.setX(x-45);
		txt.setY(y+15);
		txt.setFill(Color.WHITE);
		txt.setFont(Font.loadFont("file:res/fonts/OptimusPrinceps.ttf", 35.16));
		

		nodes[0] = ellipse;
		nodes[2] = rct;
		nodes[1] = txt;
		
		
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
