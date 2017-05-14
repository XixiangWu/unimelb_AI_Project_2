package aiproj.slider.gameobject;

import aiproj.*;

public class PieceH extends Piece {

	static enum legalMoveDirection {UP, DOWN, RIGHT};
	
	public PieceH(int xPos, int yPos) {
		super(xPos, yPos);

	}
	
	public Coordinate getCoordinateOfNextMove(legalMoveDirection lmd) {
		switch (lmd) {
			case UP:return new Coordinate(getXPos()+1,getYPos());
			case DOWN:return new Coordinate(getXPos()-1, getYPos());
			case RIGHT: return new Coordinate(getXPos(), getYPos()+1);
			default:return null;
		}
	}
	
}
