package areaInfo;

import generalTasks.Drop;
import generalTasks.ManageDepositBox;
import generalTasks.Mine;
import generalTasks.Pickup;
import generalTasks.Run;
import generalTasks.WalkToBank;

import java.util.ArrayList;

import methods.BackpackMethods;
import methods.DebugMethods;
import methods.MyCalculations;
import methods.MyMethods;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Skills;
import org.powerbot.script.rt6.TilePath;

import superMiner.Area;
import superMiner.AreaInfo;
import superMiner.Ore;
import superMiner.Pickaxe;
import superMiner.Rock;
import superMiner.Task;
import util.MiningAnimationWatcher;

/**
 * TODO Add coal support
 */
public class LivingRockCaverns extends AreaInfo {

	private static final Tile[] TO_LRC_TILES = {new Tile(2965, 3380, 0), new Tile(2975, 3380, 0),
		new Tile(2984, 3375, 0), new Tile(3022, 3330, 0), new Tile(2990, 3370, 0), new Tile(2999, 3365, 0),
		new Tile(3009, 3365, 0), new Tile(3019, 3364, 0), new Tile(3028, 3358, 0), new Tile(3068, 3350, 0),
		new Tile(3028, 3358, 0), new Tile(3038, 3358, 0), new Tile(3043, 3367, 0), new Tile(3053, 3368, 0),
		new Tile(3061, 3374, 0), new Tile(3061, 3374, 0), new Tile(3085, 9801, 0), new Tile(3058, 9777, 0),
		new Tile(3058, 9777, 0), new Tile(3049, 9783, 0), new Tile(3044, 9792, 0), new Tile(3042, 9802, 0),
		new Tile(3036, 9810, 0), new Tile(3034, 9814, 0), new Tile(3039, 9823, 0), new Tile(3034, 9832, 0),
		new Tile(3024, 9833, 0), new Tile(3019, 9849, 0), new Tile(3019, 9839, 0), new Tile(3014, 9832, 0),
		new Tile(3012, 9832, 0)};

	private final static Tile[] TO_PULLEY_LIFT_TILES = {new Tile(3689, 5106, 0), new Tile(3679, 5102, 0),
		new Tile(3673, 5094, 0), new Tile(3665, 5089, 0), new Tile(3640, 5094, 0), new Tile(3650, 5094, 0),
		new Tile(3668, 5078, 0), new Tile(3664, 5088, 0), new Tile(3659, 5093, 0), new Tile(3658, 5100, 0),
		new Tile(3658, 5110, 0), new Tile(3654, 5113, 0)};

	private MiningAnimationWatcher miningAnimationWatcher;

