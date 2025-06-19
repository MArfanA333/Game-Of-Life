package guiWindow;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.*;

public class Menu {

	// Menu Items directly on the Menu Bar
	private JMenuItem openMenuItem = new JMenuItem("Open");
	private JMenuItem saveMenuItem = new JMenuItem("Save");
	private JMenuItem preferencesMenuItem = new JMenuItem("Preferences");
	private JMenuItem exitMenuItem = new JMenuItem("Exit");
	private JMenuItem helpMenuItem = new JMenuItem("Help");
	private JMenu fileMenu = new JMenu("File");
	private JMenu editMenu = new JMenu("View");
	private JMenu helpMenu = new JMenu("Help");
	private JMenu exitMenu = new JMenu("Exit");
	private GridPanel grid = new GridPanel();
	// private JPopupMenu rightClickMenu = new JPopupMenu();

	public Menu() {

	}

	public Menu(GridPanel grid) {
		// Add ActionListeners to the menu items

		openMenuItem.addActionListener(new Open(grid));

		saveMenuItem.addActionListener(new Save(grid));

		helpMenuItem.addActionListener(new Help());

		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
			}
		});
	}

	public void addController(Controller controller) {
		preferencesMenuItem.addActionListener(new Preferences(grid, controller));
	}

	public void addToMenuBar(JMenuBar menuBar) {
		fileMenu.add(openMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(saveMenuItem);
		editMenu.add(preferencesMenuItem);
		helpMenu.add(helpMenuItem);
		exitMenu.add(exitMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		menuBar.add(exitMenu);

	}
}

class RightClickMouseListener extends JPopupMenu {

	protected JMenuItem openRightClickItem = new JMenuItem("Open");
	protected JMenuItem saveRightClickItem = new JMenuItem("Save");
	protected JMenuItem preferencesRightClickItem = new JMenuItem("Preferences");
	protected JMenuItem helpRightClickItem = new JMenuItem("Help");

	public RightClickMouseListener(GridPanel grid, Controller controller) {
		openRightClickItem.addActionListener(new Open(grid));

		saveRightClickItem.addActionListener(new Save(grid));

		preferencesRightClickItem.addActionListener(new Preferences(grid, controller));

		helpRightClickItem.addActionListener(new Help());

		addToRightClick();
	}

	public void addToRightClick() {
		add(openRightClickItem);
		addSeparator();
		add(saveRightClickItem);
		addSeparator();
		add(preferencesRightClickItem);
		addSeparator();
		add(helpRightClickItem);
	}

}

class Open implements ActionListener {
	GridPanel grid = new GridPanel();

	public Open() {

	}

	public Open(GridPanel grid) {
		this.grid = grid;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Logic for opening a file
		openFile();
	}

	private void openFile() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(fileChooser);

		if (result == JFileChooser.APPROVE_OPTION) {
			boolean[][] temp_grid = new boolean[300][300];
			File selectedFile = fileChooser.getSelectedFile();
			try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
				// Process the file content as needed
				for (int i = 0; i < 300; i++) {
					for (int j = 0; j < 300; j++) {
						if (reader.read() == '0')
							temp_grid[i][j] = false;
						else
							temp_grid[i][j] = true;
					}
				}
				grid.drawGrid(temp_grid, reader.read());
				JOptionPane.showMessageDialog(null, "File opened successfully");
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error opening the file");
			}
		}
	}
}

class Save implements ActionListener {
	private GridPanel grid = new GridPanel();

	public Save() {

	}

	public Save(GridPanel grid) {
		this.grid = grid;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		saveFile();
	}

	private void saveFile() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showSaveDialog(fileChooser);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile + ".txt"))) {
				// writer.write();
				for (int i = 0; i < 300; i++) {
					for (int j = 0; j < 300; j++) {
						if (grid.getGrid()[i][j])
							writer.write('1');
						else
							writer.write('0');
					}
					// writer.write('\n');
				}
				writer.write(ControlsPanel.generations);
				JOptionPane.showMessageDialog(null, "File saved successfully");
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error saving the file");
			}
		}
	}

}

class Preferences implements ActionListener {

	GridPanel grid;
	Controller controller;
	private JSlider zoomSlider = new JSlider();
	private JSlider speedSlider = new JSlider();
	private String[] startingPatterns = { "Clear", "Block", "Beacon", "Boat", "Beehive", "Glider", "Pond", "Toad",
			"Ship", "Tub", "Ship", "Barge", "Long Boat", "Loaf", "Mango", "Long Barge", "Half-Fleet", "Half-Bakery" };
	private JComboBox<String> startingPatternComboBox = new JComboBox<>(startingPatterns);

	public Preferences() {

	}

