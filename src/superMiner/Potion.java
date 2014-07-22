package superMiner;

public enum Potion {

	SUPER_RESTORE_1 (3030),
	SUPER_RESTORE_2 (3028),
	SUPER_RESTORE_3 (3026),
	SUPER_RESTORE_4 (3024),
	SUMMONNG_1 (12146),
	SUMMONNG_2 (12144),
	SUMMONNG_3 (12142),
	SUMMONNG_4 (12140);

	private final int id;

	Potion(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
