package aiproj.slider.gameobject;

public abstract class Piece {

	int xPos;
	int yPos;
	
	public Piece(int xPos, int yPos) {
		
		this.xPos = xPos;
		this.yPos = yPos;
		
	}
		
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
		
}
