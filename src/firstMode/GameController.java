package firstMode;

public class GameController {
	public static final int START_TIME = 800;
	public static final int MAX_TIME = 1200;
	public static final int TIME_INCREMENT = 50;
	public static final int TIME_DECAY = 0;
	
	public static final int DEFAULT_PLAYER_SPEED = 8;
	public static final int BOOSTED_PLAYER_SPEED = 15;
	public static final int SLOWED_PLAYER_SPEED = 5;
	
	public static final int PLAYER_NORMAL = 0;
	public static final int PLAYER_BOOST = 1;
	public static final int PLAYER_SLOW = 2;
	
	public static final int MAX_BOOST_GAUGE = 500;
	public static final int BOOST_INCREMENT = 2;
	public static final int BOOST_DECAY = 4;
	
	public static boolean inGame = true;
	public static int currentTime;
	public static int playerState;
	public static int score;
	public static int playerBoostGauge;
	public static boolean boostTrying;
	public static boolean boostBan;

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
	}
	
	public static void increaseTime() {
		setCurrentTime(Math.min(MAX_TIME, currentTime + TIME_INCREMENT));		
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
		switch(getPlayerState()) {
		case PLAYER_NORMAL:
			return DEFAULT_PLAYER_SPEED;
		case PLAYER_BOOST:
			return BOOSTED_PLAYER_SPEED;
		case PLAYER_SLOW:
			return SLOWED_PLAYER_SPEED;
		default:
			return DEFAULT_PLAYER_SPEED;			
		}
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

	public static boolean isBoostTrying() {
		return boostTrying;
	}

	public static void setBoostTrying(boolean boostTrying) {
		GameController.boostTrying = boostTrying;
	}
}
