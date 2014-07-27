package tasks;

import methods.DebugMethods;
import methods.MyCalculations;
import methods.MyMethods;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import superMiner.AreaInfo;
import superMiner.Rock;
import superMiner.Task;

public class Mine extends Task {

	private AreaInfo areaInfo;

	public Mine(ClientContext ctx, AreaInfo areaInfo) {
		super(ctx);
		this.areaInfo = areaInfo;
	}

	@Override
	public boolean activate() {
		GameObject rock = getRock();
		return ctx.backpack.select().count() < 28
				&& !ctx.players.local().inMotion()
				&& rock.valid()
				&& !miningThisRock(rock)
				;
	}

	@Override
	public void execute() {
		mineRock(getRock());
	}

	private boolean miningThisRock(GameObject rock) {
		if (ctx.players.local().animation() != -1
				&& rock.valid()
				&& rock.tile().distanceTo(ctx.players.local()) == 1 
				&& MyCalculations.playerIsFacing(ctx.players.local(), rock)) {
			return true;
		}

		return false;
	}

	public boolean mineRock(GameObject rock) {

		if (script().debug()) {
			DebugMethods.println("mineRock(GameObject rock)");
			if (rock.valid()) {
				DebugMethods.println("rock != null");
				DebugMethods.println("rock.name(): " + rock.name());
			} else {
				DebugMethods.println("rock == null");
			}
		}

		/*if (rock.tile().distanceTo(ctx.players.local()) > 18) {
			return;
		}*/

		if (rock.valid()) {
			if (!rock.inViewport()) {

				if (rock.tile().distanceTo(ctx.players.local()) < 7) {
					if (script().debug()) {
						DebugMethods.println("turning camera to rock");
					}
					ctx.camera.turnTo(rock);
				}

				if (!rock.inViewport()) {
					if (script().debug()) {
						DebugMethods.println("walking to rock");
					}
					ctx.movement.step(rock);
					MyMethods.sleep(100, 300);
					return false;
				}	
			}

			if (script().debug()) {
				DebugMethods.println("doing interact: \"" + "Mine " + rock.name() + "\"");
			}
			if (!rock.name().isEmpty()
					//&& MyMethods.interact(ctx, rock, "Mine", rock.name())) {
					&& rock.interact("Mine", rock.name())) {
				MyMethods.sleep(1100, 1400);
				return true;
			}

		}
		return false;
	}

	/**
	 * 
	 * @return The closest selected rock that is on the map and the areaInfo rock filter allows. 
	 * If the player is next to more than one valid rock, priority will be given to the rock the player is facing.
	 */
	public GameObject getRock() {

		//loop through the user's selected rocks
		for (Rock rock : script().rocks()) {

			//create a query, loop though and return rock if my player is facing it
			for (GameObject gameObject : ctx.objects.select().id(rock.getIds()).select(areaInfo.rockFilter()).nearest()) {

				//break if rock is too far for player to be facing it
				if (gameObject.tile().distanceTo(ctx.players.local()) > 1) {
					break;
				}

				if (MyCalculations.playerIsFacing(ctx.players.local(), gameObject)) {
					return gameObject;
				}

			}

			//return rock if it is on the map
			if (ctx.objects.peek().tile().matrix(ctx).onMap()) {
				return ctx.objects.poll();
			}
		}

		return ctx.objects.nil();

	}

}
