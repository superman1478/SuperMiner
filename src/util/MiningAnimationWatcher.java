package util;

import methods.ArrayMethods;
import methods.MyCalculations;

import org.powerbot.script.rt6.ClientContext;

/**
 * @author Jake
 * 
 * Starts a thread that updates a long when the local player has a mining animation
 * and allows you to easily calculate time since mining.
 *
 */
public class MiningAnimationWatcher extends Thread {
	
	private ClientContext ctx;
	
	public MiningAnimationWatcher(ClientContext ctx) {
		this.ctx = ctx;
		lastMining = System.currentTimeMillis() - 2000;
	}

	private long lastMining = 0;

	private final static int BORNZE_PICK_ON_MINERAL_DEPOSIT_ANIMATION = 6747;

	private final static int RUNE_PICK_ON_NORMAL_ROCK_ANIMATION = 624;
	private final static int RUNE_PICK_ON_MINERAL_DEPOSIT_ANIMATION = 6746;

	private final static int DRAGON_PICK_ON_NORMAL_ROCK_ANIMATION = 12188;
	private final static int DRAGON_PICK_ON_MINERAL_DEPOSIT_ANIMATION = 12189;

	private final static int RUNE_PICK_ON_ESSENCE_PILLAR_ANIMATION = 6752;

	public final static int[] MINING_ANIMATIONS = {RUNE_PICK_ON_NORMAL_ROCK_ANIMATION, BORNZE_PICK_ON_MINERAL_DEPOSIT_ANIMATION,
		DRAGON_PICK_ON_NORMAL_ROCK_ANIMATION, RUNE_PICK_ON_MINERAL_DEPOSIT_ANIMATION, 
		DRAGON_PICK_ON_MINERAL_DEPOSIT_ANIMATION, RUNE_PICK_ON_ESSENCE_PILLAR_ANIMATION};

	public double secondsSinceLastMining() {
		return MyCalculations.getSecondsSince(lastMining);
	}

	@Override
	public void run() {
		
		while (!ctx.controller.isStopping()) {
			if (ArrayMethods.arrayContainsInt(MINING_ANIMATIONS, ctx.players.local().animation())) {
				lastMining = System.currentTimeMillis();
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
