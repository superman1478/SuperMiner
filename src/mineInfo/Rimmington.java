package mineInfo;

import java.util.ArrayList;

import methods.ArrayMethods;
import methods.DebugMethods;
import methods.MyMethods;
import myAPI.MyMagic;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import superMiner.Area;
import superMiner.MineInfo;
import superMiner.Rock;
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

public class Rimmington extends MineInfo {

	private final Tile[] lodestoneTilesToDepositBox = new Tile[] {
			new Tile(2975, 3237, 0), new Tile(2971, 3235, 0),
			new Tile(2969, 3230, 0), new Tile(2971, 3225, 0),
			new Tile(2976, 3224, 0), new Tile(2981, 3222, 0),
			new Tile(2986, 3220, 0), new Tile(2991, 3219, 0),
			new Tile(2996, 3218, 0), new Tile(3001, 3217, 0),
			new Tile(3006, 3217, 0), new Tile(3010, 3214, 0),
			new Tile(3015, 3213, 0), new Tile(3016, 3216, 0),
			new Tile(3021, 3217, 0), new Tile(3025, 3220, 0),
			new Tile(3028, 3224, 0), new Tile(3028, 3229, 0),
			new Tile(3029, 3234, 0), new Tile(3034, 3236, 0),
			new Tile(3039, 3236, 0), new Tile(3044, 3237, 0)
	};

	private final Tile[] directTilesToDepositBox = new Tile[] {new Tile(2970, 3237, 0), new Tile(2972, 3247, 0),
			new Tile(2982, 3251, 0), new Tile(2992, 3249, 0), new Tile(3002, 3246, 0), new Tile(3012, 3243, 0),
			new Tile(3022, 3241, 0), new Tile(3068, 3230, 0), new Tile(3028, 3238, 0), new Tile(3038, 3236, 0),
			new Tile(3048, 3236, 0), new Tile(3047, 3236, 0),
	};

	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return !o.tile().equals(new Tile(2981, 3234, 0))
					&& !o.tile().equals(new Tile(2980, 3233, 0))
					&& !o.tile().equals(new Tile(2971, 3237, 0))
					;
		}
	};

	public Rimmington(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void addTasks(ArrayList<Task> tasks) {

		tasks.add(new RimmingtonScbscribeWindowCloser(ctx));

		if (ctx.backpack.select().id(SuperMiner.COALBAG_ID).count() > 0) {
			tasks.add(new FillCoalBag(ctx, lodestoneTilesToDepositBox));
		}

		if (script().pickupOreOnGround()) {
			tasks.add(new Pickup(ctx));
		}

		tasks.add(new Run(ctx));

		if (script().bankingEnabled()) {
			script().miningMethod("Banking");
			MyMethods.println("Banking is enabled.");

			tasks.add(new ManageBank(ctx, lodestoneTilesToDepositBox[lodestoneTilesToDepositBox.length - 1]));
			tasks.add(new PortSarimRecall(ctx, lodestoneTilesToDepositBox));
			tasks.add(new RimmingtonWalkToBank(ctx, lodestoneTilesToDepositBox));
			tasks.add(new WalkToMine(ctx, directTilesToDepositBox));
		} else {
			if (script().dropASAP()) {
				script().miningMethod("Drop ASAP");
			}
			tasks.add(0, new Drop(ctx));
		}

		tasks.add(new RimmingtonMine(ctx, this));

	}

	@Override
	public Tile[] tilesToBank() {
		return lodestoneTilesToDepositBox;
	}

	@Override
	public Filter<GameObject> rockFilter() {
		return rockFilter;
	}

	@Override
	public boolean shouldBeUsed() {
		return Area.RIMMINGTON.area().contains(ctx.players.local());
	}

	private static class PortSarimRecall extends Task {

		private Tile[] tilesToBank;

		private MyMagic myMagic = new MyMagic(ctx);

		public PortSarimRecall(ClientContext ctx, Tile[] tilesToBank) {
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
					&& ctx.players.local().tile().x() < 3001;
		}

		@Override
		public void execute() {
			myMagic.homeTeleportTo(MyMagic.PORT_SARIM_LOADESTONE_CHOICE);
			MyMethods.sleep(1000, 2000);
		}

	}

	/**
	 * widget is often opened when missclicking the depositbox
	 */
	private static class RimmingtonScbscribeWindowCloser extends Task {

		public RimmingtonScbscribeWindowCloser(ClientContext ctx) {
			super(ctx);
		}

		@Override
		public boolean activate() {
			return ctx.widgets.component(1401, 30).component(14).valid();
		}

		@Override
		public void execute() {
			ctx.widgets.component(1401, 31).component(1).click();
			MyMethods.sleep(300, 500);
		}

	}

	private static class RimmingtonWalkToBank extends WalkToBank {

		public RimmingtonWalkToBank(ClientContext ctx, Tile[] tilesToBank) {
			super(ctx, tilesToBank);
		}

		@Override
		public boolean activate() {
			return ctx.players.local().animation() != SuperMiner.HOME_TELEPORT_ANIMATION_1
					&& ctx.players.local().animation() != SuperMiner.HOME_TELEPORT_ANIMATION_2
					&& ctx.players.local().animation() != SuperMiner.HOME_TELEPORT_ANIMATION_3
					&& super.activate();
		}

	}

	private static class RimmingtonMine extends Mine {

		public RimmingtonMine(ClientContext ctx, MineInfo areaInfo) {
			super(ctx, areaInfo);
		}

		@Override
		public void execute() {

			GameObject rock = getRock();

			if (rock.valid()) {
				if (!rock.inViewport()) {

					if (rock.tile().distanceTo(ctx.players.local()) < 8) {
						DebugMethods.println(script().debug(), "turning camera to rock");
						ctx.camera.turnTo(rock);
					}

					if (!rock.inViewport()) {
						DebugMethods.println(script().debug(), "walking to rock");
						if (ArrayMethods.arrayContainsInt(Rock.IRON.getIds(), rock.id())) {
							int newTileX = rock.tile().x();
							int newTileY = rock.tile().y();
							if (rock.tile().x() < 2967) {
								newTileX = 2967;
							}
							if (rock.tile().y() > 3239) {
								newTileY = 3239;
							}
							MyMethods.println("correcting y coordinate to keep player in mine");
							ctx.movement.step(new Tile(newTileX, newTileY, 0));	
						} else {	
							ctx.movement.step(rock.tile());
						}
						MyMethods.sleep(100, 300);
						return;
					}
				}

				DebugMethods.println(script().debug(), "interact with rock");
				if (!rock.name().isEmpty() && rock.interact("Mine", rock.name())) {
					MyMethods.sleep(1100, 1400);
				}

			}

		}

	}

}
