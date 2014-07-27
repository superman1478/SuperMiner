package mineInfo;

import java.util.ArrayList;

import methods.MyMethods;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import superMiner.Area;
import superMiner.AreaInfo;
import superMiner.Ore;
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
import util.OreInfoListMethods;

public class BarbarianVillageBankEdgeville extends AreaInfo {

	//tilesToBank will change if user is mining clay
	private Tile[] tilesToBank = new Tile[] {new Tile(3084, 3423, 0), new Tile(3087, 3433, 0), 
			new Tile(3087, 3443, 0), new Tile(3086, 3453, 0), new Tile(3088, 3461, 0), new Tile(3080, 3467, 0),
			new Tile(3080, 3477, 0), new Tile(3086, 3486, 0), new Tile(3094, 3490, 0)};

	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return true;
		}
	};

	public BarbarianVillageBankEdgeville(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void addTasks(ArrayList<Task> tasks) {

		if (OreInfoListMethods.ListContains(script().oreInfoList(), Ore.CLAY)) {
			tilesToBank = new Tile[] {new Tile(3082, 3400, 0), new Tile(3081, 3410, 0), 
					new Tile(3083, 3418, 0), new Tile(3084, 3423, 0), new Tile(3087, 3433, 0),
					new Tile(3087, 3443, 0), new Tile(3086, 3453, 0), new Tile(3088, 3461, 0),
					new Tile(3080, 3467, 0), new Tile(3080, 3477, 0), new Tile(3086, 3486, 0),
					new Tile(3094, 3490, 0)};
		}

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
		return Area.BARBARIAN_VILLAGE_EDGEVILLE.area().contains(ctx.players.local());
	}

}
