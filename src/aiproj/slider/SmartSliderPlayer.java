package aiproj.slider;

import aiproj.slider.Referee.Piece;
import aiproj.slider.exception.IllegalMoveException;
import aiproj.slider.gameobject.Board;

public class SmartSliderPlayer implements SliderPlayer {
	private Board myboard; 
	private Piece[] Hs;
	private Piece[] Vs;
	private Piece player;
	@Override
	public void init(int dimension, String board, char player) {
		// TODO Auto-generated method stub
		if (player=='H'){
			this.player=Piece.HSLIDER;
		}else{
			this.player=Piece.VSLIDER;
		}	
		myboard = new Board(dimension);		
	}

	@Override
	public void update(Move move)  {
	}

	@Override
	public Move move() {
		// TODO Auto-generated method stub
		return new Move(0,1,Move.Direction.RIGHT);
	}

}