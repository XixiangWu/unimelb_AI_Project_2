package aiproj.slider.brain;

import java.util.ArrayList;

import aiproj.slider.Move;
import aiproj.slider.Referee.Piece;
import aiproj.slider.gameobject.Board;
import aiproj.slider.gameobject.Player;

public class BoardEvaluateAlgorithm {

	public BoardEvaluateAlgorithm() {}
	
	/** this method is used to analyze the whole game in current state, it assumes that the bs is at the 
	 * current state(which is waiting player's move), move is the testing move which a score needs to be 
	 * provided after evaluating */
	private static float BEA(BrainState bs, Move[] moveLst) {
		
		Piece playerTurn = bs.turn;
		Piece oppoTurn = (playerTurn == Piece.HSLIDER) ? Piece.VSLIDER : Piece.VSLIDER;
		Piece roundTurn;
		
		// Step 1: Piece point
		for (int i = 0; i<moveLst.length; i++) {
			
			roundTurn = (i%2==0) ? playerTurn : oppoTurn;
			
			SmartPiece sp = retrieveSmartPiece(moveLst[i].i, 
											   moveLst[i].j,
											   roundTurn);
			
			
		}
		
		
		return 0.0f;
	}
	
	
	/** Retrieve SmartPiece from */
	public static SmartPiece retrieveSmartPiece(int x, int y, Piece turn) {
		
		SmartPiece sp = null;
		
		if (turn == Piece.VSLIDER) {
			
		}
		
		return sp;
		
	}
	/** Determine if the move is on the way */
	private static float OSACompatibleTest(BrainState bs, Move move) {
		
		// Determine which Piece is
		
		for ()
		
		
		
		return 0.0f;
	}
	
	
	

	//========== Preset scores =============
	public static final float OSA_PATH_SCORE = 2.0f; 
}
