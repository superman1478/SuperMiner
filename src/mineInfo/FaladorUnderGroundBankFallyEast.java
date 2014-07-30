package mineInfo;

import java.util.ArrayList;

import methods.DebugMethods;
import methods.MyMethods;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Skills;

import superMiner.Area;
import superMiner.MineInfo;
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

public class FaladorUnderGroundBankFallyEast extends MineInfo {

	private final Tile[] tilesToBank = new Tile[] {new Tile(3043, 9770, 0), new Tile(3050, 9770, 0),
			new Tile(3058, 9778, 0), new Tile(3061, 3377, 0), new Tile(3058, 3370, 0), new Tile(3050, 3370, 0),
			new Tile(3043, 3362, 0), new Tile(3034, 3360, 0), new Tile(2987, 3344, 0), new Tile(3027, 3360, 0),
			new Tile(3017, 3360, 0), new Tile(3013, 3355, 0)};

	private final Filter<GameObject> filter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return o.tile().distanceTo(o.ctx.players.local()) < 19
					&& o.tile().y() > 9756
					&& !o.tile().equals(new Tile(3044, 9769, 0))
					&& !o.tile().equals(new Tile(3045, 9771, 0))
					;
		}
	};

	public FaladorUnderGroundBankFallyEast(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void addTasks(ArrayList<Task> tasks) {

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

			tasks.add(new FaladorAboveGroundStairs(ctx));
			tasks.add(new FaladorDoorOpener(ctx));
			tasks.add(new FaladorUndergroundStairs(ctx));
			tasks.add(new ManageBank(ctx, tilesToBank[tilesToBank.length - 1]));
			tasks.add(new WalkToBank(ctx, tilesToBank));
			tasks.add(new WalkToMine(ctx, this));
		} else {
			if (script().dropASAP()) {
				script().miningMethod("Drop ASAP");
			}
			tasks.add(0, new Drop(ctx));
		}

		tasks.add(new FaladorUnderGroundMine(ctx, this));

	}

	@Override
	public Tile[] tilesToBank() {
		return tilesToBank;
	}

	@Override
	public Filter<GameObject> rockFilter() {
		return filter;
	}

	@Override
	public boolean shouldBeUsed() {

		if (Area.EAST_FALADOR_ABOVE_GROUND.area().contains(ctx.players.local())
				&& ctx.skills.realLevel(Skills.MINING) > 59
				&& OreInfoListMethods.ListDoesntContainAnythingOtherThan(script().oreInfoList(), Ore.COAL.getId(), Ore.MITHRIL.getId())) {
			return false;//mine in mining guild instead
		}
		return Area.EAST_FALADOR_ABOVE_GROUND.area().contains(ctx.players.local())
				|| Area.FALADOR_MINING.area().contains(ctx.players.local());
	}

	private static class FaladorUnderGroundMine extends Mine {

		private final Tile ironWaitTile = new Tile(3039, 9775, 0);

		private final Tile coalWaitTile = new Tile(3045, 9769, 0);

		public FaladorUnderGroundMine(ClientContext ctx, MineInfo areaInfo) {
			super(ctx, areaInfo);
		}

		@Override
		public void execute() {

			GameObject rock = getRock();

			if (rock.inViewport()) {
				mineRock(rock);
			} else {
				if (OreInfoListMethods.ListContains(script().oreInfoList(), Ore.COAL)) {
					DebugMethods.println(script().debug(), "4585");
					if (coalWaitTile.distanceTo(ctx.players.local()) > 6) {
						MyMethods.println("walking to coalWaitTile");
						ctx.movement.step(coalWaitTile);
						MyMethods.sleep(100, 300);
					}
				} else if (OreInfoListMethods.ListContains(script().oreInfoList(), Ore.IRON)) {
					DebugMethods.println(script().debug(), "4587");
					if (ironWaitTile.distanceTo(ctx.players.local()) > 6) {
						MyMethods.println("walking to ironWaitTile");
						ctx.movement.step(ironWaitTile);
						MyMethods.sleep(100, 300);
					}
				}
			}
		}
	}

	private static class FaladorAboveGroundStairs extends Task {

		public FaladorAboveGroundStairs(ClientContext ctx) {
			super(ctx);
		}

		@Override
		public boolean activate() {
			GameObject aboveGroundStairs = ctx.objects.select().id(30944).nearest().poll();
			return aboveGroundStairs.valid()
					&& aboveGroundStairs.inViewport()
					&& aboveGroundStairs.tile().distanceTo(ctx.players.local()) < 5
					&& ctx.backpack.select().count() < 28 
					&& !ctx.players.local().inMotion();
		}

		@Override
		public void execute() {
			GameObject aboveGroundStairs = ctx.objects.select().id(30944).nearest().poll();
			if (aboveGroundStairs.valid() && aboveGroundStairs.interact("Climb")) {
				MyMethods.sleep(1200, 1500);
			}
		}

	}

	private static class FaladorDoorOpener extends Task {

		public FaladorDoorOpener(ClientContext ctx) {
			super(ctx);
		}

		@Override
		public boolean activate() {
			GameObject door = ctx.objects.select()
					//.id(11714)
					.at(new Tile(3061, 3374, 0)).poll();
			return door.inViewport()
					&& (ctx.backpack.select().count() == 28 && ctx.players.local().tile().y() > door.tile().y())
					|| (ctx.backpack.select().count() < 28 && ctx.players.local().tile().y() < door.tile().y());

		}

		@Override
		public void execute() {
			GameObject door = ctx.objects.select().id(11714).at(new Tile(3061, 3374, 0)).poll();
			if (door.valid() && door.interact("Open")) {
				MyMethods.sleep(800, 1100);
			}
		}

	}

	private static class FaladorUndergroundStairs extends Task {

		public FaladorUndergroundStairs(ClientContext ctx) {
			super(ctx);
		}

		@Override
		public boolean activate() {
			GameObject stairs = ctx.objects.select().id(30943).nearest().poll();
			return stairs.valid()
					&& stairs.inViewport()
					&& stairs.tile().distanceTo(ctx.players.local()) < 5
					&& ctx.backpack.select().count() == 28 
					&& !ctx.players.local().inMotion();
		}

		@Override
		public void execute() {
			GameObject stairs = ctx.objects.select().id(30943).nearest().poll();
			if (stairs.valid() && stairs.interact("Climb")) {
				MyMethods.sleep(1200, 1500);
			}
		}

	}

}
