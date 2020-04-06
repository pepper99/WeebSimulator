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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import sprite.Landmine;
import sprite.Player;
import sprite.Target;

public class GraphicsUtil implements Commons {
	
	private static final double MENU_OFFSETX = 242;

	public static final int HELP_STAGECOUNT = 15;
	private static final int HELPASSET_COUNT = 6;
	
	private static final int GIORNO_CENTER = 472;
	private static final int GIORNO_LEFT = 50;
	private static final int GIORNO_RIGHT = 894;
	private static final int GIORNO_LOW = 360;
	private static final int GIORNO_Y = 60;
    
    private static int STATUSBAR_X = 211;
    private static int STATUSBAR_Y = 10;

    private static int TIMER_HEIGHT = 10;
    private static int TIMER_Y = 40;
    
    private static int BOOSTBAR_Y = 59;
    private static int BOOSTBAR_HEIGHT = 4;
    private static Color BOOSTBAR_COLOR = Color.rgb(242, 215, 131);
    
    private static int SCORE_Y = 100;
    private static int SCORE_FONTSIZE = 24;

    private static int DIALOGUE_FONTSIZE = 24;
    private static int DIALOGUE_X = 265;
    private static int DIALOGUE_Y = 605;
    private static int DIALOGUEBOX_X = 232;
    private static int DIALOGUEBOX_Y = 527;

    private static int GAMEOVERSCORE_FONTSIZE = 56;
    private static int GAMEOVERSCORE_X = 640;
    private static int GAMEOVERSCORE_Y = 440;
    
    private static Color DIALOGUE_STROKE = Color.rgb(32, 6, 2);
	
	private static double menuX;
	private static double menuY;
	
	private static Image[] bg;
	private static Image[] helpAssets;
	private static Image menuBG;
	private static Image menu;
	private static Image statusBar;
	private static Image gameOver;
	
	private static Image helpBG;	
	private static Image giorno;
	private static Image dialogueBox;
	
	private static Font scoreFont;
	private static Font dialogueFont;
	private static Font gameOverFont;
  
	public static void init() {
		int bgCount = GameController.MAP_COUNT;
		bg = new Image[bgCount];
		for(int i = 0; i < bgCount; i++) {
			bg[i] = new Image(ClassLoader.getSystemResource("images/bg/" + i + ".png").toString());
		}
		
		helpAssets = new Image[HELPASSET_COUNT];
		for(int i = 0; i < HELPASSET_COUNT; i++) {
			helpAssets[i] = new Image(ClassLoader.getSystemResource("images/help/" + i + ".png").toString());
		}
		
		menu = new Image(ClassLoader.getSystemResource("images/menu.png").toString());
		menuBG = new Image(ClassLoader.getSystemResource("images/menu_bg.png").toString());
		statusBar = new Image(ClassLoader.getSystemResource("images/status_bar.png").toString());
		gameOver = new Image(ClassLoader.getSystemResource("images/gameover.png").toString());
		helpBG = new Image(ClassLoader.getSystemResource("images/help_bg.png").toString());
		
		giorno = new Image(ClassLoader.getSystemResource("images/giorno.png").toString());
		dialogueBox = new Image(ClassLoader.getSystemResource("images/dialoguebox.png").toString());
		
		scoreFont = Font.loadFont(ClassLoader.getSystemResource("fonts/Fipps-Regular.otf").toString(), SCORE_FONTSIZE);
		dialogueFont = Font.loadFont(ClassLoader.getSystemResource("fonts/Aller_Rg.ttf").toString(), DIALOGUE_FONTSIZE);
		gameOverFont = Font.loadFont(ClassLoader.getSystemResource("fonts/OptimusPrinceps.ttf").toString(), GAMEOVERSCORE_FONTSIZE);
	}

	public static void doGameDrawing(GraphicsContext g, ArrayList<Target> targets, Player player,  ArrayList<Landmine> landmines,
			FloatingTextController floatingTextController) {
		g.setEffect(null);
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

		g.drawImage(statusBar, STATUSBAR_X, STATUSBAR_Y);
		GraphicsUtil.drawTimeBar(g);
		GraphicsUtil.drawBoostBar(g);
		
		GraphicsUtil.drawScore(g);
		
		if(GraphicsTimer.screenFlash(GameController.isStarting())) {
			drawScreenFlash(g);
		}
	}
	
