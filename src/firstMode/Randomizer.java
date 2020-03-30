package firstMode;

import java.util.ArrayList;
import java.util.Random;

import firstMode.sprite.Target;
import main.Commons;

public class Randomizer implements Commons {
	private static final int TARGET_RADIUS = 90;
	private static final int PLAYER_RADIUS = 100;
	
	private static Random random = new Random();
	
	public static ArrayList<Target> targetsRandomizer(int playerX, int playerY){
		ArrayList<Target> targets = new ArrayList<>();
		int[][] coordinates = new int[4][3];
		
		coordinates[3][0] = playerX;
		coordinates[3][1] = playerY;
		coordinates[3][2] = PLAYER_RADIUS;
		
		int count = 0;
		while(count < 3) {
			int x = random.nextInt(WINDOW_WIDTH - TARGET_WIDTH) + TARGET_WIDTH/2;
			int y = random.nextInt(WINDOW_HEIGHT - TARGET_HEIGHT) + TARGET_HEIGHT/2;
			
			boolean overlapped = false;
			for(int[] coor : coordinates) {
				if(distance(coor[0], x, coor[1], y) < coor[2] + TARGET_RADIUS) {			
					overlapped = true;
					break;
				}
			}
			if(!overlapped) {
				coordinates[count][0] = x - TARGET_WIDTH/2;
				coordinates[count][1] = y - TARGET_HEIGHT/2;
				coordinates[count][2] = TARGET_RADIUS;
				count++;
			}
		}

		for (int i = 0; i < 3; i++) {
			Target target = new Target(coordinates[i][0], coordinates[i][1], i);
			targets.add(target);
		}
		return targets;
	}
	
	private static double distance(int x1, int x2, int y1, int y2) {	         
	    return Math.hypot(Math.abs(y2 - y1), Math.abs(x2 - x1));
	}
}
