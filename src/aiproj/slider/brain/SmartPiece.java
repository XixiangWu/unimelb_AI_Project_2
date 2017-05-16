package aiproj.slider.brain;

import java.util.ArrayList;

import aiproj.slider.Move;
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
	public ArrayList<ArrayList<Coordinate>> pathTableOSA;
	public int noShortestOSA;
	public int wasteStep = 0;
	
	/** Smart piece stores all the information that should be known by algorithm and derived attributes for further calculation */
	public SmartPiece(int i, int j, Piece turn) {
		this.i = i;
		this.j = j;
		this.co = new Coordinate(i, j); // used for further calculation
		this.turn = turn;
	}
	
	/** This is a method used for evaluating several possible short path from current position to goal edge. This function will 
	 * generally ignore the piece which is not a BLOCK.*/
	public void setup(ArrayList<ArrayList<Coordinate>> pathOSA) {
		
		pathOSA = new ArrayList<ArrayList<Coordinate>>();
		this.pathTableOSA= pathOSA;
		noShortestOSA = pathOSA.size();
		
	}
	
	/** Evaluation of all the score obatained by SmartPiece*/
	public float Eval(Move move) {
		
		// INIT: a float score
		float totScore = 0.0f;
		
		// Step 1: 
		
		
		
		return totScore;
	}
	
}
