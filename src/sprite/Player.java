package sprite;

import java.util.HashMap;

import base.Commons;
import control.GameController;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import sprite.base.Movable;
import sprite.base.Sprite;
import sprite.base.Type;

public class Player extends Sprite implements Commons, Type, Movable {
	
	public static final int TYPE_ALIEN = 0;
	public static final int TYPE_ANIME_GIRL = 1;
	public static final int TYPE_INF_GAUNTLET = 2;

    private int type;
    private int playerSpeed;
    private boolean[] keyPressed;

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
        setX(WINDOW_WIDTH / 2);
        setY(WINDOW_HEIGHT / 2);
        
        playerSpeed = GameController.getPlayerSpeed();
    }
    
    @Override
	protected void initImageMaps() {
    	imageMaps = new HashMap<Integer, WritableImage[]>();
    	for(int i = 0; i < MAX_TYPE; i++) {
    		Image ii = new Image(ClassLoader.getSystemResource("images/player/" + i + ".png").toString());
    		int frames = (int) (ii.getWidth() / PLAYER_WIDTH);
    		imageMaps.put(i, new WritableImage[frames]);
    		for(int j = 0; j < frames; j++) {
    			imageMaps.get(i)[j] = new WritableImage(ii.getPixelReader(), PLAYER_WIDTH * j, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
    		}
    	}
    }
	
    @Override
	public void updateSpriteIndex() {
		spriteIndex = (spriteIndex + 1) % imageMaps.get(getType()).length;
		updateImage();
	}

	@Override
	public void updateImage() {
        setImage(imageMaps.get(getType())[spriteIndex]);
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
        spriteIndex = 0;
		updateImage();
	}
    
    @Override
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

        if (x <= BORDER_LEFT) {
            x = BORDER_LEFT;
        }

        if (x >= WINDOW_WIDTH - PLAYER_WIDTH - BORDER_RIGHT) {
            x = WINDOW_WIDTH - PLAYER_WIDTH - BORDER_RIGHT;
        }
        
        if (y <= BORDER_TOP) {
        	y = BORDER_TOP;
        }
        
        if (y >= WINDOW_HEIGHT - PLAYER_HEIGHT - BORDER_BOTTOM) {
        	y = WINDOW_HEIGHT - PLAYER_HEIGHT - BORDER_BOTTOM;
        }
    }
    
	@Override
    public void keyPressed(KeyEvent e) {
        KeyCode key = e.getCode();
        
        if (key.equals(KeyCode.SHIFT)) {
        	GameController.setBoostTrying(true);
        }

        if (key.equals(KeyCode.LEFT) || key.equals(KeyCode.A)) {
        	keyPressed[0] = true;
        }

        if (key.equals(KeyCode.RIGHT) || key.equals(KeyCode.D)) {
        	keyPressed[1] = true;
        }

        if (key.equals(KeyCode.UP) || key.equals(KeyCode.W)) {
        	keyPressed[2] = true;
        }

        if (key.equals(KeyCode.DOWN) || key.equals(KeyCode.S)) {
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

        if (key.equals(KeyCode.LEFT) || key.equals(KeyCode.A)) {
        	keyPressed[0] = false;
        }

        if (key.equals(KeyCode.RIGHT) || key.equals(KeyCode.D)) {
        	keyPressed[1] = false;
        }

        if (key.equals(KeyCode.UP) || key.equals(KeyCode.W)) {
        	keyPressed[2] = false;
        }

        if (key.equals(KeyCode.DOWN) || key.equals(KeyCode.S)) {
        	keyPressed[3] = false;
        }
    }

	public int getPlayerSpeed() {
		return playerSpeed;
	}

	@Override
	public int getTrueX() {
		return getX() + PLAYER_WIDTH/2;
	}

	@Override
	public int getTrueY() {
		return getY() + PLAYER_HEIGHT/2;
	}

	@Override
	public int getSpriteInterval() {
		return 10;
	}
}
