package control;

import sprite.base.Sprite;

public class SpriteController {
	public static final int MAX_TIME = 60; //LCM of all intervals
	private static final int FLASH_INTERVAL = 20;
	private static final int FLASH_DURATION = 11;
	
	private static int timer;
	private static int flashTimer;
	
	public static void initContorller() {
		timer = 1;
	}
	
	public static void increaseTime() {
		timer = (timer + 1) % MAX_TIME;
	}
	
	public static void switchSprite(Sprite sprite) {
		if (timer % sprite.getSpriteInterval() == 0) {
			sprite.updateSpriteIndex();
		}
	}
	
	public static boolean flash() {
		if (timer % FLASH_INTERVAL == 0) {
			flashTimer = FLASH_DURATION;
		}
		flashTimer = Math.max(0, flashTimer - 1);
		return flashTimer > 0 ? true : false;
	}
}