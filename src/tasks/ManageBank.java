package tasks;

import methods.BankMethods;
import methods.DebugMethods;
import methods.DepositBoxMethods;
import methods.MyMethods;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

import superMiner.Ore;
import superMiner.SuperMiner;
import superMiner.Task;

public class ManageBank extends Task {

	private Tile tileByBooth;

	public ManageBank(ClientContext ctx, Tile tileByBooth) {
		super(ctx);
		this.tileByBooth = tileByBooth;
	}

	@Override
	public boolean activate() {
		return ctx.bank.opened()
				|| ctx.depositBox.opened()
				|| (ctx.backpack.select().count() == 28 && tileByBooth.distanceTo(ctx.players.local()) <= 4)
				;
	}

	@Override
	public void execute() {

		if (ctx.bank.opened()) {
			if (ctx.backpack.select().id(SuperMiner.COALBAG_ID).count() > 0
					&& script().coalBagCount > 0
					&& ctx.backpack.select().count() != 28) {
				coalBagWithdraw();
			}

			if (ctx.backpack.select().count() < 28) {
				ctx.bank.close();
				MyMethods.sleep(100, 300);
				return;
			}

			if (ctx.backpack.select().id(SuperMiner.ITEM_IDS_TO_KEEP).count() > 0) {
				BankMethods.depositAllExcept(ctx, SuperMiner.ITEM_IDS_TO_KEEP);
				MyMethods.sleep(500, 800);
			} else {
				ctx.bank.depositInventory();
				MyMethods.sleep(500, 800);
			}

		} else if (ctx.depositBox.open()) {
			if (ctx.backpack.select().id(SuperMiner.COALBAG_ID).count() > 0
					&& script().coalBagCount > 0
					&& ctx.backpack.select().count() != 28) {
				coalBagWithdraw();
			}

			if (ctx.backpack.select().count() < 28) {
				ctx.depositBox.close();
				MyMethods.sleep(100, 300);
				return;
			}

			if (ctx.backpack.select().id(SuperMiner.ITEM_IDS_TO_KEEP).count() > 0) {
				DepositBoxMethods.depositAllExcept(ctx, SuperMiner.ITEM_IDS_TO_KEEP);
				MyMethods.sleep(500, 800);
			} else {
				DebugMethods.println(script().debug(), "depositing inventory");
				ctx.depositBox.depositInventory();
				MyMethods.sleep(500, 800);
			}
		} else {

			if (ctx.players.local().animation() == -1) {
				DebugMethods.println(script().debug(), "attempting to open bank");
				if (ctx.bank.nearest().tile().matrix(ctx).onMap()) {
					if (ctx.bank.open()) {
						MyMethods.sleep(300, 600);
					} else {
						MyMethods.println("failed to open bank");
					}
				} else if (ctx.depositBox.nearest().tile().matrix(ctx).onMap()) {
					if (ctx.depositBox.open()) {
						MyMethods.sleep(300, 600);
					} else {
						MyMethods.println("failed to depositBox");
					}
				}
			}

		}

	}
	
	private void coalBagWithdraw() {
		DebugMethods.println(script().debug(), "manageColaBagWithdraw()");
		Item coalbag = ctx.backpack.select().id(SuperMiner.COALBAG_ID).poll();
		if (coalbag.valid()) {
			if (coalbag.interact("Withdraw-many")) {
				MyMethods.sleep(700, 1000);
			}
			if (ctx.backpack.select().id(Ore.COAL.getId()).count() > 0) {
				script().coalBagCount = 0;
			}
		}
	}

}
