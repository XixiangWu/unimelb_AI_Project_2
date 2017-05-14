package aiproj.slider.brain;

import java.util.ArrayList;

import aiproj.slider.Referee.Piece;
import aiproj.slider.exception.IllegalBrainStateInitialization;
import aiproj.slider.gameobject.Board;

public class BrainState {
	
	private Board board;
	private ArrayList<SmartPiece> pieceListSelf; // Smart piece list of the player itself, 
	private ArrayList<SmartPiece> pieceListOpp; // Smart piece list of the opponent
	private Piece turn;
	
	/* This is a class that stores all the attributes related to the strategies*/
	protected BrainState(Board board, ArrayList<SmartPiece> pieceListOpp, ArrayList<SmartPiece> pieceListSelf, Piece turn) {
		

		this.board = board;
		this.turn = turn;
		this.pieceListSelf = new ArrayList<SmartPiece>();
		this.pieceListOpp = new ArrayList<SmartPiece>();
		this.pieceListSelf = pieceListSelf;
		this.pieceListOpp = pieceListOpp;
//		
//		for (SmartPiece sp: board.getHlist()) {
//			System.out.println(sp.i +" "+sp.j+" "+sp.turn);
//		}
	}
	
	public void initBoard(int dimension, String board) {
		this.board = new Board(dimension, board);
	}
	
	// A Builder Pattern for init all the information needs to be stored in brain.
	public static class BrainStateBuilder {

		private Board board;
		private ArrayList<SmartPiece> pieceListSelf; // Smart piece list of the player itself, 
		private ArrayList<SmartPiece> pieceListOpp; // Smart piece list of the opponent
		private Piece turn;
		
		public BrainStateBuilder setBoard(int dimension, String board) {
			this.board = new Board(dimension, board);
			return this;
		}
		
		public BrainStateBuilder buildPieceList(char player) throws IllegalBrainStateInitialization {
			pieceListOpp = new ArrayList<SmartPiece>();
			pieceListSelf = new ArrayList<SmartPiece>();
			
			for (SmartPiece sp: board.getHlist()) {
				System.out.println(sp.i +" "+sp.j+" "+sp.turn);
			}
			
			System.out.println(this.board.toString());
			
			if (player=='H') {
				pieceListSelf = this.board.getHlist();
				pieceListOpp = this.board.getVlist();
				this.turn = Piece.HSLIDER;
			} else if (player=='V') {
				pieceListSelf = this.board.getVlist();
				pieceListOpp = this.board.getHlist();
				this.turn = Piece.VSLIDER;
			}
			return this;
		}
				
		public BrainState build() {
			return new BrainState(board, pieceListOpp, pieceListSelf, turn);
		}
	}
	
}