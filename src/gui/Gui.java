package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import methods.MyMethods;

import org.powerbot.script.rt6.ClientContext;

import superMiner.Area;
import superMiner.Gem;
import superMiner.GemInfo;
import superMiner.Ore;
import superMiner.OreInfo;
import superMiner.Rock;
import superMiner.SuperMiner;
import util.PriceLookupThread;

//TODO (Maybe) Add option to select AreaInfo in GUI
@SuppressWarnings("serial")
public class Gui extends JFrame {

	private OreChoice copper = new OreChoice(Rock.COPPER, new OreInfo(Ore.COPPER));
	private OreChoice tin = new OreChoice(Rock.TIN, new OreInfo(Ore.TIN));
	private OreChoice clay = new OreChoice(Rock.CLAY, new OreInfo(Ore.CLAY));
	private OreChoice iron = new OreChoice(Rock.IRON, new OreInfo(Ore.IRON));
	private OreChoice silver = new OreChoice(Rock.SILVER, (new OreInfo(Ore.SILVER)));
	private OreChoice coal = new OreChoice(Rock.COAL, new OreInfo(Ore.COAL));
	private OreChoice gold = new OreChoice(Rock.GOLD, new OreInfo(Ore.GOLD));
	private OreChoice mithril = new OreChoice(Rock.MITHRIL, new OreInfo(Ore.MITHRIL));
	private OreChoice adamantite = new OreChoice(Rock.ADAMANTITE, new OreInfo(Ore.ADAMANTITE));
	private OreChoice granite = new OreChoice(Rock.GRANITE, new OreInfo(Ore.GRANITE_5KG),
			new OreInfo(Ore.GRANITE_2KG), new OreInfo(Ore.GRANITE_500G));
	private OreChoice essence = new OreChoice("Essence", new OreInfo(Ore.PURE_ESSENCE),
			new OreInfo(Ore.RUNE_ESSENCE));//TODO consider adding essence rock

	private JPanel checkboxPanel = new JPanel();

	private JPanel radioPanel = new JPanel();
	private ButtonGroup buttonGroup = new ButtonGroup();

	private JPanel otherPanel = new JPanel();
	private JCheckBox pickupOreCheck = new JCheckBox("Pickup Ore on Ground");

	private JRadioButton dropWhenFullRadio = new JRadioButton("Drop ore when full", true);
	private JRadioButton asapDropRadio = new JRadioButton("Drop ore ASAP");
	private JRadioButton bankOreRadio = new JRadioButton("Bank ore");

	private JCheckBox saveCheck = new JCheckBox("Save Settings");

	private JButton button1 = new JButton("Start");

	private File settingsFile;

	private SuperMiner script;

