package superMiner;

import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

public abstract class Task extends ClientAccessor {

	private SuperMiner script;

	public Task(ClientContext ctx) {
		super(ctx);
		script = (SuperMiner)ctx.controller.script();
	}

	public SuperMiner script() {
		return script;
	}

	public abstract boolean activate();

	public abstract void execute();

}
