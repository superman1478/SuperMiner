package superMiner;

import gui.Gui;
import gui.ShowMoreGui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import methods.DebugMethods;
import methods.MyCalculations;
import methods.MyMethods;
import mineInfo.AlKharid;
import mineInfo.BarbarianVillageBankEdgeville;
import mineInfo.DropOnly;
import mineInfo.FaladorUnderGroundBankFallyEast;
import mineInfo.LivingRockCaverns;
import mineInfo.LumbridgeSwampWest;
import mineInfo.MiningGuild;
import mineInfo.Rimmington;
import mineInfo.VarrockEast;
import mineInfo.VarrockWest;
import mineInfo.Yanille;
import myAPI.SkillTracker;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Skills;

import tasks.Mine;

//banned words = "mineral", "Granite"
@Manifest(name = "Super Miner", 
description = "Supports banking and powermining for many locations. "
		+ "LRC "
		//+ "and Essence "
		+ "supported. Visit thread for details.",
		properties = "topic=1127043;")

public class SuperMiner extends PollingScript<ClientContext> implements PaintListener, MessageListener, MouseListener {

	private final double version = 2.1;

	private long startTime = 0;

	public static final int SHARK_ID = 385, SPIN_TICKET_ID = 24154, COALBAG_ID = 18339;

	public static final int[] ITEM_IDS_TO_KEEP = {SPIN_TICKET_ID, Pickaxe.IRON.getId(), Pickaxe.STEEL.getId(), 
		Pickaxe.MITHRIL.getId(), Pickaxe.ADAMANT.getId(), Pickaxe.RUNE.getId(), Pickaxe.DRAGON.getId(),
		Pickaxe.LENT_DRAGON.getId(), COALBAG_ID, SummoningItem.LAVA_TITAN_POUCH.getId(), SHARK_ID, 
		Potion.SUPER_RESTORE_1.getId(), Potion.SUPER_RESTORE_2.getId(), Potion.SUPER_RESTORE_3.getId(), 
		Potion.SUPER_RESTORE_4.getId(), Potion.SUMMONNG_1.getId(), Potion.SUMMONNG_2.getId(), 
		Potion.SUMMONNG_3.getId(), Potion.SUMMONNG_4.getId(), SummoningItem.UNKNOWN_SUMMONING_NECKLACE.getId(), 
		SummoningItem.UNKNOWN_SUMMONING_NECKLACE_2.getId()};

	public static final int[] strangerockIDs = {15532, 15533};

	public static final int[] UNCUT_STONE_IDS = {Gem.UNCUT_SAPPHIRE.getId(), 
		Gem.UNCUT_EMERALD.getId(), Gem.UNCUT_RUBY.getId(), Gem.UNCUT_DIAMOND.getId()};

	private String miningMethod = "Dropping";

	private String displayname;

	private boolean bankingEnabled;

	private boolean dropASAP = false;

	private boolean useLodestone = true;

	private boolean pickupOreOnGround;

	//private int pickaxeId;

	private String message;

	private boolean debug = false;

	public static final int HOME_TELEPORT_ANIMATION_1 = 16385;
	public static final int HOME_TELEPORT_ANIMATION_2 = 16386;
	public static final int HOME_TELEPORT_ANIMATION_3 = 16393;

	public int coalBagCount = 0;

	private ArrayList<OreInfo> oreInfoList = new ArrayList<OreInfo>();
	public GemInfo[] gemInfos = new GemInfo[0];
	private ArrayList<Rock> rocks = new ArrayList<Rock>();

	private int moneyGained = 0;

	private static final Color PaintBackgroundColor = new Color(0, 0, 0, 180);

	private SkillTracker skillTracker = null;

	private ArrayList<Task> tasks = new ArrayList<Task>();

	private MineInfo areaInfo = null;

	@Override
	public void start() {

		DebugMethods.println(debug, "start");

		if (!ctx.game.loggedIn()) {
			MyMethods.println("Please login before starting the script.");
			ctx.controller.stop();
			return;
		}

		if (ctx.backpack.collapsed()) {
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame,
					"Please fully expand your backpack before starting the script.");
			frame.dispose();
			ctx.controller.stop();
			return;
		}

		displayname = ctx.players.local().name();

		skillTracker = new SkillTracker(ctx, Skills.MINING);

