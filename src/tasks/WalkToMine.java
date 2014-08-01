package tasks;

import methods.DebugMethods;
import methods.MyMethods;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.TilePath;

import superMiner.Task;

public class WalkToMine extends Task {
	
	private TilePath tilePath;

	private Tile firstTile;

	/**
	 * A reversed copy of tilesToBank will be used to walk to the mine.
	 * @param ctx
	 * @param tilesToBank
	 */
	public WalkToMine(ClientContext ctx, Tile[] tilesToBank) {
		super(ctx);
		tilePath = new TilePath(ctx, tilesToBank);
		tilePath.reverse();
		firstTile = tilesToBank[0];
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().count() < 28
				&& (!firstTile.matrix(ctx).onMap()
						|| firstTile.distanceTo(ctx.players.local()) > 15)
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
