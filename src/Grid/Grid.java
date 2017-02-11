package Grid;
public class Grid {

	private int sizeX;
	private int sizeY;
	private Case [][] grid;

	public Grid(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.grid = new Case[sizeX][sizeY];
		// Initializes all the cases of the grid
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				this.grid[i][j] = new Case();
			}
		}
	}

	/** Copy constructor, used to create an independent path for the robot
	 * @param grid
	 */
	public Grid(Grid g) {
		this.sizeX = g.sizeX;
		this.sizeY = g.sizeY;
		this.grid = new Case[sizeX][sizeY];
		// Copies all the cases of the grid
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				this.grid[i][j] = new Case(g.getCase(i, j));
			}
		}
	}

	/**
	 * @return sizeX
	 */
	public int getSizeX() {
		return this.sizeX;
	}

	/**
	 * @return sizeY
	 */
	public int getSizeY() {
		return this.sizeY;
	}

	/**
	 * @return grid
	 */
	public Case[][] getGrid() {
		return this.grid;
	}

	/**
	 * @param grid
	 */
	public void setGrid(Case[][] grid) {
		this.grid = grid;
	}

	/** Returns the [x,y] case of the grid if existing, else null
	 * @param x
	 * @param y
	 * @return grid[x][y]
	 */
	public Case getCase(int x, int y) {
		if (x >= 0 && y >=0 && x < this.sizeX && y < this.sizeY) {
			return this.grid[x][y];
		} else {
			return null;
		}
	}

}