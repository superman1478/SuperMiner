package superMiner;

public enum Pickaxe {
	IRON (1267),
	STEEL (1269),
	MITHRIL (1273),
	ADAMANT (1271),
	RUNE (1275),
	DRAGON (15259),
	LENT_DRAGON (15261);

	private final int id;

	Pickaxe(int id) {
		this.id = id;
	}

	public int getId() { 
		return id; 
	}
}