	public Gui(final ClientContext ctx) {
		script = (SuperMiner)ctx.controller.script();
		settingsFile = getSettingsFile();

		if (Area.LIVING_ROCK_CAVERN.area().contains(ctx.players.local())) {
			gold = new OreChoice(Rock.GOLD_MINERAL_DEPOSIT, new OreInfo(Ore.GOLD));
			coal = new OreChoice(Rock.COAL_MINERAL_DEPOSIT, new OreInfo(Ore.COAL));
		}

		setTitle("GUI");
		setLayout(new GridBagLayout());
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent winEvt) {
				System.out.println("GUI exited");
				setVisible(false);
				ctx.controller.stop();
			}
		});

		initComponents();
		pack();
		setLocationRelativeTo(getOwner());
	}

	private void initComponents() {

		checkboxPanel.setBorder(BorderFactory.createTitledBorder("Select Ore(s)"));
		checkboxPanel.setLayout(new GridBagLayout());

		final JCheckBox[] oreCheckboxes = {copper, tin, clay, iron, silver, essence, coal, gold,
				mithril, adamantite, granite};

		GridBagConstraints oreCheckboxConstraints = new GridBagConstraints();
		oreCheckboxConstraints.gridx = 0;
		oreCheckboxConstraints.gridy = 0;
		oreCheckboxConstraints.anchor = GridBagConstraints.LINE_START;
		oreCheckboxConstraints.insets = new Insets(-2, 0, -2, 0);
		oreCheckboxConstraints.ipady = 0;
		for (int i = 0; i < oreCheckboxes.length; i++) {
			oreCheckboxes[i].setFocusPainted(false);
			checkboxPanel.add(oreCheckboxes[i], oreCheckboxConstraints);
			oreCheckboxConstraints.gridy++;
			if (i == oreCheckboxes.length / 2) {
				oreCheckboxConstraints.gridx++;
				oreCheckboxConstraints.gridy = 0;
			}
		}

		GridBagConstraints checkboxPanelConstraints = new GridBagConstraints();

		checkboxPanelConstraints.gridy = 0;
		checkboxPanelConstraints.insets = new Insets(3, 3, 0, 3);
		checkboxPanelConstraints.fill = GridBagConstraints.BOTH;
		add(checkboxPanel, checkboxPanelConstraints);

		radioPanel.setBorder(BorderFactory.createTitledBorder("Method"));
		dropWhenFullRadio.setFocusPainted(false);
		radioPanel.add(dropWhenFullRadio);
		asapDropRadio.setFocusPainted(false);
		radioPanel.add(asapDropRadio);
		bankOreRadio.setFocusPainted(false);
		radioPanel.add(bankOreRadio);

		GridBagConstraints radioPanelConstraints = new GridBagConstraints();
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
		radioPanelConstraints.insets = new Insets(0, 3, 0, 3);
		radioPanelConstraints.gridy = 1;
		radioPanelConstraints.fill = GridBagConstraints.BOTH;
		add(radioPanel, radioPanelConstraints);

		buttonGroup.add(dropWhenFullRadio);
		buttonGroup.add(asapDropRadio);
		buttonGroup.add(bankOreRadio);

		GridBagConstraints otherPanelConstraints = new GridBagConstraints();
		otherPanel.setBorder(BorderFactory.createTitledBorder("Other"));
		otherPanel.setLayout(new BorderLayout());
		otherPanel.add(pickupOreCheck, BorderLayout.LINE_START);
		otherPanelConstraints.gridy = 2;
		otherPanelConstraints.insets = new Insets(0, 3, 0, 3);
		otherPanelConstraints.fill = GridBagConstraints.BOTH;
		add(otherPanel, otherPanelConstraints);

		GridBagConstraints saveCheckConstraints = new GridBagConstraints();
		saveCheckConstraints.gridy = 3;
		saveCheckConstraints.insets = new Insets(0, 4, 0, 0);
		saveCheckConstraints.fill = GridBagConstraints.BOTH;
		saveCheck.setFocusPainted(false);
		add(saveCheck, saveCheckConstraints);

		GridBagConstraints button1Constraints = new GridBagConstraints();
		button1Constraints.gridy = 4;
		button1Constraints.insets = new Insets(0, 5, 5, 5);
		button1Constraints.fill = GridBagConstraints.BOTH;
		button1.setFocusPainted(false);
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				MyMethods.println("button1 clicked");

				if (script.displayname() != null && saveCheck.isSelected()) {
					writeSettings();
				}

				final OreChoice[] oreChoices = {essence, granite, adamantite, mithril, gold, coal,
						clay, silver, iron, tin, copper};

				for (OreChoice oreChoice : oreChoices) {
					if (oreChoice.isSelected()) {
						if (oreChoice.getRock() != null) {
							script.rocks().add(oreChoice.getRock());
						}
						for (OreInfo oreInfo : oreChoice.getOreInfos()) {
							script.oreInfoList().add(oreInfo);
						}
					}
				}

				if (!essence.isSelected()) {
					script.gemInfos = new GemInfo[4];
					script.gemInfos[0] = new GemInfo(Gem.UNCUT_SAPPHIRE);
					script.gemInfos[1] = new GemInfo(Gem.UNCUT_EMERALD); 
					script.gemInfos[2] = new GemInfo(Gem.UNCUT_RUBY);
					script.gemInfos[3] = new GemInfo(Gem.UNCUT_DIAMOND);
				}

				if (bankOreRadio.isSelected() && pickupOreCheck.isSelected()) {
					script.pickupOreOnGround(true);
				}

				script.bankingEnabled(bankOreRadio.isSelected());
				script.dropASAP(asapDropRadio.isSelected());

				setVisible(false);

				if (script.debug()) {
					System.out.print("rocks selected:");
					for (Rock rock : script.rocks()) {
						System.out.println(rock.getName() + ", ");
					}
				}

				script.taskSetup();

				new PriceLookupThread(script).start();

				script.startTime(System.currentTimeMillis());
				dispose();
			}
		});
		add(button1, button1Constraints);

		updateSettings();

	}

	private void updateSettings() {
		if (settingsFile.exists()) {
			readSettings();
			saveCheck.setSelected(true);
		}
	}

	private void readSettings() {

		try {
			BufferedReader input = new BufferedReader(new FileReader(settingsFile));
			try {
				copper.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				tin.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				clay.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				iron.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				silver.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				coal.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				gold.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				mithril.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				adamantite.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				granite.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				pickupOreCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				bankOreRadio.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				dropWhenFullRadio.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				asapDropRadio.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				essence.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
			} finally {
				if (input != null) input.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void writeSettings() {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(settingsFile));
			try {
				output.write("copper: " + copper.isSelected() + System.lineSeparator()
						+ "tin: " + tin.isSelected() + System.lineSeparator()
						+ "clay: " + clay.isSelected() + System.lineSeparator()
						+ "iron: " + iron.isSelected() + System.lineSeparator()
						+ "silver: " + silver.isSelected() + System.lineSeparator()
						+ "coal: " + coal.isSelected() + System.lineSeparator()
						+ "gold: " + gold.isSelected() + System.lineSeparator()
						+ "mithril: " + mithril.isSelected() + System.lineSeparator()
						+ "adamantite: " + adamantite.isSelected() + System.lineSeparator()
						+ "granite: " + granite.isSelected() + System.lineSeparator()
						+ "pickupOreCheck: " + pickupOreCheck.isSelected() + System.lineSeparator()
						+ "bankOreRadio: " + bankOreRadio.isSelected() + System.lineSeparator()
						+ "dropWhenFull: " + dropWhenFullRadio.isSelected() + System.lineSeparator()
						+ "asapDrop1: " + asapDropRadio.isSelected() + System.lineSeparator()
						+ "essenceCheck: " + essence.isSelected() + System.lineSeparator()
						);
			} finally {
				if (output != null) output.close();
			}
		} catch (IOException e) {
			MyMethods.println("writeaccountinfo error");
			e.printStackTrace();
		}
	}

	private File getSettingsFile() {

		File storageDir = new File(script.getStorageDirectory().getAbsolutePath());
		storageDir.delete();

		System.out.println("storageDir.getAbsolutePath(): " + storageDir.getAbsolutePath());

		String tempDirPath = storageDir.getAbsolutePath().split("RSBot")[0];

		String stringpath = tempDirPath + "RSBot" + File.separator + script.getName();

		File dir = new File(stringpath);

		if (!dir.exists()) {
			if (!dir.mkdir()) {
				System.err.println("unable to create " + dir.getAbsolutePath());
				return null;
			}
		}

		stringpath += File.separator + script.displayname() + ".txt";

		MyMethods.println("Settings file: " + stringpath);

		return new File(stringpath);
	}

}