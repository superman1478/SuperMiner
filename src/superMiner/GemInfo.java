package superMiner;

/**
 * This class is used to keep track of information about a gem.
 *
 */
public class GemInfo {

	private Gem gem;
	private int count = 0;
	private int price;

	public GemInfo(Gem gem) {
		this.gem = gem;
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
		return gem.getName();
	}

	public int getId() {
		return gem.getId();
	}

}
