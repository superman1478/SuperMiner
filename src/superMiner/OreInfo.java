package superMiner;

public class OreInfo {
	private Ore ore;
	private int count = 0;
	private int price;

	public OreInfo(Ore ore) {
		this.ore = ore;
	}

	public int getCount() {
		return count;
	}
	
	public void incrementCount() {
		count++;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public String getName() {
		return ore.getName();
	}

	public int getId() {
		return ore.getId();
	}

}
