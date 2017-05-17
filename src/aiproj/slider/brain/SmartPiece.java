package aiproj.slider.brain;

import java.util.ArrayList;

import aiproj.slider.Move;
import aiproj.slider.Move.Direction;
import aiproj.slider.brain.BrainState.Piece;
import aiproj.slider.brain.OptimisedSearchAlgorithm.OSA_STATE;
import aiproj.slider.gameobject.Coordinate;

public class SmartPiece {
	
	// Foundation attributes
	public Coordinate co;
	public Piece turn;
	public Boolean isOffEdge;
	
	// Derived attributes
	public ArrayList<ArrayList<Coordinate>> pathTableOSA;
	public int noShortestOSA;
	public int wasteStep = 0;
	public OSA_STATE osaState;
	
	/** Smart piece stores all the information that should be known by algorithm and derived attributes for further calculation */
	public SmartPiece(int i, int j, Piece turn) {
		this.co = new Coordinate(i, j); // used for further calculation
		this.turn = turn;
		osaState = OSA_STATE.NEED_RECALC;
		isOffEdge = false;
	}
	
	/** This is a method used for evaluating several possible short path from current position to goal edge. This function will 
	 * generally ignore the piece which is not a BLOCK.*/
	public void setup(ArrayList<ArrayList<Coordinate>> pathTableOSA) {
		
		this.pathTableOSA = new ArrayList<ArrayList<Coordinate>>();
		this.pathTableOSA= pathTableOSA;
		noShortestOSA = pathTableOSA.size();
		osaState = OSA_STATE.FINISHED;
		
//		System.out.println(i+" "+j);
//		for (ArrayList<Coordinate> coList : pathTableOSA) {
//			for (Coordinate co: coList) {
//				System.out.println(co.toString());
//			}
//			System.out.println();
//		}
	}
	
	public void osaResetup(OptimisedSearchAlgorithm osa) {
		if (osaState != OSA_STATE.FINISHED) {
			pathTableOSA = osa.OptimisedSearchAlgorithmEdge(this);
		}
	}
	
	public void updateOSA() {
		
//		// when this piece is moved
//		switch (d) {
//		case UP: this.co.y++; break;
//		case DOWN: this.co.y--; break;
//		case RIGHT: this.co.x++; break;
//		case LEFT: this.co.y++; break;
//		}
		
		//System.out.println(String.format("moved %s to %d %d",turn, co.x,co.y));
		
		// if V is off edge leave it alone and no need to recalc again
		if (isOffEdge) {return;}
		
		// check if the piece is still on the original shortest path 
		boolean isFound = false;
		ArrayList<ArrayList<Coordinate>> newShortestPathTable = new ArrayList<ArrayList<Coordinate>>();
		Coordinate tempFlagCoor = null;
		
		for (ArrayList<Coordinate> coList : pathTableOSA) {
			
			ArrayList<Coordinate> tempCoorList = new ArrayList<Coordinate>();
			
			if (!isFound) {
				
				for (Coordinate co: coList) {	
					if (co.x == this.co.x && co.y == this.co.y) {
					
						// found a match
						isFound = true;
					
						// record this coordinate
						tempFlagCoor = co;
					
						ArrayList<Coordinate> tempCoorListInner = new ArrayList<Coordinate>();
						
						for (Coordinate tempCo: coList) {
							tempCoorListInner.add(tempCo);
						}						
						newShortestPathTable.add(tempCoorListInner);
						break;
					}
				} 
			} else if (coList.contains(tempFlagCoor)) {
				for (Coordinate tempCo: coList) {
					tempCoorList.add(tempCo);
				}
				newShortestPathTable.add(tempCoorList);
			}
			
			
		}
		
		// if it is still on the path
		if (isFound) {
			
			for (ArrayList<Coordinate> path: pathTableOSA) {
				//System.out.println(path.toString());
			}
			// delete every thing else in table and leave only this path
			pathTableOSA = newShortestPathTable;
			osaState = OSA_STATE.FINISHED;
			
		} else { // if it is off the path
			wasteStep++;
			osaState = OSA_STATE.NEED_RECALC;
		}
		
	}
	
	/** Evaluation of all the scores obtained by SmartPiece*/
	public float Eval(Move[] move) {
		
		// INIT: a float score
		float totScore = 0.0f;
		
		return totScore;
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