	private static void drawScreenFlash(GraphicsContext g) {
		g.setGlobalAlpha(GraphicsTimer.getScreenFlashOpacity());
		g.setFill(Color.WHITE);
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		g.setGlobalAlpha(1);
	}
	
	private static void drawBG(GraphicsContext g) {
		if(GraphicsTimer.screenFlash(GameController.isMapChange())) {
			drawScreenFlash(g);
		}
		g.drawImage(bg[GameController.getCurrentMap()], 0, 0);
	}

	private static void drawTargets(GraphicsContext g, ArrayList<Target> targets) {
		for (Target target : targets) {
			if (target.isVisible()) {
				GraphicsTimer.switchSprite(target);
				g.drawImage(target.getImage(), target.getX(), target.getY());
			}
		}
	}

	private static void drawPlayer(GraphicsContext g, Player player) {
		if (player.isVisible()) {
			DropShadow e = (DropShadow) g.getEffect(null);
			GraphicsTimer.switchSprite(player);
			if(GameController.isSlowed() && GraphicsTimer.boostFlash()) {
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

	private static void drawLandmine(GraphicsContext g, ArrayList<Landmine> landmines) {
		for (Landmine landmine : landmines) {
			if (landmine.isVisible()) {
				GraphicsTimer.switchSprite(landmine);
				g.drawImage(landmine.getImage(), landmine.getX(), landmine.getY());
			}
		}
	}
	
	private static void drawScore(GraphicsContext g) {
		String score = Integer.toString(GameController.getScore());
		g.setFont(scoreFont);
		g.setLineWidth(2);
		g.setStroke(Color.WHITE);
		g.setFill(Color.BLACK);
		g.setTextAlign(TextAlignment.CENTER);
		g.strokeText(score, WINDOW_WIDTH / 2, SCORE_Y);
		g.fillText(score, WINDOW_WIDTH / 2, SCORE_Y);
	}
	
	private static void drawTimeBar(GraphicsContext g) {
		double time = GameController.getGameTime();
		double timeFraction = 1 - time / GameController.MAX_TIME;
		time *= 0.625;
		int dRed = (int) (timeFraction * 206);
		int dGreen = (int) (timeFraction * 201);
		g.setFill(Color.rgb(24 + dRed, 219 - dGreen, 18));
		g.fillRect((WINDOW_WIDTH - time) / 2, TIMER_Y, time, TIMER_HEIGHT);
	}
	
	private static void drawBoostBar(GraphicsContext g) {
		if(GameController.isBoostBan()) {
			if(GraphicsTimer.boostFlash()) {
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
	
	private static void drawFloatingText(GraphicsContext g, FloatingTextController floatingTextController) {
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

	protected static void drawGameOver(GraphicsContext g) {
		String score = Integer.toString(GameController.getScore());
		g.drawImage(gameOver, 0, 0);
		
		g.setFont(gameOverFont);
		g.setFill(Color.WHITE);
		g.setTextAlign(TextAlignment.CENTER);
		g.fillText(score, GAMEOVERSCORE_X, GAMEOVERSCORE_Y);
	}
	
	protected static void drawMenu(GraphicsContext g) {
		int x = MENU_BG_WIDTH - (int) menuX - 1;
		int y = MENU_BG_HEIGHT - (int) menuY - 1;
		g.drawImage(menuBG, x - MENU_BG_WIDTH, y - MENU_BG_HEIGHT);
		g.drawImage(menuBG, x, y - MENU_BG_HEIGHT);
		g.drawImage(menuBG, x - MENU_BG_WIDTH, y);
		g.drawImage(menuBG, x, y);
		g.drawImage(menu, 0, 0);
		
		menuX = (menuX + 0.5) % MENU_BG_WIDTH;
		menuY = (menuY + 0.5) % MENU_BG_HEIGHT;
	}
	
	protected static void resetMenuCoordinates() {
		menuX = MENU_OFFSETX;
		menuY = 0;
	}
	
	protected static void drawHelp(GraphicsContext g, int stage) {
		g.drawImage(helpBG, 0, 0);
		
		String message;
		int giornoX, giornoY;
		switch(stage) {
		case 0:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "Hello.";
			break;
		case 1:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "I, Giorno Giovanna, have a dream!";
			break;
		case 2:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "But that’s probably not what you want to know right now.";
			break;
		case 3:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "You probably want to know how to play this game, right?";
			break;
		case 4:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "This game can only be played by a stand user.";
			break;
		case 5:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "But as I expected, you already have a stand!  This should be fun.";
			break;
		case 6:
			g.drawImage(helpAssets[0], 456, 165);
			giornoX = GIORNO_LEFT;
			giornoY = GIORNO_Y;
			message = "You can use WASD or arrow keys to move around and Shift to\n"
					+ "speed boost!";
			break;
		case 7:
			g.drawImage(helpAssets[1], 616, 220);
			giornoX = GIORNO_LEFT;
			giornoY = GIORNO_Y;
			message = "Beware that you cannot boost all the time.  If you completely used\n"
					+ "all of the stamina, you won't be able to speed boost again until\n"
					+ "your stamina is fully recovered!";
			break;
		case 8:
			g.drawImage(helpAssets[2], 0, 0);
			giornoX = 0;
			giornoY = GIORNO_LOW;
			message = "You get to play as 3 different characters; Frank the Ninjutsu runner,\n"
					+ "Weeb boi from Akiba, and The Mad Titan Thanos.\n"
					+ "You will have to run and get what these characters are looking for.";
			break;
		case 9:
			g.drawImage(helpAssets[3], 199, 34);
			giornoX = GIORNO_RIGHT;
			giornoY = GIORNO_Y;
			message = "Frank aims to save all the aliens captured in Area 51.\n"
					+ "Weeb boi is thirsty for those Anime girls(?).\n"
					+ "Thanos will do whatever it takes to hold the Infinity Gauntlet.";
			break;
		case 10:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "Getting all of them right as many as possible is the goal!\n"
					+ "But sadly, you don't get all the time in the world.";
			break;
		case 11:
			g.drawImage(helpAssets[4], 523, 200);
			giornoX = GIORNO_LEFT;
			giornoY = GIORNO_Y;
			message = "As you play, the time will gradually deplete.  If the time runs out?\n"
					+ "It's game over.  But don't worry too much, getting things right will\n"
					+ "get you some additional bonus time so you can keep going!";
			break;
		case 12:
			g.drawImage(helpAssets[5], 570, 180);
			giornoX = GIORNO_RIGHT;
			giornoY = GIORNO_Y;
			message = "As you progress, landmines will start to appear more and more.\n"
					+ "Stepping on one of these will slow you down temporary.\n"
					+ "You better avoid them, ok?";
			break;
		case 13:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "Try to get as many points as possible.  With my [Golden Experience],\n"
					+ "my high score is 420.  But I believe you can get at least a quarter\n"
					+ "of my score, as a fellow stand user.";
			break;
		case 14:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "I, Giorno Giovanna, will say goodbye, and back to eating baguette.";
			break;
		case 15:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "And remember kids, don't do drugs";
			break;
		default:
			giornoX = GIORNO_CENTER;
			giornoY = GIORNO_Y;
			message = "I, Giorno Giovanna, have a dream";
			break;
		}
		
		g.drawImage(giorno, giornoX, giornoY);
		
		g.drawImage(dialogueBox, DIALOGUEBOX_X, DIALOGUEBOX_Y);
		
		g.setFont(dialogueFont);
		g.setLineWidth(3);
		g.setStroke(DIALOGUE_STROKE);
		g.setFill(Color.WHITE);
		g.setTextAlign(TextAlignment.LEFT);

		g.strokeText("\"" + message + "\"", DIALOGUE_X, DIALOGUE_Y);
		g.fillText("\"" + message + "\"", DIALOGUE_X, DIALOGUE_Y);
	}
}
