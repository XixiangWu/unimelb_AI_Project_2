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
		
		if (this == null || co == null) {
			return false;
		}
		
		if (this.x == co.x && this.y == co.y) {
			return true;
		}
		return false;
	}
	
	/** matrix transformation when the coordinate should be transformed in further implement*/
	public void transform() {
		int temp = x;
		x = y;
		y = temp;
	}
	
	/** To String*/
	public String toString() {
		
		return String.format("[x: %d | y: %d]", x,y);
	}
	
} 