	private Filter<GameObject> f = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return true;
		}
	};

	public LivingRockCaverns(ClientContext ctx) {
		super(ctx);

		miningAnimationWatcher = new MiningAnimationWatcher(ctx);
		miningAnimationWatcher.start();
	}

	@Override
	public void addTasks(ArrayList<Task> tasks) {

		tasks.add(new CloseKeepItemInterface(ctx));

		if (script().pickupOreOnGround()) {
			tasks.add(new Pickup(ctx));
		}

		tasks.add(new Run(ctx));

		if (script().bankingEnabled()) {
			script().miningMethod("Banking");
			MyMethods.println("Banking is enabled.");
			tasks.add(new ManageDepositBox(ctx, tilesToBank()[tilesToBank().length - 1]));
			tasks.add(new WalkToBank(ctx, tilesToBank()));
			tasks.add(new LRCWalkToGold(ctx));
		} else {
			if (script().dropASAP()) {
				script().miningMethod("Drop ASAP");
			}
			tasks.add(0, new Drop(ctx));
		}

		tasks.add(new Equip(ctx));
		tasks.add(new FaladorAboveGround(ctx));
		tasks.add(new FaladorUnderground(ctx));
		tasks.add(new Edgeville(ctx));
		tasks.add(new Retreat(ctx));

		tasks.add(new LRCMine(ctx, this));
	}

	@Override
	public Tile[] tilesToBank() {
		return TO_PULLEY_LIFT_TILES;
	}

	@Override
	public Filter<GameObject> rockFilter() {
		return f;
	}

	@Override
	public boolean shouldBeUsed() {
		return Area.LIVING_ROCK_CAVERN.area().contains(ctx.players.local());
	}
	
	private static class LRCMine extends Mine {
		
		private MiningAnimationWatcher miningAnimationWatcher;

		public LRCMine(ClientContext ctx, AreaInfo areaInfo) {
			super(ctx, areaInfo);
			miningAnimationWatcher = new MiningAnimationWatcher(ctx);
			miningAnimationWatcher.start();
		}
		
		@Override
		public boolean activate() {
			GameObject rock = getRock();
			return ctx.backpack.select().count() < 28
					&& !ctx.players.local().inMotion()
					&& rock.valid()
					&& miningAnimationWatcher.secondsSinceLastMining() > 2
					;
		}
		
	}

	private static class CloseKeepItemInterface extends Task {

		public CloseKeepItemInterface(ClientContext ctx) {
			super(ctx);
		}

		@Override
		public boolean activate() {
			//18 38 is title
			return ctx.widgets.component(18, 38).valid();
		}

		@Override
		public void execute() {
			ctx.widgets.component(18, 34).click();
		}

	}

	private static class LRCWalkToGold extends Task {
		
		private static final Tile[] TO_EAST_DEPOSIT_Tiles = {new Tile(3657, 5119, 0), 
				new Tile(3640, 5095, 0), new Tile(3650, 5095, 0), new Tile(3660, 5092, 0), 
				new Tile(3654, 5113, 0), new Tile(3657, 5103, 0), new Tile(3659, 5093, 0), 
				new Tile(3668, 5050, 0), new Tile(3660, 5090, 0), new Tile(3665, 5081, 0), 
				new Tile(3667, 5078, 0)};

		private static final Tile[] TO_WEST_DEPOSIT_Tiles = {new Tile(3657, 5119, 0), 
				new Tile(3667, 5078, 0), new Tile(3662, 5087, 0), new Tile(3654, 5094, 0), 
				new Tile(3654, 5113, 0), new Tile(3657, 5099, 0), new Tile(3651, 5091, 0), 
				new Tile(3647, 5092, 0),
				new Tile(3640, 5094, 0)};

		private static final Tile EAST_MINERAL_DEPOSIT_LOCATION = new Tile(3668, 5076, 0);
		private static final Tile WEST_MINERAL_DEPOSIT_LOCATION = new Tile(3638, 5095, 0);
		//private static final Tile GOLD_WAIT_TILE = new Tile(3653, 5086, 0);

		//private Tile currentGoldDepositLocation;
		
		private TilePath pathToEastDeposit;
		
		private TilePath pathToWestDeposit;
		
		private long eastDepositHadGoldAt, westDepositHadGoldAt;

		public LRCWalkToGold(ClientContext ctx) {
			super(ctx);
			pathToEastDeposit = new TilePath(ctx, TO_EAST_DEPOSIT_Tiles);
			pathToWestDeposit = new TilePath(ctx, TO_WEST_DEPOSIT_Tiles);
		}

		@Override
		public boolean activate() {
			GameObject deposit = ctx.objects.select().id(Rock.GOLD_MINERAL_DEPOSIT.getIds()).nearest().poll();
			return Area.LIVING_ROCK_CAVERN.area().contains(ctx.players.local())
					&& ctx.backpack.select().count() < 28
					&& (!deposit.valid() || deposit.tile().distanceTo(ctx.players.local()) > 7);
		}

		@Override
		public void execute() {
			GameObject deposit = ctx.objects.select().id(Rock.GOLD_MINERAL_DEPOSIT.getIds()).nearest().poll();
			if (deposit.valid()) {
				if (script().debug()) {
					DebugMethods.println("deposit valid");
				}
				if (deposit.tile().equals(WEST_MINERAL_DEPOSIT_LOCATION)) {
					westDepositHadGoldAt = System.currentTimeMillis();
					if (script().debug()) {
						DebugMethods.println("deposit valid pathToWestDeposit.traverse()");
					}
					pathToWestDeposit.traverse();
					MyMethods.sleep(100, 400);
				} else { //ore is east
					eastDepositHadGoldAt = System.currentTimeMillis();
					if (script().debug()) {
						DebugMethods.println("deposit valid pathToEastDeposit.traverse()");
					}
					pathToEastDeposit.traverse();
					MyMethods.sleep(100, 400);
				}
			} else {//TODO conitnue testing
				if (script().debug()) {
					DebugMethods.println("deposit not valid");
				}
				if (eastDepositHadGoldAt > westDepositHadGoldAt) { //gold was at east last, go to west
					if (WEST_MINERAL_DEPOSIT_LOCATION.distanceTo(ctx.players.local()) > 4) {
						if (script().debug()) {
							DebugMethods.println("pathToWestDeposit.traverse()");
						}
						pathToWestDeposit.traverse();
						MyMethods.sleep(100, 400);
					}
				} else {
					if (EAST_MINERAL_DEPOSIT_LOCATION.distanceTo(ctx.players.local()) > 4) {
						if (script().debug()) {
							DebugMethods.println("pathToEastDeposit.traverse()");
						}
						pathToEastDeposit.traverse();
						MyMethods.sleep(100, 400);
					}
				}
			}
		}

	}

	private static class Retreat extends Task {

		private static final int LRC_PICKAXE_ONLY_DEFENSE_ANIMATION = 18292,
				LRC_PICK_AND_SHIELD_DEFENSE_ANIMATION = 18345, 
				//LRC_NOTHING_EQUIPPED_ANIMATION = 18346;
				LRC_NOTHING_EQUIPPED_ANIMATION = 424;

		private long lastAttacked = 0;

		TilePath pathToBank;

		public Retreat(ClientContext ctx) {
			super(ctx);
			pathToBank = new TilePath(ctx, LivingRockCaverns.TO_PULLEY_LIFT_TILES);
		}

		@Override
		public boolean activate() {
			if (ctx.players.local().animation() == LRC_PICKAXE_ONLY_DEFENSE_ANIMATION
					|| ctx.players.local().animation() == LRC_PICK_AND_SHIELD_DEFENSE_ANIMATION
					|| ctx.players.local().animation() == LRC_NOTHING_EQUIPPED_ANIMATION) {
				lastAttacked = System.currentTimeMillis();
			}
			return MyCalculations.getSecondsSince(lastAttacked) < 8.0;
		}

		@Override
		public void execute() {
			if (LivingRockCaverns.TO_PULLEY_LIFT_TILES[LivingRockCaverns.TO_PULLEY_LIFT_TILES.length - 1]
					.distanceTo(ctx.players.local()) > 5) {
				if (script().debug()) {
					DebugMethods.println("pathToBank.traverse()");
				}
				pathToBank.traverse();
				MyMethods.sleep(200, 500);
			}
		}

	}

	private class Equip extends Task {

		public Equip(ClientContext ctx) {
			super(ctx);
		}

		@Override
		public boolean activate() {
			return BackpackMethods.getCountExcept(ctx, Ore.GOLD.getId()) > 0;
		}

		@Override
		public void execute() {
			for (int i = 0; i < 3; i++) {
				Item currentItem = ctx.backpack.itemAt(i);
				if (currentItem.valid() && currentItem.id() != Ore.GOLD.getId() && currentItem.id() != Ore.COAL.getId()) {
					if (currentItem.id() == Pickaxe.STEEL.getId()) {
						if (ctx.skills.realLevel(Skills.ATTACK) >= 20) {
							equipItem(currentItem);
						}
					} else if (currentItem.id() == Pickaxe.MITHRIL.getId()) {
						if (ctx.skills.realLevel(Skills.ATTACK) >= 30) {
							equipItem(currentItem);
						}
					} else if (currentItem.id() == Pickaxe.ADAMANT.getId()) {
						if (ctx.skills.realLevel(Skills.ATTACK) >= 40) {
							equipItem(currentItem);
						}
					} else if (currentItem.id() == Pickaxe.RUNE.getId()) {
						if (ctx.skills.realLevel(Skills.ATTACK) >= 50) {
							equipItem(currentItem);
						}
					} else if (currentItem.id() == Pickaxe.DRAGON.getId()) {
						if (ctx.skills.realLevel(Skills.ATTACK) >= 60) {
							equipItem(currentItem);	
						}
					} else {
						equipItem(currentItem);
					}
				}
			}
		}

		private void equipItem(Item currentItem) {
			if (currentItem.interact("Wield")) {
				MyMethods.sleep(600, 900);
			} else if (currentItem.interact("Wear")) {
				MyMethods.sleep(600, 900);
			}
		}

	}

	/**
	 * 
	 * Change path to go to whitewolf mountain
	 *
	 */
	private static class FaladorAboveGround extends Task {

		private final TilePath PathToLRC;

		public FaladorAboveGround(ClientContext ctx) {
			super(ctx);
			PathToLRC = new TilePath(ctx, TO_LRC_TILES);
		}

		@Override
		public boolean activate() {
			return Area.FALADOR.area().contains(ctx.players.local());
		}

		@Override
		public void execute() {

			GameObject door = ctx.objects.select().id(11714).at(new Tile(3061, 3374, 0)).poll(); //DoorblockingStairsToFaladorMine
			GameObject stairs = ctx.objects.select().id(30944).nearest().poll();

			if (door.valid() && door.tile().distanceTo(ctx.players.local()) < 5) {
				if (door.inViewport()) {
					door.interact("Open");
					MyMethods.sleep(600, 900);
				} else {
					ctx.camera.turnTo(door);
				}

			} else if (stairs.valid() && stairs.tile().distanceTo(ctx.players.local()) < 5) {
				if (stairs.inViewport()) {
					if (ctx.players.local().animation() == -1) {
						stairs.interact("Climb");
						MyMethods.sleep(1000, 1300);
					}
				} else {
					ctx.camera.turnTo(stairs);
					return;
				}

			} else {
				if (script().debug()) {
					DebugMethods.println("PathToLRC.traverse()");
				}
				PathToLRC.traverse();
			}

		}

	}

	private static class FaladorUnderground extends Task {

		private final TilePath PathToLRC;

		public FaladorUnderground(ClientContext ctx) {
			super(ctx);
			PathToLRC = new TilePath(ctx, TO_LRC_TILES);
		}

		@Override
		public boolean activate() {
			return Area.FALADOR_UNDERGROUND.area().contains(ctx.players.local());
		}

		@Override
		public void execute() {

			if (script().debug()) {
				DebugMethods.println("managefaladorUnderGroundArea()");
			}

			GameObject rope = ctx.objects.select().id(45077).nearest().poll();
			if (rope.valid() && rope.tile().distanceTo(ctx.players.local()) < 5) {
				MyMethods.println("rope is less than 5 distance");
				if (rope.inViewport()) {
					DebugMethods.println("rope is on screen");
					Component c = ctx.widgets.component(1262, 18);
					if (c.text().contains("Proceed regardless")) {
						c.click();
						MyMethods.sleep(1400, 1700);
					} else {
						if (!ctx.players.local().inMotion()) {
							if (rope.interact("Climb")) {
								MyMethods.sleep(600, 900);
							}
						}
					}
				} else {
					ctx.camera.turnTo(rope);
				}
			} else {
				if (script().debug()) {
					DebugMethods.println("PathToLRC.traverse()");
				}
				PathToLRC.traverse();
			}

		}

	}

	private static class Edgeville extends Task {

		private static final Tile[] TO_DWARVEN_LADDER_FROM_EDGEVILLE = {new Tile(3103, 3502, 0), 
			new Tile(3103, 3489, 0), new Tile(3093, 3487, 0),new Tile(3083, 3487, 0), new Tile(3073, 3484, 0),
			new Tile(3069, 3474, 0), new Tile(3062, 3466, 0), new Tile(3062, 3466, 0), new Tile(3059, 3456, 0),
			new Tile(3049, 3453, 0), new Tile(3039, 3450, 0), new Tile(3031, 3444, 0), new Tile(2987, 3413, 0),
			new Tile(3027, 3437, 0), new Tile(3017, 3436, 0), new Tile(3016, 3446, 0), new Tile(3018, 3450, 0)};

		private TilePath pathToDwarvenLadderFromEdge;

		public Edgeville(ClientContext ctx) {
			super(ctx);
			pathToDwarvenLadderFromEdge = new TilePath(ctx, TO_DWARVEN_LADDER_FROM_EDGEVILLE);
		}

		@Override
		public boolean activate() {
			return Area.EDGEVILLE.area().contains(ctx.players.local());
		}

		@Override
		public void execute() {

			GameObject ladder = ctx.objects.select().id(30942).nearest().poll();
			if (ladder.valid() && ladder.inViewport()) {
				if (ctx.players.local().animation() == -1 
						&& ladder.interact("Climb-down")) {
					MyMethods.sleep(600, 900);
				}
			} else {
				if (script().debug()) {
					DebugMethods.println("pathToDwarvenLadderFromEdge.traverse()");
				}
				pathToDwarvenLadderFromEdge.traverse();
			}
		}

	}

}
