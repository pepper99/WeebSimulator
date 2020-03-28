package sprite;

import base.Commons;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import sprite.base.Sprite;

public class Landmine extends Sprite implements Commons {
	
	private static final int FRAMES = 2;

    public Landmine(int x, int y) {
		super();
        initTarget(x, y);
    }

    private void initTarget(int x, int y) {
    	setX(x);
        setY(y);
        updateImage();
    }
    
    public void trigger() {
    	
    }

	@Override
	protected void initImageArrays() {
    	imageArrays = new WritableImage[1][FRAMES];
    	String playerImg = ClassLoader.getSystemResource("images/landmine.png").toString();
    	Image ii = new Image(playerImg);
    	for(int i = 0; i < FRAMES; i++) {
    		imageArrays[0][i] = new WritableImage(ii.getPixelReader(), LANDMINE_WIDTH * i, 0, LANDMINE_WIDTH, LANDMINE_HEIGHT);
    	}
	}

	@Override
	public void updateImage() {
        setImage(imageArrays[0][index]);		
	}
	
	@Override
	public int getTrueX() {
		return getX() + LANDMINE_WIDTH/2;
	}

	@Override
	public int getTrueY() {
		return getY() + LANDMINE_HEIGHT/2;
	}
}
