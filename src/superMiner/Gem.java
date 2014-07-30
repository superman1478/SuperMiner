package superMiner;

public enum Gem {

	UNCUT_SAPPHIRE ("Uncut Sapphire", 1623),
	UNCUT_EMERALD ("Uncut Emerald", 1621),
	UNCUT_RUBY ("Uncut Ruby", 1619),
	UNCUT_DIAMOND ("Uncut Diamond", 1617);

	private final String name;
	private final int id;

	Gem(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name; 
	}

	public int getId() { 
		return id; 
	}

}
