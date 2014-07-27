package GUI;

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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.powerbot.script.rt6.ClientContext;

import superMiner.Area;
import superMiner.Gem;
import superMiner.GemInfo;
import superMiner.Ore;
import superMiner.OreInfo;
import superMiner.Rock;
import superMiner.SuperMiner;
import methods.MyMethods;

//TODO (Maybe) Add option to select AreaInfo in GUI
//TODO use layoutManager
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

	public static final int FORM_WIDTH = 185, FORM_HEIGHT = 348;

	private File settingsFile;

	private SuperMiner script;
	private ClientContext ctx;

	public Gui(ClientContext ctx) {
		this.ctx = ctx;
		script = (SuperMiner)ctx.controller.script();
		settingsFile = getSettingsFile();
		initComponents();
	}

	private void initComponents() {

		//essenceCheck.setVisible(false);

		setTitle("GUI");
		setSize(FORM_WIDTH, FORM_HEIGHT);
		setLocationRelativeTo(getOwner());
		setResizable(false);
		setLayout(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent winEvt) {
				System.out.println("GUI exited");
				setVisible(false);
				ctx.controller.stop();
			}
		});

		checkboxPanel.setLayout(null);
		checkboxPanel.setBounds(5, 5, FORM_WIDTH - 17, 140);
		checkboxPanel.setBorder(BorderFactory.createTitledBorder("Select Ore(s)"));
		copperCheck.setBounds(5, 20, 70, 15);
		checkboxPanel.add(copperCheck);
		tinCheck.setBounds(5, 40, 70, 15);
		checkboxPanel.add(tinCheck);
		clayCheck.setBounds(5, 60, 70, 15);
		checkboxPanel.add(clayCheck);
		ironCheck.setBounds(5, 80, 70, 15);
		checkboxPanel.add(ironCheck);
		silverCheck.setBounds(5, 100, 60, 15);
		checkboxPanel.add(silverCheck);
		coalCheck.setBounds(75, 20, 60, 15);
		checkboxPanel.add(coalCheck);
		goldCheck.setBounds(75, 40, 60, 15);
		checkboxPanel.add(goldCheck);
		mithrilCheck.setBounds(75, 60, 60, 15);
		checkboxPanel.add(mithrilCheck);
		adamantiteCheck.setBounds(75, 80, 90, 15);
		checkboxPanel.add(adamantiteCheck);
		graniteCheck.setBounds(75, 100, 90, 15);
		checkboxPanel.add(graniteCheck);
		essenceCheck.setBounds(5, 120, 90, 15);
		checkboxPanel.add(essenceCheck);

		add(checkboxPanel);

		radioPanel.setLayout(null);
		radioPanel.setBounds(5, 145, FORM_WIDTH - 17, 83);
		radioPanel.setBorder(BorderFactory.createTitledBorder("Method"));
		dropWhenFullRadio.setBounds(5, 18, 120, 15);
		radioPanel.add(dropWhenFullRadio);
		asapDropRadio.setBounds(5, 38, 120, 15);
		radioPanel.add(asapDropRadio);
		bankOreRadio.setBounds(5, 58, 80, 15);
		radioPanel.add(bankOreRadio);
		add(radioPanel);

		buttonGroup.add(dropWhenFullRadio);
		buttonGroup.add(asapDropRadio);
		buttonGroup.add(bankOreRadio);

		saveCheck.setBounds(5, 270, 110, 15);
		add(saveCheck);

		button1.setBounds(5, 290, FORM_WIDTH - 17, 25);
		add(button1);

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				MyMethods.println("button1 clicked");

				if (script.displayname() != null && saveCheck.isSelected()) {
					writeSettings();
				}

				if (graniteCheck.isSelected()) {
					script.rocks().add(Rock.GRANITE);
					script.oreInfoList().add(new OreInfo(ctx, Ore.GRANITE_5KG));
					script.oreInfoList().add(new OreInfo(ctx, Ore.GRANITE_2KG));
					script.oreInfoList().add(new OreInfo(ctx, Ore.GRANITE_500G));
				}

				if (adamantiteCheck.isSelected()) {
					script.rocks().add(Rock.ADAMANTITE);
					script.oreInfoList().add(new OreInfo(ctx, Ore.ADAMANTITE));
				}

				if (mithrilCheck.isSelected()) {
					script.rocks().add(Rock.MITHRIL);
					script.oreInfoList().add(new OreInfo(ctx, Ore.MITHRIL));
				}

				if (goldCheck.isSelected()) {
					if (Area.LIVING_ROCK_CAVERN.area().contains(ctx.players.local())) {
						script.rocks().add(Rock.GOLD_MINERAL_DEPOSIT);
					} else {	
						script.rocks().add(Rock.GOLD);
					}
					script.oreInfoList().add(new OreInfo(ctx, Ore.GOLD));
				}

				if (coalCheck.isSelected()) {
					if (Area.LIVING_ROCK_CAVERN.area().contains(ctx.players.local())) {
						script.rocks().add(Rock.COAL_MINERAL_DEPOSIT);
					} else {	
						script.rocks().add(Rock.COAL);
					}
					script.oreInfoList().add(new OreInfo (ctx, Ore.COAL));
				}

				if (clayCheck.isSelected()) {
					script.rocks().add(Rock.CLAY);
					script.oreInfoList().add(new OreInfo(ctx, Ore.CLAY));
				}

				if (silverCheck.isSelected()) {
					script.rocks().add(Rock.SILVER);
					script.oreInfoList().add(new OreInfo(ctx, Ore.SILVER));
				}

				if (ironCheck.isSelected()) {
					script.rocks().add(Rock.IRON);
					script.oreInfoList().add(new OreInfo(ctx, Ore.IRON));
				}

				if (tinCheck.isSelected()) {
					script.rocks().add(Rock.TIN);
					script.oreInfoList().add(new OreInfo(ctx, Ore.TIN));
				}

				if (copperCheck.isSelected()) {
					script.rocks().add(Rock.COPPER);
					script.oreInfoList().add(new OreInfo(ctx, Ore.COPPER));
				}

				if (essenceCheck.isSelected()) {
					script.oreInfoList().add(new OreInfo(ctx, Ore.PURE_ESSENCE));
					script.oreInfoList().add(new OreInfo(ctx, Ore.RUNE_ESSENCE));
				}

				if (!essenceCheck.isSelected()) {
					script.gems = new GemInfo[4];
					script.gems[0] = new GemInfo(ctx, Gem.UNCUT_SAPPHIRE);
					script.gems[1] = new GemInfo(ctx, Gem.UNCUT_EMERALD); 
					script.gems[2] = new GemInfo(ctx, Gem.UNCUT_RUBY);
					script.gems[3] = new GemInfo(ctx, Gem.UNCUT_DIAMOND);
				}

				if (bankOreRadio.isSelected() && pickupOreCheck.isSelected()) {
					script.pickupOreOnGround(true);
				}

				script.bankingEnabled(bankOreRadio.isSelected());
				script.dropASAP(asapDropRadio.isSelected());

				setVisible(false);
			}
		});

		otherPanel.setLayout(null);
		otherPanel.setBounds(5, 225, FORM_WIDTH - 17, 43);
		otherPanel.setBorder(BorderFactory.createTitledBorder("Other"));
		pickupOreCheck.setBounds(5, 18, 130, 15);
		otherPanel.add(pickupOreCheck);
		add(otherPanel);

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
	
	public JButton getButton1() {
		return button1;
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

		//String packageName = script.getClass().getPackage().getName();
		
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