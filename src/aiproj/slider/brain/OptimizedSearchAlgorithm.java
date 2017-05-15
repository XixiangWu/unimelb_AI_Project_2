package aiproj.slider.brain;

import aiproj.slider.gameobject.Board;
import aiproj.slider.gameobject.Player;

public class OptimizedSearchAlgorithm {

	Board board;
	Player turn;
	
	
	/** An optimized Depth first search algorithm for obtaining a general calculated path to goal 
	 * coordinate. It combines both the advantages of DFS and BFS algorithm.*/
	public OptimizedSearchAlgorithm(Board board, Player turn) {
		
		this.board = board;
		this.turn = turn;
		
	}
	
	

}
