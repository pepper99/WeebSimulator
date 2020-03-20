package firstMode;

public class GameController {
	public static final int START_TIME = 1000;
	public static final int MAX_TIME = 1280;
	public static final int TIME_INCREMENT = 50;
	public static final int TIME_DECAY = 0;
	public static final int DEFAULT_PLAYER_SPEED = 8;
	
	public static boolean inGame = true;
	public static int currentTime;
	public static int playerSpeed;
	public static int score;

	public static void initController() {
		setInGame(true);
		setCurrentTime(START_TIME);
		setPlayerSpeed(DEFAULT_PLAYER_SPEED);
		setScore(0);
	}
	
	public static void decreaseTime() {
		setCurrentTime(Math.max(0, currentTime - TIME_DECAY));
	}
	
	public static void increaseTime() {
		setCurrentTime(Math.min(MAX_TIME, currentTime + TIME_INCREMENT));		
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
		return playerSpeed;
	}

	public static void setPlayerSpeed(int playerSpeed) {
		GameController.playerSpeed = playerSpeed;
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
}
