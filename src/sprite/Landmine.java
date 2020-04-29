package sprite;

import java.util.HashMap;

import base.Commons;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import sprite.base.Sprite;

public class Landmine extends Sprite implements Commons {
	
    public Landmine(int x, int y) {
		super();
        initTarget(x, y);
    }

    private void initTarget(int x, int y) {
    	setX(x);
        setY(y);
        updateImage();
    }
    
    @Override
	protected void initImageMaps() {
    	imageMaps = new HashMap<Integer, WritableImage[]>();
    	Image ii = new Image(ClassLoader.getSystemResource("images/landmine.png").toString());
    	int frames = (int) (ii.getWidth() / LANDMINE_WIDTH);
    	imageMaps.put(0, new WritableImage[frames]);
    	for(int i = 0; i < frames; i++) {
    		imageMaps.get(0)[i] = new WritableImage(ii.getPixelReader(), LANDMINE_WIDTH * i, 0, LANDMINE_WIDTH, LANDMINE_HEIGHT);
    	}
    }
	
    @Override
	public void updateSpriteIndex() {
		spriteIndex = (spriteIndex + 1) % imageMaps.get(0).length;
		updateImage();
	}

	@Override
	public void updateImage() {
        setImage(imageMaps.get(0)[spriteIndex]);
	}
	
	@Override
	public int getTrueX() {
		return getX() + LANDMINE_WIDTH/2;
	}

	@Override
	public int getTrueY() {
		return getY() + LANDMINE_HEIGHT/2;
	}

	@Override
	public int getSpriteInterval() {
		return 5;
	}
}
