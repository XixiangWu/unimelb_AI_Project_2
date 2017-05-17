package aiproj.slider;
import java.util.ArrayList;
import aiproj.slider.brain.BrainState.Piece;
import aiproj.slider.brain.BrainState;
import aiproj.slider.brain.OptimisedSearchAlgorithm.OSA_STATE;
import aiproj.slider.brain.SmartPiece;
import aiproj.slider.exception.IllegalBrainStateInitialization;
import aiproj.slider.gameobject.Coordinate;

public class SmartSliderPlayer implements SliderPlayer {

	private BrainState bs;
	private CPUTimer timer;
	
	@Override
	public void init(int dimension, String board, char player) {
		

		
		timer = new CPUTimer();
		timer.start();
		
		// INIT: BrainState for storing all the information that need to be known by Algorithm
		try {
			
			bs = new BrainState.BrainStateBuilder()
					.setBoard(dimension, board)
					.buildPieceList(player)
					.buildOSA()
					.buildSmartPieceByOSA()
					.build();
			
		} catch (IllegalBrainStateInitialization e) {
			e.printStackTrace();
		}
		
//		System.out.println(String.format("Time usage: %f",timer.clock()/1000000000.0f));
	}

	@Override
	public void update(Move move) {

		
		//update board stored in bs
		
		bs.board.update(move,false);
		
		

		if (move!=null) {
		
		Coordinate newCoor = new Coordinate(move.i, move.j);
		
		switch (move.d) {
		case UP: newCoor.y++; break;
		case DOWN: newCoor.y--; break;
		case LEFT: newCoor.x--; break;
		case RIGHT: newCoor.x++; break;

		default:
			break;
		}
		
		for (SmartPiece sp: bs.pieceListSelf) {
			
			// Retrieve SmartPiece
			if (sp.co.x == newCoor.x && sp.co.y == newCoor.y) {
				
				// Thats the moved piece
				sp.updateOSA();
				
				// The OSA need to be recalculated
				if (sp.osaState == OSA_STATE.NEED_RECALC) {
					sp.osaResetup(bs.osa);
				}
				

			
			}
		}
		
		for (SmartPiece sp: bs.pieceListOpp) {
			
			// Retrieve SmartPiece
			if (sp.co.x == newCoor.x && sp.co.y == newCoor.y) {
				
				// Thats the moved piece
				sp.updateOSA();
				
				// The OSA need to be recalculated
				if (sp.osaState == OSA_STATE.NEED_RECALC) {
					sp.osaResetup(bs.osa);
				}
				
			
			}
		}
		
		}
	}

	public Move move() {
		// TODO Auto-generated method stub
		//Get the score and best move in a integer list by minimax
		int[] result = minimax(5,bs.turn,bs,Integer.MIN_VALUE, Integer.MAX_VALUE);
		//If best move is the initial value return null
		if(result[1]==100){
			return null;
		}
		//update our memory
		bs.board.update(new Move(result[1],result[2],Move.Direction.values()[result[3]]),false);

		return new Move(result[1],result[2],Move.Direction.values()[result[3]]);

		
	}

	// MINIMAX with alpha-beta pruning
	private int[] minimax(int depth, Piece player,BrainState bs,int alpha, int beta) {
		 Move bestMove = new Move(100,100,Move.Direction.RIGHT);
	      // Generate possible next moves in a List 
	      ArrayList<Move> nextMoves = bs.board.generateMoves(player);
	      // myself is maximizing; while opp is minimizing
	      Piece opp = (player == Piece.HSLIDER) ? Piece.VSLIDER : Piece.HSLIDER;
	      int currentScore;
	      
	 
	      if (nextMoves.isEmpty() || depth == 0) {
	         // Game over or depth reached, evaluate score
             /* This evaluation algorithm still have some problem, so we use a simple one instead as a early version*/
	    	 //currentScore = ((int)BoardEvaluateAlgorithm.BEA(bs, pastMoves)*1000);

	         currentScore = bs.board.BlockOpps(player)+bs.board.validMoves(player);
	         

	    	  //currentScore = evaluate();

//	    	  currentScore = evaluate();
	    	 //currentScore = ((int)BoardEvaluateAlgorithm.BEA(bs, bs.board.pastMoves)*1000);

	         //currentScore = bs.board.BlockOpps(player)+bs.board.validMoves(player);

	         return new int[] {currentScore, bestMove.i, bestMove.j,bestMove.d.ordinal()};
		      
	      } else {
	    	  
	         for (Move move : nextMoves) {
	            // Try this move for the current "player"

	        	if(bs.board.canMove(move.i,move.j)){
	        		bs.board.update(move,true);
	        	}
	        	
	            
	            if (player == bs.turn) {  // my turn is maximizing player
	               currentScore = minimax(depth - 1, opp, bs, alpha, beta)[0];
	               if (currentScore > alpha) {
	                  alpha = currentScore;
	                  bestMove = move;
	               }
	            } else {  // opp is minimizing player
	               currentScore = minimax(depth - 1, bs.turn,bs, alpha, beta)[0];
	               if (currentScore < beta) {
	                  beta = currentScore;
	                  bestMove = move;
	               }
	            }
	            
	            // Undo move
	            bs.board.undoMove(move,true);
	            
	            if (alpha >= beta) break;
	         }
	         return new int[] {(player == bs.turn) ? alpha : beta, bestMove.i,bestMove.j,bestMove.d.ordinal()};
	      }
	      
	   }

}
