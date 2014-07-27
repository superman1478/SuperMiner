package tasks;

import methods.DebugMethods;
import methods.MyMethods;
import myAPI.MyDepositbox;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

import superMiner.Ore;
import superMiner.SuperMiner;
import superMiner.Task;

public class ManageDepositBox extends Task {

	private Tile tileByBox;
	private MyDepositbox myDepositBox;

	public ManageDepositBox(ClientContext ctx, Tile tileByBox) {
		super(ctx);
		myDepositBox = new MyDepositbox(ctx);
		this.tileByBox = tileByBox;
	}

	@Override
	public boolean activate() {
		return ctx.depositBox.opened()
				|| (ctx.backpack.select().count() == 28 && tileByBox.distanceTo(ctx.players.local()) <= 4)
				;
	}

	@Override
	public void execute() {
		
		if (ctx.depositBox.open()) {
			
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

			script().countInventoryOresAndGemsAsBanked();

			if (ctx.backpack.select().id(SuperMiner.ITEM_IDS_TO_KEEP).count() > 0) {
				myDepositBox.depositAllExcept(SuperMiner.ITEM_IDS_TO_KEEP);
				MyMethods.sleep(500, 800);
			} else {
				if (script().debug()) {
					DebugMethods.println("depositing inventory");
				}
				ctx.depositBox.depositInventory();
				MyMethods.sleep(500, 800);
			}

		} else {

			if (ctx.players.local().animation() == -1) {
				MyMethods.println("attempting to deposit box");
				if (ctx.depositBox.open()) {
					MyMethods.sleep(300, 600);
				} else {
					MyMethods.println("unable to deposit box");
				}
			}

		}

	}

	private void coalBagWithdraw() {
		if (script().debug()) {
			DebugMethods.println("manageColaBagWithdraw()");
		}
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
