package aiproj.slider.gameobject;

public class Coordinate {
	public int x; 
	public int y; 
	  
	public Coordinate(int x, int y) { 
	  this.x = x; 
	  this.y = y; 
	} 
	
	
	/** Compare coordinate */
	public boolean equal(Coordinate co) {
		if (this.x == co.x && this.y == co.y) {
			return true;
		}
		return false;
	}
} 