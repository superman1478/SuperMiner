package generalTasks;

import methods.MyMethods;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

import superMiner.Ore;
import superMiner.SuperMiner;
import superMiner.Task;

public class FillCoalBag extends Task {
	
	Tile[] tilesToBank;

	public FillCoalBag(ClientContext ctx, Tile[] tilesToBank) {
		super(ctx);
		this.tilesToBank = tilesToBank;
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(SuperMiner.COALBAG_ID).count() > 0
				&& ctx.backpack.select().id(Ore.COAL.getId()).count() > 0
				&& ctx.backpack.select().count() == 28
				&& script().coalBagCount < 27
				&& tilesToBank[tilesToBank.length - 1].distanceTo(ctx.players.local()) > 10;
	}

	@Override
	public void execute() {
		if (ctx.backpack.select().id(Ore.COAL.getId()).count() > 0 && ctx.backpack.select().count() == 28) {
			Item coalbag = ctx.backpack.select().id(SuperMiner.COALBAG_ID).poll();
			if (coalbag.valid()) {
				int startCoalCount = ctx.backpack.select().id(Ore.COAL.getId()).count();
				if (coalbag.interact("Fill")) {
					MyMethods.sleep(1100, 1400);
					script().coalBagCount += startCoalCount - ctx.backpack.select().id(Ore.COAL.getId()).count();
				}
			}
		}
	}

}
