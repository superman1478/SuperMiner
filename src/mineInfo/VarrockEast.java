package areaInfo;

import generalTasks.Drop;
import generalTasks.ManageBank;
import generalTasks.Mine;
import generalTasks.Pickup;
import generalTasks.Run;
import generalTasks.WalkToBank;
import generalTasks.WalkToMine;

import java.util.ArrayList;

import methods.MyMethods;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import superMiner.Area;
import superMiner.AreaInfo;
import superMiner.Task;

public class VarrockEast extends AreaInfo {
	
	private final Tile[] tilesToBank = new Tile[] {new Tile(3285, 3362, 0), new Tile(3287, 3372, 0),
			new Tile(3291, 3382, 0), new Tile(3290, 3392, 0), new Tile(3290, 3402, 0),
			new Tile(3290, 3405, 0), new Tile(3287, 3415, 0), new Tile(3285, 3425, 0),
			new Tile(3275, 3427, 0), new Tile(3265, 3428, 0), new Tile(3219, 3444, 0),
			new Tile(3259, 3428, 0), new Tile(3254, 3420, 0)};
	
	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return !o.tile().equals(new Tile(3285, 3368, 0));
		}
	};

	public VarrockEast(ClientContext ctx) {
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
		return Area.VARROCK_EAST.area().contains(ctx.players.local());
	}

}
