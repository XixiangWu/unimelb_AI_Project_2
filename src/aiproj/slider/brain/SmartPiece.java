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
	public ArrayList<Coordinate> pathOSA;
	public int noShortestOSA;
	public int wasteStep = 0;
	
	// 
	
	
	/** Smart piece stores all the information that should be known by algorithm and derived attributes for further calculation */
	public SmartPiece(int i, int j, Piece turn) {
		this.i = i;
		this.j = j;
		this.co = new Coordinate(i, j); // used for further calculation
		this.turn = turn;
	}
	
	/** For analysing all the information in piece */
	public void setup(ArrayList<Coordinate> pathOSA) {
		
		pathOSA = new ArrayList<Coordinate>();
		this.pathOSA = pathOSA;
		noShortestOSA = pathOSA.size();
		
	}
	
	/** This is a method used for evaluating a possible shortest path from current position to goal edge. 
	 * This function will generally ignore the piece which is not a BLOCK even if it blocks the path to goal
	 * edge at present */
	public void evalPossiblePath(Board board) {
		
	}
	
	
}
