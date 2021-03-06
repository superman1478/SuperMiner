package tasks;

import methods.DebugMethods;
import methods.MyMethods;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GroundItem;

import superMiner.OreInfo;
import superMiner.Task;

public class Pickup extends Task {

	public Pickup(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return !ctx.bank.opened() && !ctx.depositBox.open() && ctx.backpack.select().count() != 28;
	}

	@Override
	public void execute() {
		checkForAndPickupOre();
	}

	//return true if there is an item to pickup
	private boolean pickupOre(int oreId) {

		DebugMethods.println(script().debug(), "pickupOre(" + oreId + ")");

		//GroundItem groundOre = getNearestGroundItem(oreID);
		GroundItem groundOre = ctx.groundItems.select().id(oreId).nearest().poll();

		if (groundOre.valid() && groundOre.tile().distanceTo(ctx.players.local()) < 10 
				//&& groundOre.tile().canReach()
				) {

			if (groundOre.inViewport()) {
				//if (MyMethods.interact(ctx, groundOre, "Take", groundOre.name())) {
				if (groundOre.interact("Take", groundOre.name())) {
					//MyMethods.sleep(100, 300);
				}
			} else {
				ctx.movement.step(groundOre);
				MyMethods.sleep(200, 500);
			} 

			return true;
		} else {
			DebugMethods.println(script().debug(), "groundOre is null or is unreachable");
		}
		return false;
	}

	/**
	 * 
	 * @return return true if there was an item to pickup
	 */
	private boolean checkForAndPickupOre() {

		DebugMethods.println(script().debug(), "manageOrePickup()");

		for (OreInfo ore : script().oreInfoList()) {
			if (pickupOre(ore.getId())) {
				DebugMethods.println(script().debug(), ore.getName() + " ore found on ground");
				return true;
			}
		}

		return false;
	}

}
