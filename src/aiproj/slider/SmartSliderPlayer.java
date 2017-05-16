package aiproj.slider;
import aiproj.slider.Move.Direction;
import aiproj.slider.brain.BrainState;
import aiproj.slider.brain.OptimizedSearchAlgorithm;
import aiproj.slider.exception.IllegalBrainStateInitialization;
import aiproj.slider.exception.IllegalMoveException;

public class SmartSliderPlayer implements SliderPlayer {

	private BrainState bs;
	private OptimizedSearchAlgorithm osa;
	
	@Override
	public void init(int dimension, String board, char player) {

		// Init: BrainState for storing all the information that need to be known by Algorithm
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
	
		return new Move(0,1, Direction.RIGHT);
	}


}