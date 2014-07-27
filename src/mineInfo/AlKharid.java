package areaInfo;

import generalTasks.Drop;
import generalTasks.FillCoalBag;
import generalTasks.ManageBank;
import generalTasks.Mine;
import generalTasks.Pickup;
import generalTasks.Run;
import generalTasks.WalkToBank;
import generalTasks.WalkToMine;

import java.util.ArrayList;

import methods.MyMethods;
import myAPI.MyMagic;
import myAPI.MyWorldMethods;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Skills;

import superMiner.Area;
import superMiner.AreaInfo;
import superMiner.Ore;
import superMiner.SuperMiner;
import superMiner.Task;
import util.OreInfoListMethods;

public class AlKharid extends AreaInfo {
	
	private Tile[] tilesToBank = new Tile[] {new Tile(3300, 3313, 0), new Tile(3300, 3303, 0),
			new Tile(3300, 3293, 0),new Tile(3299, 3283, 0), new Tile(3298, 3275, 0), new Tile(3295, 3265, 0),
			new Tile(3294, 3255, 0), new Tile(3292, 3245, 0), new Tile(3287, 3236, 0), new Tile(3284, 3233, 0),
			new Tile(3279, 3224, 0), //new Tile(3290, 3184, 0), new Tile(3283, 3184, 0),
			new Tile(3281, 3214, 0), new Tile(3283, 3204, 0), new Tile(3281, 3195, 0), new Tile(3278, 3185, 0),
			new Tile(3276, 3180), new Tile(3274, 3175, 0), new Tile(3271, 3168, 0)};
	
	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return !o.tile().equals(new Tile(3292, 3299, 0));
		}
	};

	public AlKharid(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void addTasks(ArrayList<Task> tasks) {

		if ( (OreInfoListMethods.ListContains(script().oreInfoList(), Ore.GOLD)
				|| OreInfoListMethods.ListContains(script().oreInfoList(), Ore.SILVER))
				&& ctx.skills.realLevel(Skills.DUNGEONEERING) >= 75
				&& new MyWorldMethods(ctx).currentWorldIsMembers()) {
			tilesToBank = new Tile[] {new Tile(3300, 3313, 0), new Tile(3300, 3303, 0), new Tile(3300, 3293, 0),
					new Tile(3299, 3283, 0), new Tile(3298, 3275, 0), new Tile(3295, 3265, 0), 
					new Tile(3294, 3255, 0), new Tile(3292, 3245, 0), new Tile(3287, 3236, 0), 
					new Tile(3284, 3233, 0), new Tile(3279, 3224, 0),
					//new Tile(3290, 3184, 0), new Tile(3283, 3184, 0),
					new Tile(3281, 3214, 0), new Tile(3283, 3204, 0),
					new Tile(3281, 3195, 0), new Tile(3278, 3185, 0),
					new Tile(3276, 3180),
					new Tile(3274, 3175, 0), 
					new Tile(3271, 3168, 0)};
		}

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

			tasks.add(new ManageBank(ctx, tilesToBank[tilesToBank.length - 1]));
			tasks.add(new HomeTeleport(ctx, tilesToBank));
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
		return Area.ALKHARID.area().contains(ctx.players.local());
	}
	
	private static class HomeTeleport extends Task {

		private Tile[] tilesToBank;

		private MyMagic myMagic = new MyMagic(ctx);

		public HomeTeleport(ClientContext ctx, Tile[] tilesToBank) {
			super(ctx);
			this.tilesToBank = tilesToBank;
		}

		@Override
		public boolean activate() {
			return ctx.backpack.select().count() == 28
					&& ctx.players.local().animation() != SuperMiner.HOME_TELEPORT_ANIMATION_1
					&& ctx.players.local().animation() != SuperMiner.HOME_TELEPORT_ANIMATION_2
					&& ctx.players.local().animation() != SuperMiner.HOME_TELEPORT_ANIMATION_3
					&& tilesToBank[tilesToBank.length - 1].distanceTo(ctx.players.local()) > 4
					&& ctx.players.local().tile().y() > 3211;
		}

		@Override
		public void execute() {
			myMagic.homeTeleportTo(MyMagic.AL_KHARID_LOADESTONE_CHOICE);
			MyMethods.sleep(1000, 2000);
		}

	}

}
