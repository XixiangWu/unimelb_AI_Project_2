package aiproj.slider;
import aiproj.slider.Referee.Piece;
import aiproj.slider.brain.BrainState;
import aiproj.slider.gameobject.Board;

public class SmartSliderPlayer implements SliderPlayer {

	private BrainState bs;
	private Board myboard;
	
	@Override
	public void init(int dimension, String board, char player) {
		
		// INIT: Board
		myboard= new Board(dimension,board);

	
	}

	@Override
	public void update(Move move) {
		
	}

	@Override
	public Move move() {
		// TODO Auto-generated method stub
		
		
		
		
		return new Move(0,2,Move.Direction.UP);
	}


}