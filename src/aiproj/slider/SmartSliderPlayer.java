package aiproj.slider;
import java.util.ArrayList;
import java.util.List;
import aiproj.slider.Referee.Piece;
import aiproj.slider.Move.Direction;
import aiproj.slider.brain.BrainState;
import aiproj.slider.brain.SmartPiece;
import aiproj.slider.exception.IllegalBrainStateInitialization;
import aiproj.slider.exception.IllegalMoveException;
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
		
		System.out.println(String.format("Time usage: %f",timer.clock()/1000000000.0f));
	}

	@Override
	public void update(Move move) {
			final char[] DRE = {'U', 'D', 'L', 'R'};
			bs.board.update(move);
			
		      List<Move> nextMoves = bs.board.generateMoves(bs.turn);
		      for (Move move1 : nextMoves) {
		    	  System.out.format("all moves%d,%d,%c\n",move1.i,move1.j,DRE[move1.d.ordinal()]);
		      }
			
	}


	public Move move() {
		final char[] DRE = {'U', 'D', 'L', 'R'};
		// TODO Auto-generated method stub
		int[] result = minimax(4,bs.turn,bs,Integer.MIN_VALUE, Integer.MAX_VALUE);
		bs.board.update(new Move(result[1],result[2],Move.Direction.values()[result[3]]));
		//System.out.format("new move:%d,%d,%c\n",result[1],result[2],DRE[Move.Direction.values()[result[3]].ordinal()]);
		return new Move(result[1],result[2],Move.Direction.values()[result[3]]);

		
	}
	
	
	 
	private int[] minimax(int depth, Piece player,BrainState bs,int alpha, int beta) {
		 final char[] DRE = {'U', 'D', 'L', 'R'};
		 Move bestMove = new Move(100,100,Move.Direction.RIGHT);
	      // Generate possible next moves in a List 
	      List<Move> nextMoves = bs.board.generateMoves(player);
	      //for (Move move : nextMoves) {
	    	//  System.out.format("all moves%d,%d,%c\n",move.i,move.j,DRE[move.d.ordinal()]);
	      //}
	      ArrayList<Move> pastMoves = new ArrayList<Move>();
	      
	      // myself is maximizing; while opp is minimizing
	      
	      Piece opp = (bs.turn == Piece.HSLIDER) ? Piece.VSLIDER : Piece.HSLIDER;
	      int currentScore;
	      
	 
	      if (nextMoves.isEmpty() || depth == 0) {
	         // Game over or depth reached, evaluate score
	    	  //currentScore = evaluate();
	         currentScore = bs.board.BlockOpps(player)+bs.board.validMoves(player);
	         return new int[] {currentScore, bestMove.i, bestMove.j,bestMove.d.ordinal()};
		      //for (Move ss : pastMoves) {
		    	//  System.out.format("past:%d,%d,%c\n",ss.i,ss.j,DRE[ss.d.ordinal()]);
		      //}
	      } else {
	         for (Move move : nextMoves) {
	            // Try this move for the current "player"
	        	 //System.out.format("moveby:%d,%d,%c\n",move.i,move.j,DRE[move.d.ordinal()]);
				bs.board.update(move);
	            pastMoves.add(move);
	            
	            if (player == bs.turn) {  // my turn is maximizing player
	               currentScore = minimax(depth - 1, opp, bs, alpha, beta)[0];
	               if (currentScore > alpha) {
	                  alpha = currentScore;
	                  bestMove = move;
	                 
	               }
	            } else {  // opp is minimizing player
	               currentScore = minimax(depth - 1, player,bs, alpha, beta)[0];
	               if (currentScore < beta) {
	                  beta = currentScore;
	                  bestMove = move;
	               }
	            }
	            
	            // Undo move
	            bs.board.undoMove(move);
	            if (alpha >= beta) break;
	         }
	         return new int[] {(player == bs.turn) ? alpha : beta, bestMove.i,bestMove.j,bestMove.d.ordinal()};
	      }
	      
	   }
	public int evaluate(){
		return (int)(Math.random()*100);
		
	}

}