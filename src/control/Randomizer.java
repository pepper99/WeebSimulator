package control;

import java.util.Random;

import main.Commons;

public class Randomizer implements Commons {
	private static final int LANDMINE_RADIUS = 60;
	private static final int TARGET_RADIUS = 90;	
	private static final int PLAYER_RADIUS = 100;
	
	public static final int LANDMINE_ITT = 4;
	
	private static Random random = new Random();
	
	public static int[][] coordinatesRandomizer(int playerX, int playerY){
		
		int count = 1;
		int max = 4;
		if(GameController.isLandminePhase()) max++;
		
		int[][] coordinates = new int[max][3];		
		coordinates[0][0] = playerX;
		coordinates[0][1] = playerY;
		coordinates[0][2] = PLAYER_RADIUS;
		
		while(count < max) {
			int width, height, radius;
			
			switch(count) {
			case(LANDMINE_ITT):
				width = LANDMINE_WIDTH;
				height = LANDMINE_HEIGHT;
				radius = LANDMINE_RADIUS;
			default:
				width = TARGET_WIDTH;
				height = TARGET_HEIGHT;
				radius = TARGET_RADIUS;
			}
			
			int x = random.nextInt(WINDOW_WIDTH - width - BORDER_RIGHT - BORDER_LEFT) + width/2 + BORDER_LEFT;
			int y = random.nextInt(WINDOW_HEIGHT - height - BORDER_BOTTOM - BORDER_TOP) + height/2 + BORDER_TOP;
			
			boolean overlapped = false;
			for(int[] coor : coordinates) {
				if(distance(coor[0], x, coor[1], y) < coor[2] + radius) {			
					overlapped = true;
					break;
				}
			}
			if(!overlapped) {
				coordinates[count][0] = x - width/2;
				coordinates[count][1] = y - height/2;
				coordinates[count][2] = radius;
				count++;
			}
		}
		
		return coordinates;
	}
	
	public static int getPlayerType() {
		return random.nextInt(3);
	}
	
	private static double distance(int x1, int x2, int y1, int y2) {	         
	    return Math.hypot(Math.abs(y2 - y1), Math.abs(x2 - x1));
	}
}
