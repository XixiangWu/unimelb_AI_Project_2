package aiproj.slider.brain;

import java.util.ArrayList;

import aiproj.slider.Referee.Piece;
import aiproj.slider.gameobject.Board;

public class BrainState {
	
	private Piece turn;
	private Board board;
	private ArrayList<SmartPiece> pieceListSelf; // Smart piece list of the player itself, 
	private ArrayList<SmartPiece> pieceListOpp; // Smart piece list of the opponent
	
	
	/* This is a class that stores all the attributes related to the strategies*/
	public BrainState(Piece turn) {
		this.turn = turn;
	}
	
	public void initBoard(int dimension, String board) {
		this.board = new Board(dimension, board);
	}
	
	// A Builder Pattern for init all the information needs to be stored in brain.
	public static class BrainStateBuilder {

		private Board board;
		private ArrayList<SmartPiece> pieceListSelf; // Smart piece list of the player itself, 
		private ArrayList<SmartPiece> pieceListOpp; // Smart piece list of the opponent
		
		public BrainStateBuilder setBoard(int dimension, String board) {
			this.board = new Board(dimension, board);
			return this;
		}
		
		public void buildPieceList() {
			pieceListSelf = board.Vlist;
			pieceListOpp = board.Hlist;
		}
		
		
	}
	
	
}