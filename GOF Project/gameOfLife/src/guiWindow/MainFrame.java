import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
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
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame {

	// Constructor:
	private MainFrame() {
		// Properties of MainFrame:
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setTitle("Game of Life");
		getContentPane().setBackground(Color.gray);

		GridPanel grid = new GridPanel();
		ControlsPanel controlsPanel = new ControlsPanel();
		Menu menu = new Menu(grid);
		JMenuBar menuBar = new JMenuBar();

		Controller controller = new Controller(grid, controlsPanel, menu, menuBar);

		menu.addController(controller);
		// Adding Components to MainFrame:
		add(grid);
		add(controlsPanel, BorderLayout.SOUTH);

		add(menuBar, BorderLayout.NORTH);
		// setJMenuBar(menuBar);

		RightClickMouseListener rightClick = new RightClickMouseListener(grid, controller);

		grid.addRightClick(rightClick);

		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	// Main:
	public static void main(String[] args) {
		new MainFrame();
	}

	public class GridPanel extends JPanel {
		// Variables:
		private int height;
		private int width;
		private int gridSize = 50;
		private int gridSizeHor;
		private int boxSize;
		public boolean[][] grid = new boolean[300][300];
		private int paddingLeft;
		private int paddingTop;
		private int centerX = 150;
		private int centerY = 150;
		private int offsetX, offsetY;
		private String shape = "Clear";
		private boolean editMode = false;
		private Color[] colorScheme = { Color.DARK_GRAY, Color.white, Color.red, Color.orange, Color.green,
				Color.yellow };
		private RightClickMouseListener rightClick;
		private int centerXb = 0;
		private int centerYb = 0;

		// private Border border = new EmptyBorder(10,10,10,10);
		// Constructor:
		public GridPanel() {
			drawGrid();

			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					offsetX = e.getX();
					offsetY = e.getY();
					if (SwingUtilities.isLeftMouseButton(e)) {
						if (offsetX >= paddingLeft && offsetX <= (paddingLeft + (boxSize * gridSizeHor))
								&& offsetY <= boxSize * gridSize && offsetY >= paddingTop && !editMode) {
							setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						} else {
							checkAndEdit(offsetY, offsetX);
						}

					}

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					setCursor(Cursor.getDefaultCursor());
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					offsetX = e.getX();
					offsetY = e.getY();
				}

			});

			addMouseMotionListener(new MouseAdapter() {

				@Override
				public void mouseDragged(MouseEvent e) {

					int dx = e.getX() - offsetX;
					int dy = e.getY() - offsetY;

					// Adjust the grid position

					if (offsetX >= paddingLeft && offsetX <= (paddingLeft + (boxSize * gridSize))

							&& offsetY <= boxSize * gridSize) {
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					}
					if (offsetX >= paddingLeft && offsetX <= (paddingLeft + boxSize * gridSizeHor)
							&& offsetY <= boxSize * gridSize && offsetY >= paddingTop && !editMode) {
						centerY -= (dx / (boxSize * 3));
						centerX -= (dy / (boxSize * 3));
						// resetCenter(centerX,centerY);
						if (Math.abs((dx / (boxSize * 3))) > 3 | Math.abs((dy / (boxSize * 3))) > 3) {
							offsetX = e.getX();
							offsetY = e.getY();
						}
					}

					repaint();
				}
			});

		}

		public void drawGrid() {

			if (shape.equals("Clear")) {
				clearGrid();
				ControlsPanel.resetGenerations();
			}

			if (shape.equals("Block")) {
				grid[centerX][centerY] = true;
				grid[centerX + 1][centerY] = true;
				grid[centerX][centerY + 1] = true;
			}

			else if (shape.equals("Blinker")) {
				grid[centerX][centerY] = true;
				grid[centerX][centerY - 1] = true;
				grid[centerX][centerY + 1] = true;
			}

			else if (shape.equals("Beacon")) {
				grid[centerX][centerY - 2] = true;
				grid[centerX][centerY - 1] = true;
				grid[centerX + 1][centerY - 2] = true;
				grid[centerX + 2][centerY + 1] = true;
				grid[centerX + 3][centerY + 1] = true;
				grid[centerX + 3][centerY] = true;

			}

			else if (shape.equals("Boat")) {
				grid[centerX][centerY - 1] = true;
				grid[centerX - 1][centerY - 1] = true;
				grid[centerX - 1][centerY] = true;
				grid[centerX][centerY + 1] = true;
				grid[centerX + 1][centerY] = true;
			}

			else if (shape.equals("Beehive")) {
				grid[centerX][centerY + 1] = true;
				grid[centerX][centerY - 2] = true;
				grid[centerX - 1][centerY] = true;
				grid[centerX - 1][centerY - 1] = true;
				grid[centerX + 1][centerY] = true;
				grid[centerX + 1][centerY - 1] = true;
			}

			else if (shape.equals("Glider")) {
				grid[centerX - 1][centerY] = true;
				grid[centerX - 1][centerY - 1] = true;
				grid[centerX - 1][centerY + 1] = true;
				grid[centerX][centerY - 1] = true;
				grid[centerX + 1][centerY] = true;
			}

			else if (shape.equals("Pond")) {
				grid[centerX][centerY - 1] = true;
				grid[centerX + 1][centerY - 1] = true;
				grid[centerX][centerY + 2] = true;
				grid[centerX + 1][centerY + 2] = true;
				grid[centerX - 1][centerY] = true;
				grid[centerX - 1][centerY + 1] = true;
				grid[centerX + 2][centerY] = true;
				grid[centerX + 2][centerY + 1] = true;

			}

			else if (shape.equals("Ship")) {
				grid[centerX - 1][centerY] = true;
				grid[centerX + 1][centerY] = true;
				grid[centerX][centerY + 1] = true;
				grid[centerX][centerY - 1] = true;
				grid[centerX + 1][centerY + 1] = true;
				grid[centerX - 1][centerY - 1] = true;
			}

			else if (shape.equals("Toad")) {
				grid[centerX][centerY] = true;
				grid[centerX][centerY + 1] = true;
				grid[centerX + 1][centerY] = true;
				grid[centerX + 1][centerY + 1] = true;
				grid[centerX + 1][centerY + 2] = true;
				grid[centerX][centerY - 1] = true;
			}

			else if (shape.equals("Tub")) {
				grid[centerX - 1][centerY] = true;
				grid[centerX + 1][centerY] = true;
				grid[centerX][centerY + 1] = true;
				grid[centerX][centerY - 1] = true;
			}

			else if (shape.equals("Barge")) {
				grid[centerX - 1][centerY] = true;
				grid[centerX + 1][centerY] = true;
				grid[centerX][centerY + 1] = true;
				grid[centerX][centerY - 1] = true;
				grid[centerX + 1][centerY + 2] = true;
				grid[centerX + 2][centerY + 1] = true;

			}

			else if (shape.equals("Long Boat")) {
				grid[centerX][centerY - 1] = true;
				grid[centerX - 1][centerY - 1] = true;
				grid[centerX - 1][centerY] = true;
				grid[centerX][centerY + 1] = true;
				grid[centerX + 1][centerY] = true;
				grid[centerX + 1][centerY + 2] = true;
				grid[centerX + 2][centerY + 1] = true;

			}

			else if (shape.equals("Loaf")) {
				grid[centerX + 1][centerY + 1] = true;
				grid[centerX - 1][centerY + 1] = true;
				grid[centerX - 1][centerY] = true;
				grid[centerX][centerY - 1] = true;
				grid[centerX + 1][centerY - 1] = true;
				grid[centerX][centerY + 2] = true;
				grid[centerX + 2][centerY] = true;
			}

			else if (shape.equals("Mango")) {
				grid[centerX + 1][centerY] = true;
				grid[centerX - 1][centerY] = true;
				grid[centerX][centerY - 1] = true;
				grid[centerX - 1][centerY + 1] = true;
				grid[centerX][centerY + 2] = true;
				grid[centerX + 1][centerY + 3] = true;
				grid[centerX + 1][centerY] = true;
				grid[centerX + 2][centerY + 2] = true;
				grid[centerX + 2][centerY + 1] = true;
			}

			else if (shape.equals("Long Barge")) {
				grid[centerX - 1][centerY] = true;
				grid[centerX + 1][centerY] = true;
				grid[centerX][centerY + 1] = true;
				grid[centerX][centerY - 1] = true;
				grid[centerX - 1][centerY - 2] = true;
				grid[centerX - 2][centerY - 1] = true;
				grid[centerX + 1][centerY + 2] = true;
				grid[centerX + 2][centerY + 1] = true;
			}

			else if (shape.equals("Half-Fleet")) {
				grid[centerX][centerY] = true;
				grid[centerX][centerY - 1] = true;
				grid[centerX - 1][centerY] = true;
				grid[centerX - 2][centerY - 1] = true;
				grid[centerX - 1][centerY - 2] = true;
				grid[centerX - 2][centerY - 2] = true;

				grid[centerX + 1][centerY + 1] = true;
				grid[centerX + 1][centerY + 2] = true;
				grid[centerX + 2][centerY + 1] = true;
				grid[centerX + 2][centerY + 3] = true;
				grid[centerX + 3][centerY + 2] = true;
				grid[centerX + 3][centerY + 3] = true;

			}

			else if (shape.equals("Half-Bakery")) {
				grid[centerX - 1][centerY] = true;
				grid[centerX + 1][centerY] = true;
				grid[centerX][centerY + 1] = true;
				grid[centerX][centerY - 1] = true;

				grid[centerX - 2][centerY] = true;
				grid[centerX - 3][centerY - 1] = true;
				grid[centerX - 3][centerY - 2] = true;
				grid[centerX - 2][centerY - 3] = true;
				grid[centerX - 1][centerY - 2] = true;

				grid[centerX + 2][centerY + 1] = true;
				grid[centerX][centerY + 2] = true;
				grid[centerX + 1][centerY + 3] = true;
				grid[centerX + 2][centerY + 3] = true;
				grid[centerX + 3][centerY + 2] = true;

			}

			repaint();
		}

		public void drawGrid(boolean[][] temp_grid, int generations) {
			this.grid = temp_grid;
			ControlsPanel.generations = generations;
			repaint();
		}

		private void clearGrid() {
			for (int i = 0; i < 300; i++) {
				for (int j = 0; j < 300; j++)
					grid[i][j] = false;
			}

		}

		// PaintComponent Override:
		@Override
		public void paintComponent(Graphics g) {

			Graphics2D g2d = (Graphics2D) g;

			// Clearing Screen:
			g2d.setColor(colorScheme[0]);
			g2d.fillRect(0, 0, getWidth(), getHeight());

			g2d.setColor(colorScheme[1]);
			this.height = getHeight();
			this.width = getWidth();
			boxSize = Math.min(width / gridSize, height / gridSize);
			paddingTop = (height - (boxSize * gridSize)) / 2;

			int x = paddingLeft;
			int y = paddingTop;
			gridSizeHor = (width - 10) / boxSize;
			paddingLeft = (width - (boxSize * gridSizeHor)) / 2;

			setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			int startRow = centerX - (gridSize / 2);
			int endRow = centerX + (gridSize / 2);
			int startCol = centerY - (gridSizeHor / 2);
			int endCol = centerY + (gridSizeHor / 2);

			for (int i = startRow; i < endRow; i++) {
				for (int j = startCol; j < endCol; j++) {
					// Drawing live cells:

					// Infinite Grid:
					int infI = i, infJ = j;

					while (infI > 299)
						infI -= 299;
					while (infI < 0)
						infI = 299 + infI;
					while (infJ > 299)
						infJ -= 299;
					while (infJ < 0)
						infJ = 299 + infJ;

					int count = liveCellsSurr(infI, infJ);
					if (grid[infI][infJ]) {

						if (count == 2 | count == 3)
							g2d.setColor(colorScheme[5]);
						else if (count < 2)
							g2d.setColor(colorScheme[2]);
						else if (count > 3)
							g2d.setColor(colorScheme[3]);
						g2d.fillRect(x, y, boxSize, boxSize);

					} else {
						if (count == 3) {
							g2d.setColor(colorScheme[4]);
							g2d.fillRect(x, y, boxSize, boxSize);
						}
					}

					g2d.setColor(colorScheme[1]);
					// Drawing dead cells:
					g2d.drawRect(x, y, boxSize, boxSize);

					x += boxSize;
				}
				x = paddingLeft;
				y += boxSize;
			}
		}

		// Methods:
		public void setEditMode(boolean state) {
			this.editMode = state;
		}

		public void setGridSize(int size) {
			this.gridSize = size;
			repaint();
		}

		public void nextGen() {
			boolean[][] newGrid = new boolean[300][300];

			for (int i = 0; i < 300; i++) {
				for (int j = 0; j < 300; j++) {
					int surrLiveCells = this.liveCellsSurr(i, j);
					if (surrLiveCells <= 1)
						newGrid[i][j] = false;
					else if (surrLiveCells >= 4 && surrLiveCells <= 8)
						newGrid[i][j] = false;
					else if ((surrLiveCells == 2 | surrLiveCells == 3) && grid[i][j])
						newGrid[i][j] = true;
					else if (surrLiveCells == 3 && !grid[i][j])
						newGrid[i][j] = true;
					else
						newGrid[i][j] = false;
				}
			}

			this.grid = newGrid;
			repaint();
		}

		public int liveCellsSurr(int i, int j) {
			int count = 0;
			int infI = i, infJ = j;

			while (infI > 299)
				infI -= 299;
			while (infI < 0)
				infI = 299 + infI;
			while (infJ > 299)
				infJ -= 299;
			while (infJ < 0)
				infJ = 299 + infJ;

			if (infI - 1 >= 0 && infJ - 1 >= 0)
				if (grid[infI - 1][infJ - 1])
					count++;
			if (infI - 1 >= 0)
				if (grid[infI - 1][infJ])
					count++;
			if (infI - 1 >= 0 && infJ + 1 <= 299)
				if (grid[infI - 1][infJ + 1])
					count++;

			if (infJ - 1 >= 0)
				if (grid[infI][infJ - 1])
					count++;
			if (infJ + 1 <= 299)
				if (grid[infI][infJ + 1])
					count++;

			if (infI + 1 <= 299 && infJ - 1 >= 0)
				if (grid[infI + 1][infJ - 1])
					count++;
			if (infI + 1 <= 299)
				if (grid[infI + 1][infJ])
					count++;
			if (infI + 1 <= 299 && infJ + 1 <= 299)
				if (grid[infI + 1][infJ + 1])
					count++;

			return count;
		}

		public void setColor(Color[] colors) {
			this.colorScheme = colors;
			repaint();
		}

		public void checkAndEdit(int x, int y) {
			int i, j;
			i = (x - paddingTop) / boxSize;
			j = (y - paddingLeft) / boxSize;
			int startRow = centerX - (gridSize / 2);
			int startCol = centerY - (gridSizeHor / 2);

			int infI = i + startRow, infJ = j + startCol;
			while (infI > 299)
				infI -= 299;
			while (infI < 0)
				infI = 299 + infI;
			while (infJ > 299)
				infJ -= 299;
			while (infJ < 0)
				infJ = 299 + infJ;

			if (grid[infI][infJ])
				grid[infI][infJ] = false;
			else
				grid[infI][infJ] = true;

			repaint();
		}

		public void randomizeCells() {
			Random random = new Random();
			int randomNumber;

			for (int i = 0; i < 300; i++) {
				for (int j = 0; j < 300; j++) {
					randomNumber = random.nextInt(2);
					if (randomNumber <= 0.5)
						grid[i][j] = !grid[i][j];
				}
			}
			repaint();
		}

		public void addRightClick(RightClickMouseListener rightClick) {
			this.rightClick = rightClick;
			this.setComponentPopupMenu(rightClick);

		}

		public void resetCenter(int x, int y) {
			centerX = x;
			centerY = y;
		}

		public void setShape(String s) {
			shape = s;
			repaint();
		}

		public boolean[][] getGrid() {
			return grid;
		}
	}

	
	class ControlsPanel extends JPanel {
	    // Variables for controls:
	    private JCheckBox editCheckBox;
	    private JButton nextButton;
	    private JButton randomizeButton;
	    public JSlider sizeSlider;
	    private JButton startButton;
	    private JSlider speedSlider;
	    public JComboBox<String> shapeDropDown;
	    private JLabel genLabel;
	    public static int generations;
	    private JToggleButton modeswitch;
	    String[] options = {"Clear", "Block", "Blinker","Beacon", "Boat", "Beehive", "Glider", "Pond", "Ship", "Toad", "Tub","Barge","Long Boat", "Loaf","Mango","Long Barge","Half-Fleet","Half-Bakery"};


	    // Constructor:
	    public ControlsPanel() {
	    	generations = 0;
	    	
	        editCheckBox = new JCheckBox("Edit");
	        nextButton = new JButton("Next");
	        randomizeButton = new JButton("Randomize Cells");
	        sizeSlider = new JSlider();
	        startButton = new JButton("Start");
	        speedSlider = new JSlider();
	        shapeDropDown = new JComboBox<>(options);
	        modeswitch = new JToggleButton("Light Mode");
	        genLabel = new JLabel("Generation: " + generations);
	      
	        // Modify sizeSlider
	        sizeSlider.setPaintLabels(true);
	        sizeSlider.setPaintTicks(true);
	        sizeSlider.setMajorTickSpacing(50);
	        sizeSlider.setMinorTickSpacing(5);
	        Hashtable<Integer, JLabel> genLabelTable = new Hashtable<>();
	        genLabelTable.put(0, new JLabel("Small"));
	        genLabelTable.put(50, new JLabel("Medium"));
	        genLabelTable.put(100, new JLabel("Large"));
	        sizeSlider.setLabelTable(genLabelTable);
	        
	        // Modify speedSlider
	        speedSlider.setPaintLabels(true);
	        speedSlider.setPaintTicks(true);
	        speedSlider.setMajorTickSpacing(50);
	        speedSlider.setMinorTickSpacing(5);
	        Hashtable<Integer, JLabel> genLabelTable1 = new Hashtable<>();
	        genLabelTable1.put(0, new JLabel("Slow"));
	        genLabelTable1.put(50, new JLabel("Medium"));
	        genLabelTable1.put(100, new JLabel("Fast"));
	        speedSlider.setLabelTable(genLabelTable1);
	        
	        
	        // Add controls to the panel:
	        add(editCheckBox);
	        add(nextButton);
	        add(randomizeButton);
	        add(sizeSlider);
	        add(startButton);
	        add(speedSlider); 
	        add(shapeDropDown);
	        add(modeswitch);
	        add(genLabel);
	    }
	    
	    //Methods:
	    public JToggleButton getModeToggle() {
	    	return modeswitch;
	    }
	    
	    public JCheckBox getEditBox() {
	    	return editCheckBox;
	    }
	    
	    public JButton getStartButton() {
	    	return startButton;
	    }
	    
	    public JButton getNextButton() {
	    	return nextButton;
	    }
	    
	    public int getSizeSliderVal() {
	    	return sizeSlider.getValue();
	    }
	    
	    public int getSpeedSliderVal() {
	    	return speedSlider.getValue();
	    }
	    
	    public JSlider getSpeedSlider() {
	    	return speedSlider;
	    }
	   
	    public static void resetGenerations() {
	    	generations=0;
	    }
	    
	    public void updateGenLabel() {
	    	genLabel.setText("Generation: " + generations);
	    }
	    
	    public void addRandomizeListener(ActionListener aL) {
	    	randomizeButton.addActionListener(aL);
	    }
	    
	    public void addEditCheckListener(ActionListener aL) {
	    	editCheckBox.addActionListener(aL);
	    }
	    public void addModeToggleListener(ActionListener aL) {
	    	modeswitch.addActionListener(aL);
	    }
	    
	    public void addNextListener(ActionListener aL) {
	    	nextButton.addActionListener(aL);
	    }
	    public void addSizeSlideListener(ChangeListener aL) {
	    	sizeSlider.addChangeListener(aL);;
	    }
	    public void addStartListener(ActionListener aL) {
	    	startButton.addActionListener(aL);
	    }
	    public void addSpeedSlideListener(ChangeListener aL) {
	    	speedSlider.addChangeListener(aL);
	    }
	    public void addShapeComboListener(ActionListener aL) {
	    	shapeDropDown.addActionListener(aL);
	    }

		public String[] getOptions() {
			return options;
		}

		public JComboBox<String> getShapeDropDown() {
			return shapeDropDown;
		}

		public void setSpeedSliderVal(int val) {
			speedSlider.setValue(val);
			
		}	 
	}

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

	public class Controller {
		// Variables:
		private GridPanel grid;
		private ControlsPanel controlsPanel;
		private Menu menu;
		private Color[] darkModeColors = { Color.DARK_GRAY, Color.white, Color.red, Color.orange, Color.green,
				Color.yellow };
		private Color[] whiteModeColors = { Color.white, Color.black, Color.red, Color.orange, Color.green, Color.yellow };
		private Timer timer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.nextGen();
				controlsPanel.generations++;
				controlsPanel.updateGenLabel();
			}
		});

		// Constructor:
		public Controller(GridPanel grid, ControlsPanel controlsPanel, Menu menu, JMenuBar menuBar) {
			this.grid = grid;
			this.controlsPanel = controlsPanel;
			this.menu = menu;

			controlsPanel.addSizeSlideListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					int value = controlsPanel.getSizeSliderVal();
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
				}

			});

			controlsPanel.addSpeedSlideListener(e -> updateTimer(controlsPanel.getSpeedSlider().getValue()));

			controlsPanel.addNextListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					grid.nextGen();
					controlsPanel.generations++;
					controlsPanel.updateGenLabel();
				}
			});

			controlsPanel.addStartListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (timer != null && timer.isRunning()) {
						stopTimer();
						controlsPanel.getStartButton().setText("Start");

						// Enabling buttons while timer is not running
						controlsPanel.getNextButton().setEnabled(true);
					} else {
						startTimer();
						controlsPanel.getStartButton().setText("Stop");

						// Disabling buttons while timer is running
						controlsPanel.getNextButton().setEnabled(false);
					}
				}
			});
			
			controlsPanel.addRandomizeListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					grid.randomizeCells();
				}
			});
			

			controlsPanel.addEditCheckListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (controlsPanel.getEditBox().isSelected())
						grid.setEditMode(true);
					else
						grid.setEditMode(false);

				}

			});

			controlsPanel.addModeToggleListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (controlsPanel.getModeToggle().isSelected()) {
						controlsPanel.getModeToggle().setText("Dark Mode");
						grid.setColor(whiteModeColors);

					} else {
						controlsPanel.getModeToggle().setText("Light Mode");
						grid.setColor(darkModeColors);

					}
				}
			});
			
			controlsPanel.addShapeComboListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
			    	for (String s:controlsPanel.getOptions()) {
			    		if (controlsPanel.getShapeDropDown().getSelectedItem().equals(s)) {
			    			grid.setShape(s);
			    			grid.drawGrid();
			    			break;
			    		}
			    	}
				}
		});

			
			menu.addToMenuBar(menuBar);
			
		}

		// Methods:
		public void startTimer() {
			timer.start();

		}

		public void stopTimer() {
			timer.stop();
		}

		public void updateTimer(int val) {
			int delay = 100 - val;
			if (delay < 5)
				delay = 5;
			if (timer != null) {
				timer.setDelay(delay * 50 + 1);
				timer.setInitialDelay(delay * 50 + 1);
			}
			controlsPanel.setSpeedSliderVal(val);
		}
		
		
		public void updateSize(int val) {
			controlsPanel.sizeSlider.setValue(val);
		}
		
		public void setShape(String shape) {
			controlsPanel.shapeDropDown.setSelectedItem(shape);
		}
	}
}
