import Agents.Manor;
import Agents.Robot;
import GUI.GridFrame;

public class GUILauncher {

	public static void main(String[] args) throws InterruptedException {
		// Creation of the Manor
		Manor manor = new Manor(5,5);
		// Creation of a new robot
		Robot robot = new Robot(manor);
		manor.setRobotPosition(2, 2);
		// Creates the GUI, pass it to the environment
		GridFrame gui = new GridFrame(manor.getRooms());
		manor.setGui(gui);
		// Launches the environment in a new thread
		Thread envThread = new Thread(manor);
		envThread.start();
		// Launches the robot in a new thread
		Thread robotThread = new Thread(robot);
		robotThread.start();
	}

}