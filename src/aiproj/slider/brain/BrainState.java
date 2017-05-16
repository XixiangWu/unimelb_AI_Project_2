package aiproj.slider.brain;

import java.util.ArrayList;
import aiproj.slider.Referee.Piece;
import aiproj.slider.exception.IllegalBrainStateInitialization;
import aiproj.slider.gameobject.Board;

public class BrainState {
	
	public Board board;
	public ArrayList<SmartPiece> pieceListSelf; // Smart piece list of the player itself, 
	public ArrayList<SmartPiece> pieceListOpp; // Smart piece list of the opponent
	public Piece turn;
	public OptimizedSearchAlgorithm osa;
	
	/* This is a class that stores all the attributes related to the strategies*/
	protected BrainState(Board board, ArrayList<SmartPiece> pieceListOpp, ArrayList<SmartPiece> pieceListSelf, Piece turn, OptimizedSearchAlgorithm osa) {
		

		this.board = board;
		this.turn = turn;
		this.pieceListSelf = new ArrayList<SmartPiece>();
		this.pieceListOpp = new ArrayList<SmartPiece>();
		this.pieceListSelf = pieceListSelf;
		this.pieceListOpp = pieceListOpp;
		this.osa = osa;
		
		
//		for (SmartPiece sp: board.getHlist()) {
//			System.out.println(sp.i +" "+sp.j+" "+sp.turn);
//		}
	}
	
	
	// A Builder Pattern for init all the information needs to be stored in brain.
	public static class BrainStateBuilder {

		private Board board;
		private ArrayList<SmartPiece> pieceListSelf; // Smart piece list of the player itself, 
		private ArrayList<SmartPiece> pieceListOpp; // Smart piece list of the opponent
		private Piece turn;
		private OptimizedSearchAlgorithm osa;
		
		public BrainStateBuilder setBoard(int dimension, String board) {
			this.board = new Board(dimension, board);
			return this;
		}
		
		public BrainStateBuilder buildPieceList(char player) throws IllegalBrainStateInitialization {
			pieceListOpp = new ArrayList<SmartPiece>();
			pieceListSelf = new ArrayList<SmartPiece>();
			
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
		
		public BrainStateBuilder buildOSA() {
			
			// INIT: Optimzed Search Algorithm
			this.osa = new OptimizedSearchAlgorithm(board, board.getN(), turn);
			
			return this;
		}
		
		public BrainStateBuilder buildSmartPieceByOSA() {
						
			for (SmartPiece sp: pieceListSelf) {
				sp.setup(osa.OptimizedSearchAlgorithmEdge(sp));
			}

			for (SmartPiece sp: pieceListOpp) {
				sp.setup(osa.OptimizedSearchAlgorithmEdge(sp));
			}
			
			return this;
		}
		public BrainState build() {
			return new BrainState(board, pieceListOpp, pieceListSelf, turn, osa);
		}
	}
	
}