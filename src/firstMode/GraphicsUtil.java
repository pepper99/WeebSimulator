package firstMode;

import java.util.ArrayList;

import firstMode.sprite.Landmine;
import firstMode.sprite.Player;
import firstMode.sprite.Target;
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
import main.Commons;

public class GraphicsUtil implements Commons {
	
	private static WritableImage bg;
	
	public static void init() {
		Image ii = new Image(ClassLoader.getSystemResource("images/bg.png").toString());
		bg = new WritableImage(ii.getPixelReader(), WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public static void doDrawing(GraphicsContext g, ArrayList<Target> targets, Player player, Landmine landmine) {
		
		if(GameController.boost()) {
			g.setEffect(new InnerShadow(100, Color.WHITE));
        }
		GraphicsUtil.drawBG(g);

		if (GameController.isInGame()) {
			g.setEffect(new DropShadow(20, 0, 30, Color.BLACK));
			if(GameController.isLandminePhase()) GraphicsUtil.drawLandmine(g, landmine);
			GraphicsUtil.drawPlayer(g, player);
			GraphicsUtil.drawTargets(g, targets);
			g.setEffect(null);
			GraphicsUtil.drawTimeBar(g);
			GraphicsUtil.drawBoostBar(g);
			GraphicsUtil.drawScore(g);
		}
		else {
			gameOver(g);
		}
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

	public static void drawLandmine(GraphicsContext g, Landmine landmine) {
		if (landmine.isVisible()) {
			SpriteController.switchSprite(landmine);
			g.drawImage(landmine.getImage(), landmine.getX(), landmine.getY());
		}
	}
	
	public static void drawScore(GraphicsContext g) {
		String scoreMessage = Integer.toString(GameController.getScore());
		Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );
	    g.setFont( theFont );
	    g.setLineWidth(1);
		g.setFill(Color.WHITE);
		g.setTextAlign(TextAlignment.CENTER);
		g.fillText(scoreMessage, WINDOW_WIDTH / 2, 100 );
	}
	
	public static void drawTimeBar(GraphicsContext g) {
		g.setFill(Color.GREEN);
		g.fillRect((WINDOW_WIDTH - GameController.getCurrentTime()) / 2, TIMER_Y, GameController.getCurrentTime(), TIMER_HEIGHT);
	}
	
	public static void drawBoostBar(GraphicsContext g) {
		g.setFill(Color.DEEPSKYBLUE);
		g.fillRect(BOOSTBAR_X, BOOSTBAR_Y, BOOSTBAR_WIDTH, GameController.getPlayerBoostGauge());
	}

	private static void gameOver(GraphicsContext g) {
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 64 );
	    g.setFont( theFont );
	    g.setStroke( Color.WHITE );
	    g.setLineWidth(1);		
		g.setFill( Color.RED );
		g.setTextAlign(TextAlignment.CENTER);
        g.fillText( "Game Over", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 );
        g.strokeText( "Game Over", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 );
	}
}
