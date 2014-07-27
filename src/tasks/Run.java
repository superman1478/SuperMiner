package tasks;

import methods.MyMethods;

import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;

import superMiner.Task;

public class Run extends Task {

	public Run(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return !ctx.movement.running()
				&& ctx.movement.energyLevel() >= Random.nextInt(50, 80);
	}

	@Override
	public void execute() {
			MyMethods.sleep(200, 500);
			ctx.movement.running(true);
			MyMethods.sleep(100, 300);
	}
	
}
