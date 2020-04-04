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
	
	private static final int BG_COUNTS = 3;
	private static final int BG_CHANGE = 30;
	
	private static WritableImage[] bg;
	private static WritableImage menuBG;
	private static WritableImage menu;
	private static WritableImage statusBar;
	private static Font scoreFont;
	private static WritableImage gameOver;
	public static void init() {
		bg = new WritableImage[BG_COUNTS];
		for(int i = 0; i < BG_COUNTS; i++) {
			bg[i] = new WritableImage(new Image(ClassLoader.getSystemResource("images/bg" + i + ".png").toString()).getPixelReader(),
					WINDOW_WIDTH, WINDOW_HEIGHT);
		}
		
		menu = new WritableImage(new Image(ClassLoader.getSystemResource("images/menu.png").toString()).getPixelReader(),
				WINDOW_WIDTH, WINDOW_HEIGHT);
		menuBG = new WritableImage(new Image(ClassLoader.getSystemResource("images/menu_bg.png").toString()).getPixelReader(),
				MENU_BG_WIDTH, MENU_BG_HEIGHT);
		statusBar = new WritableImage(new Image(ClassLoader.getSystemResource("images/status_bar.png").toString()).getPixelReader(),
				STATUSBAR_WIDTH, STATUSBAR_HEIGHT);
		scoreFont = Font.loadFont(ClassLoader.getSystemResource("fonts/Fipps-Regular.otf").toString(), SCORE_FONTSIZE);
		gameOver = new WritableImage(new Image(ClassLoader.getSystemResource("images/Youdie2.png").toString()).getPixelReader(),
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
		g.drawImage(statusBar, STATUSBAR_X, STATUSBAR_Y);
		GraphicsUtil.drawScore(g);
	}
	
	public static void drawBG(GraphicsContext g) {
		g.drawImage(bg[(GameController.getScore() / BG_CHANGE) % BG_COUNTS], 0, 0);
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
		g.setFont(scoreFont);
	    g.setLineWidth(2);
	    g.setStroke(Color.WHITE);
	  	g.setFill(Color.BLACK);
		g.setTextAlign(TextAlignment.CENTER);
		g.fillText(scoreMessage, WINDOW_WIDTH / 2, SCORE_Y );
	}
	
	public static void drawTimeBar(GraphicsContext g) {
		double time = GameController.getCurrentTime();
		double timeFraction = 1 - time / GameController.MAX_TIME;
		time *= 0.625;
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
			g.setFill(BOOSTBAR_COLOR);
		}
		g.fillRect((WINDOW_WIDTH - GameController.getPlayerBoostGauge()) / 2, BOOSTBAR_Y,
				GameController.getPlayerBoostGauge(), BOOSTBAR_HEIGHT);
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
		
		g.drawImage(gameOver, 0, 0);
		
	}
	
	public static void drawMenu(GraphicsContext g, int x, int y) {	
		g.drawImage(menuBG, x - MENU_BG_WIDTH, y - MENU_BG_HEIGHT);
		g.drawImage(menuBG, x, y - MENU_BG_HEIGHT);
		g.drawImage(menuBG, x - MENU_BG_WIDTH, y);
		g.drawImage(menuBG, x, y);
		g.drawImage(menu, 0, 0);
	}
}
