package firstMode;

import firstMode.sprite.Landmine;
import firstMode.sprite.Player;
import firstMode.sprite.Sprite;
import firstMode.sprite.Target;
import main.Commons;

public class GameController implements Commons {
	public static final int START_TIME = 800;
	public static final int MAX_TIME = 1200;
	public static final int TIME_INCREMENT = 50;
	public static final int TIME_DECAY = 0;
	
	public static final int DEFAULT_PLAYER_SPEED = 10;
	public static final int SLOWED_PLAYER_SPEED = 4;
	public static final int BOOST_SPEED = 4;
	
	public static final int PLAYER_NORMAL = 0;
	public static final int PLAYER_SLOW = 1;
	
	public static final int MAX_BOOST_GAUGE = 500;
	public static final int BOOST_INCREMENT = 2;
	public static final int BOOST_DECAY = 4;
	
	public static final int SLOWED_TIME = 200;
	public static final int SLOWED_TIME_DECAY = 1;

	public static final int LANDMINE_PHASE = 1;
	
	public static boolean inGame = true;
	public static int currentTime;
	public static int playerState;
	public static int score;
	public static int playerBoostGauge;
	public static boolean boostTrying;
	public static boolean boostBan;
	public static int slowTime;

	public static void initController() {
		setInGame(true);
		setCurrentTime(START_TIME);
		setPlayerState(PLAYER_NORMAL);
		setScore(0);
		setPlayerBoostGauge(MAX_BOOST_GAUGE);
		setBoostBan(false);
	}
	
	public static void decreaseTime() {
		setCurrentTime(Math.max(0, currentTime - TIME_DECAY));
		if(isSlowed()) {
			System.out.println(getSlowTime());
			System.out.println(getPlayerSpeed());
			decreaseSlowTime();
		}
	}
	
	public static void increaseTime() {
		setCurrentTime(Math.min(MAX_TIME, currentTime + TIME_INCREMENT));		
	}
	
	public static boolean isSlowed() {
		return getSlowTime() > 0 ? true : false;
	}
	
	public static void triggerSlow() {
		setSlowTime(SLOWED_TIME);
	}
	
	public static void decreaseSlowTime() {
		setSlowTime(Math.max(0, slowTime - SLOWED_TIME_DECAY));
	}
	
	public static void increasePlayerBoostGauge() {
		setPlayerBoostGauge( Math.min(MAX_BOOST_GAUGE, getPlayerBoostGauge() + BOOST_INCREMENT));
	}
	
	public static void decreasePlayerBoostGauge() {
		setPlayerBoostGauge( Math.max(0, getPlayerBoostGauge() - BOOST_DECAY));
	}
	
	public static boolean canBoost() {
		return getPlayerBoostGauge() > 0 && !isBoostBan() ? true : false;
	}
	
	public static boolean boostFull() {
		return getPlayerBoostGauge() == MAX_BOOST_GAUGE ? true : false;
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
		setScore(getScore() + 1);
	}
	
	public static int getScore() {
		return score;
	}

	public static void setScore(int score) {
		GameController.score = score;
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
		return isBoostTrying() && canBoost() ? true : false;
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
	
	public static int getCurrentTime() {
		return currentTime;
	}
	
	public static void setCurrentTime(int currentTime) {
		GameController.currentTime = currentTime;
	}
	
	public static int getSlowTime() {
		return slowTime;
	}
	
	public static void setSlowTime(int slowTime) {
		GameController.slowTime = slowTime;
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
						&& playerY + 100 >= (spriteY) && playerY + PLAYER_HEIGHT - 100 <= (spriteY + LANDMINE_HEIGHT)) ? true : false;
		}
		else if(b instanceof Target) {
			return (playerX + 80 >= (spriteX) && playerX + PLAYER_WIDTH - 80 <= (spriteX + TARGET_WIDTH)
					&& playerY + 80 >= (spriteY) && playerY + PLAYER_HEIGHT - 80 <= (spriteY + TARGET_HEIGHT)) ? true : false;
		}
		return false;
	}
	
	public static boolean isLandminePhase() {
		return score >= LANDMINE_PHASE ? true : false;
	}
}
