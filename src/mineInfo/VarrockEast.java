package mineInfo;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import superMiner.Area;
import superMiner.MineInfo;

public class VarrockEast extends MineInfo {
	
	private final Tile[] tilesToBank = new Tile[] {new Tile(3285, 3362, 0), new Tile(3287, 3372, 0),
			new Tile(3293, 3378, 0), new Tile(3292, 3383, 0), new Tile(3292, 3387, 0),
			new Tile(3290, 3392, 0), new Tile(3290, 3399, 0), new Tile(3290, 3405, 0),
			new Tile(3287, 3415, 0), new Tile(3285, 3425, 0), new Tile(3275, 3427, 0),
			new Tile(3269, 3428, 0), new Tile(3265, 3428, 0), new Tile(3219, 3444, 0),
			new Tile(3259, 3428, 0), new Tile(3254, 3420, 0)};

	private final Filter<GameObject> rockFilter = new Filter<GameObject>() {
		@Override
		public boolean accept(GameObject o) {
			return !o.tile().equals(new Tile(3285, 3368, 0));
		}
	};

	public VarrockEast(ClientContext ctx) {
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
		return Area.VARROCK_EAST.area().contains(ctx.players.local());
	}

}
