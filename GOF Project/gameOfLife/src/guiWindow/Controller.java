package guiWindow;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
