package generalTasks;

import methods.MyMethods;

import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;

import superMiner.Task;

public class CameraAntiPattern extends Task {

	public CameraAntiPattern(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return true;
	}

	@Override
	public void execute() {
		if (Random.nextInt(0, 100) == 0) {
			MyMethods.println("checking if camera pitch is true");
			if (ctx.camera.pitch() < 89) {
				MyMethods.println("setting camera pitch true");
				ctx.camera.pitch(true);
			}
		}

		if (Random.nextInt(0, 250) == 10) {
			MyMethods.println("Randomly setting camera pitch 40-100");
			ctx.camera.pitch(Random.nextInt(40, 100));
		}
	}

}
