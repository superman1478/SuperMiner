package superMiner;

import org.powerbot.script.rt6.ClientContext;

public class GemInfo {

	private String name;
	private int id;
	private int inventoryCount = 0;
	private int bankCount = 0;
	private int price;
	
	private ClientContext ctx;

	public GemInfo(ClientContext ctx, Gem gem) {
		this.ctx = ctx;
		name = gem.getName();
		id = gem.getId();
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public int getBankCount() {
		return bankCount;
	}
	
	public void updateInventoryCount() {
		inventoryCount = ctx.backpack.select().id(id).count();
	}

	public int getCount() {
		return inventoryCount + bankCount;
	}

	public void increaseBankCountBy(int amount) {
		bankCount += amount;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

}
