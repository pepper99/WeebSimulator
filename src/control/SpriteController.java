package control;

import sprite.base.Sprite;

public class SpriteController {
	public static final int MAX_TIME = 100;
	
	private static int timer;
	private static int flashTimer;
	
	public static void initContorller() {
		timer = 0;
	}
	
	public static void increaseTime() {
		timer = (timer + 1) % MAX_TIME;
	}
	
	public static void switchSprite(Sprite sprite) {
		if (timer % 20 == 0) {
			sprite.updateIndex();
			sprite.updateImage();
		}
	}
	
	public static boolean flash() {
		if (timer % 20 == 0) {
			flashTimer = 11;
		}
		flashTimer = Math.max(0, flashTimer - 1);
		return flashTimer > 0 ? true : false;
	}
}