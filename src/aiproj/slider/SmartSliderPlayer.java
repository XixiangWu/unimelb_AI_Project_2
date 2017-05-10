package aiproj.slider;

public class SmartSliderPlayer implements SliderPlayer {

	@Override
	public void init(int dimension, String board, char player) {
		// TODO Auto-generated method stub
		
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