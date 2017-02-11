package Grid;

public class Coordinates {
	
	public int x;
	public int y;
	
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/** Computes the distance from a set of coordinates to another
	 * @param c
	 * @return distance
	 */
	public int distance(Coordinates c) {
		return Math.abs(this.x - c.x) + Math.abs(this.y - c.y);
	}
	
	/** Compares two set of coordinates
	 * @param c
	 * @return equals
	 * True if the coordinates are the same, else false
	 */
	public boolean equals(Coordinates c) {
		return c.x == this.x && c.y == this.y;
	}
	
	/** Changes the value of the coordinates according to a given direction
	 * @param dir
	 */
	public void move(Direction dir) {
		switch (dir) {
		case UP: this.x--;
			break;
		case DOWN: this.x++;
			break;
		case LEFT: this.y--;
			break;
		case RIGHT: this.y++;
			break;
		default: System.out.println(" # Unexpected Direction value !");
			break;
		}
	}
	
	public String toString() {
		return "x : " + this.x + " ; y : " + this.y;
	}
	
}