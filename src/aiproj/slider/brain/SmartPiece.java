package aiproj.slider.brain;

import java.util.ArrayList;

import aiproj.slider.Referee.Piece;
import aiproj.slider.gameobject.Board;
import aiproj.slider.gameobject.Coordinate;

public class SmartPiece {
	
	// Foundation attributes
	public int i;
	public int j;
	public Coordinate co;
	public Piece turn;
	// Derived attributes
	public ArrayList<Coordinate> shortestPathToWin;
	public int noShortestSteps;
	
	/** Smart piece stores all the information that should be known by algorithm and derived attributes for further calculation */
	public SmartPiece(int i, int j, Piece turn) {
		this.i = i;
		this.j = j;
		this.co = new Coordinate(i, j); // used for further calculation
		this.turn = turn;
	}
	
	/** For analysing all the information in piece */
	public void setup(Board board) {
		// find the shortest path
		calcShortestPath(board);
	}
	
	public void calcShortestPath(Board board) {
		
	}
	
	
}
