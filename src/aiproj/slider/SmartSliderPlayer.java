package aiproj.slider;
import java.util.ArrayList;

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