package Grid;
/* A case which can contain dust, jewels, and/or a robot
 */
public class Case {
	
	private int dustLevel;
	private boolean jewel;
	private boolean robot;
	
	/** Default constructor
	 */
	public Case() {
		this.dustLevel = 0;
		this.jewel = false;
		this.robot = false;
	}
	
	/**
	 * @param dustLevel
	 * @param jewel
	 * @param robot
	 */
	public Case(int dustLevel, boolean jewel, boolean robot) {
		this.dustLevel = dustLevel;
		this.jewel = jewel;
		this.robot = robot;
	}
	
	/** Copy constructor, used to create an independent path for the robot
	 * @param c
	 */
	public Case(Case c) {
		this.dustLevel = c.dustLevel;
		this.jewel = c.jewel;
		this.robot = c.robot;
	}
	
	/**
	 * @return dustLevel
	 */
	public int getDustLevel() {
		return this.dustLevel;
	}
	
	/** Adds some dust (1 level) to the case
	 * @return dustLevel
	 */
	public int addDust() {
		return ++this.dustLevel;
	}
	
	/** Decreases the dust level of a case by 1, if possible
	 * @return dustLevel
	 */
	public int removeDust() {
		if (this.dustLevel >= 1) {
			return --this.dustLevel;
		} else {
			return 0;
		}
	}
	
	/**
	 * @return jewel
	 */
	public boolean hasJewel() {
		return this.jewel;
	}
	
	/** Adds a jewel to the case
	 * @return jewelAdded
	 * True if a jewel has been added, else false
	 */
	public boolean addJewel() {
		boolean jewelAdded = this.jewel;
		this.jewel = true;
		return jewelAdded;
	}
	
	/** Removes the jewel from a case
	 * @return jewelRemoved
	 * True if a jewel has been removed, else false
	 */
	public boolean removeJewel() {
		boolean jewelRemoved = this.jewel;
		this.jewel = false;
		return jewelRemoved;
	}
	
	/**
	 * @return robot
	 */
	public boolean isRobot() {
		return this.robot;
	}
	
	/**
	 * @param robot
	 */
	public void setRobot(boolean robot) {
		this.robot = robot;
	}
	
	/** Overwriting of the toString method, debug use
	 */
	public String toString() {
		return "DL : " + this.dustLevel + " ; J : " + this.jewel + " ; R : " + this.robot;
	}
	
}