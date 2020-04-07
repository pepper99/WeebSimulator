package sprite;

import java.util.HashMap;

import base.Commons;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import sprite.base.Sprite;
import sprite.base.Type;

public class Target extends Sprite implements Commons, Type {
	
	public static final int TYPE_ALIEN = 0;
	public static final int TYPE_ANIME_GIRL = 1;
	public static final int TYPE_INF_GAUNTLET = 2;
	
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
	protected void initImageMaps() {
    	imageMaps = new HashMap<Integer, WritableImage[]>();
    	for(int i = 0; i < MAX_TYPE; i++) {
    		Image ii = new Image(ClassLoader.getSystemResource("images/target/" + i + ".png").toString());
    		int frames = (int) (ii.getWidth() / TARGET_WIDTH);
    		imageMaps.put(i, new WritableImage[frames]);
    		for(int j = 0; j < frames; j++) {
    			imageMaps.get(i)[j] = new WritableImage(ii.getPixelReader(), TARGET_WIDTH * j, 0, TARGET_WIDTH, TARGET_HEIGHT);
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

	@Override
	public int getTrueX() {
		return getX() + TARGET_WIDTH/2;
	}

	@Override
	public int getTrueY() {
		return getY() + TARGET_HEIGHT/2;
	}

	@Override
	public int getSpriteInterval() {
		return 30;
	}
}
