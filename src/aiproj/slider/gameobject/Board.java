package aiproj.slider.gameobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import aiproj.slider.Move;
import aiproj.slider.brain.SmartPiece;
import aiproj.slider.exception.*;
import aiproj.slider.brain.BrainState.Piece;

/**
 * Referee's (simplified) internal representation of the board,
 * handles validation and rendering
 */
public class Board {
	
	public static java.util.Random rng = new java.util.Random();

	private Piece[][] grid;
	public int hsliders = 0, vsliders = 0, passes = 0;
	private final int n;
	public ArrayList<SmartPiece> Vlist=new ArrayList<SmartPiece>();
	public ArrayList<SmartPiece> Hlist=new ArrayList<SmartPiece>();
	public ArrayList<Move> pastMoves = new ArrayList<Move>();
	public ArrayList<SmartPiece> PieceList = new ArrayList<SmartPiece>();
	
	public ArrayList<SmartPiece> getVlist() {
		return Vlist;
	}


	public ArrayList<SmartPiece> getHlist() {
		return Hlist;
	}
	
	public int getN() {
		return n;
	}
	
	/** represent a board as text for rendering */
	public static final char[] SYMBOLS = {'+', 'B', 'H', 'V'};
	public String toString(){
		StringBuilder s = new StringBuilder(2 * n * n);
		for (int j = n-1; j >= 0; j--) {
			s.append(SYMBOLS[grid[0][j].ordinal()]);
			for (int i = 1; i < n; i++) {
				s.append(' ');
				s.append(SYMBOLS[grid[i][j].ordinal()]);
			}
			s.append('\n');
		}
		return s.toString();
	}

	

	public Piece[][] getGrid() {
		return grid;
	}

	public boolean canMove(int i, int j) {
		if (grid[i][j] == Piece.HSLIDER) {
			// for HSLIDERs, check right, up, and down
			return (i+1 == n) || (grid[i+1][j] == Piece.BLANK)
				|| (j+1 < n && grid[i][j+1] == Piece.BLANK)
				|| (j-1 >= 0 && grid[i][j-1] == Piece.BLANK);
		
		} else if (grid[i][j] == Piece.VSLIDER) {
			// for VSLIDERs, check up, right, and left
			return (j+1 == n) || (grid[i][j+1] == Piece.BLANK)
				|| (i+1 < n && grid[i+1][j] == Piece.BLANK)
				|| (i-1 >= 0 && grid[i-1][j] == Piece.BLANK);
		
		} else {
			// any other square can't be moved
			return false;
		}
	}
	
	public boolean finished() {
		return (hsliders == 0) || (vsliders == 0) || (passes > 1);
	}

		
	
/****************************************************************/
	
	   // Methods created by ourselves
	
		// Another constructor to convert string to pieces configuration
		public Board(int dimension,String board) {
			this.n = dimension;
			this.grid = new Piece[n][n];
			String row;
			Scanner sc=new Scanner(board);

			for (int i = n-1; i >= 0; i--) {
				row = sc.nextLine().replaceAll(" ", "");
				for (int j = 0; j < n; j++) {
					switch(row.charAt(j)){
					case 'H':
						grid[j][i]=Piece.HSLIDER;
						Hlist.add(new SmartPiece(j,i,Piece.HSLIDER));
						hsliders++;
						break;
					case 'V':
						grid[j][i]=Piece.VSLIDER;
						Vlist.add(new SmartPiece(j,i,Piece.VSLIDER));
						vsliders++;
						break;
					case '+':
						grid[j][i]=Piece.BLANK;
						break;
					case 'B':
						grid[j][i]=Piece.BLOCK;
						break;
					}
				}
			}
			sc.close();
		}

	// Method for calculating the number of valid moves
	public int validMoves(Piece turn){
		int moves = 0;
		int i,j;
		if(turn == Piece.HSLIDER){
			for (SmartPiece slider:Hlist){
				i=slider.co.x;
				j=slider.co.y;
				if((i+1 < n && grid[i+1][j] == Piece.BLANK) || (i+1 == n)){
					moves=moves+10;
				}else if(j+1 < n && grid[i][j+1] == Piece.BLANK){
					moves++;
				}else if(j-1 >= 0 && grid[i][j-1] == Piece.BLANK){
					moves++;
				}
				
			}
		}else{
			for (SmartPiece slider:Vlist){
				i=slider.co.x;
				j=slider.co.y;
				if((j+1 < n && grid[i][j+1] == Piece.BLANK )||(j+1 == n)){
					moves=moves+10;
				}else if(i+1 < n && grid[i+1][j] == Piece.BLANK){
					moves++;
				}else if(i-1 >= 0 && grid[i-1][j] == Piece.BLANK){
					moves++;
				}
			}
		}
		
		return moves;
		
	}
	
	//Method for calculating blocked opponents.
	public int BlockOpps(Piece turn){
		int blocks = 0;
		int i,j;
		if(turn == Piece.HSLIDER){
			for (SmartPiece slider:Hlist){
				i=slider.co.x;
				j=slider.co.y;
				if(j-1 >= 0 && grid[i][j-1] == Piece.VSLIDER){
					blocks+=3;
				}
				
			}
		}else{
			for (SmartPiece slider:Vlist){
				i=slider.co.x;
				j=slider.co.y;
				if(i-1 >= 0 && grid[i-1][j] == Piece.HSLIDER){
					blocks++;
				}
			}
		}
		
		return blocks;
		
	}
	
