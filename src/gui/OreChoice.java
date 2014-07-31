package gui;

import javax.swing.JCheckBox;

import superMiner.OreInfo;
import superMiner.Rock;

public class OreChoice extends JCheckBox {

	private Rock rock;
	private OreInfo[] oreInfos;

	public OreChoice(Rock rock, OreInfo... oreInfos) {
		super(rock.getName());
		this.rock = rock;
		this.oreInfos = oreInfos;
	}
	
	public OreChoice(String name, OreInfo... oreInfos) {
		super(name);
		this.oreInfos = oreInfos;
	}

	public Rock getRock() {
		return rock;
	}

	public OreInfo[] getOreInfos() {
		return oreInfos;
	}

}
