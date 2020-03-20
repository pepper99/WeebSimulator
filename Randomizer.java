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
		ArrayList<Integer> tempX = new ArrayList<Integer>();
		ArrayList<Integer> tempY = new ArrayList<Integer>();
		randomX.add(playerX);
		randomX.add(playerY);
		
//		while(randomX.size() < 4) {
//			randomX.add(random.nextInt(WINDOW_WIDTH - 2 * TARGET_WIDTH) + TARGET_WIDTH);
//		}
//		
//		while(randomY.size() < 4) {
//			randomY.add(random.nextInt(WINDOW_HEIGHT - 2 * TARGET_HEIGHT) + TARGET_HEIGHT);
//		}
		for(int i = 0 ; i < 3 ; i ++)
		{
			if(i == 0)
			{
				tempX.add(random.nextInt(WINDOW_WIDTH - 2 * TARGET_WIDTH) + TARGET_WIDTH);
				tempY.add(random.nextInt(WINDOW_HEIGHT - 2 * TARGET_HEIGHT) + TARGET_HEIGHT);
			}
			else if(i == 1)
			{
				int rand1 = random.nextInt(1);
				int rand2 = random.nextInt(1);
				if(rand1 == 0)
				{
//					tempX.add(random.nextInt(tempX.get(0) - 2* TARGET_WIDTH) + TARGET_WIDTH);
					tempX.add(random.nextInt(tempX.get(0)));
				}
				else
				{
//					tempX.add(random.nextInt(WINDOW_WIDTH - 2 * (TARGET_WIDTH+tempX.get(0))) +TARGET_WIDTH + tempX.get(0));
					tempX.add(random.nextInt(WINDOW_WIDTH - tempX.get(0))+tempX.get(0));
				}
				if(rand2 == 0)
				{
//					tempY.add(random.nextInt(tempY.get(0) - 2*TARGET_HEIGHT) + TARGET_HEIGHT);
					tempY.add(random.nextInt(tempY.get(0)));
				}
				else
				{
//					tempY.add(random.nextInt(WINDOW_HEIGHT - 2*(tempY.get(0) + TARGET_HEIGHT)) + TARGET_WIDTH +tempY.get(0));
					tempY.add(random.nextInt(WINDOW_HEIGHT - tempY.get(0)) + tempY.get(0));
				}
			}
			else if(i == 2)
			{
//			{
//				tempX.add(100);
//				tempY.add(100);
//			}
////				
//				
				if(tempX.get(0) > tempX.get(1))
					{	
						if(tempX.get(0) - tempX.get(1) > TARGET_WIDTH)
						{
							int rand = random.nextInt(2);
							if(rand == 0)
							{
//								tempX.add(random.nextInt(tempX.get(1) - 2* TARGET_WIDTH) + TARGET_WIDTH);
								tempX.add(random.nextInt(tempX.get(1)));
							}
							else if(rand == 1)
							{
								tempX.add(random.nextInt(tempX.get(0) -(TARGET_WIDTH + tempX.get(1))) +tempX.get(1) +TARGET_WIDTH);
//								tempX.add(random.nextInt(tempX.get(0) - tempX.get(0) - TARGET_WIDTH) + tempX.get(0) + TARGET_WIDTH);
							}
							else
							{
								tempX.add(random.nextInt(WINDOW_WIDTH - (tempX.get(0) + TARGET_WIDTH)) + TARGET_WIDTH + tempX.get(0));
							}
						}
						else
						{
							int rand = random.nextInt(1);
							if(rand == 0)
							{
								tempX.add(random.nextInt(tempX.get(1) - TARGET_WIDTH) + TARGET_WIDTH);
							}
							else
							{
								tempX.add(random.nextInt(WINDOW_WIDTH - (tempX.get(0) + TARGET_WIDTH)) + TARGET_WIDTH + tempX.get(0));
							}
						}
					}
				else
					{
						if(tempX.get(1) - tempX.get(0) > TARGET_WIDTH)
						{
							int rand = random.nextInt(2);
							if(rand == 0)
							{
								tempX.add(random.nextInt(tempX.get(0)));
							}
							else if(rand == 1)
							{
								tempX.add(random.nextInt(tempX.get(1) -(TARGET_WIDTH + tempX.get(0))) +tempX.get(0) +TARGET_WIDTH);
							}
							else
							{
								tempX.add(random.nextInt(WINDOW_WIDTH - (tempX.get(1) + TARGET_WIDTH)) + TARGET_WIDTH + tempX.get(1));
							}
						}
						else
						{
							int rand = random.nextInt(1);
							if(rand == 0)
							{
								tempX.add(random.nextInt(tempX.get(0)));
							}
							else
							{
								tempX.add(random.nextInt(WINDOW_WIDTH - (tempX.get(1) + TARGET_WIDTH)) + TARGET_WIDTH + tempX.get(1));
							}
						}
					}
				if(tempY.get(0) > tempY.get(1))
					{
						if(tempY.get(0) - tempY.get(1) > TARGET_HEIGHT)
						{
							int rand = random.nextInt(2);
							if(rand == 0)
							{
								tempY.add(random.nextInt(tempY.get(1)));
							}
							else if(rand == 1)
							{
								tempY.add(random.nextInt(tempY.get(0) -(TARGET_HEIGHT + tempY.get(1))) +tempY.get(1) +TARGET_HEIGHT);
							}
							else
							{
								tempY.add(random.nextInt(WINDOW_HEIGHT - (tempY.get(0) + TARGET_HEIGHT)) + TARGET_HEIGHT + tempY.get(0));
							}
						}
						else
						{
							int rand = random.nextInt(1);
							if(rand == 0)
							{
								tempY.add(random.nextInt(tempY.get(1)));
							}
							else
							{
								tempY.add(random.nextInt(WINDOW_HEIGHT - 2*(tempY.get(0) + TARGET_HEIGHT)) + TARGET_HEIGHT + tempY.get(0));
							}
						}
					}
				else
					{
					if(tempY.get(1) - tempY.get(0) > TARGET_HEIGHT)
						{
							int rand = random.nextInt(2);
							if(rand == 0)
							{
								tempY.add(random.nextInt(tempY.get(0)));
							}
							else if(rand == 1)
							{
								tempY.add(random.nextInt(tempY.get(1) -(TARGET_HEIGHT + tempY.get(0))) +tempY.get(1) +TARGET_HEIGHT);
							}
							else
							{
								tempY.add(random.nextInt(WINDOW_HEIGHT - (tempY.get(1) + TARGET_HEIGHT)) + TARGET_HEIGHT + tempY.get(1));
							}
						}
					else
						{
							int rand = random.nextInt(1);
							if(rand == 0)
							{
								tempY.add(random.nextInt(tempY.get(0)));
							}
							else
							{
								tempY.add(random.nextInt(WINDOW_HEIGHT - (tempY.get(1) + TARGET_HEIGHT)) + TARGET_HEIGHT + tempY.get(1));
							}
						}
					}
			}
		}
		randomX.remove(playerX);
		randomY.remove(playerY);
		for(int i = 0 ; i < 3 ; i++)
		{
			randomX.add(tempX.get(i));
			randomY.add(tempY.get(i));
		}
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
