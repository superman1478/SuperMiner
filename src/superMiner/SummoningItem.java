package superMiner;

public enum SummoningItem {
	
	LAVA_TITAN_POUCH (12788),
	UNKNOWN_SUMMONING_NECKLACE (25469), 
	UNKNOWN_SUMMONING_NECKLACE_2 (24747);

	private final int id;

	SummoningItem(int id) {
		this.id = id;
	}

	public int getId() { 
		return id; 
	}

}