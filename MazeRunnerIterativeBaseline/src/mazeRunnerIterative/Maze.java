package mazeRunnerIterative;

import javafx.scene.canvas.Canvas;

/**
 * <p>
 * Title: Maze
 * </p>
 * 
 * <p>
 * Description: This is the entity class that implements the maze by extending the JavaFX class canvas
 * 
 * 		This is a baseline file that must be updated by the intern in order to function properly.
 * </p>
 * 
 * <p>
 * Copyright: Copyright © 2018 Lynn Robert Carter
 * </p>
 * 
 * @author Lynn Robert Carter
 * @version 3.00	Update the application to JavaFX
 * 
 */

public class Maze extends Canvas {
	
	private static final int MAX_INT = Integer.MAX_VALUE;
	
	// This is a data object that is used to initialize the maze... this really should come from an input file
	private final char[][] THE_DATA = { 
			{'+', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'G', '+', '-', '-', '+'},
			{'|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', '|'},
			{'|', ' ', '+', '-', '-', '+', ' ', '+', '-', '+', '-', '-', '-', '-', '-', '-', '+', '+', ' ', '|'},
			{'|', ' ', '|', ' ', ' ', '|', ' ', '|', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|'},
			{'|', ' ', '|', ' ', '+', '+', ' ', '|', ' ', '|', ' ', '+', '-', '-', '-', '+', ' ', '|', ' ', '|'},
			{'|', ' ', '|', ' ', '+', '+', ' ', '|', ' ', '|', ' ', '|', ' ', ' ', ' ', '|', ' ', '|', ' ', '|'},
			{'|', ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', '|', ' ', '|', ' ', '|', ' ', '|', ' ', '|', ' ', '|'},
			{'|', ' ', '+', '-', '-', '+', '-', '+', ' ', '|', ' ', '+', '-', '+', ' ', '|', ' ', '|', ' ', '|'},
			{'|', ' ', '|', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|', ' ', '|'},
			{'|', ' ', '+', '-', ' ', '|', ' ', '+', ' ', '+', '-', '-', '-', '-', '-', '+', ' ', '|', ' ', '|'},
			{'|', ' ', ' ', ' ', ' ', '|', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|'},
			{'+', '-', '-', '-', ' ', '|', ' ', '|', ' ', '+', '-', '-', '-', '-', '-', '+', ' ', '|', ' ', '|'},
			{'|', ' ', ' ', ' ', ' ', '|', ' ', '|', ' ', '|', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|', ' ', '|'},
			{'|', ' ', '+', '-', ' ', '|', ' ', '|', ' ', '|', ' ', '-', '-', '-', '-', '+', ' ', '|', ' ', '|'},
			{'|', ' ', '|', ' ', ' ', '|', ' ', '|', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|'},
			{'|', ' ', '+', '-', '-', '+', ' ', '|', ' ', '|', ' ', '-', '-', '-', '-', '-', '-', '+', ' ', '|'},
			{'|', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', '|'},
			{'+', '-', '-', '-', '-', '-', '-', '+', ' ', '+', '-', '-', '-', '-', '-', '-', '-', '+', ' ', '|'},
			{'|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|'},
			{'+', '-', 'S', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '+'},
	};


	protected char[][] board;		// This is the actual maze... a set of visited cells and walls
	protected int[][] mark;			// This is the record of chalk marks

	protected int startRow = 0;		// These are the current row and column indexes for our solver
	protected int startCol = 0;

	protected int maxRow = 0;		// These are the height and width of the maze
	protected int maxCol = 0;
	
	protected int nextRow = -1;		// These are the coordinates for the next move if there is one
	protected int nextCol = -1;

	/**********
	 * This constructor establishes a maze, moving the data into the board and establishing
	 * the height and width attributes
	 */
	public Maze(double width, double height){
		// Establish the canvas width and height
		super (width, height);
		
		// Establish the arrays used based on the table constant defined above
		board = new char[THE_DATA.length][THE_DATA[0].length];
		mark = new int[THE_DATA.length][THE_DATA[0].length];
		
		// Establish the walls and the start and goal elements in the maze
		for (int r = 0; r < THE_DATA.length; r++)
			for (int c = 0; c < THE_DATA[0].length; c++) {
				board[r][c] = THE_DATA[r][c];
				if (THE_DATA[r][c] == ' ')
					mark[r][c] = 0;
				else
					mark[r][c] = -1;
			}
		maxRow = board.length;
		maxCol = board[0].length;
	}

	/**********
	 * This is a copy constructor for the maze
	 * 
	 * @param m
	 */
	public Maze(Maze m){
		board = new char[m.board.length][m.board[0].length];
		mark = new int[m.board.length][m.board[0].length];
		for (int r = 0; r < m.board.length; r++)
			for (int c = 0; c < m.board[0].length; c++) {
				board[r][c] = m.board[r][c];
				mark[r][c] = m.mark[r][c];
			}
		maxRow = board.length;
		maxCol = board[0].length;
		startRow = m.startRow;
		startCol = m.startCol;
	}

	/**********
	 * The getter for a cell in the maze
	 * 
	 * @param r		row index
	 * @param c		column index
	 * @return		the character at that position in the maze
	 */
	public char getCell(int r, int c){
		return board[r][c];
	}

	/**********
	 * The getter for the number of marks for a cell in the maze
	 * 
	 * @param r		row index
	 * @param c		column index
	 * @return		the number of marks at that position in the maze
	 */
	public int getNumMarks(int r, int c){
		return mark[r][c];
	}

	/**********
	 * The setter for a cell in the maze
	 * 
	 * @param r		row index
	 * @param c		column index
	 * @param ch	the character to be inserted into the specified cell
	 * @return		the inserted character
	 */
	public char setCell(int r, int c, char ch){
		return board[r][c] = ch;
	}

	/**********
	 * Getter for the index past the last row
	 * 
	 * @return		the limit row index
	 */
	public int getMaxRow(){
		return maxRow;
	}

	/**********
	 * Getter for the index past the last column
	 * @return		the limit column index
	 */
	public int getMaxCol(){
		return maxCol;
	}

	/**********
	 * Routine that sees if the specified cell is one move away from the goal
	 * @return	true if the goal is just one move away, else return false
	 */
	public boolean done(int r, int c){
		for (int row = r - 1; row <= r + 1; row+=2)
			if (row >= 0 && row < maxRow && board[row][c] == 'G') return true;
		for (int col = c - 1; col <= c + 1; col+=2)
			if (col >= 0 && col < maxCol && board[r][col] == 'G') return true;
		return false;
	}

	/**********
	 * Scan the maze to find the start cell and set current row and column to it
	 */
	public void findStart(){
		for (int row = 0; row < maxRow; row++)
			for (int col = 0; col < maxCol; col++)
				if (board[row][col] == 'S'){
					startRow = row;
					startCol = col;
					return;
				}
	}
	
	/**********
	 * makeMove move the maze runner to a new position by determining the number of marks for each
	 * of the four possible moves, up, right, down, and left. If any of those moves is off the
	 * board or is a wall, the value MaxInt is used for that potential move. The method then uses 
	 * a decision tree to find the move to a position that is the smallest, or it tied for the
	 * smallest number of counts.
	 * 
	 * @param r		The row number of the position (0 is the top row)
	 * @param c		The column number of the position (0 is the left side)
	 * @return		Return true if the move can be made, else return false
	 */
	private boolean makeMove(int r, int c) {
		
		// These two comments and the following return must be replaced with the method's code as
		// specified in the assignment
		//we will make the cell empty to make the move
		if (board[r - 1][c] == ' ' && board[r][c - 1] == ' ') {
		nextRow = r;
		nextCol = c - 1;
		return true;
		}
				
		// We will try to move the cell up.
		if (board[r - 1][c] == ' ') {
		nextRow = r - 1;
		nextCol = c;
		return true;
		}
		
		// We will try to move the cell to the right.
		else if (board[r][c + 1] == ' ') {
		nextRow = r;
		nextCol = c + 1;
		return true;
		}
				
		// We will try to move the cell to the left.
		else if (board[r][c - 1] == ' ') {
		nextRow = r;
		nextCol = c - 1;
		return true;
		}
				
		// We will try to move the cell down.
		else if (board[r + 1][c] == ' ') {
		nextRow = r + 1;
		nextCol = c;
		return true;
		}
				
		System.out.println("Error due to Array Bound Index");
		return false;
	}
	

	/**********
	 * solveMaze tries to solve a maze by iteratively moving to a new position after increasing the
	 * number of time this position has been visited. The new position is one that has or is tied 
	 * for the least number of visits. There is a problem with this algorithm, however. Can you
	 * figure out what it might be?
	 * 
	 * @param r		The row of the requested move
	 * @param c		The column of the requested move
	 * @return		Null if no solution is possible, else return a solved maze
	 */
	public Maze solveMaze(int r, int c){
		// Verify that the requested move is actually on the board.  If not, it can't win
		if (r < 0 || r >= maxRow || c < 0 || c >= maxCol) return null;

		// Verify that the specified place to start is not in a wall
		if(board[r][c] != ' ') return null;

		// Make a copy of the board so we can show places visited without destroying the original
		Maze solution = new Maze(this);

		while (! done(r,c)) {

			if (makeMove(r, c)) {
				solution.mark[r][c]++; mark[r][c]++;
				solution.board[r][c] = 'X'; board[r][c] = 'X';
				r = nextRow;
				c = nextCol;
			}
			else {
				return null;
			}
		}

		// When the loop finishes, we must finish updating the marks and the board
		solution.mark[r][c]++; mark[r][c]++;
		solution.board[r][c] = 'X'; board[r][c] = 'X';
		
		// Then we can return the solution to the caller.
		return solution;	
	}
	
	
	/**********
	 * The solveMaze method recognizes that there are possibly four different ways the maze runner
	 * can start moving from the start position. This method tries each of the four, in turn, and
	 * stops looking as soon as it finds one starting direction that has a path to the goal.
	 * 
	 * @return		Null if no solution is possible, else return a solved maze
	 */
	public Maze solveMaze() {
		Maze result;

		// Find the start symbol on the maze and set up that location.
		findStart();
				
		// There are four possible moves: up, right, down, left.  
		result = solveMaze(startRow-1, startCol);		// Try up
		
		if (result == null) {							// If up did not work, it will come 
			result = solveMaze(startRow, startCol+1);	// back null, so try going to the right
			
			if (result == null) {						// If right did not work, try down
				result = solveMaze(startRow-1, startCol);
				
				if (result == null) {					// If not down, try to the left
					result = solveMaze(startRow, startCol-1);
				}
			}
		}
		
		// When we get here, either result is not null and refers to a winning path or it is null
		if (result != null)									// and that means that no path is a
			this.board = result.board;						// winning path
		return this;
	}

	/**********
	 * Debugging toString to help display the current maze to the console
	 * 
	 * The choice of representation makes it trivial to display the maze.
	 */
	public String toString(){
		String str = "";
		for (int i = 0; i < maxRow; i++) {
			for (int j = 0; j < maxCol; j++)
				if (board[i][j] == 'X')
					str += mark[i][j];
				else 
					str += board[i][j];
			str += '\n';
		}
		return str;
	}
}
