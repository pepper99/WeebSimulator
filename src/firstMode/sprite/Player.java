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
    private boolean[] keyPressed;
    WritableImage[] imageArrays;

	public Player() {
		super();
        initPlayer();
        keyPressed = initKeyArray();
    	setType(0);
    }

	public Player(int type) {
		super();
        initPlayer();
        keyPressed = initKeyArray();
    	setType(type);
    }

    private void initPlayer() {
    
        int START_X = WINDOW_WIDTH / 2;
        setX(START_X);

        int START_Y = WINDOW_HEIGHT / 2;
        setY(START_Y);

        initImageArrays();
        setPlayerSpeed(GameController.getPlayerSpeed());
    }
    
    private void initImageArrays() {
    	imageArrays = new WritableImage[3];
    	for(int i = 0; i < 3; i++) {
    		String playerImg = ClassLoader.getSystemResource("images/player_" + i + ".png").toString();
    		Image ii = new Image(playerImg);
    		imageArrays[i] = new WritableImage(ii.getPixelReader(), PLAYER_WIDTH, PLAYER_HEIGHT);
    	}
    }
    
    private boolean[] initKeyArray() {
    	boolean[] newArray = {false, false, false, false};
    	return newArray;
    }
    
	@Override
	public int getType() {
		return type;
	}

	@Override
	public void setType(int type) {
		this.type = type;
		updateImage();
	}

	@Override
	public void updateImage() {
        setImage(imageArrays[getType()]);
	}

    public void act() {
    	
    	playerSpeed = GameController.getPlayerSpeed();
    	dx = 0; dy = 0;
    	if(keyPressed[0]) {
    		dx -= playerSpeed;
    	}
    	if(keyPressed[1]) {
    		dx += playerSpeed;
    	}
    	if(keyPressed[2]) {
    		dy -= playerSpeed;
    	}
    	if(keyPressed[3]) {
    		dy += playerSpeed;
    	}

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
        KeyCode key = e.getCode();
        
        if (key.equals(KeyCode.SHIFT)) {
        	GameController.setBoostTrying(true);
        }

        if (key.equals(KeyCode.LEFT)) {
        	keyPressed[0] = true;
        }

        if (key.equals(KeyCode.RIGHT)) {
        	keyPressed[1] = true;
        }

        if (key.equals(KeyCode.UP)) {
        	keyPressed[2] = true;
        }

        if (key.equals(KeyCode.DOWN)) {
        	keyPressed[3] = true;
        }
    }
    
	@Override
    public void keyReleased(KeyEvent e) {
        KeyCode key = e.getCode();
        
        if (key.equals(KeyCode.SHIFT)) {
        	GameController.setBoostTrying(false);
        }
    	
    	playerSpeed = GameController.getPlayerSpeed();

        if (key.equals(KeyCode.LEFT)) {
        	keyPressed[0] = false;
        }

        if (key.equals(KeyCode.RIGHT)) {
        	keyPressed[1] = false;
        }

        if (key.equals(KeyCode.UP)) {
        	keyPressed[2] = false;
        }

        if (key.equals(KeyCode.DOWN)) {
        	keyPressed[3] = false;
        }
    }

	public int getPlayerSpeed() {
		return playerSpeed;
	}

	public void setPlayerSpeed(int playerSpeed) {
		this.playerSpeed = playerSpeed;
	}

	@Override
	public int getTrueX() {
		return getX() + PLAYER_WIDTH/2;
	}

	@Override
	public int getTrueY() {
		return getY() + PLAYER_HEIGHT/2;
	}
}
