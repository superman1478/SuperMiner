package tasks;

import methods.DebugMethods;
import methods.MyMethods;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.TilePath;

import superMiner.SuperMiner;
import superMiner.Task;

public class WalkToBank extends Task {

	private TilePath tilePath;
	private Tile[] tileArray;

	public WalkToBank(ClientContext ctx, Tile[] tilesToBank) {
		super(ctx);
		this.tileArray = tilesToBank;
		tilePath = new TilePath(ctx, tileArray);
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().count() == 28
				&& tileArray[tileArray.length - 1].distanceTo(ctx.players.local()) > 4
				&& ctx.players.local().animation() != SuperMiner.HOME_TELEPORT_ANIMATION_1
				&& ctx.players.local().animation() != SuperMiner.HOME_TELEPORT_ANIMATION_3
				&& ctx.players.local().animation() != SuperMiner.HOME_TELEPORT_ANIMATION_3
				;
	}

	@Override
	public void execute() {
		DebugMethods.println(script().debug(), "walking to " + tilePath.next());
		if (tilePath.traverse()) {
			MyMethods.sleep(400, 700);
		}
	}

}
