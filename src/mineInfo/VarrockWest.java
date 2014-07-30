package mineInfo;

import java.util.ArrayList;

import methods.MyMethods;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import superMiner.Area;
import superMiner.MineInfo;
import superMiner.Task;
import tasks.Drop;
import tasks.ManageBank;
import tasks.Mine;
import tasks.Pickup;
import tasks.Run;
import tasks.WalkToBank;
import tasks.WalkToMine;

public class VarrockWest extends MineInfo {
	
	private static final Tile[] tilesToBank = new Tile[] {new Tile(3173, 3364, 0), new Tile(3181, 3368, 0),
		new Tile(3182, 3378, 0), new Tile(3178, 3388, 0), new Tile(3173, 3397, 0), new Tile(3172, 3405, 0),
			new Tile(3172, 3415, 0), new Tile(3172, 3425, 0), new Tile(3178, 3428, 0),
			new Tile(3182, 3429, 0), new Tile(3182, 3436, 0)};
	
	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return !o.tile().equals(new Tile(3181, 3373, 0));
		}
	};

	public VarrockWest(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void addTasks(ArrayList<Task> tasks) {

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
		return Area.VARROCK_WEST.area().contains(ctx.players.local());
	}

}
