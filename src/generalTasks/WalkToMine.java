package generalTasks;

import methods.DebugMethods;
import methods.MyMethods;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.TilePath;

import superMiner.AreaInfo;
import superMiner.Task;

public class WalkToMine extends Task {

	private AreaInfo areaInfo;

	private TilePath tilePath = null;

	public WalkToMine(ClientContext ctx, AreaInfo areaInfo) {
		super(ctx);
		this.areaInfo = areaInfo;
		tilePath = new TilePath(ctx, areaInfo.tilesToBank());
		tilePath.reverse();
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().count() < 28
				&& !areaInfo.tilesToBank()[0].matrix(ctx).onMap()
				;
	}

	@Override
	public void execute() {
		if (script().debug()) {
			DebugMethods.println("walking to " + tilePath.next());
		}
		if (tilePath.traverse()) {
			MyMethods.sleep(400, 700);
		}
	}

}
