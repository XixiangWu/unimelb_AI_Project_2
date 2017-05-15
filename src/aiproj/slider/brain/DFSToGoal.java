package aiproj.slider.brain;
import java.util.ArrayList;
import aiproj.slider.Referee.Piece;
import aiproj.slider.gameobject.Board;
import aiproj.slider.gameobject.Coordinate;

public class DFSToGoal {

	public static enum GOAL {SHORTEST_PATH}
	public static enum EDGE {UP, DOWN, LEFT, RIGHT}; 
	public static enum DIRECTION {UP, DOWN, LEFT, RIGHT, CLEAR};
	
	private int n;                 // dimension of grid
    private boolean[][] visited;
    private boolean done = false;
    
    private Piece[][] grid;
	
	private Coordinate goalCoor;
	private Coordinate currCoor;
	private ArrayList[] pathTable;
	
	public DFSToGoal(Coordinate goalCoor, Coordinate currCoor,Board board) {
		this.n = board.getN();
		this.goalCoor = goalCoor;
		this.currCoor = currCoor;
		this.grid = board.getGrid();
		ArrayList<ArrayList<Coordinate>> pathTable = new ArrayList<ArrayList<Coordinate>>(); // may have several different road
	}
	
	public ArrayList<ArrayList<Coordinate>> solvePieceShortestPath(EDGE edge, GOAL goal, SmartPiece sp, Board board) {

		// init current goal position
		Coordinate currPieceCoor = new Coordinate(sp.i, sp.j);
		
		// find the current goal position
		Coordinate goalCoor = determineGoalCoor(currPieceCoor, edge, board);
		
		recursion(currPieceCoor.x, currPieceCoor.y);
		
		return null;
	}
	
	
	/** Using DFS to reach to the provided goal(Edge) */	
	public void recursion(int x, int y) {
		
		int[] DirSequence = {1,2,4}; //TODO: different searching goal
	     // Examine each direction
	     for (int i = 0; i < DirSequence.length; i++) {
	    	 
	    	 System.out.println(x + " " +y);
	    	 
	         switch(DirSequence[i]){
	         case 1: // Up
	             if (goalCoor.x == x && goalCoor.y == y)
	                 continue;
	             if (grid[x][y+1] == Piece.BLANK) {
	                 grid[x][y+1] = Piece.BLANK;
	                 recursion(x,y+1);
	             }
	             break;
	         case 2: // Right
	        	 if (goalCoor.x == x && goalCoor.y == y)
	                 continue;
	             if (grid[x+1][y] == Piece.BLANK) {
	                 grid[x+1][y] = Piece.BLANK;
	                 recursion(x+1,y);
	             }
	             break;
	         case 3: // Down
	        	 if (goalCoor.x == x && goalCoor.y == y)
	                 continue;
	             if (grid[x][y-1] == Piece.BLANK) {
	                 grid[x][y-1] = Piece.BLANK;
	                 recursion(x,y-1);
	             }
	             break;
	         case 4: // Left
	        	 if (goalCoor.x == x && goalCoor.y == y)
	                 continue;
	             if (grid[x-1][y] == Piece.BLANK) {
	                 grid[x-1][y] = Piece.BLANK;
	                 recursion(x-1,y);
	             }
	             break;
	         }
	     }
		
		
		
//		
//		// init current goal position
//		Coordinate currPieceCoor = new Coordinate(sp.i, sp.j);
//		
//		// find the current goal position
//		Coordinate goalCoor = determineGoalCoor(currPieceCoor, edge, board);
//		
//		// a flag indicating whether the next step is blocked
//		DIRECTION dirBlocked = DIRECTION.CLEAR;
//		
//		// start looping to find the shortest path
//		while (!currPieceCoor.equal(goalCoor)) {
//			
//			// Change the GoalCoor
//			if (dirBlocked!=DIRECTION.CLEAR) {
//				modifyGoalCoorWhenBlocked(currPieceCoor, goalCoor, dirBlocked, board);
//			}
//			
//			// Make a turn first and then go straight
//			if (!move_TurnFirst(goalCoor, currPieceCoor, board, sp.turn)) {}
//			
//		}
	}


	public static boolean move_TurnFirst(Coordinate goalCoor, Coordinate currPieceCoor, Board board, Piece turn)  {
		
		if (turn == Piece.HSLIDER) {
			// Goal is at right, so move up and down first (in y coordinate)
			if (goalCoor.y-currPieceCoor.y>=1 && board.canMove(goalCoor.x,goalCoor.y+1)) {
				currPieceCoor.y++;
			} else if (goalCoor.y-currPieceCoor.y<=1 && board.canMove(goalCoor.x,goalCoor.y-1)) {
				currPieceCoor.y--;
			} else if (goalCoor.x-currPieceCoor.x>=1 && board.canMove(goalCoor.x+1,goalCoor.y)) {
				currPieceCoor.x++;
			} else if (goalCoor.x-currPieceCoor.x<=1 && board.canMove(goalCoor.x-1,goalCoor.y)) {
				currPieceCoor.x--;
			}
			return true;
		} else if (turn == Piece.VSLIDER) {
			if (goalCoor.x-currPieceCoor.x>=1 && board.canMove(goalCoor.x+1,goalCoor.y)) {
				currPieceCoor.x++;
			} else if (goalCoor.x-currPieceCoor.x<=1 && board.canMove(goalCoor.x-1,goalCoor.y)) {
				currPieceCoor.x--;
			} else if (goalCoor.y-currPieceCoor.y>=1 && board.canMove(goalCoor.x,goalCoor.y+1)) {
				currPieceCoor.y++;
			} else if (goalCoor.y-currPieceCoor.y<=1 && board.canMove(goalCoor.x,goalCoor.y-1)) {
				currPieceCoor.y--;
			}
			return true;
		}
		return false;
	}
	
	
	public static Coordinate determineGoalCoor(Coordinate currCoor, EDGE edge, Board board) {
		
		Coordinate gc = null;
		
		switch(edge) {
		case UP: gc = new Coordinate(currCoor.x, board.getN()-1); break;
		case DOWN: gc = new Coordinate(currCoor.x, 0); break;
		case LEFT: gc = new Coordinate(0, currCoor.y); break;
		case RIGHT: gc = new Coordinate(board.getN()-1, currCoor.y);	
		default: break;
		}
		return gc;
	}
	
	public static void modifyGoalCoorWhenBlocked(Coordinate currCoor,Coordinate goalCoor, DIRECTION dirBlocked, Board board) {
		
		switch(dirBlocked) {
		case UP: goalCoor.x--; break;  // move goal coordinate left
		case DOWN: goalCoor.x--; break;				// move goal coordinate left
		case LEFT: goalCoor.y--; break;				// move goal coordinate down
		case RIGHT: goalCoor.y--; break;		// move goal coordinate down
		default: break;
		}
	}

}
