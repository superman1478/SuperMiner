package gui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import org.powerbot.script.rt6.ClientContext;

import superMiner.SuperMiner;
import methods.MyCalculations;
import methods.MyMethods;


@SuppressWarnings("serial")
public class ShowMoreGui extends JFrame {

	private SuperMiner script;
	private String[][] data;

	public ShowMoreGui(final ClientContext ctx) {

		this.script = (SuperMiner)ctx.controller.script();

		setSize(520, 240);
		setLocationRelativeTo(null);
		setTitle("More Information");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);

		final String[] columnNames = {
				"Item",
				"Amount Collected",
				"Price",
		"Profit From Item"};

		updateData();

		final JTable table = new JTable(data, columnNames);

		add(new JScrollPane(table));

		setVisible(true);

		Thread thread = new Thread() {

			@Override
			public void run() {
				while (ShowMoreGui.this.isVisible() && !ctx.controller.isStopping()) {
					//MyMethods.println("updating showmore");

					updateData();

					for (int i = 0; i < data.length; i++) {
						for (int j = 0; j < data[i].length; j++) {
							table.getModel().setValueAt(data[i][j], i, j);
							//MyMethods.println("data[" + i + "][" + j + "]: " + data[i][j]);
						}
					}

					MyMethods.sleep(500, 500);
				}

				ShowMoreGui.this.dispose();

			}


		};

		thread.start();

	}

	private void updateData() {
		data = new String[script.oreInfoList().size() + script.gemInfos.length + 1][4];

		int dataIndex = 0;

		for (; dataIndex < script.oreInfoList().size(); dataIndex++) {
			int profit = script.oreInfoList().get(dataIndex).getCount() * script.oreInfoList().get(dataIndex).getPrice();
			String[] row = {script.oreInfoList().get(dataIndex).getName(),
					script.oreInfoList().get(dataIndex).getCount() + " (" +
							MyMethods.formatDouble(script.oreInfoList().get(dataIndex).getCount() / MyCalculations.getHoursSince(script.startTime())) + " Per Hour)",

							Integer.toString(script.oreInfoList().get(dataIndex).getPrice()),
							MyMethods.formatDouble(profit) + " (" + MyMethods.formatDouble(profit / MyCalculations.getHoursSince(script.startTime())) + " Per Hour)"};
			data[dataIndex] = row;
		}

		for (int i = 0; i < script.gemInfos.length; i++, dataIndex++) {
			String[] row = {script.gemInfos[i].getName(), 
					Integer.toString(script.gemInfos[i].getCount()),
					Integer.toString(script.gemInfos[i].getPrice()),
					Integer.toString(script.gemInfos[i].getCount() * script.gemInfos[i].getPrice())};
			data[dataIndex] = row;
		}

		String[] row = {"Total", 
				"",
				"",
				MyMethods.formatDouble((double)script.moneyGained()) 
				+ " (" + MyMethods.formatDouble(script.moneyGained() / MyCalculations.getHoursSince(script.startTime()))
				+ " Per Hour)"};
		data[dataIndex] = row;

	}

}
