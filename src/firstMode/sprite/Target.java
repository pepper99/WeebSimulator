package firstMode.sprite;

import firstMode.base.Type;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import main.Commons;

public class Target extends Sprite implements Commons, Type {
	
	public static final int TYPE_ALIEN = 0;
	public static final int TYPE_ANIME_GIRL = 1;
	public static final int TYPE_INF_GAUNTLET = 2;
	
	private static final int FRAMES = 2;
	
	private int type;

    public Target(int x, int y, int type) {
		super();
        initTarget(x, y, type);
    }

    private void initTarget(int x, int y, int type) {
    	setX(x);
        setY(y);
        setType(type);
        updateImage();
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		updateImage();
	}
    
    @Override
	protected void initImageArrays() {
    	imageArrays = new WritableImage[MAX_TYPE][FRAMES];
    	for(int i = 0; i < MAX_TYPE; i++) {
    		String playerImg = ClassLoader.getSystemResource("images/target_" + i + ".png").toString();
    		Image ii = new Image(playerImg);
    		for(int j = 0; j < FRAMES; j++) {
    			imageArrays[i][j] = new WritableImage(ii.getPixelReader(), TARGET_WIDTH * j, 0, TARGET_WIDTH, PLAYER_HEIGHT);
    		}
    	}
    }
	
	@Override
	public void updateImage() {
        setImage(imageArrays[getType()][index]);
	}

	@Override
	public int getTrueX() {
		return getX() + TARGET_WIDTH/2;
	}

	@Override
	public int getTrueY() {
		return getY() + TARGET_HEIGHT/2;
	}
}
