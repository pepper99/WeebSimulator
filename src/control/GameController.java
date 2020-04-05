package control;

import base.Commons;
import sprite.Landmine;
import sprite.Player;
import sprite.Target;
import sprite.base.Sprite;

public class GameController implements Commons {
	private static final double START_TIME = 800;
	public static final double MAX_TIME = 1200;
	private static final double INITIAL_TIME_DECAY = 2.5;
	private static final double INITIAL_TIME_INCREMENT = 400;
	private static final double MIN_TIME_INCREMENT = 200;
	public static final double MAX_TIME_DECAY = 300;
	
	private static final int DEFAULT_PLAYER_SPEED = 10;
	private static final int SLOWED_PLAYER_SPEED = 4;
	private static final int BOOST_SPEED = 4;
	
	public static final int PLAYER_NORMAL = 0;
	public static final int PLAYER_SLOW = 1;
	
	public static final int MAX_BOOST_GAUGE = 500;
	private static final int BOOST_INCREMENT = 2;
	private static final int BOOST_DECAY = 4;
	
	private static final int SLOWED_TIME = 200;
	private static final int SLOWED_TIME_DECAY = 1;

	private static final int LANDMINE_STAGE = 12;
	private static final int MAX_LANDMINE_COUNT = 5;
	
	private static boolean inGame;
	private static double currentTime;
	private static int playerState;
	private static int score;
	private static int playerBoostGauge;
	private static boolean boostTrying;
	private static boolean boostBan;
	private static int slowTime;
	private static double timeDecay;
	private static int landmineCount;
	private static double timeIncrement;

	public static void initController() {
		setInGame(true);
		currentTime = START_TIME;
		setPlayerState(PLAYER_NORMAL);
		score = 0;
		setPlayerBoostGauge(MAX_BOOST_GAUGE);
		setBoostBan(false);
		timeDecay = INITIAL_TIME_DECAY;
		timeIncrement = INITIAL_TIME_INCREMENT;
		landmineCount = 0;
		setInGame(true);
	}
	
	public static void decreaseTime() {
		currentTime = Math.max(0, currentTime - timeDecay);
		if(isSlowed()) {
			decreaseSlowTime();
		}
	}
	
	public static void increaseTime() {
		currentTime = Math.min(MAX_TIME, currentTime + timeIncrement);
	}
	
	public static boolean isSlowed() {
		return slowTime > 0;
	}
	
	public static void triggerSlow() {
		slowTime = SLOWED_TIME;
	}
	
	public static void decreaseSlowTime() {
		slowTime = Math.max(0, slowTime - SLOWED_TIME_DECAY);
	}
	
	public static void increasePlayerBoostGauge() {
		setPlayerBoostGauge( Math.min(MAX_BOOST_GAUGE, getPlayerBoostGauge() + BOOST_INCREMENT));
	}
	
	public static void decreasePlayerBoostGauge() {
		setPlayerBoostGauge( Math.max(0, getPlayerBoostGauge() - BOOST_DECAY));
	}
	
	public static boolean canBoost() {
		return getPlayerBoostGauge() > 0 && !isBoostBan();
	}
	
	public static boolean boostFull() {
		return getPlayerBoostGauge() == MAX_BOOST_GAUGE;
	}
	
	public static boolean checkBoostBan() {
		if(getPlayerBoostGauge() == 0) {
			setBoostBan(true);
		}
		else if(getPlayerBoostGauge() == MAX_BOOST_GAUGE) {
			setBoostBan(false);
		}
		return isBoostBan();
	}
	
	public static void setBoostBan(boolean boostBan) {
		GameController.boostBan = boostBan;
	}
	
	public static boolean isBoostBan() {
		return boostBan;
	}

		public static int getPlayerBoostGauge() {
		return playerBoostGauge;
	}

	public static void setPlayerBoostGauge(int playerBoostGauge) {
		GameController.playerBoostGauge = playerBoostGauge;
	}
	
	public static void increaseScore() {
		score++;
		updateTimeDecay();
		updateTimeIncrement();
		updateLandmineCount();
	}
	
	public static int getScore() {
		return score;
	}
	
	public static int getPlayerSpeed() {
		int playerSpeed;
		
		switch(getPlayerState()) {
		case PLAYER_NORMAL:
			playerSpeed = DEFAULT_PLAYER_SPEED;
			break;
		case PLAYER_SLOW:
			playerSpeed = SLOWED_PLAYER_SPEED;
			break;
		default:
			playerSpeed = DEFAULT_PLAYER_SPEED;
		}
		
		if(boost()) {
			playerSpeed += BOOST_SPEED;
    	}
		return playerSpeed;
	}
	
	public static boolean boost() {
		return isBoostTrying() && canBoost();
	}
	
	public static int getPlayerState() {
		return playerState;
	}

	public static void setPlayerState(int playerState) {
		GameController.playerState = playerState;
	}

	public static boolean isInGame() {
		return inGame;
	}
	
	public static void setInGame(boolean inGame) {
		GameController.inGame = inGame;
	}
	
	public static double getCurrentTime() {
		return currentTime;
	}

	public static boolean isBoostTrying() {
		return boostTrying;
	}

	public static void setBoostTrying(boolean boostTrying) {
		GameController.boostTrying = boostTrying;
	}
	
	public static boolean playerCollideCheck(Player a, Sprite b) {
		int playerX = a.getX();
		int playerY = a.getY();
		int spriteX = b.getX();
		int spriteY = b.getY();
		if(b instanceof Landmine) {
			return (playerX + 30 >= (spriteX) && playerX + PLAYER_WIDTH - 30 <= (spriteX + LANDMINE_WIDTH)
						&& playerY + 100 >= (spriteY) && playerY + PLAYER_HEIGHT - 100 <= (spriteY + LANDMINE_HEIGHT));
		}
		else if(b instanceof Target) {
			return (playerX + 80 >= (spriteX) && playerX + PLAYER_WIDTH - 80 <= (spriteX + TARGET_WIDTH)
					&& playerY + 80 >= (spriteY) && playerY + PLAYER_HEIGHT - 80 <= (spriteY + TARGET_HEIGHT));
		}
		return false;
	}
	
	public static boolean hasLandmine() {
		return getLandmineCount() > 0;
	}
	
	private static void updateTimeDecay() {
		timeDecay = Math.min(MAX_TIME_DECAY, timeDecay * 1.01);
	}
	
	public static int getLandmineCount() {
		return landmineCount;
	}
	
	public static void increaseLandmineCount() {
		landmineCount = Math.min(MAX_LANDMINE_COUNT, landmineCount + 1);
	}
	
	public static void updateLandmineCount() {
		if(score % LANDMINE_STAGE == 0) {
			increaseLandmineCount();
		}
	}
	
	public static void updateTimeIncrement() {
		timeIncrement = Math.max(MIN_TIME_INCREMENT, timeIncrement /= 1.002);
	}
}