	public Preferences(GridPanel grid, Controller controller) {
		this.grid = grid;
		this.controller = controller;
		openDefaultFile();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Create the PreferencesPanel
		JPanel preferencesPanel = new JPanel(new GridLayout(4, 2)); // 4 rows, 2 columns

		// Combo box for starting pattern
		JLabel startingPatternLabel = new JLabel("Starting Pattern:");

		// Slider for simulation speed
		JLabel speedLabel = new JLabel("Simulation Speed:");
		speedSlider.setPaintLabels(true);
		speedSlider.setPaintTicks(true);
		speedSlider.setMajorTickSpacing(50);
		speedSlider.setMinorTickSpacing(5);
		Hashtable<Integer, JLabel> genLabelTable1 = new Hashtable<>();
		genLabelTable1.put(0, new JLabel("Slow"));
		genLabelTable1.put(50, new JLabel("Medium"));
		genLabelTable1.put(100, new JLabel("Fast"));
		speedSlider.setLabelTable(genLabelTable1);

		// Slider for zoom factor
		JLabel zoomLabel = new JLabel("Zoom Factor:");
		zoomSlider.setPaintLabels(true);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setMajorTickSpacing(50);
		zoomSlider.setMinorTickSpacing(5);
		Hashtable<Integer, JLabel> genLabelTable2 = new Hashtable<>();
		genLabelTable2.put(0, new JLabel("Small"));
		genLabelTable2.put(50, new JLabel("Medium"));
		genLabelTable2.put(100, new JLabel("Large"));
		zoomSlider.setLabelTable(genLabelTable2);

		// Add components to preferencesPanel
		preferencesPanel.add(startingPatternLabel);
		preferencesPanel.add(startingPatternComboBox);
		preferencesPanel.add(speedLabel);
		preferencesPanel.add(speedSlider);
		preferencesPanel.add(zoomLabel);
		preferencesPanel.add(zoomSlider);

		// Show the PreferencesPanel
		int result = JOptionPane.showConfirmDialog(null, preferencesPanel, "Preferences", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		// Handle OK button click
		if (result == JOptionPane.OK_OPTION) {
			// Get selected preferences
			String selectedStartingPattern = (String) startingPatternComboBox.getSelectedItem();
			int selectedSpeed = speedSlider.getValue();
			int selectedZoom = zoomSlider.getValue();
			controller.updateTimer(selectedSpeed);
			speedSlider.setValue(selectedSpeed);
			grid.setGridSize(selectedZoom);
			saveDefaultFile();
		}
	}

	public void saveDefaultFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/User/Preferences.txt"))) {
			int selectedSpeed = speedSlider.getValue();
			int selectedZoom = zoomSlider.getValue();

			writer.write(selectedSpeed);
			writer.write(selectedZoom);

			String selectedStartingPattern = (String) startingPatternComboBox.getSelectedItem();
			writer.write(selectedStartingPattern);


		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}



	public void openDefaultFile() {
		try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/User/Preferences.txt"))) {

			int selectedSpeed = reader.read();

			controller.updateTimer(selectedSpeed);
			speedSlider.setValue(selectedSpeed);

			int value = reader.read();

			controller.updateSize(value);

			if (value < 20 && value >= 0)
				grid.setGridSize(100);
			else if (value < 40)
				grid.setGridSize(85);
			else if (value < 60)
				grid.setGridSize(70);
			else if (value < 80)
				grid.setGridSize(50);
			else if (value <= 100)
				grid.setGridSize(30);

			zoomSlider.setValue(value);

			String shape = reader.readLine();

			controller.setShape(shape);
			startingPatternComboBox.setSelectedItem(shape);


		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}

class Help implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

		// Display the HelpPanel with JTextArea
		JTextArea helpTextArea = new JTextArea("								       ABOUT THE GAME\n\n"
				+ "The Game of Life is probably the most famous example of cellular automata, "
				+ "which are computer simulations inspired by biological cells. The game was "
				+ "invented by Cambridge’s mathematician John Conway, and popularized by "
				+ "fellow mathematician and popular science writer Martin Gardner in a Scientific "
				+ "American article in 1970. For further information, a number of references about "
				+ "the Game of Life (or, simply, Life) are provided at the end of this document.\n\n" +

				"The Game of Life consists of a 2-dimensional grid of square cells which, based "
				+ "on simple rules, can live, reproduce, or die. Depending on initial conditions, the "
				+ "cells may form various interesting patterns throughout the course of the game.\n\n" +

				"Thus, Life is not a game is the traditional sense (i.e., there is no winner or loser) "
				+ "but more a game of exploration where one may observe, and even try to predict, "
				+ "how a cell population will evolve, also to discover new patterns of interest, etc.\n\n" +

				"								     RULES OF THE GAME\n\n" +

				"It is played on a 2D grid that extends infinitely " + "in every direction. "
				+ "Every cell in the grid is either live (“populated”) or dead (“empty”, represented by a white/dark gray color depending on the theme). A live cell "
				+ "with 0 or 1 neighbor dies (“starvation”, represented by a red color), a live cell with 4 to 8 neighbors dies "
				+ " (“overpopulation”, represented by an orange color), but a live cell with 2 or 3 neighbors survives (represented by a yellow color). Conversely, a "
				+ "dead cell with exactly 3 neighbors becomes alive (“reproduction”, represented by a green color). In all other "
				+ "cases, a dead cell remains dead (“starvation” or “overpopulation”).");

		helpTextArea.setEditable(false);
		helpTextArea.setLineWrap(true);
		helpTextArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(helpTextArea);
		scrollPane.setPreferredSize(new Dimension(500, 450));

		JOptionPane.showMessageDialog(null, scrollPane, "Help", JOptionPane.PLAIN_MESSAGE);
	}
}
