package superMiner;

import org.powerbot.script.rt6.ClientContext;

public class OreInfo {
	private Ore ore;
	private int inventoryCount = 0;
	private int bankCount = 0;
	private int price;
	
	private ClientContext ctx;

	public OreInfo(ClientContext ctx, Ore ore) {
		this.ctx = ctx;
		this.ore = ore;
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
		inventoryCount = ctx.backpack.select().id(ore.getId()).count();
	}

	public int getCount() {
		return inventoryCount + bankCount;
	}

	public void increaseBankCountBy(int amount) {
		bankCount += amount;
	}

	public String getName() {
		return ore.getName();
	}

	public int getId() {
		return ore.getId();
	}
	
	public Ore getOre() {
		return ore;
	}

}
