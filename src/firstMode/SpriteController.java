package firstMode;

import firstMode.sprite.Sprite;

public class SpriteController {
	public static final int MAX_TIME = 20;
	
	private static int timer;
	
	public static void initContorller() {
		timer = 0;
	}
	
	public static void increaseTime() {
		timer = (timer + 1) % MAX_TIME;
	}
	
	public static void switchSprite(Sprite sprite) {
		if (timer == 0) {
			sprite.updateIndex();
			sprite.updateImage();
		}
	}
}
