package generalTasks;

import methods.ArrayMethods;
import methods.DebugMethods;
import methods.MyMethods;
import myAPI.MyInventory;

import org.powerbot.script.rt6.Action;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Hud;
import org.powerbot.script.rt6.Item;

import superMiner.SuperMiner;
import superMiner.Task;
import util.OreInfoListMethods;

public class Drop extends Task {

	private boolean dropping = false;

	private MyInventory myInventory;
	
	private int[] oresIds;

	public Drop(ClientContext ctx) {
		super(ctx);
		myInventory = new MyInventory(ctx);
		oresIds = OreInfoListMethods.oreInfoListToIdIntArray(script().oreInfoList());
	}

	@Override
	public boolean activate() {
		return dropping
				|| ctx.combatBar.expanded()
				|| ctx.backpack.select().count() == 28
				|| (script().dropASAP() && myInventory.getCountExcept(SuperMiner.ITEM_IDS_TO_KEEP) != 0);
	}

	@Override
	public void execute() {

		if (ctx.backpack.select().id(SuperMiner.UNCUT_STONE_IDS).count() == 0
				&& ctx.backpack.select().id(oresIds).count() == 0) {
			if (ctx.combatBar.expanded(false)) {
				MyMethods.sleep(100, 300);
			}
			dropping = false;
			return;
		} else {
			dropping = true;
		}

		if (ctx.chat.queryContinue()) {
			if (ctx.chat.clickContinue()) {
				MyMethods.sleep(100, 300);
			}
		}

		destroystrangerocks();
		if (gameModeIsEOC()) {
			dropAllUsingActionBar(SuperMiner.UNCUT_STONE_IDS);
			dropAllUsingActionBar(oresIds);
		} else {
			dropAllUsingMouse(SuperMiner.UNCUT_STONE_IDS);
			dropAllUsingMouse(oresIds);
		}
	}

	private boolean gameModeIsEOC() {
		return ctx.varpbits.varpbit(4332) == 0;
	}

	private void destroystrangerocks() {

		if (script().debug()) {
			MyMethods.println("destroystrangerocks()");
		}

		for (int i = 0; ctx.backpack.select().id(SuperMiner.strangerockIDs).count() > 0 && i < 50; i++) {
			if (ctx.widgets.component(1183, 17).valid()) {
				if (script().debug()) {
					DebugMethods.println("clickwidget 1183, 17");
				}
				ctx.widgets.component(1183, 17).click();
				MyMethods.sleep(700, 1000);
			} else {
				MyMethods.println("destroy strange rock");
				Item strangeRock = ctx.backpack.select().id(SuperMiner.strangerockIDs).poll();
				if (strangeRock.valid()) {
					if (strangeRock.interact("Destroy")) {
						MyMethods.sleep(1000, 1300);
					}
				}
			}
		}
	}

	public void dropAllUsingMouse(int... itemIDs) {

		if (script().debug()) {
			MyMethods.println("dropAllUsingMouse(int... itemIDs) ");
		}

		for (int i = 0; !ctx.hud.opened(Hud.Window.BACKPACK) && i < 10; i++) {
			ctx.hud.open(Hud.Window.BACKPACK);
			MyMethods.sleep(800, 1100);
		}

		if (!ctx.hud.opened(Hud.Window.BACKPACK)) {
			System.out.println("unable to open backback");
			return;
		}

		for (int i = 0; ctx.backpack.select().id(itemIDs).count() > 0 && i < 10; i++) {
			if (script().debug()) {
				DebugMethods.println("debug43943");
			}
			for (int j = 0; j < 28; j++) {
				if (ctx.controller.isSuspended() || ctx.controller.isStopping()) {
					if (script().debug()) {
						DebugMethods.println("canceling drop");
					}
					return;
				}
				Item item = ctx.backpack.itemAt(j);
				if (ArrayMethods.arrayContainsInt(itemIDs, item.id())) {
					MyMethods.println("Dropping " + item.name());
					if (item.interact("Drop")) {
						MyMethods.sleep(150, 250);
					}
				}
			}
			MyMethods.sleep(400, 700);
		}
	}

	private void dropAllUsingActionBar(int... itemIDs) {

		if (script().debug()) {
			MyMethods.println("dropAllUsingActionBar(int... itemIDs) ");
		}

		for (int i = 0;!ctx.combatBar.expanded() && i < 10; i++) {
			ctx.combatBar.expanded(true);
			MyMethods.sleep(300, 600);
		}

		if (!ctx.combatBar.expanded()) {
			System.out.println("unable to expand combatbar");
			return;
		}

		for (int i = 0; ctx.widgets.component(1186, 2).text().contains("Your inventory is too") && i < 10; i++) {
			MyMethods.println("Pressing spacebar");
			ctx.input.send(" ");
			MyMethods.sleep(300, 600);
		}

		for (int itemIDIndex = 0; itemIDIndex < itemIDs.length; itemIDIndex++) {
			Action currentAction = ctx.combatBar.select().id(itemIDs[itemIDIndex]).poll();
			if (!currentAction.valid()) {
				if (script().debug()) {
					DebugMethods.println("action not set in actionbar");
				}
				dropAllUsingMouse(itemIDs[itemIDIndex]);
			} else {
				for (int i = 0; i < 55 && ctx.backpack.select().id(itemIDs[itemIDIndex]).count() > 0; i++) {
					if (script().debug()) {
						DebugMethods.println("selecting action " + currentAction.bind());
					}
					if (ctx.controller.isSuspended() || ctx.controller.isStopping()) {
						if (script().debug()) {
							DebugMethods.println("canceling drop");
						}
						return;
					}
					currentAction.select();
					MyMethods.sleep(150, 250);
				}
			}
		}
		if (script().debug()) {
			DebugMethods.println("finished dropping");
		}
	}
	
}
