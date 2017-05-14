package aiproj.slider.gameobject;

import aiproj.*;

public class PieceV extends Piece {

	static enum legalMoveDirection {UP, LEFT, RIGHT};
	
	public PieceV(int xPos, int yPos) {
		super(xPos, yPos);
		// TODO Auto-generated constructor stub
	}
	
	public Coordinate getCoordinateOfNextMove(legalMoveDirection lmd) {
			switch (lmd) {
				case UP:return new Coordinate(getXPos()+1,getYPos());
				case LEFT:return new Coordinate(getXPos(), getYPos()-1);
				case RIGHT: return new Coordinate(getXPos(), getYPos()+1);
				default:return null;
			}
	}

}
