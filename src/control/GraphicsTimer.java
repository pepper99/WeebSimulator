package control;

import sprite.base.Sprite;

public class GraphicsTimer {
	public static final int MAX_TIME = 60; //LCM of all intervals
	private static final int BOOSTFLASH_INTERVAL = 20;
	private static final int BOOSTFLASH_DURATION = 11;
	
	private static final int SCREENFLASH_DURATION = 30;
	private static final double SCREENFLASH_MAX_OPACITY = 0.7;
	
	private static int timer;
	private static int boostFlashTimer;
	private static int screenFlashTimer;
	
	public static void init() {
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
	
	public static boolean boostFlash() {
		if (timer % BOOSTFLASH_INTERVAL == 0) {
			boostFlashTimer = BOOSTFLASH_DURATION;
		}
		boostFlashTimer = Math.max(0, boostFlashTimer - 1);
		return boostFlashTimer > 0;
	}
	
	public static boolean screenFlash(boolean startCondition) {
		if (screenFlashTimer == 0 && startCondition) {
			screenFlashTimer = SCREENFLASH_DURATION;
		}
		else {
			screenFlashTimer = Math.max(0, screenFlashTimer - 1);			
		}
		return screenFlashTimer > 0;
	}
	
	public static double getScreenFlashOpacity() {
		return screenFlashTimer / (double) SCREENFLASH_DURATION * SCREENFLASH_MAX_OPACITY;
	}
}