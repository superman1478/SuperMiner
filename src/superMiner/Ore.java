package superMiner;

public enum Ore {
	
	COPPER ("Copper",436),
	TIN ("Tin", 438),
	IRON ("Iron", 440),
	CLAY ("Clay", 434),
	SILVER ("Silver", 442),
	GOLD ("Gold", 444),
	COAL ("Coal", 453),
	MITHRIL("Mithril", 447),
	ADAMANTITE ("Adamantite", 449),
	GRANITE_5KG ("Granite (5kg)", 6983),
	GRANITE_2KG ("Granite (2kg)", 6981),
	GRANITE_500G ("Granite (500g)", 6979),
	RUNE_ESSENCE ("Rune essence", 1436),
	PURE_ESSENCE ("Pure essence", 7936);

	private final int id;
	private final String name;

	private Ore(String name, int id) {
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
