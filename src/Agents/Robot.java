package Agents;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Grid.Case;
import Grid.Coordinates;
import Grid.Direction;
import Grid.Grid;

public class Robot implements Runnable {

	private Manor environment;
	// ==========================
	// Internal state information
	private Grid map;
	private PathComputer pathComputer;
	private Coordinates position;
	private int maxRefreshRate = 20;
	private int refreshRate;
	private float performance;
	private int nbActions;
	// ==========================

	public Robot(Manor environment) {
		this.environment = environment;
		// The robot knows the map and its own position once it has scanned at least once the environment
		this.map = null;
		this.pathComputer = null;
		this.position = null;
		// The number of iterations between two use of the robot sensors
		this.refreshRate = this.maxRefreshRate;
		this.nbActions = 0;
	}

	/**
	 * @param x
	 * @param y
	 */
	private void suckUp(int x, int y) {
		this.environment.suckUp(x, y);
		// Modifies the internal state of the robot
		this.map.getCase(x, y).removeDust();
		this.nbActions++;
	}

	/**
	 * @param x
	 * @param y
	 */
	private void pickJewel(int x, int y) {
		this.environment.pickJewel(x, y);
		// Modifies the internal state of the robot
		this.map.getCase(x, y).removeJewel();
		this.nbActions++;
	}

	/**
	 * @param dir
	 * @return validMove
	 */
	private boolean move(Direction dir) {
		if (this.environment.moveRobot(dir)) {
			this.map.getCase(this.position.x, this.position.y).setRobot(false);
			this.position.move(dir);
			this.map.getCase(this.position.x, this.position.y).setRobot(true);
			this.nbActions++;
			return true;
		}
		return false;
	}

	/** Makes the robot follow its current path
	 */
	private void followPath() {
		// Get the current value of the path from the path computer
		ArrayList<Coordinates> path = this.pathComputer.getPath();
		// If there is at least one objective
		// If there is no objective on the map, the robot doesn't move
		if (path.size() > 0) {
			Coordinates nextObjective = path.get(0);
			int diffX = nextObjective.x - this.position.x;
			if (diffX < 0) {
				this.move(Direction.UP);
			} else if (diffX > 0) {
				this.move(Direction.DOWN);
			} else {
				int diffY = nextObjective.y - this.position.y;
				if (diffY < 0) {
					this.move(Direction.LEFT);
				} else if (diffY > 0) {
					this.move(Direction.RIGHT);
				} else {
					// If the next objective is the current case, removes it, and then calls the
					// followPath again with the next objective
					this.pathComputer.removeFirstPathElement();
					this.followPath();
				}
			}
		}
	}

	/** The robot chooses an action depending on its internal state, then does it
	 */
	private void chooseAction() {
		if (this.map != null && this.position != null) {
			Case currentCase = map.getCase(this.position.x, this.position.y);
			// If there is a jewel on the current case, according to the robot's copy of the map
			if (currentCase.hasJewel()) {
				this.pickJewel(this.position.x, this.position.y);
			}
			// If there is dust on the current case, according to the robot's copy of the map
			else if (currentCase.getDustLevel() > 0) {
				this.suckUp(this.position.x, this.position.y);
			}
			// If there is nothing to do on the current case, the robot moves
			else {
				this.followPath();
			}
		}
	}

	/** The robot uses its sensors to observe the environment
	 */
	private void observeEnvironment() {
		this.map = this.environment.getRoomsCopy();
		this.position = this.environment.getRobotPosition();
	}

	/** The robot computes the path it has to follow, in a new thread
	 */
	private void updateState() {
		this.pathComputer = new PathComputer(this.map, this.position);
		// Then the robot computes the path it has to follow, in a new thread
		(new Thread(this.pathComputer)).start();
		this.performanceMeasure();
	}

	/** Computes the performance of the robot over the last iterations
	 */
	private void performanceMeasure() {
		float nextPerf = (float) this.environment.getLostPoints() / ((float) this.nbActions + 1);
		//System.out.println("Perf : " + nextPerf);
		float perfDiff = nextPerf - this.performance;
		// If the robot has lost more points than during the previous cycle, decreases the refresh rate
		if (perfDiff > 0 && this.refreshRate - 2 >= 1) {
			this.refreshRate -= 2;
			this.environment.notifyRefreshRateGUI(this.refreshRate);
			//System.out.println("Decreasing refreshing rate.");
		} else if (perfDiff == 0 && this.refreshRate < this.maxRefreshRate) {
			this.refreshRate++;
			this.environment.notifyRefreshRateGUI(this.refreshRate);
			//System.out.println("Increasing refreshing rate.");
		}
		this.performance = nextPerf;
		//System.out.println("Refresh rate : " + this.refreshRate);
		// Reset the values used to measure performance
		this.nbActions = 0;
		this.environment.resetPoints();
		this.environment.notifyRefreshRateGUI(this.refreshRate);
	}

	@Override
	public void run() {
		// Time between two actions of the robot
		int sleepTime = 1000;
		// Remaining iterations before next internal state update
		int beforeUpdate = 1;
		// The robot runs permanently
		while (true) {
			// First, the robot asks the environment for a map, and updates its internal state
			if (beforeUpdate <= 0) {
				this.observeEnvironment();
				this.updateState();
				beforeUpdate = this.refreshRate;
			}
			// Then, it chooses an action
			this.chooseAction();
			try {
				TimeUnit.MILLISECONDS.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			beforeUpdate--;
			//System.out.println("Before Update : " + beforeUpdate);
		}
	}

}