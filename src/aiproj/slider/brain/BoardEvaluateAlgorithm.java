package aiproj.slider.brain;

import java.util.ArrayList;
import java.util.HashMap;
import aiproj.slider.Move;
import aiproj.slider.Move.Direction;
import aiproj.slider.brain.BrainState.Piece;
import aiproj.slider.gameobject.Coordinate;

public class BoardEvaluateAlgorithm {

	// public BoardEvaluateAlgorithm() {}

	/**
	 * this method is used to analyze the whole game in current state, it
	 * assumes that the bs is at the current state(which is waiting player's
	 * move), move is the testing move which a score needs to be provided after
	 * evaluating
	 */
	public static float BEA(BrainState bs, ArrayList<Move> moveLst, ArrayList<SmartPiece> spLst) {

		// overall Score
		float overAllScore = 0.0f;

		Piece playerTurn = bs.turn;
		Piece oppoTurn = (playerTurn == Piece.HSLIDER) ? Piece.VSLIDER : Piece.VSLIDER;
		Piece roundTurn;
		
		for (int i = 0; i < moveLst.size(); i++) {

			// Step 1: Retrieve SmartPiece for every move
			SmartPiece sp;
			HashMap<SmartPiece, Integer> moveTimeMap = new HashMap<SmartPiece, Integer>();

			roundTurn = (i % 2 == 0) ? playerTurn : oppoTurn;
			
			ArrayList<SmartPiece> tempList = (roundTurn == Piece.HSLIDER) ? bs.board.getHlist() : bs.board.getVlist();

			sp = spLst.get(i);

			if (sp != null) {
				if (moveTimeMap.get(sp) != null) {
					moveTimeMap.put(sp, moveTimeMap.get(sp) + 1);
				} else {
					moveTimeMap.put(sp, 0);
				}
			}

			// Create a next move coordinate for analyzing
			Coordinate co = new Coordinate(sp.co.x, sp.co.y);

			switch (moveLst.get(i).d) {
			case UP:
				co.y++;
				break;
			case DOWN:
				co.y--;
				break;
			case RIGHT:
				co.x++;
				break;
			case LEFT:
				co.y++;
				break;
			}
			
			// if the move go beyond the boundary
			if (co.y > bs.board.getN() && co.x > bs.board.getN()) {
				
				overAllScore += CROSS_EDGE;
				
			} else {
				
				// Step 2: analyzing every move score.
				// Step 2.1: comparison against fastest path (for SmartPiece itself)
				overAllScore += OSACompatibleTest(sp, moveLst.get(i).d, moveTimeMap.get(sp),co);
	
				// Step 2.2: find if the next move will block others move
				overAllScore += OSABlockingTest(bs, sp, moveLst.get(i).d,co);
	
				// Step 2.3: penalty for piece already has wasting move
				overAllScore += wastePenalty(sp,moveLst.get(i).d);

				// Step 2.4: add score when line approaching to others end edge
				overAllScore += lineAddition(sp,bs.board.getN());
				
//				System.out.println(sp.toString() + " " + overAllScore);

			}
		}

		return overAllScore;
	}

	/** Determine if the move is the fastest move current */
	public static float OSACompatibleTest(SmartPiece sp, Direction d, int index, Coordinate co) {

		// Determine whether the OSA is in the list or not
		for (ArrayList<Coordinate> coList : sp.pathTableOSA) {
			
			if (coList.isEmpty()) {return -OSA_PATH_SCORE * (float) Math.pow(((double) DECREMENT_OSA_PATH), index);}
			
			
			if (coList.get(index).x == co.x && coList.get(index).y == co.y) {
				return OSA_PATH_SCORE * (float) Math.pow(((double) DECREMENT_OSA_PATH), index);
			}
		}
		return -OSA_PATH_SCORE * (float) Math.pow(((double) DECREMENT_OSA_PATH), index);
	}

	/** Determine if the move will block others move */
	public static float OSABlockingTest(BrainState bs, SmartPiece sp, Direction d, Coordinate co) {
		float totalScore = 0.0f;

		boolean isOnePathBlocked = false;

		for (SmartPiece spOpp : bs.pieceListOpp) {
			for (ArrayList<Coordinate> coOppList : spOpp.pathTableOSA) {
				for (Coordinate coOpp : coOppList) {

					// blocking others way
					if (co.x == coOpp.x && co.y == coOpp.y) {
						totalScore += OSA_BLOCK_SCORE
								* ((float) Math.pow(DECREMENT_OSA_PATH, coOppList.indexOf(coOpp)));
//						System.out.println(String.format("# Increase %f score because: %s blocked %s", totalScore,
//								co.toString(), coOpp.toString()));

					}

					isOnePathBlocked = true;
				}
			}
			if (isOnePathBlocked) {
				break;
			}
		}

		return totalScore;
	}

	/**
	 * Wasting move will cause the piece has more priority when moving toward
	 * end
	 */
	public static float wastePenalty(SmartPiece sp, Direction d) {

		// if the move direction is not towards to win edge, penalty score will
		// be added
		if (sp.turn == Piece.VSLIDER && d == Direction.UP) {
			return ENCOURAGE_MOVE;
		}

		if (sp.turn == Piece.HSLIDER && d == Direction.RIGHT) {
			return ENCOURAGE_MOVE;
		}

		//return sp.wasteStep * WASTE_PENALTY;
		return 0.0f;
	}
	
	public static float lineAddition(SmartPiece sp, int n) {
		
		float totalScore = 0.0f;
		
		if (sp.turn == Piece.VSLIDER) {
			return LINE_ADDITION*((float)sp.co.y)/n;
		}
		
		if (sp.turn == Piece.HSLIDER) {
			return LINE_ADDITION*((float)sp.co.x)/n;
		}
		
		return 0.0f;
	}

	// ========== Preset scores =============
	public static final float OSA_PATH_SCORE = 20.0f;
	public static final float DECREMENT_OSA_PATH = 0.5f;
	public static final float OSA_BLOCK_SCORE = 100.0f;
	public static final float INCREMENT_OSA_PATH = 15.0f;
	public static final float WASTE_PENALTY = -50f;
	public static final float ENCOURAGE_MOVE = 100f;
	public static final float CROSS_EDGE = 5.0f;
	public static final float LINE_ADDITION = 15.0f;
}
