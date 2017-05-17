package aiproj.slider.brain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PrimitiveIterator.OfDouble;

import com.sun.org.apache.xpath.internal.operations.Bool;

import aiproj.slider.Move;
import aiproj.slider.Move.Direction;
import aiproj.slider.Referee.Piece;
import aiproj.slider.gameobject.Board;
import aiproj.slider.gameobject.Coordinate;
import aiproj.slider.gameobject.Player;
import sun.launcher.resources.launcher;

public class BoardEvaluateAlgorithm {

//	public BoardEvaluateAlgorithm() {}
	
	/** this method is used to analyze the whole game in current state, it assumes that the bs is at the 
	 * current state(which is waiting player's move), move is the testing move which a score needs to be 
	 * provided after evaluating */
	public static float BEA(BrainState bs, Move[] moveLst) {
		
		// overall Score
		float overAllScore = 0.0f;
		
		Piece playerTurn = bs.turn;
		Piece oppoTurn = (playerTurn == Piece.HSLIDER) ? Piece.VSLIDER : Piece.VSLIDER;
		Piece roundTurn;
		
		for (int i = 0; i<moveLst.length; i++) {
			
			// Step 1: Retrieve SmartPiece for every move
			SmartPiece sp;
			HashMap<SmartPiece, Integer> moveTimeMap = new HashMap<SmartPiece, Integer>();
			
			roundTurn = (i%2==0) ? playerTurn : oppoTurn;
			
			if (roundTurn == playerTurn) {
				sp = retrieveSmartPiece(moveLst[i].i, 
										moveLst[i].j,
										bs.pieceListSelf);
			} else {
				sp = retrieveSmartPiece(moveLst[i].i, 
										moveLst[i].j,
										bs.pieceListOpp);
			}
			
			if (sp!=null) {
				if (moveTimeMap.get(sp)!=null) {
					moveTimeMap.put(sp,moveTimeMap.get(sp)+1);
				} else {
					moveTimeMap.put(sp, 0);
				}
			}
			
			// Step 2: analyzing every move score.
			// Step 2.1: comparison against fastest path (for SmartPiece itself)
			overAllScore += OSACompatibleTest(sp, moveLst[i].d, moveTimeMap.get(sp));
			
			System.out.println(sp.toString() + " " + overAllScore);
			
			// Step 2.2: find if the next move will block others move
			overAllScore += OSABlockingTest(bs, sp, moveLst[i].d);
			
		}
		
		
		
		return overAllScore;
	}
	
	
	/** Retrieve SmartPiece from */
	public static SmartPiece retrieveSmartPiece(int x, int y, ArrayList<SmartPiece> pieceLst) {
		
		for (SmartPiece sp: pieceLst) {
			if (sp.co.x == x && sp.co.y == y) {
				return sp;
			}
		}
		
		return null;
		
	}
	/** Determine if the move is the fastest move current */
	public static float OSACompatibleTest(SmartPiece sp, Direction d, int index) {
		
		
		Coordinate co = new Coordinate(sp.co.x, sp.co.y);
		
		switch (d) {
		case UP: co.y++; break;
		case DOWN: co.y--; break;
		case RIGHT: co.x++; break;
		case LEFT: co.y++; break;
		}
		
		// Determine whether the OSA is in the list or not
		for (ArrayList<Coordinate> coList: sp.pathTableOSA) {
			if (coList.get(index).x == co.x && coList.get(index).y == co.y) {
				return OSA_PATH_SCORE*(float)Math.pow(((double)DECREMENT_OSA_PATH),index);
			}
		}
		return -OSA_PATH_SCORE*(float)Math.pow(((double)DECREMENT_OSA_PATH),index);
	}
	
	/** Determine if the move will block others move */
	public static float OSABlockingTest(BrainState bs, SmartPiece sp, Direction d) {
		
		Coordinate co = new Coordinate(sp.co.x, sp.co.y);
		
		float totalScore = 0.0f;
		
		switch (d) {
		case UP: co.y++; break;
		case DOWN: co.y--; break;
		case RIGHT: co.x++; break;
		case LEFT: co.y++; break;
		}
		
		boolean isOnePathBlocked = false;
		
		for (SmartPiece spOpp: bs.pieceListOpp) {
			for (ArrayList<Coordinate> coOppList: spOpp.pathTableOSA) {
				for (Coordinate coOpp: coOppList) {
					
					// blocking others way
					if (co.x == coOpp.x && co.y == coOpp.y) {
						totalScore+=OSA_BLOCK_SCORE*((float)Math.pow(DECREMENT_OSA_PATH, coOppList.indexOf(coOpp)));
						System.out.println(String.format("# Increase %f score because: %s blocked %s",totalScore, co.toString(), coOpp.toString()));
					}
					
					isOnePathBlocked = true;
				}
			}
			if (isOnePathBlocked) {break;}
		}
		
		return totalScore;
	}
	

	//========== Preset scores =============
	public static final float OSA_PATH_SCORE = 2.0f; 
	public static final float DECREMENT_OSA_PATH = 0.8f;
	public static final float OSA_BLOCK_SCORE = 0.5f;
	public static final float INCREMENT_OSA_PATH = 1.1f;

}
