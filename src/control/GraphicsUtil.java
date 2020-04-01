package control;

import java.util.ArrayList;

import base.Commons;
import control.FloatingTextController.FloatingText;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import sprite.Landmine;
import sprite.Player;
import sprite.Target;

public class GraphicsUtil implements Commons {
	
	private static WritableImage bg;
	private static WritableImage menu;
	
	public static void init() {
		bg = new WritableImage(new Image(ClassLoader.getSystemResource("images/bg.png").toString()).getPixelReader(),
				WINDOW_WIDTH, WINDOW_HEIGHT);
		menu = new WritableImage(new Image(ClassLoader.getSystemResource("images/menu.png").toString()).getPixelReader(),
				WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public static void doDrawing(GraphicsContext g, ArrayList<Target> targets, Player player,  ArrayList<Landmine> landmines,
			FloatingTextController floatingTextController) {
		if(GameController.boost()) {
			g.setEffect(new InnerShadow(100, Color.WHITE));
	    }
		GraphicsUtil.drawBG(g);
		g.setEffect(new DropShadow(20, 0, 14, Color.rgb(18, 18, 18)));
		if(GameController.hasLandmine()) GraphicsUtil.drawLandmine(g, landmines);
		GraphicsUtil.drawPlayer(g, player);
		GraphicsUtil.drawTargets(g, targets);
		g.setEffect(null);
		if(!floatingTextController.isEmpty()) drawFloatingText(g, floatingTextController);
		GraphicsUtil.drawTimeBar(g);
		GraphicsUtil.drawBoostBar(g);
		GraphicsUtil.drawScore(g);
	}
	
	public static void drawBG(GraphicsContext g) {
		g.drawImage(bg, 0, 0);
	}

	public static void drawTargets(GraphicsContext g, ArrayList<Target> targets) {
		for (Target target : targets) {
			if (target.isVisible()) {
				SpriteController.switchSprite(target);
				g.drawImage(target.getImage(), target.getX(), target.getY());
			}
		}
	}

	public static void drawPlayer(GraphicsContext g, Player player) {
		if (player.isVisible()) {
			DropShadow e = (DropShadow) g.getEffect(null);
			SpriteController.switchSprite(player);
			if(GameController.isSlowed() && SpriteController.flash()) {
	        	g.setGlobalAlpha(0.5);
	        }
			else if(GameController.boost()) {
				double boostGauge = (double) GameController.getPlayerBoostGauge();
				double maxBoost = (double) GameController.MAX_BOOST_GAUGE;
				double val = boostGauge/maxBoost + 0.5;
				DropShadow c1 = (DropShadow) g.getEffect(null);
				Bloom c2 = new Bloom(1 - val);
				c2.setInput(new Glow(val));
				c1.setInput(c2);
				g.setEffect(c1);
	        }
			g.drawImage(player.getImage(), player.getX(), player.getY());
			g.setEffect(e);
	        g.setGlobalAlpha(1);
		}
	}

	public static void drawLandmine(GraphicsContext g, ArrayList<Landmine> landmines) {
		for (Landmine landmine : landmines) {
			if (landmine.isVisible()) {
				SpriteController.switchSprite(landmine);
				g.drawImage(landmine.getImage(), landmine.getX(), landmine.getY());
			}
		}
	}
	
	public static void drawScore(GraphicsContext g) {
		String scoreMessage = Integer.toString(GameController.getScore());
	    g.setFont(Font.font( "Helvetica", FontWeight.BOLD, 24));
	    g.setLineWidth(1);
		g.setFill(Color.WHITE);
		g.setTextAlign(TextAlignment.CENTER);
		g.fillText(scoreMessage, WINDOW_WIDTH / 2, 100 );
	}
	
	public static void drawTimeBar(GraphicsContext g) {
		double time = GameController.getCurrentTime();
		double timeFraction = 1 - time / GameController.MAX_TIME;
		int dRed = (int) (timeFraction * 206);
		int dGreen = (int) (timeFraction * 201);
		g.setFill(Color.rgb(24 + dRed, 219 - dGreen, 18));
		g.fillRect((WINDOW_WIDTH - time) / 2, TIMER_Y, time, TIMER_HEIGHT);
	}
	
	public static void drawBoostBar(GraphicsContext g) {
		if(GameController.isBoostBan()) {
			if(SpriteController.flash()) {
				g.setFill(Color.WHITE);
			}
			else {
				g.setFill(Color.RED);
			}
        }
		else {
			g.setFill(Color.DEEPSKYBLUE);
		}
		g.fillRect((WINDOW_WIDTH - GameController.getPlayerBoostGauge()) / 2, BOOSTBAR_Y, GameController.getPlayerBoostGauge(), BOOSTBAR_HEIGHT);
	}
	
	public static void drawFloatingText(GraphicsContext g, FloatingTextController floatingTextController) {
		for(FloatingText f : floatingTextController.getFloatingTexts()) {
			if (f.isVisible()) {
			    g.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
			    g.setFill(f.getColor());
	        	g.setGlobalAlpha(f.getTimer() / FloatingText.START_TIME);
		        g.fillText(f.getMessage(), f.getX(), f.getY());
	        	g.setGlobalAlpha(1);
			}
		}
	}

	public static void drawGameOver(GraphicsContext g) {
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
//		Font font = Font.font( "font/MosterFriendBack.otf", FontWeight.BOLD, 64 );
	    g.setFont(Font.loadFont("MonsterFreindBack.otf", 600));
//	    g.setStroke(Color.WHITE);
	    g.setLineWidth(1);		
		g.setFill(Color.WHITE);
		g.setTextAlign(TextAlignment.CENTER);
        g.fillText("Game Over", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 );
        g.strokeText("Game Over", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 );
//        GraphicsUtil.drawScore(g);
	}
	
	public static void drawMenu(GraphicsContext g) {
		g.drawImage(menu, 0, 0);
	}
}
