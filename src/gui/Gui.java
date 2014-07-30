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

	private JCheckBox copperCheck = new JCheckBox("Copper");
	private JCheckBox tinCheck = new JCheckBox("Tin");
	private JCheckBox clayCheck = new JCheckBox("Clay");
	private JCheckBox ironCheck = new JCheckBox("Iron");
	private JCheckBox silverCheck = new JCheckBox("Silver");
	private JCheckBox coalCheck = new JCheckBox("Coal");
	private JCheckBox goldCheck = new JCheckBox("Gold");
	private JCheckBox mithrilCheck = new JCheckBox("Mithril");
	private JCheckBox adamantiteCheck = new JCheckBox("Adamantite");
	private JCheckBox graniteCheck = new JCheckBox("Granite");
	private JCheckBox essenceCheck = new JCheckBox("Essence");

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
	private ClientContext ctx;

	public Gui(final ClientContext ctx) {
		this.ctx = ctx;
		script = (SuperMiner)ctx.controller.script();
		settingsFile = getSettingsFile();

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

		JCheckBox[] oreCheckboxes = {copperCheck, tinCheck, clayCheck, ironCheck, silverCheck,
				essenceCheck, coalCheck, goldCheck, mithrilCheck, adamantiteCheck, graniteCheck};

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

				if (graniteCheck.isSelected()) {
					script.rocks().add(Rock.GRANITE);
					script.oreInfoList().add(new OreInfo(Ore.GRANITE_5KG));
					script.oreInfoList().add(new OreInfo(Ore.GRANITE_2KG));
					script.oreInfoList().add(new OreInfo(Ore.GRANITE_500G));
				}

				if (adamantiteCheck.isSelected()) {
					script.rocks().add(Rock.ADAMANTITE);
					script.oreInfoList().add(new OreInfo(Ore.ADAMANTITE));
				}

				if (mithrilCheck.isSelected()) {
					script.rocks().add(Rock.MITHRIL);
					script.oreInfoList().add(new OreInfo(Ore.MITHRIL));
				}

				if (goldCheck.isSelected()) {
					if (Area.LIVING_ROCK_CAVERN.area().contains(ctx.players.local())) {
						script.rocks().add(Rock.GOLD_MINERAL_DEPOSIT);
					} else {	
						script.rocks().add(Rock.GOLD);
					}
					script.oreInfoList().add(new OreInfo(Ore.GOLD));
				}

				if (coalCheck.isSelected()) {
					if (Area.LIVING_ROCK_CAVERN.area().contains(ctx.players.local())) {
						script.rocks().add(Rock.COAL_MINERAL_DEPOSIT);
					} else {	
						script.rocks().add(Rock.COAL);
					}
					script.oreInfoList().add(new OreInfo (Ore.COAL));
				}

				if (clayCheck.isSelected()) {
					script.rocks().add(Rock.CLAY);
					script.oreInfoList().add(new OreInfo(Ore.CLAY));
				}

				if (silverCheck.isSelected()) {
					script.rocks().add(Rock.SILVER);
					script.oreInfoList().add(new OreInfo(Ore.SILVER));
				}

				if (ironCheck.isSelected()) {
					script.rocks().add(Rock.IRON);
					script.oreInfoList().add(new OreInfo(Ore.IRON));
				}

				if (tinCheck.isSelected()) {
					script.rocks().add(Rock.TIN);
					script.oreInfoList().add(new OreInfo(Ore.TIN));
				}

				if (copperCheck.isSelected()) {
					script.rocks().add(Rock.COPPER);
					script.oreInfoList().add(new OreInfo(Ore.COPPER));
				}

				if (essenceCheck.isSelected()) {
					script.oreInfoList().add(new OreInfo(Ore.PURE_ESSENCE));
					script.oreInfoList().add(new OreInfo(Ore.RUNE_ESSENCE));
				}

				if (!essenceCheck.isSelected()) {
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

		if (Area.LIVING_ROCK_CAVERN.area().contains(ctx.players.local())) {

			MyMethods.println("In Living Rock Cavern Area" );
			MyMethods.println("disabling some ore options" );

			copperCheck.setSelected(false);
			copperCheck.setEnabled(false);

			tinCheck.setSelected(false);
			tinCheck.setEnabled(false);

			clayCheck.setSelected(false);
			clayCheck.setEnabled(false);

			ironCheck.setSelected(false);
			ironCheck.setEnabled(false);

			silverCheck.setSelected(false);
			silverCheck.setEnabled(false);

			//coalCheck.setSelected(false);

			//goldCheck.setSelected(true);

			mithrilCheck.setSelected(false);
			mithrilCheck.setEnabled(false);

			adamantiteCheck.setSelected(false);
			adamantiteCheck.setEnabled(false);

			graniteCheck.setSelected(false);
			graniteCheck.setEnabled(false);

			essenceCheck.setSelected(false);
			essenceCheck.setEnabled(false);

		} else if (Area.KELDAGRIM.area().contains(ctx.players.local()) 
				|| Area.DONDAKAN_MINE.area().contains(ctx.players.local())
				|| Area.DONDAKAN_MINE_2.area().contains(ctx.players.local())) {

			MyMethods.println("In dondakan mine area Area" );
			MyMethods.println("disabling some ore options" );

			copperCheck.setSelected(false);
			copperCheck.setEnabled(false);

			tinCheck.setSelected(false);
			tinCheck.setEnabled(false);

			clayCheck.setSelected(false);
			clayCheck.setEnabled(false);

			ironCheck.setSelected(false);
			ironCheck.setEnabled(false);

			silverCheck.setSelected(false);
			silverCheck.setEnabled(false);

			coalCheck.setSelected(false);
			coalCheck.setEnabled(false);

			goldCheck.setSelected(true);
			goldCheck.setEnabled(false);

			mithrilCheck.setSelected(false);
			mithrilCheck.setEnabled(false);

			adamantiteCheck.setSelected(false);
			adamantiteCheck.setEnabled(false);

			graniteCheck.setSelected(false);
			graniteCheck.setEnabled(false);

		}

	}

	private void readSettings() {

		try {
			BufferedReader input = new BufferedReader(new FileReader(settingsFile));
			try {
				copperCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				tinCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				clayCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				ironCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				silverCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				coalCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				goldCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				mithrilCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				adamantiteCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				graniteCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				pickupOreCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				bankOreRadio.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				dropWhenFullRadio.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				asapDropRadio.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
				essenceCheck.setSelected(Boolean.parseBoolean(input.readLine().split(": ")[1]));
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
				output.write("copperCheck: " + copperCheck.isSelected() + System.lineSeparator()
						+ "tinCheck: " + tinCheck.isSelected() + System.lineSeparator()
						+ "clayCheck: " + clayCheck.isSelected() + System.lineSeparator()
						+ "ironCheck: " + ironCheck.isSelected() + System.lineSeparator()
						+ "silverCheck: " + silverCheck.isSelected() + System.lineSeparator()
						+ "coalCheck: " + coalCheck.isSelected() + System.lineSeparator()
						+ "goldCheck: " + goldCheck.isSelected() + System.lineSeparator()
						+ "mithrilCheck: " + mithrilCheck.isSelected() + System.lineSeparator()
						+ "adamantiteCheck: " + adamantiteCheck.isSelected() + System.lineSeparator()
						+ "graniteCheck: " + graniteCheck.isSelected() + System.lineSeparator()
						+ "pickupOreCheck: " + pickupOreCheck.isSelected() + System.lineSeparator()
						+ "bankOreRadio: " + bankOreRadio.isSelected() + System.lineSeparator()
						+ "dropWhenFull: " + dropWhenFullRadio.isSelected() + System.lineSeparator()
						+ "asapDrop1: " + asapDropRadio.isSelected() + System.lineSeparator()
						+ "essenceCheck: " + essenceCheck.isSelected() + System.lineSeparator()
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