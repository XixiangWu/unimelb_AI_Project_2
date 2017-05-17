package aiproj.slider.gameobject;

import aiproj.slider.brain.BrainState.Piece;

/** Collection of game helper functions and constants */
public interface Player {
	static final int H = 0, V = 1;
	static final Piece[] pieces = new Piece[]{Piece.HSLIDER, Piece.VSLIDER};
	static int other(int player) { return 1 - player; }
	// 1 - 0 is 1, and 1 - 1 is 0, so 1 - H is V, and 1 - V is H!
}