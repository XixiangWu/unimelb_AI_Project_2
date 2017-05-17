package aiproj.slider;
import java.util.ArrayList;
import java.util.List;
import aiproj.slider.Referee.Piece;
import aiproj.slider.brain.BoardEvaluateAlgorithm;
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
		

		System.out.println(" ======== next player ========");
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
		
		for (SmartPiece piece: bs.board.getVlist()){
			 if(piece.isOffEdge==false){
				 System.out.format("piece:%d,%d\n",piece.co.x,piece.co.y);
			}
		
		}
		

		if (move!=null) {
		
		Coordinate newCoor = new Coordinate(move.i, move.j);
		
		switch (move.d) {
		case UP: newCoor.y++; break;
		case DOWN: newCoor.y--; break;
		case LEFT: newCoor.x--; break;
		case RIGHT: newCoor.y++; break;

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
		final char[] DRE = {'U', 'D', 'L', 'R'};
		// TODO Auto-generated method stub
		
		int[] result = minimax(4,bs.turn,bs,Integer.MIN_VALUE, Integer.MAX_VALUE);
//		System.out.format("new move:%d,%d,%c\n",result[1],result[2],DRE[Move.Direction.values()[result[3]].ordinal()]);
//		System.out.println("Before update");
//		System.out.println(bs.board.toString());
		if(result[1]==100){
			return null;
		}
		bs.board.update(new Move(result[1],result[2],Move.Direction.values()[result[3]]),false);
//		System.out.println("After update");
//		System.out.println(bs.board.toString());

		
		return new Move(result[1],result[2],Move.Direction.values()[result[3]]);

		
	}
	
	
	 
	private int[] minimax(int depth, Piece player,BrainState bs,int alpha, int beta) {
		 final char[] DRE = {'U', 'D', 'L', 'R'};
		 Move bestMove = new Move(100,100,Move.Direction.RIGHT);
	      // Generate possible next moves in a List 
	      ArrayList<Move> nextMoves = bs.board.generateMoves(player);
	      
	      //for (Move move : nextMoves) {
	    	//  System.out.format("all moves%d,%d,%c\n",move.i,move.j,DRE[move.d.ordinal()]);
	      //}
	     
	      
	      // myself is maximizing; while opp is minimizing
	      
	      Piece opp = (player == Piece.HSLIDER) ? Piece.VSLIDER : Piece.HSLIDER;
	      int currentScore;
	      
	 
	      if (nextMoves.isEmpty() || depth == 0) {
	         // Game over or depth reached, evaluate score

	    	  //currentScore = evaluate();

	    	  currentScore = evaluate();
//	    	 currentScore = ((int)BoardEvaluateAlgorithm.BEA(bs, pastMoves)*1000);

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
	public int evaluate(){
		
		
		return (int)(Math.random()*100);
		
	}

}