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

public class LumbridgeSwampWest extends AreaInfo {

	private final Tile[] tilesToBank = new Tile[] {new Tile(3148, 3146, 0), new Tile(3148, 3156, 0),
			new Tile(3148, 3166, 0), new Tile(3144, 3176, 0), new Tile(3142, 3186, 0),
			new Tile(3139, 3195, 0), new Tile(3134, 3204, 0), new Tile(3127, 3212, 0),
			new Tile(3118, 3218, 0), new Tile(3112, 3227, 0), new Tile(3066, 3265, 0),
			new Tile(3106, 3233, 0), new Tile(3100, 3241, 0), new Tile(3092, 3244, 0)};

	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return true;
		}
	};

	public LumbridgeSwampWest(ClientContext ctx) {
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
		return Area.LUMBRIDGE_SWAMP_WEST.area().contains(ctx.players.local());
	}

}