		Gui mygui = new Gui(ctx);
		mygui.setVisible(true);

	}

	/**
	 * This method is used to select the MineInfo that will work best for the local player's
	 * location, skills and selected ores.
	 * <p>
	 * Once the MineInfo has been determined, its tasks will be added to tasks.
	 *
	 */
	public void taskSetup() {

		ArrayList<MineInfo> areaInfos = new ArrayList<MineInfo>() {{

			add(new AlKharid(ctx));
			add(new BarbarianVillageBankEdgeville(ctx));
			add(new FaladorUnderGroundBankFallyEast(ctx));
			add(new LivingRockCaverns(ctx));
			add(new LumbridgeSwampWest(ctx));
			add(new MiningGuild(ctx));
			add(new Rimmington(ctx));
			add(new VarrockEast(ctx));
			add(new VarrockWest(ctx));
			add(new Yanille(ctx));

			add(new DropOnly(ctx));

		}};

		for (MineInfo areaInfo : areaInfos) {
			if (areaInfo.shouldBeUsed()) {
				MyMethods.println(areaInfo.getClass().getSimpleName() + " selected");
				this.areaInfo = areaInfo;
				areaInfo.addTasks(tasks);
				break;
			}
		}

	}

	public void startTime(long startTime) {
		this.startTime = startTime;
	}

	public String message() {
		return message;
	}

	public void message(String message) {
		this.message = message;
	}

	public void miningMethod(String method) {
		this.miningMethod = method;
	}

	public boolean pickupOreOnGround() {
		return pickupOreOnGround;
	}

	public void pickupOreOnGround(boolean newValue) {
		pickupOreOnGround = newValue;
	}

	public ArrayList<OreInfo> oreInfoList() {
		return oreInfoList;
	}

	public ArrayList<Rock> rocks() {
		return rocks;
	}

	public long startTime() {
		return startTime;
	}

	public boolean useLodestone() {
		return useLodestone;
	}

	public int moneyGained() {
		return moneyGained;
	}

	public boolean debug() {
		return debug;
	}

	public boolean bankingEnabled() {
		return bankingEnabled;
	}

	public void bankingEnabled(boolean newValue) {
		bankingEnabled = newValue;
	}

	public boolean dropASAP() {
		return dropASAP;
	}

	public String displayname() {
		return displayname;
	}

	public void dropASAP(boolean newValue) {
		dropASAP = newValue;
	}

	@Override
	public void poll() {

		DebugMethods.println(debug, "poll()");

		try {

			//if (ctx.hud.isOpen(Hud.Window.BACKPACK)) {
			moneyGained = getMoneyGained();
			//}

			for (Task task : tasks) {
				if (task.activate()) {
					DebugMethods.println(debug, task.getClass().getSimpleName() + ".execute()");
					task.execute();
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		MyMethods.sleep(100, 300);
		return;
	}

	@Override
	public void repaint(Graphics g) {

		Point m = ctx.input.getLocation();
		g.setColor(Color.cyan);
		g.drawRoundRect(m.x - 6, m.y, 15, 3, 5, 5);
		g.drawRoundRect(m.x, m.y - 6, 3, 15, 5, 5);
		g.fillRoundRect(m.x - 6, m.y, 15, 3, 5, 5);
		g.fillRoundRect(m.x, m.y - 6, 3, 15, 5, 5);

		int x = 5, y = 5;

		if (message != null) {

			g.setColor(PaintBackgroundColor);
			g.fillRect(x, y, 200, 21);
			x += 5;

			g.setColor(Color.RED);
			g.setFont(new Font("Tahoma", Font.BOLD, 12));
			g.drawString(message, x, 70);
			return;
		}

		if (startTime == 0) {
			return;
		}

		//draw background
		g.setColor(PaintBackgroundColor);
		g.fillRect(x, y, 129, 168);
		x += 5;
		y += 15;

		g.setColor(Color.white);
		g.setFont(new Font("Tahoma", Font.BOLD, 11));
		g.drawString("Super Miner " + version, x, y);
		y += 15;

		g.setColor(Color.red); 
		g.setFont(new Font("Tahoma", Font.PLAIN, 11));

		g.drawString("Time: " + MyMethods.formattedTimeSince(startTime) , x, y);
		y += 15;

		g.setColor(Color.RED);
		g.setFont(new Font("Tahoma", Font.PLAIN, 11));

		g.drawString("Method: " + miningMethod, x, y);
		y += 15;
		if (skillTracker != null) {
			g.drawString("Current level: " + skillTracker.getCurrentLevel(), x, y);
			y += 15;
			g.drawString("Levels Gained: " + skillTracker.getLevelsGained(), x, y);
			y += 15;
			g.drawString("Current Exp: " + skillTracker.getCurrentExperience(), x, y);
			y += 15;
			g.drawString("Exp Gained: " + skillTracker.getExperienceGained(), x, y);
			y += 15;
			g.drawString("Exp / Hour: " + MyMethods.formatDouble(skillTracker.getExperiencePerHour()), x, y);
			y += 15;
			g.drawString("Exp TNL: " + skillTracker.getExperienceToNextLevel(), x, y);
			y += 15;


			if (skillTracker.getExperienceGained() == 0) {
				g.drawString("Time TNL: ", x, y);
			} else {	
				g.drawString("Time TNL: " + MyMethods.formatTime(skillTracker.getMillisecondsToNextLevel()), x, y);
			}
			y += 7;

			//fill bar
			g.fillRect(x, y, 100, 4);
			g.setColor(Color.GREEN);
			g.fillRect(x, y, (int)Math.round(skillTracker.getPercentOfLevelCompletion()), 4);
			y += 18;
		}

		if (bankingEnabled) {

			for (OreInfo oreInfo : oreInfoList) {
				g.setColor(PaintBackgroundColor);
				g.fillRect(x - 5, y - 7, 129, 15);
				g.setColor(Color.RED);
				g.drawString(oreInfo.getName() + " Mined: " + oreInfo.getCount(), x, y);
				y += 15;
			}

			for (GemInfo gem : gemInfos) {
				g.setColor(PaintBackgroundColor);
				g.fillRect(x - 5, y - 7, 129, 15);
				g.setColor(Color.RED);
				g.drawString(gem.getName() + "s Mined: " + gem.getCount(), x, y);
				y += 15;
			}

			g.setColor(PaintBackgroundColor);
			g.fillRect(x - 5, y - 7, 129, 15);
			g.setColor(Color.RED);
			g.drawString("Money Gained: " + MyMethods.formatDouble(moneyGained), x, y);
			y += 15;

			g.setColor(PaintBackgroundColor);
			g.fillRect(x - 5, y - 7, 129, 15);
			g.setColor(Color.RED);
			g.drawString("Money / Hour: " + MyMethods.formatDouble((double)moneyGained 
					/ MyCalculations.getHoursSince(startTime)), x, y);
			y += 15;

			g.setColor(PaintBackgroundColor);
			g.fillRect(x - 5, y - 7, 129, 15);
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("Click Here for more info", x, y);
			y += 15;

		}

		if (debug) {
			g.setColor(Color.GREEN);

			g.drawString("Animation: " + ctx.players.local().animation(), x, y);
			y += 15;

			g.drawString("inMotion: " + ctx.players.local().inMotion(), x, y);
			y += 15;

			g.drawString("ctx.players.local().animation(): " + ctx.players.local().animation(), x, y);
			y += 15;

			Mine mine = new Mine(ctx, areaInfo);
			GameObject rock = mine.getRock();
			g.drawString("rock.valid(): " + rock.valid(), x, y);
			y += 15;
			g.drawString("rock.tile().distanceTo(ctx.players.local()): " + rock.tile().distanceTo(ctx.players.local()), x, y);
			y += 15;
			g.drawString("MyCalculations.playerIsFacing(ctx.players.local(), rock): " + MyCalculations.playerIsFacing(ctx.players.local(), rock), x, y);

			DebugMethods.drawTileArrayOnMap(ctx, g, areaInfo.tilesToBank(), Color.GREEN);
			GameObject door = ctx.objects.select().id(11714).at(new Tile(3061, 3374, 0)).poll();
			DebugMethods.drawTileArrayOnMap(ctx, g, new Tile[]{door.tile()}, Color.RED);
			door.draw(g);

		}

	}

	private int getMoneyGained() {
		int moneyGained = 0;

		for (OreInfo oreInfo : oreInfoList) {
			moneyGained += oreInfo.getCount() * oreInfo.getPrice();
		}

		for (GemInfo gem : gemInfos) {
			moneyGained += gem.getCount() * gem.getPrice();
		}

		return moneyGained;
	}

	@Override
	public void messaged(MessageEvent e) {

		if(e.source().isEmpty()) {

			if (e.text().contains("You manage to mine")) {
				for (OreInfo oreInfo : oreInfoList) {
					if (e.text().contains(oreInfo.getName().toLowerCase())) {
						oreInfo.incrementCount();
					}
				}
				return;
			}

			if (e.text().contains("You just found")) {
				for (GemInfo gemInfo : gemInfos) {
					if (e.text().contains(gemInfo.getName().replace("Uncut ", "").toLowerCase())) {
						gemInfo.incrementCount();
					}
				}
				return;
			}

			if (e.text().contains("You'll need to activate")) {
				useLodestone = false;
			} else if (e.text().contains("Your coal bag is already")) {
				coalBagCount = 27;
			} else if (e.text().contains("There is no coal in")) {
				coalBagCount = 0;
				//} else if (msg.contains("Oh dear, you are d")) {
				//if (!currentMiningSubScript.getClass().getName().equals("LivingRockCavern")) {
				//MyMethods.println(msg);
				//shutdown();	
				//}
			}

		}
	}

	@Override
	public void stop() {

		System.out.println();
		MyMethods.println("Ore Mined:");
		for (OreInfo oreInfo : oreInfoList) {
			MyMethods.println(oreInfo.getName() + ": " + oreInfo.getCount());
		}
		System.out.println();

		MyMethods.println("Gems Mined:");
		for (GemInfo gemInfo : gemInfos) {
			MyMethods.println(gemInfo.getName() + ": " + gemInfo.getCount());
		}
		System.out.println();

		MyMethods.println(displayname + " Stopped after " + MyMethods.formattedTimeSince(startTime));

		if (skillTracker == null) {
			MyMethods.println("stopping");
			return;
		}
		MyMethods.println("Levels Gained: " + skillTracker.getLevelsGained());
		MyMethods.println("Experience Gained: " + skillTracker.getExperienceGained());
		MyMethods.println("Experience / Hour: " + MyMethods.formatDouble(skillTracker.getExperiencePerHour()));
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		Rectangle paint = new Rectangle(5, 55, 127, 343);
		if (e.getComponent() instanceof java.awt.Canvas 
				&& paint.contains(e.getPoint())) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				new ShowMoreGui(ctx);
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				debug = !debug;
			}
		}

	}


	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}