package mineInfo;

import java.util.ArrayList;

import methods.MyMethods;

import org.powerbot.script.Area;
import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Skills;

import superMiner.MineInfo;
import superMiner.Ore;
import superMiner.Task;
import tasks.Drop;
import tasks.ManageBank;
import tasks.Mine;
import tasks.Pickup;
import tasks.Run;
import tasks.WalkToBank;
import tasks.WalkToMine;
import util.OreInfoListMethods;

public class MiningGuild extends MineInfo {

	private final Tile[] toFaladorEastTiles = {new Tile(3047, 9750, 0), new Tile(3046, 9740, 0), new Tile(3036, 9736, 0),
			new Tile(3026, 9737, 0), new Tile(3021, 9739, 0), new Tile(3021, 3339, 0), new Tile(3030, 3337, 0),
			new Tile(3030, 3344, 0),new Tile(3023, 3352, 0), new Tile(3022, 3357, 0), new Tile(3017, 3360, 0),
			new Tile(3013, 3359, 0), new Tile(3013, 3355, 0)};

	//private final int MYSTERIOUS_ENTRANCE_ID = 52855;

	//private final int DEPOSITBOX_ID = 25937;

	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return o.tile().y() < 9756;
		}
	};

	public MiningGuild(ClientContext ctx) {
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

			tasks.add(new ManageBank(ctx, toFaladorEastTiles[toFaladorEastTiles.length - 1]));
			tasks.add(new ClimbUpLadder(ctx, toFaladorEastTiles));
			tasks.add(new WalkToBank(ctx, toFaladorEastTiles));
			tasks.add(new ClimbDownLadder(ctx, toFaladorEastTiles));
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
		return toFaladorEastTiles;
	}

	@Override
	public Filter<GameObject> rockFilter() {
		return rockFilter;
	}

	@Override
	public boolean shouldBeUsed() {
		final Area MINING_GUILD_AREA = new Area(new Tile(3017, 9732, 0), new Tile(3056, 9757, 0));
		final Area AROUND_FALADOR_EAST_BANK_AREA = new Area(new Tile(3007, 3336, 0), new Tile(3032, 3364, 0));
		return ctx.skills.realLevel(Skills.MINING) > 59 
				&& OreInfoListMethods.ListDoesntContainAnythingOtherThan(script().oreInfoList(), Ore.COAL.getId(), Ore.MITHRIL.getId())
				&& (MINING_GUILD_AREA.contains(ctx.players.local()) || AROUND_FALADOR_EAST_BANK_AREA.contains(ctx.players.local())
						);
	}

	private static class ClimbDownLadder extends Task {

		private Tile[] tileArray;
		
		private final int ABOVE_GROUND_LADDER_ID = 2113;

		public ClimbDownLadder(ClientContext ctx, Tile[] tileArray) {
			super(ctx);
			this.tileArray = tileArray;
		}

		@Override
		public boolean activate() {
			GameObject upperLadder = ctx.objects.select().id(ABOVE_GROUND_LADDER_ID).nearest().poll();
			return upperLadder.inViewport() 
					&& upperLadder.tile().distanceTo(ctx.players.local()) < 4
					&& ctx.backpack.select().count() != 28
					&& !tileArray[0].matrix(ctx).onMap();
		}

		@Override
		public void execute() {
			GameObject upperLadder = ctx.objects.select().id(ABOVE_GROUND_LADDER_ID).nearest().poll();
			if (upperLadder.valid() && upperLadder.tile().distanceTo(ctx.players.local()) < 4) {
				if (upperLadder.interact("Climb")) {
					MyMethods.sleep(800, 1100);
				}
			}
		}

	}

	private static class ClimbUpLadder extends Task {

		private Tile[] tileArray;

		private final int UNDERGROUND_LADDER_ID = 6226;

		public ClimbUpLadder(ClientContext ctx, Tile[] tileArray) {
			super(ctx);
			this.tileArray = tileArray;
		}

		@Override
		public boolean activate() {
			GameObject lowerLadder = ctx.objects.select().id(UNDERGROUND_LADDER_ID).nearest().poll();
			return ctx.backpack.select().count() == 28
					&& tileArray[tileArray.length - 1].distanceTo(ctx.players.local()) > 4
					&& lowerLadder.valid() && lowerLadder.tile().distanceTo(ctx.players.local()) < 4;
		}

		@Override
		public void execute() {
			GameObject lowerLadder = ctx.objects.select().id(UNDERGROUND_LADDER_ID).nearest().poll();
			if (lowerLadder.valid() && lowerLadder.tile().distanceTo(ctx.players.local()) < 4) {
				if (lowerLadder.interact("Climb")) {
					MyMethods.sleep(800, 1100);
				}
			}
		}
	}

}