	  public ArrayList<Move> generateMoves(Piece turn) {
		      ArrayList<Move> nextMoves = new ArrayList<Move>(); // allocate List

		      // If game over, i.e., no next move
		      if (finished()) {
		         return nextMoves;   // return empty list
		      }
		 
		      // Search for valid moves and add to the List
				int i,j;
				if(turn == Piece.HSLIDER){
					
					for (SmartPiece slider:Hlist){
						if (slider.isOffEdge==false){
							i=slider.co.x;
							j=slider.co.y;
							if(grid[i][j]== turn){
								if((i+1 < n && grid[i+1][j] == Piece.BLANK) || (i+1 == n)){
									nextMoves.add(new Move(i,j,Move.Direction.RIGHT));
								}
								if(j+1 < n && grid[i][j+1] == Piece.BLANK){
									nextMoves.add(new Move(i,j,Move.Direction.UP));
								}
								if(j-1 >= 0 && grid[i][j-1] == Piece.BLANK){
									nextMoves.add(new Move(i,j,Move.Direction.DOWN));
								}
							}
						}
						
					}
				}else{
					for (SmartPiece slider:Vlist){
						if (slider.isOffEdge==false){
							i=slider.co.x;
							j=slider.co.y;
	
							if(grid[i][j]== turn){
								if((j+1 < n && grid[i][j+1] == Piece.BLANK )||(j+1 == n)){
									
									nextMoves.add(new Move(i,j,Move.Direction.UP));
								}
								if(i+1 < n && grid[i+1][j] == Piece.BLANK){
									nextMoves.add(new Move(i,j,Move.Direction.RIGHT));
								}
								if(i-1 >= 0 && grid[i-1][j] == Piece.BLANK){
									nextMoves.add(new Move(i,j,Move.Direction.LEFT));
								}
							}
						}
					}
		      }

		      return nextMoves;
		   }
	  public void update(Move move,boolean isSimulation){
		  Piece piece;

		// null move
			if (move == null) {
				return;
				} 
			if(isSimulation){
				this.pastMoves.add(move);
				
			}
			piece = grid[move.i][move.j];
		
			// where's the next space?
			int toi = move.i, toj = move.j;
			switch(move.d){
				case UP:	toj++; break;
				case DOWN:	toj--; break;
				case RIGHT:	toi++; break;
				case LEFT:	toi--; break;
			}
			
			if (piece == Piece.HSLIDER && toi == n) {
				grid[move.i][move.j] = Piece.BLANK;
				hsliders--;
				updateList(Hlist,move.i,move.j,toi,toj,2);
				return;

			} else if (piece == Piece.VSLIDER && toj == n){
				grid[move.i][move.j] = Piece.BLANK;
				vsliders--;
				updateList(Vlist,move.i,move.j,toi,toj,2);
				
				return;
			}else{
				
				grid[toi][toj] = piece;
				grid[move.i][move.j]=Piece.BLANK;

				if(piece == Piece.HSLIDER){		
					updateList(Hlist,move.i,move.j,toi,toj,1);
				}else{
					updateList(Vlist,move.i,move.j,toi,toj,1);
				}
			}
			return;
	  }
	  public void undoMove(Move move,boolean isSimulation){
		  Piece piece;
		// null move
			if (move == null) {
				return;
				}
			if(isSimulation){
				this.pastMoves.remove(pastMoves.size()-1);
			}
			// where's the next space?
			int toi = move.i, toj = move.j;
			switch(move.d){
				case UP:	toj++; break;
				case DOWN:	toj--; break;
				case RIGHT:	toi++; break;
				case LEFT:	toi--; break;
			}
			

			// If a piece off the board?
			if (move.d == Move.Direction.RIGHT && toi == n) {
				piece = Piece.HSLIDER;
				hsliders++;
				grid[move.i][move.j]=piece;
				updateList(Hlist,move.i,move.j,toi,toj,3);
				
			} else if (move.d == Move.Direction.UP && toj == n){
				piece = Piece.VSLIDER;
				vsliders++;
				grid[move.i][move.j]=piece;
               
				updateList(Vlist,move.i,move.j,toi,toj,3);
				
			}else{
				piece = grid[toi][toj];
				
				grid[move.i][move.j]=piece;
				
				grid[toi][toj] = Piece.BLANK;
				if(piece == Piece.HSLIDER){
					updateList(Hlist,toi,toj,move.i,move.j,1);		
				}else{
					updateList(Vlist,toi,toj,move.i,move.j,1);
				}
			}

			// no? all good? alright, let's make the move!
			grid[move.i][move.j] = piece;

			return;
	  }
	  
	  public void updateList(ArrayList<SmartPiece> list,int i,int j,int toi,int toj,int command){
		  Iterator<SmartPiece> iterator = list.iterator();
		  
		  while(iterator.hasNext()){
			  SmartPiece piece = iterator.next();
			  if(piece.co.x==i && piece.co.y==j){

				  switch (command){
				  //SWAP
				  case 1:
					 piece.co.x = toi;
					 piece.co.y = toj;
				  //OUT
				  case 2:
					 piece.isOffEdge=true;

				  //IN
				  case 3:
					 piece.isOffEdge=false;
				 }
				 

			  }
		  }
	  }
}