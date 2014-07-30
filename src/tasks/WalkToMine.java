package tasks;

import methods.DebugMethods;
import methods.MyMethods;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.TilePath;

import superMiner.MineInfo;
import superMiner.Task;

public class WalkToMine extends Task {

	private MineInfo areaInfo;

	private TilePath tilePath = null;

	public WalkToMine(ClientContext ctx, MineInfo areaInfo) {
		super(ctx);
		this.areaInfo = areaInfo;
		tilePath = new TilePath(ctx, areaInfo.tilesToBank());
		tilePath.reverse();
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().count() < 28
				&& (!areaInfo.tilesToBank()[0].matrix(ctx).onMap()
						|| areaInfo.tilesToBank()[0].distanceTo(ctx.players.local()) > 15)
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
