package aiproj.slider;
import java.util.List;

import aiproj.slider.Referee.Piece;
import aiproj.slider.brain.BrainState;
import aiproj.slider.exception.IllegalBrainStateInitialization;
import aiproj.slider.gameobject.Board;

public class SmartSliderPlayer implements SliderPlayer {

	private BrainState bs;
	
	@Override
	public void init(int dimension, String board, char player) {
		
		try {
			bs = new BrainState.BrainStateBuilder().setBoard(dimension, board).buildPieceList(player).build();
		} catch (IllegalBrainStateInitialization e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void update(Move move) {
		
	}

	@Override
	public Move move() {
		// TODO Auto-generated method stub
		
		
		
		
		return new Move(0,2,Move.Direction.UP);
	}
	
	
	
	private int[] minimax(int depth, Piece player,BrainState bs) {
	      // Generate possible next moves in a List 
	      List<Move> nextMoves = bs.board.generateMoves(player);
	 
	      // myself is maximizing; while opp is minimizing
	      int bestScore = (player == bs.turn) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
	      Piece opp = (bs.turn == Piece.HSLIDER) ? Piece.VSLIDER : Piece.HSLIDER;
	      int currentScore;
	      Move bestMove = new Move(0,0,Move.Direction.UP);
	 
	      if (nextMoves.isEmpty() || depth == 0) {
	         // Game over or depth reached, evaluate score
	         bestScore = BEA(bs.board,bestMove);
	      } else {
	         for (Move move : nextMoves) {
	            // Try this move for the current "player"
	            bs.board.move(move, player);
	            if (player == bs.turn) {  // mySeed (computer) is maximizing player
	               currentScore = minimax(depth - 1, opp,bs)[0];
	               if (currentScore > bestScore) {
	                  bestScore = currentScore;
	                  bestMove = move;
	               }
	            } else {  // opp is minimizing player
	               currentScore = minimax(depth - 1, player,bs)[0];
	               if (currentScore < bestScore) {
	                  bestScore = currentScore;
	                  bestMove = move;
	               }
	            }
	            // Undo move
	            bs.board.undoMove(move);
	         }
	      }
	      return new int[] {bestScore, bestMove.i, bestMove.j,bestMove.d.ordinal()};
	   }

}