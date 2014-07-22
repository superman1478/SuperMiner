package superMiner;

import org.powerbot.script.Tile;

public enum Area {

	ALKHARID (new org.powerbot.script.Area(new Tile(3267, 3159, 0), new Tile(3307, 3318, 0))),
	ALKHRID_DUNGEON (new org.powerbot.script.Area(new Tile(3267, 3159, 0), new Tile(3307, 3318, 0))),
	BARBARIAN_VILLAGE_EDGEVILLE (new org.powerbot.script.Area(new Tile(3075, 3395, 0), new Tile(3102, 3502, 0))),
	DONDAKAN_MINE (new org.powerbot.script.Area(new Tile(2601, 4962, 0), new Tile(2619, 4971, 0))),
	DONDAKAN_MINE_2 (new org.powerbot.script.Area(new Tile(2498, 4949, 0), new Tile(2513, 4959, 0))),
	EDGEVILLE (new org.powerbot.script.Area(new Tile(3015, 3435, 0), new Tile(3104 + 1, 3502 + 1, 0))),
	FALADOR_MINING (new org.powerbot.script.Area(new Tile(3033, 9757, 0), new Tile(3061, 9789, 0))),
	FALADOR (new org.powerbot.script.Area(new Tile(2963, 3352, 0), new Tile(3064 + 1, 3388 + 1, 0))),
	EAST_FALADOR_ABOVE_GROUND (new org.powerbot.script.Area(new Tile(3007, 3349, 0), new Tile(3063, 3379, 0))),
	FALADOR_UNDERGROUND (new org.powerbot.script.Area(new Tile(3012, 9771, 0), new Tile(3060 + 1, 9852 + 1, 0))),
	KELDAGRIM (new org.powerbot.script.Area(new Tile(2817, 10162, 0), new Tile(2831, 10174, 0))),
	LIVING_ROCK_CAVERN (new org.powerbot.script.Area(new Tile(3634, 5076, 0), new Tile(3690, 5124, 0))),
	LUMBRIDGE_SWAMP_WEST (new org.powerbot.script.Area(new Tile(3089, 3144, 0), new Tile(3157, 3252, 0))),
	RIMMINGTON (new org.powerbot.script.Area(new Tile(2958, 3212, 0), new Tile(3050, 3255, 0))),
	VARROCK_EAST (new org.powerbot.script.Area(new Tile(3251, 3358, 0), new Tile(3295, 3434, 0))),
	VARROCK_WEST (new org.powerbot.script.Area(new Tile(3170, 3361, 0), new Tile(3190, 3447, 0))),
	YANILLE (new org.powerbot.script.Area(new Tile(2602, 3086, 0), new Tile(2641, 3157, 0))),
	;

	private org.powerbot.script.Area area;

	private Area(org.powerbot.script.Area area) {
		this.area = area;
	}

	public org.powerbot.script.Area area() {
		return area;
	}

}
