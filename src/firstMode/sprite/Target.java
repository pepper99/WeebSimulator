package firstMode.sprite;

import firstMode.base.Type;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import main.Commons;

public class Target extends Sprite implements Commons, Type {
	
	public static final int TYPE_ALIEN = 0;
	public static final int TYPE_ANIME_GIRL = 1;
	public static final int TYPE_INF_GAUNTLET = 2;
	
	private int type;

    public Target(int x, int y, int type) {
        initTarget(x, y, type);
    }

    private void initTarget(int x, int y, int type) {
    	setX(x);
        setY(y);
        setType(type);
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		String playerImg = null;
		Image ii = null;
		switch(type) {
		case 0:
	        playerImg = ClassLoader.getSystemResource("images/target_0.png").toString();
			break;
		case 1:
	        playerImg = ClassLoader.getSystemResource("images/target_1.png").toString();
			break;
		case 2:
	        playerImg = ClassLoader.getSystemResource("images/target_2.png").toString();
			break;
		}
        ii = new Image(playerImg);
        setImage(new WritableImage(ii.getPixelReader(), PLAYER_WIDTH, PLAYER_HEIGHT));
	}
}
