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
	public void setup(ArrayList<ArrayList<Coordinate>> pathTableOSA) {
		
		this.pathTableOSA = new ArrayList<ArrayList<Coordinate>>();
		this.pathTableOSA= pathTableOSA;
		noShortestOSA = pathTableOSA.size();
		
//		System.out.println(i+" "+j);
//		for (ArrayList<Coordinate> coList : pathTableOSA) {
//			for (Coordinate co: coList) {
//				System.out.println(co.toString());
//			}
//			System.out.println();
//		}
		
	}
	
	/** Evaluation of all the scores obtained by SmartPiece*/
	public float Eval(Move[] move) {
		
		// INIT: a float score
		float totScore = 0.0f;
		
		return totScore;
	}
	

	/** Check if a move is in the OSA path, assume that the move is a valid move */
	public boolean checkMoveOSA(Move move) {
		return false;		
		
	//	switch (move.d) {
	//	case UP:
	//		for () {
				
	//		}
	//		break;

	//	default:
	//		break;
		}

	/** For debugging */
//	public String toString() {
//		
//		String message = "";
//		
//		for (ArrayList<Coordinate> coList : pathTableOSA) {
//			for (Coordinate co: coList) {
//				message+=(co.toString());
//			}
//			message+="\n";
//		}
//		
//		return String.format("-------------------\n| x: %d y: %d | Smart Piece | Ideal moving path: \n%s", i,j,message);
//		
//	}
	
}
