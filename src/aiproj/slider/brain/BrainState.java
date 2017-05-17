package aiproj.slider.brain;

import java.util.ArrayList;

import aiproj.slider.Move;
import aiproj.slider.brain.OptimisedSearchAlgorithm.OSA_STATE;
import aiproj.slider.exception.IllegalBrainStateInitialization;
import aiproj.slider.gameobject.Board;

public class BrainState {
	
	public Board board;
	public ArrayList<SmartPiece> pieceListSelf; // Smart piece list of the player itself, 
	public ArrayList<SmartPiece> pieceListOpp; // Smart piece list of the opponent
	public Piece turn;
	public OptimisedSearchAlgorithm osa;

	/** Enumeration of all of the possible states of a board position */
	public static enum Piece { BLANK, BLOCK, HSLIDER, VSLIDER, }

	
	/* This is a class that stores all the attributes related to the strategies*/
	protected BrainState(Board board, ArrayList<SmartPiece> pieceListOpp, ArrayList<SmartPiece> pieceListSelf, Piece turn, OptimisedSearchAlgorithm osa) {
		this.board = board;
		this.turn = turn;
		this.pieceListSelf = new ArrayList<SmartPiece>();
		this.pieceListOpp = new ArrayList<SmartPiece>();
		this.pieceListSelf = pieceListSelf;
		this.pieceListOpp = pieceListOpp;
		this.osa = osa;
	}
	
	
	// A Builder Pattern for init all the information needs to be stored in brain.
	public static class BrainStateBuilder {

		private Board board;
		private ArrayList<SmartPiece> pieceListSelf;// Smart piece list of the player itself, 
		private ArrayList<SmartPiece> pieceListOpp; // Smart piece list of the opponent
		private Piece turn;
		private OptimisedSearchAlgorithm osa;		// Algorithm used to analyze the suggest path 
		
		/** Build a new borad */
		public BrainStateBuilder setBoard(int dimension, String board) {
			this.board = new Board(dimension, board);
			return this;
		}
		
		/** Build all the piece list */
		public BrainStateBuilder buildPieceList(char player) throws IllegalBrainStateInitialization {
			pieceListOpp = new ArrayList<SmartPiece>();
			pieceListSelf = new ArrayList<SmartPiece>();
			

			
			if (player=='H') { // Horizontal Piece
				pieceListSelf = this.board.getHlist();
				pieceListOpp = this.board.getVlist();
				this.turn = Piece.HSLIDER;
			} else if (player=='V') { // Vertical Piece
				pieceListSelf = this.board.getVlist();
				pieceListOpp = this.board.getHlist();
				this.turn = Piece.VSLIDER;
			}
			
			return this;
		}
		
		/** Build OSA algorithm for further calculation */
		public BrainStateBuilder buildOSA() {
			
			// INIT: Optimzed Search Algorithm
			this.osa = new OptimisedSearchAlgorithm(board, board.getN(), turn);
			
			return this;
		}
		
		/** Build Smart pieces suggest path */
		public BrainStateBuilder buildSmartPieceByOSA() {
						
			for (SmartPiece sp: pieceListSelf) {
				sp.setup(osa.OptimisedSearchAlgorithmEdge(sp));
			}

			for (SmartPiece sp: pieceListOpp) {
				sp.setup(osa.OptimisedSearchAlgorithmEdge(sp));
			}
			
			return this;
		}
		
		/** build BrainState */
		public BrainState build() {
			return new BrainState(board, pieceListOpp, pieceListSelf, turn, osa);
		}
	}
	
}