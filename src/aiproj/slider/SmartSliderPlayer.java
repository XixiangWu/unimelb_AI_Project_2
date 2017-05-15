package aiproj.slider;
import aiproj.slider.Move.Direction;
import aiproj.slider.Referee.Piece;
import aiproj.slider.brain.BrainState;
import aiproj.slider.brain.SmartPiece;
import aiproj.slider.exception.IllegalBrainStateInitialization;
import aiproj.slider.exception.IllegalMoveException;
import aiproj.slider.gameobject.Board;

public class SmartSliderPlayer implements SliderPlayer {

	private BrainState bs;
	
	@Override
	public void init(int dimension, String board, char player) {
		
		// Init: BrainState for storing all the information that need to be known by Algorithm
		try {
			
			bs = new BrainState.BrainStateBuilder()
					.setBoard(dimension, board)
					.buildPieceList(player)
					.build();
			
		} catch (IllegalBrainStateInitialization e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void update(Move move) {				
		try {
			if (move != null) {
				bs.board.move(move(), bs.turn);
			}
		} catch (IllegalMoveException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Move move() {
		
		SmartPiece p = null;
		
		Direction d = null;
		
		int timer = 0;
		
		timer++;
			
		while (d == null && timer <= 10) {
			
			timer++;
			
			if (bs.turn == Piece.VSLIDER) {

				p = bs.pieceListSelf.get(Board.rng.nextInt(bs.pieceListSelf.size()-1));

				if(bs.board.canMove(p.i,p.j+1)) {
					d = Direction.UP;
				}	
			}
			
			if (bs.turn == Piece.HSLIDER) {
				
				p = bs.pieceListOpp.get(Board.rng.nextInt(bs.pieceListOpp.size()-1));
				
				if(bs.board.canMove(p.i+1,p.j)) {
					d = Direction.RIGHT;
				}
			}
		}		
		//Board.rng.nextInt(Direction.values().length)
		
		return new Move(p.i,
						p.j,
						d);
	}


}