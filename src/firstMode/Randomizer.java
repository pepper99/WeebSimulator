package firstMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import firstMode.sprite.Target;
import main.Commons;

public class Randomizer implements Commons {
	private static Random random = new Random();
	
	public static ArrayList<Target> targetsRandomizer(int playerX, int playerY){
		Set<Integer> randomX = new HashSet<Integer>();
		Set<Integer> randomY = new HashSet<Integer>();
		random = new Random();
		
		randomX.add(playerX);
		randomX.add(playerY);
		
		while(randomX.size() < 4) {
			randomX.add(random.nextInt(WINDOW_WIDTH - 2 * TARGET_WIDTH) + TARGET_WIDTH);
		}
		
		while(randomY.size() < 4) {
			randomY.add(random.nextInt(WINDOW_HEIGHT - 2 * TARGET_HEIGHT) + TARGET_HEIGHT);
		}
		
		randomX.remove(playerX);
		randomY.remove(playerY);

		ArrayList<Target> targets = new ArrayList<>();
		Iterator<Integer> itX = randomX.iterator();
		Iterator<Integer> itY = randomY.iterator();

		for (int i = 0; i < 3; i++) {
			Target target = new Target(itX.next(), itY.next(), i);
			targets.add(target);
		}
		return targets;
	}
}
