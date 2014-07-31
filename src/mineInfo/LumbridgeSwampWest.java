package mineInfo;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import superMiner.Area;
import superMiner.MineInfo;

public class LumbridgeSwampWest extends MineInfo {

	private final Tile[] tilesToBank = new Tile[] {new Tile(3148, 3146, 0), new Tile(3148, 3156, 0),
			new Tile(3148, 3166, 0), new Tile(3144, 3176, 0), new Tile(3142, 3186, 0),
			new Tile(3139, 3195, 0), new Tile(3134, 3204, 0), new Tile(3127, 3212, 0),
			new Tile(3118, 3218, 0), new Tile(3112, 3227, 0), new Tile(3066, 3265, 0),
			new Tile(3106, 3233, 0), new Tile(3100, 3241, 0), new Tile(3092, 3244, 0)};

	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return true;
		}
	};

	public LumbridgeSwampWest(ClientContext ctx) {
		super(ctx);
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
		return Area.LUMBRIDGE_SWAMP_WEST.area().contains(ctx.players.local());
	}

}
