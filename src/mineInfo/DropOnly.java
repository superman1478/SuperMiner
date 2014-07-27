package mineInfo;

import java.util.ArrayList;

import methods.MyMethods;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import superMiner.AreaInfo;
import superMiner.Task;
import tasks.Drop;
import tasks.Mine;
import tasks.Pickup;
import tasks.Run;

public class DropOnly extends AreaInfo {

	private final Tile[] tilesToBank = new Tile[]{};

	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return true;
		}
	};

	public DropOnly(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void addTasks(ArrayList<Task> tasks) {

		if (script().pickupOreOnGround()) {
			tasks.add(new Pickup(ctx));
		}

		tasks.add(new Run(ctx));

		if (script().bankingEnabled()) {
			script().message("Unsupported banking Location");
			MyMethods.println(script().message());
			MyMethods.sleep(6000, 6000);
			ctx.controller.stop();
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
		return true;
	}

}
