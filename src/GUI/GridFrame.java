package GUI;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

import Grid.Case;
import Grid.Grid;

public class GridFrame extends JFrame{

	private int gridSizeX;
	private int gridSizeY;
	private BorderLayout borderLayout;
	private JPanel jpanelEnvironment;
	private JPanel jpanelRobot;
	private JLabel refreshLabel;
	private JLabel jewelLabel;
	private int lostJewels;

	private static final long serialVersionUID = 1L;

	public GridFrame(Grid grid) {
		// Set the text in the menu bar
		this.setTitle("Manor");
		// Set the dimensions of the frame
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int)dimension.getHeight()/2;
		int width  = (int)dimension.getWidth()/2;
		this.setSize(width, height);
		// Set the relative position of the frame
		this.setLocationRelativeTo(null);
		// Set the default operation when the frame is closed
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.borderLayout = new BorderLayout();
		this.setLayout(this.borderLayout);
		this.gridSizeX = grid.getSizeX();
		this.gridSizeY = grid.getSizeY();
		// Environment
		this.jpanelEnvironment = new JPanel();
		this.add(this.jpanelEnvironment, BorderLayout.CENTER);
		// Robot
		this.buildRobotInfo();
		// Draws the map
		this.update(grid);
		// Sets the frame to visible
		this.setVisible(true);
	}

	/**
	 * @param c
	 * @return l
	 */
	private JLabel buildCase(Case c) {
		
		JLabel l = new JLabel("", JLabel.CENTER);
		String labelText = "<html>";
		int dust = c.getDustLevel();
		if (c.hasJewel() && dust > 0) {
			this.addIcon(l, "icons/jewel_dust.png");
			labelText += "x " + dust;
		} else if (dust > 0) {
			this.addIcon(l, "icons/dust.png");
			labelText += "x " + dust;
		} else if (c.hasJewel()) {
			this.addIcon(l, "icons/jewel.png");
		}
		labelText += "</html>";
		l.setText(labelText);
		l.setFont(new Font("Georgia", Font.BOLD, 14));
		Border border = BorderFactory.createLineBorder(Color.black, 1);
		l.setBorder(border);
		l.setOpaque(true);
		if (c.isRobot()) {l.setBackground(Color.blue);l.setForeground(Color.white);}
		return l;
	}
	
	/** Builds the robot info panel
	 */
	private void buildRobotInfo() {
		this.lostJewels = 0;
		this.jpanelRobot = new JPanel();
		this.add(this.jpanelRobot, BorderLayout.NORTH);
		GridLayout gridLayoutRobot = new GridLayout(1, 2);
		this.jpanelRobot.setLayout(gridLayoutRobot);
		this.refreshLabel = new JLabel("<html>Refresh rate : " +  20 + "</html>", JLabel.CENTER);
		this.formatLabel(this.refreshLabel);
		this.jewelLabel = new JLabel("<html>&nbsp;&nbsp;Lost jewels : " + this.lostJewels + "</html>", JLabel.CENTER);
		this.formatLabel(this.jewelLabel);
		this.addIcon(this.jewelLabel, "icons/jewel.png");
		this.jpanelRobot.add(refreshLabel);
		this.jpanelRobot.add(jewelLabel);
	}
	
	/** Formats the label of the upper panel
	 * @param label
	 */
	private void formatLabel(JLabel label) {
		label.setFont(new Font("Georgia", Font.CENTER_BASELINE, 15));
		Border border = BorderFactory.createLineBorder(Color.black, 1);
		label.setBorder(border);
		label.setOpaque(true);
		label.setBackground(Color.darkGray);
		label.setForeground(Color.white);
	}
	
	private void addIcon(JLabel label, String iconPath) {
		ImageIcon icon = new ImageIcon(iconPath);
		label.setIcon(icon);
	}
	
	/** Draws/updates the robot info
	 * @param refreshRate
	 */
	public void updateRefreshRate(int refreshRate) {
		this.refreshLabel.setText("<html>Refresh rate : " + refreshRate + "</html>");
		// Refreshes the frame
		this.jpanelRobot.validate();
	}

	/** Draws/updates the robot info
	 */
	public void updateLostJewels() {
		this.lostJewels++;
		this.jewelLabel.setText("<html>Lost jewels : " + this.lostJewels + "</html>");
		// Refreshes the frame
		this.jpanelRobot.validate();
	}

	/** Draws/updates the map corresponding to the input grid in the frame
	 * @param grid
	 */
	public void update(Grid grid) {
		this.jpanelEnvironment.removeAll();
		GridLayout gridLayoutEnvironment = new GridLayout(this.gridSizeX, this.gridSizeY);
		this.jpanelEnvironment.setLayout(gridLayoutEnvironment);
		for (int i = 0; i < this.gridSizeX; i++) {
			for (int j = 0; j < this.gridSizeY; j++) {
				this.jpanelEnvironment.add(buildCase(grid.getCase(i, j)));
			}
		}
		// Refreshes the frame
		this.jpanelEnvironment.validate();
	}

}