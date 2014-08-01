package superMiner;

import java.util.ArrayList;

import methods.MyMethods;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import tasks.Drop;
import tasks.FillCoalBag;
import tasks.ManageBank;
import tasks.Mine;
import tasks.Pickup;
import tasks.Run;
import tasks.WalkToBank;
import tasks.WalkToMine;

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

	public void addTasks(ArrayList<Task> tasks) {

		if (ctx.backpack.select().id(SuperMiner.COALBAG_ID).count() > 0) {
			tasks.add(new FillCoalBag(ctx, tilesToBank()));
		}

		if (script().pickupOreOnGround()) {
			tasks.add(new Pickup(ctx));
		}

		tasks.add(new Run(ctx));

		if (script().bankingEnabled()) {
			script().miningMethod("Banking");
			MyMethods.println("Banking is enabled.");

			tasks.add(new ManageBank(ctx, tilesToBank()[tilesToBank().length - 1]));
			tasks.add(new WalkToBank(ctx, tilesToBank()));
			tasks.add(new WalkToMine(ctx, tilesToBank()));
		} else {
			if (script().dropASAP()) {
				script().miningMethod("Drop ASAP");
			}
			tasks.add(0, new Drop(ctx));
		}

		tasks.add(new Mine(ctx, this));

	}

	public abstract Tile[] tilesToBank();
	public abstract Filter<GameObject> rockFilter();
	public abstract boolean shouldBeUsed();
	
}
