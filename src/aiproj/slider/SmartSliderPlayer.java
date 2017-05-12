package aiproj.slider;

import aiproj.slider.gameobject.Board;

public class SmartSliderPlayer implements SliderPlayer {

	private Board board;
	
	
	
	@Override
	public void init(int dimension, String board, char player) {
		
		// INIT: Board
		

	
	}

	@Override
	public void update(Move move) {
		// TODO Auto-generated method stub
	}

	@Override
	public Move move() {
		// TODO Auto-generated method stub
		return new Move(0,1,Move.Direction.RIGHT);
	}

}