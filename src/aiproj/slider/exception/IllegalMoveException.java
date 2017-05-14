package aiproj.slider.exception;

/** Simple exception describing a move that fails validation */
public class IllegalMoveException extends Exception {
	public IllegalMoveException(String message) {
		super(message);
	}
}