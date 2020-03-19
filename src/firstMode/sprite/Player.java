package firstMode.sprite;

import firstMode.GameController;
import firstMode.base.Movable;
import firstMode.base.Type;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.Commons;

public class Player extends Sprite implements Commons, Type, Movable {
	
	public static final int TYPE_ALIEN = 0;
	public static final int TYPE_ANIME_GIRL = 1;
	public static final int TYPE_INF_GAUNTLET = 2;

    private int type;
    private int playerSpeed;

	public Player() {
        initPlayer();
    }

    private void initPlayer() {
    	setType(0);
    
        int START_X = WINDOW_WIDTH / 2;
        setX(START_X);

        int START_Y = WINDOW_HEIGHT / 2;
        setY(START_Y);
        
        setPlayerSpeed(GameController.getPlayerSpeed());
    }
    
	@Override
	public int getType() {
		return type;
	}

	@Override
	public void setType(int type) {
		this.type = type;
		String playerImg = null;
		Image ii = null;
		switch(type) {
		case 0:
	        playerImg = ClassLoader.getSystemResource("images/player_0.png").toString();
			break;
		case 1:
	        playerImg = ClassLoader.getSystemResource("images/player_1.png").toString();
			break;
		case 2:
	        playerImg = ClassLoader.getSystemResource("images/player_2.png").toString();
			break;
		}
        ii = new Image(playerImg);
        setImage(new WritableImage(ii.getPixelReader(), PLAYER_WIDTH, PLAYER_HEIGHT));
	}

    public void act() {

        x += dx;
        y += dy;

        if (x <= 0) {

            x = 0;
        }

        if (x >= WINDOW_WIDTH - PLAYER_WIDTH) {

            x = WINDOW_WIDTH - PLAYER_WIDTH;
        }
        
        if (y <= 0) {
        	y = 0;
        }
        
        if (y >= WINDOW_HEIGHT - PLAYER_HEIGHT) {
        	y = WINDOW_HEIGHT - PLAYER_HEIGHT;
        }
    }
    
	@Override
    public void keyPressed(KeyEvent e) {
    	
    	playerSpeed = GameController.getPlayerSpeed();
        KeyCode key = e.getCode();

        if (key.equals(KeyCode.LEFT)) {

            dx -= playerSpeed;
        }

        if (key.equals(KeyCode.RIGHT)) {

            dx += playerSpeed;
        }

        if (key.equals(KeyCode.UP)) {

            dy -= playerSpeed;
        }

        if (key.equals(KeyCode.DOWN)) {

            dy += playerSpeed;
        }
        
        speedCeiling();
    }
    
	@Override
    public void keyReleased(KeyEvent e) {
    	
    	playerSpeed = GameController.getPlayerSpeed();
        KeyCode key = e.getCode();

        if (key.equals(KeyCode.LEFT)) {

            dx += playerSpeed;
        }

        if (key.equals(KeyCode.RIGHT)) {

            dx -= playerSpeed;
        }

        if (key.equals(KeyCode.UP)) {

            dy += playerSpeed;
        }

        if (key.equals(KeyCode.DOWN)) {

            dy -= playerSpeed;
        }
        
        speedCeiling();
    }
    
    private void speedCeiling() {
    	playerSpeed = GameController.getPlayerSpeed();
    	
        if(dx < 0) {
        	dx = Math.max(dx, -playerSpeed);
        }
        else if(dx > 0) {
        	dx = Math.min(dx, playerSpeed);
        }
        
        if(dy < 0) {
        	dy = Math.max(dy, -playerSpeed);
        }
        else if(dy > 0) {
        	dy = Math.min(dy, playerSpeed);
        }
    }

	public int getPlayerSpeed() {
		return playerSpeed;
	}

	public void setPlayerSpeed(int playerSpeed) {
		this.playerSpeed = playerSpeed;
	}
}
