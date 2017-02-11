package Agents;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import GUI.GridFrame;
import Grid.Coordinates;
import Grid.Direction;
import Grid.Grid;

public class Manor implements Runnable {

	private Grid rooms;
	private GridFrame gui;
	private Coordinates robotPosition;
	private int lostPoints;

	public Manor(int sizeX, int sizeY) {
		this.rooms = new Grid(sizeX, sizeY);
		this.gui = null;
		this.lostPoints = 0;
	}

	/**
	 * @return rooms
	 */
	public Grid getRooms() {
		return this.rooms;
	}
	
	/** Returns a copy of the rooms of the manor
	 * @return roomsCopy
	 */
	public Grid getRoomsCopy() {
		return new Grid(this.rooms);
	}
	
	/**
	 * @return gui
	 */
	public GridFrame getGui() {
		return gui;
	}

	/**
	 * @param gui
	 */
	public void setGui(GridFrame gui) {
		this.gui = gui;
	}
	
	/** Sends the actualized version of the map to the GUI whenever it is modified,
	 * if a GUI exists
	 */
	public synchronized void notifyGUI() {
		if (this.gui != null) {
			this.gui.update(this.rooms);
		}
	}

	/** Changes the refresh rate displayed on the GUI, if it exists
	 * @param refreshRate
	 */
	public synchronized void notifyRefreshRateGUI(int refreshRate) {
		if (this.gui != null) {
			this.gui.updateRefreshRate(refreshRate);
		}
	}

	/** Increments the counter of lost jewels on the GUI, if it exists
	 */
	public synchronized void notifyJewelsGUI() {
		if (this.gui != null) {
			this.gui.updateLostJewels();
		}
	}
	
	/** Sets the position of the robot on the map
	 * @param x
	 * @param y
	 * @return isValid
	 * True if the position is valid, else false
	 */
	public boolean setRobotPosition(int x, int y) {
		if (this.isValidCase(x, y)) {
			this.robotPosition = new Coordinates(x, y);
			this.getRooms().getCase(x, y).setRobot(true);
			return true;
		}
		return false;
	}
	
	/**
	 * @return xRobot
	 */
	public Coordinates getRobotPosition() {
		return this.robotPosition;
	}
	
	/**
	 * @return lostPoints
	 */
	public int getLostPoints() {
		return this.lostPoints;
	}
	
	/** Resets the number of points lost
	 */
	public void resetPoints() {
		this.lostPoints = 0;
	}
	
	/** Asks the environment to move the robot from one case in a given direction
	 * @param dir
	 * @return authorizedMove
	 * True if the move is legal, ie if the robot is not heading outside the grid,
	 * or staying at the same place
	 */
	public boolean moveRobot(Direction dir) {
		boolean authorizedMove = false;
		int oldX = this.robotPosition.x;
		int oldY = this.robotPosition.y;
		int dx = 0;
		int dy = 0;
		switch (dir) {
		case UP: dx--;
			break;
		case DOWN: dx++;
			break;
		case LEFT: dy--;
			break;
		case RIGHT: dy++;
			break;
		default: System.out.println(" # Where am I supposed to go ?!");
			break;
		}
		// The robot can't go outside the grid, or stay at the same place (when calling "move")
		if (this.isValidCase(oldX + dx, oldY + dy) && (dx != 0 || dy != 0)) {
			// Set robot to false in the previous location
			this.rooms.getCase(oldX, oldY).setRobot(false);
			// Set it to true in the destination case
			this.setRobotPosition(oldX + dx, oldY + dy);
			authorizedMove = true;
			// The GUI is notified of the changes
			this.notifyGUI();
		}
		return authorizedMove;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return validCase
	 * True if the case with given coordinates exists, else false
	 */
	private boolean isValidCase(int x, int y) {
		return (x >= 0) && (y >= 0) && (x < this.rooms.getSizeX()) && (y < this.rooms.getSizeY());
	}
	
	/** Asks the environment to remove one unity of dust from the (x, y) case, and the
	 * jewel if one is present
	 * @param x
	 * @param y
	 */
	public void suckUp(int x, int y) {
		this.rooms.getCase(x, y).removeDust();
		if (this.rooms.getCase(x, y).removeJewel()) {
			this.lostPoints += 10;
			//System.out.println("Jewel lost !");
			this.notifyJewelsGUI();
		}
		// The GUI is notified of the changes
		this.notifyGUI();
	}
	
	/** Asks the environment to pick (remove) the jewel from the (x, y) case
	 * @param x
	 * @param y
	 */
	public void pickJewel(int x, int y) {
		this.rooms.getCase(x, y).removeJewel();
		// The GUI is notified of the changes
		this.notifyGUI();
	}
	
	/** The environment generates a fixed amount of dust in randomly chosen cases
	 */
	private void generateDust() {
		int dustQuantity = 2;
		int randX = 0;
		int randY = 0;
		for (int i = 0; i < dustQuantity; i++) {
			randX = ThreadLocalRandom.current().nextInt(0, this.rooms.getSizeX());
			randY = ThreadLocalRandom.current().nextInt(0, this.rooms.getSizeY());
			this.rooms.getCase(randX, randY).addDust();
		}
	}
	
	/** The environment generates a fixed amount of jewels in randomly chosen cases
	 */
	private void generateJewel() {
		int jewelQuantity = 1;
		int randX = 0;
		int randY = 0;
		for (int i = 0; i < jewelQuantity; i++) {
			randX = ThreadLocalRandom.current().nextInt(0, this.rooms.getSizeX());
			randY = ThreadLocalRandom.current().nextInt(0, this.rooms.getSizeY());
			this.rooms.getCase(randX, randY).addJewel();
		}
	}
	
	@Override
	public void run() {
		// The environment runs permanently
		while (true) {
			try {
				// Time before two modifications of the environment by itself
				int sleepTime = 7000;
				this.generateDust();
				this.generateJewel();
				// The GUI is notified of the changes
				this.notifyGUI();
				// The environment waits before next modification
				TimeUnit.MILLISECONDS.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}