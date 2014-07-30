package util;

import superMiner.GemInfo;
import superMiner.OreInfo;
import superMiner.SuperMiner;
import methods.MyMethods;
import methods.PriceLookup;

public class PriceLookupThread extends Thread {

	private SuperMiner script;

	public PriceLookupThread(SuperMiner script) {
		this.script = script;
	}

	@Override
	public void run() {

			for (OreInfo oreInfo : script.oreInfoList()) {
				oreInfo.setPrice(PriceLookup.getPriceUsingRSBotGeItem(oreInfo.getId()));
				//DebugMethods.println("pricelookupdebug");
				if (oreInfo.getPrice() == 0) {
					MyMethods.println("price lookup failed for " +  oreInfo.getName());
					MyMethods.println("trying again...");
					oreInfo.setPrice(PriceLookup.getPrice(oreInfo.getId()));
				}
			}

			for (GemInfo gem : script.gemInfos) {
				gem.setPrice(PriceLookup.getPriceUsingRSBotGeItem(gem.getId()));
				if (gem.getPrice() == 0) {
					MyMethods.println("price lookup failed for " +  gem.getName());
					MyMethods.println("trying again...");
					gem.setPrice(PriceLookup.getPrice(gem.getId()));
				}
			}

	}
}