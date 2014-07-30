package superMiner;

import java.util.ArrayList;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public abstract class MineInfo {

	public ClientContext ctx;
	private SuperMiner script;

	public MineInfo (ClientContext ctx) {
		this.ctx = ctx;
		script = (SuperMiner)ctx.controller.script();
	}

	public SuperMiner script() {
		return script;
	}

	public abstract void addTasks(ArrayList<Task> tasks);
	public abstract Tile[] tilesToBank();
	public abstract Filter<GameObject> rockFilter();
	public abstract boolean shouldBeUsed();
}
