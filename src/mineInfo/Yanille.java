package mineInfo;

import java.util.ArrayList;

import methods.MyMethods;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import superMiner.Area;
import superMiner.AreaInfo;
import superMiner.SuperMiner;
import superMiner.Task;
import tasks.Drop;
import tasks.FillCoalBag;
import tasks.ManageBank;
import tasks.Mine;
import tasks.Pickup;
import tasks.Run;
import tasks.WalkToBank;
import tasks.WalkToMine;

public class Yanille extends AreaInfo {

	private final Tile[] tilesToBank = new Tile[] {new Tile(2629, 3151, 0), new Tile(2626, 3141, 0),
			new Tile(2629, 3131, 0), new Tile(2627, 3121, 0), new Tile(2621, 3113, 0), new Tile(2615, 3105, 0),
			new Tile(2607, 3099, 0), new Tile(2606, 3093, 0), new Tile(2613, 3093, 0)};

	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return o.tile().distanceTo(o.ctx.players.local()) < 19
					/* below iron rocks are excluded so the player can focus on 
					 * the cluster of iron rocks closest to the bank*/
					&& !o.tile().equals(new Tile(2626, 3149, 0))
					&& !o.tile().equals(new Tile(2626, 3150, 0))
					&& !o.tile().equals(new Tile(2625, 3150, 0))
					&& !o.tile().equals(new Tile(2625, 3151, 0))
					&& !o.tile().equals(new Tile(2638, 3138, 0))
					&& !o.tile().equals(new Tile(2634, 3136, 0))
					;
		}
	};

	public Yanille(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void addTasks(ArrayList<Task> tasks) {

		if (ctx.backpack.select().id(SuperMiner.COALBAG_ID).count() > 0) {
			tasks.add(new FillCoalBag(ctx, tilesToBank));
		}

		if (script().pickupOreOnGround()) {
			tasks.add(new Pickup(ctx));
		}

		tasks.add(new Run(ctx));

		if (script().bankingEnabled()) {
			script().miningMethod("Banking");
			MyMethods.println("Banking is enabled.");

			tasks.add(new ManageBank(ctx, tilesToBank[tilesToBank.length - 1]));
			tasks.add(new WalkToBank(ctx, tilesToBank));
			tasks.add(new WalkToMine(ctx, this));
		} else {
			if (script().dropASAP()) {
				script().miningMethod("Drop ASAP");
			}
			tasks.add(0, new Drop(ctx));
		}

		tasks.add(new Mine(ctx, this));

	}

	@Override
	public Tile[] tilesToBank() {
		return tilesToBank;
	}

	@Override
	public Filter<GameObject> rockFilter() {
		return rockFilter;
	}

	@Override
	public boolean shouldBeUsed() {
		return Area.YANILLE.area().contains(ctx.players.local());
	}

}
