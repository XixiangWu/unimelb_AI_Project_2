package aiproj.slider.brain;

import java.util.ArrayList;
import java.util.HashMap;

import aiproj.slider.Move.Direction;
import aiproj.slider.brain.BrainState.Piece;
import aiproj.slider.gameobject.Board;
import aiproj.slider.gameobject.Coordinate;

public class OptimisedSearchAlgorithm {
	
	// General attributes
	private Board board;
	private Piece turn;
	private Boolean needTransform;
	private int n;
	public static enum DIRECTION_MANIPULATION {APPROACH_GOAL, TURN};
	public static enum GOAL {SHORTEST_PATH, BLOCK_TARGET}
	public static enum EDGE {UP, DOWN, LEFT, RIGHT}; 
	public static enum OSA_STATE {FINISHED, NEED_RECALC, UNIFINSHED, PATTERN_FOUND};
	public static enum PATTERN {CORNER, FORCE_DRAW}; // TODO: Implement this pattern
	public HashMap<SmartPiece, OSA_STATE> osaStateMap;
	
	
	/** An optimized Depth first search algorithm for obtaining a general calculated path to goal 
	 * coordinate. It combines both the advantages of DFS and BFS algorithm. This algorithm assumes
	 * that the given board is not transformed */
	public OptimisedSearchAlgorithm(Board board, int n, Piece turn) {
		
		this.board = board;
     	this.turn = turn;
		this.n = n;
		osaStateMap = new HashMap<SmartPiece,OSA_STATE>();
		
	}
	
	/** Use algorithm to return an ArrayList which contains the "shortest path" to goal for each piece.
	 *  The "shortest path" is a subjective expression which the path generated by this algorithm is 
	 *  not the most optimized solution. The algorithm will not only generate one solution at a time (for 
	 *  instance some times two path or three paths have same weight) */
	public ArrayList<ArrayList<Coordinate>> OptimisedSearchAlgorithmEdge(SmartPiece p) {
		
		needTransform = (p.turn == Piece.VSLIDER) ? true : false;
		
//		System.out.println("===========OSA=============");

		// INIT: Abort flag: means no move can be determined
		boolean shouldAbort = false;
		
		// INIT: Starting Flag
		boolean didStart = false;
		
		//INIT: Temporary Coordinate
		Coordinate tempCoor = (needTransform) ? new Coordinate(p.co.y, p.co.x) : new Coordinate(p.co.x, p.co.y);
		
		// INIT: optimizedPathTable
		ArrayList<ArrayList<Coordinate>> optimizedPathTable = new ArrayList<ArrayList<Coordinate>>();
		
		
		// INIT: Transform the board, after this line, all the moves are considered from left to right.
		Piece[][] grid = (needTransform) ? boardTransformation(board.getGrid(), board.getN()) : board.getGrid();
		
		// INIT: keep tracing the turning point in OSA, find all shortest possibilities
		ArrayList<Coordinate> turningPointList = new ArrayList<Coordinate>();
		ArrayList<Coordinate> turningPointListCopy = new ArrayList<Coordinate>();
		
		// Move priority: [RIGHT](APPROACH_GOAL) > [UP > DOWN](TURN) | 
		DIRECTION_MANIPULATION[] dirSeq = {DIRECTION_MANIPULATION.APPROACH_GOAL, DIRECTION_MANIPULATION.TURN};
		
		// traking the turning point | Move DOWN then UP
		while ((!didStart) || (turningPointList.size()!=0)) {
		
			// INIT: a coordinate path d
			ArrayList<Coordinate> optimizedPath = new ArrayList<Coordinate>();
			
			didStart = true;
			
			Coordinate tempTurningPoint = null;
			if (turningPointList.size()!=0) {
				tempTurningPoint = turningPointList.get(turningPointList.size()-1);
			}
			
			// while the algorithm think it is worthy to keep finding a path and the temporary coordinate did not
			// sit in the wining edge of the board, the loop keeps going
			while (!shouldAbort && tempCoor.x != n-1) {
								
				// Execute two times, move forward is eligible, otherwise determine move UP or move DOWN
				for (DIRECTION_MANIPULATION d: dirSeq) {
					
					if (d == DIRECTION_MANIPULATION.APPROACH_GOAL) {	
						
	//					System.out.println(" Approaching goal | can move to next :"+tempCoor.x+" "+tempCoor.y);
						if (grid[tempCoor.x+1][tempCoor.y] != Piece.BLOCK){
							// if the next right cell is not block, just move!
							tempCoor.x++;
							optimizedPath.add((needTransform) ? new Coordinate(tempCoor.y, tempCoor.x) : new Coordinate(tempCoor.x, tempCoor.y));
							break;
						}
						
						// Ensure the array wont out of index
						if (tempCoor.y+1==n) { // reach the top edge
							if (grid[tempCoor.x][tempCoor.y-1] != Piece.BLOCK) {
								tempCoor.y--;
								optimizedPath.add((needTransform) ? new Coordinate(tempCoor.y, tempCoor.x) : new Coordinate(tempCoor.x, tempCoor.y));
								break;
							}
						} else if (tempCoor.y-1<0) {// reach the bottom edge
							if (grid[tempCoor.x][tempCoor.y+1] != Piece.BLOCK) {
								tempCoor.y++;
								optimizedPath.add((needTransform) ? new Coordinate(tempCoor.y, tempCoor.x) : new Coordinate(tempCoor.x, tempCoor.y));
								break;
							}
						}
						
						// y plus minus 1 wont trigger the array index out of range exception
						if (grid[tempCoor.x][tempCoor.y+1] == Piece.BLOCK) { // Top is blocked
							if (grid[tempCoor.x][tempCoor.y-1] != Piece.BLOCK) {
								tempCoor.y--;
								optimizedPath.add((needTransform) ? new Coordinate(tempCoor.y, tempCoor.x) : new Coordinate(tempCoor.x, tempCoor.y));
								break;
							}
						}
						if (grid[tempCoor.x][tempCoor.y-1] == Piece.BLOCK) { // Top is blocked
							if (grid[tempCoor.x][tempCoor.y+1] != Piece.BLOCK) {
								tempCoor.y++;
								optimizedPath.add((needTransform) ? new Coordinate(tempCoor.y, tempCoor.x) : new Coordinate(tempCoor.x, tempCoor.y));
								break;
							}
						}
						
						if (grid[tempCoor.x][tempCoor.y+1] == Piece.BLOCK && grid[tempCoor.x][tempCoor.y-1]==Piece.BLOCK) {// cant go up and down, stuck!
							 
							 // abort the calculation and current solution for advisory opinions
							 shouldAbort = true;
							 osaStateMap.put(p, OSA_STATE.UNIFINSHED);
							 //System.out.println("Mission Abort!");
						}
						
					} else if (d == DIRECTION_MANIPULATION.TURN && grid[tempCoor.x+1][tempCoor.y] == Piece.BLOCK){
	//					System.out.println(" Turning point | can move to next :"+tempCoor.x+" "+tempCoor.y);
	
						// can't move forward, blocked by BLOCK cell, try to move UP or DOWN to bypass the BLOCK cell 
						Direction tempDir = potentialBlockingEvaluation(tempCoor, grid);
						
						// if algorithm suggest a path
						if (tempDir != null) {
							
							if (tempDir == Direction.UP) {
								tempCoor.y++;
								optimizedPath.add((needTransform) ? new Coordinate(tempCoor.y, tempCoor.x) : new Coordinate(tempCoor.x, tempCoor.y));
//								turningPointListCopy.add(new Coordinate(tempCoor.x, tempCoor.y));
								break;
							} else {
								tempCoor.y--;
								optimizedPath.add((needTransform) ? new Coordinate(tempCoor.y, tempCoor.x) : new Coordinate(tempCoor.x, tempCoor.y));
//								turningPointListCopy.add(new Coordinate(tempCoor.x, tempCoor.y));
								break;
							}
							
						} else { // first move down, in the next iteration move up
							
							if (tempCoor.equal(tempTurningPoint)) {
								turningPointList.remove(turningPointList.size()-1);
								tempCoor.y++;
							} else {
								turningPointList.add(new Coordinate(tempCoor.x, tempCoor.y));
								turningPointListCopy.add(new Coordinate(tempCoor.x, tempCoor.y));
								tempCoor.y--;
							}
							optimizedPath.add((needTransform) ? new Coordinate(tempCoor.y, tempCoor.x) : new Coordinate(tempCoor.x, tempCoor.y));

						}
						
					}
				}
			}
			
			// Add a flag to the osaStateTable
			if (!shouldAbort) {
				osaStateMap.put(p, OSA_STATE.FINISHED);
			}
			
//			System.out.print("optimezed Path: ");
//			for (Coordinate c:optimizedPath) {
//				System.out.print(c.toString());
//			}
			
//			System.out.println();
			
			optimizedPathTable.add(optimizedPath);
			
//			// calc other derived path
//			if (turningPointListCopy.size() != 0) {
//				addEquivalentPathToTable(
//						optimizedPathTable, 
//						turningPointListCopy, 
//						p.co
//						);
//				turningPointListCopy.remove(0);
//			}
			
			// Clean all the elements 
			tempCoor = (needTransform) ? new Coordinate(p.co.y, p.co.x) : new Coordinate(p.co.x, p.co.y);
			shouldAbort = false;
			
		}
		// calc other derived path
		if (turningPointListCopy.size() == 1) {
			addEquivalentPathToTable(
					optimizedPathTable, 
					turningPointListCopy, 
					p.co
					);
			turningPointListCopy.remove(0);
		}
		
		return optimizedPathTable;
	}
	
	private void addEquivalentPathToTable(ArrayList<ArrayList<Coordinate>> optimizedPathTable, ArrayList<Coordinate> turningPointList, Coordinate co) {
		
		// In the situation when a turning point appears, some alternative path can be derived, for instance:
		//		
		//     ^ - >  - >  - > 
		//	   |
		// H - >	 B
		//
		// is equal to
		//
		// ^ - > - >  - >
		// |
		// H	     B
		//
		
		ArrayList<ArrayList<Coordinate>> tempPathTable = new ArrayList<ArrayList<Coordinate>>();
		
		if (turningPointList.size() > 1) {
			// To many situation, abort!
			return;
		}
		
//		System.out.println("=== Derived === ");
		for (ArrayList<Coordinate> optimizedPath: optimizedPathTable) {
			
			int dir_x, dir_y, times = 0, tpIndex;
			
			Coordinate tempTp = (needTransform) ? new Coordinate(turningPointList.get(0).y, turningPointList.get(0).x) 
												: turningPointList.get(0);
						
			if (!needTransform) {
				dir_x = -1;
				dir_y = optimizedPath.get(optimizedPath.size()-1).y - tempTp.y;
				times = tempTp.x - co.x;
				tpIndex = tempTp.x - 1;
			} else {
				dir_x = optimizedPath.get(optimizedPath.size()-1).x - tempTp.x;
				dir_y = -1;
				times = tempTp.y - co.y;
				tpIndex = tempTp.y - 1;
			}
			
			if (dir_x == 0 || dir_y == 0) {
				break;
			}
			
			for (int j = 0; j < times; j++) {
				
				ArrayList<Coordinate> tempDerivedPath = new ArrayList<Coordinate>();
				
				for (int i = 0; i < optimizedPath.size(); i++) {
				
					if (i >= tpIndex-j && i <= tpIndex) { // if it is the coordinate that needs to be modified
						tempDerivedPath.add(i, new Coordinate(dir_x+ optimizedPath.get(i).x,
								  							  dir_y+ optimizedPath.get(i).y));
					} else {
						tempDerivedPath.add(i, optimizedPath.get(i));
					}
					
				}
				
				tempPathTable.add(tempDerivedPath);
			}

		}
		
		for (ArrayList<Coordinate> tempPath: tempPathTable) {
			
//			System.out.print("derived path: ");
//			for (Coordinate c:tempPath) {
//				System.out.print(c.toString());
//			}
//			System.out.println();
			
			optimizedPathTable.add(tempPath);
		}
		
//		System.out.println("=== Derived Finished ===");

		
		
	}
	
	/** This method takes target piece and return the "shortest" path to block the smart piece */
	private ArrayList<Coordinate> OptimisedSearchAlgorithmBlockTarget(SmartPiece p, Coordinate target) {
		
		// TODO: OSA for target blocking
		
		
		
		return null;
		
	}
	
	public Piece[][] boardTransformation(Piece[][] gridOriginal, int n) {
		
		Piece[][] newGrid = new Piece[n][n];
		
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				newGrid[j][i] = gridOriginal[i][j];
			}
		}
		
		return newGrid;
		
	}
	
	/** This method is used to transform the coordinate when needed(when turn==Piece.VSLIDER) */
	public void coorTransformation(Coordinate coor) {
		coor.transform();
	}
	
	/** This method assumes the received Grid(Board) is transformed(which means all the piece needs 
	 * to be move towards right)*/
	public Direction potentialBlockingEvaluation(Coordinate cc, Piece[][] grid) {
				
		// Create a overall score, the higher the score is, the lower the priority that direction has.
		float UpScore = 0.0f;
		float DownScore = 0.0f;
		
		// Evaluate Up score | Step1: find the shortest(straight) path to edge
		Coordinate gc = determineGoalCoor(new Coordinate(cc.x, cc.y+1), EDGE.RIGHT, n);
		
//		System.out.println(gc.x +" "+gc.y);

		// Evaluate Up score | Step2: evaluate score for up score
		for (int i = cc.x+1; i < gc.x; i++) {
//			System.out.println("Determining \"UP  \": "+i+" "+gc.y+" with value: "+grid[i][gc.y].ordinal()+" | "+(i-cc.x)+" "+(gc.x-cc.x));
			if (grid[i][gc.y] == Piece.VSLIDER) {
				if (grid[i][gc.y] == Piece.VSLIDER) {
					UpScore+=PLAYER_SCORE*(1.0f-(i-1-cc.x)/((float)(gc.x-cc.x))); 
				} else if (grid[i][gc.y] == Piece.HSLIDER){
					UpScore+=OPPON_SCORE*(1.0f-(i-1-cc.x)/((float)(gc.x-cc.x)));
				}
			} else if (grid[i][gc.y] == Piece.HSLIDER) {
				if (grid[i][gc.y] == Piece.VSLIDER) {
					UpScore+=OPPON_SCORE*(1.0f-(i-1-cc.x)/((float)(gc.x-cc.x))); 
				} else if (grid[i][gc.y] == Piece.HSLIDER){
					UpScore+=PLAYER_SCORE*(1.0f-(i-1-cc.x)/((float)(gc.x-cc.x)));
				}
			} else if (grid[i][gc.y] == Piece.BLOCK) {
				UpScore+=BLOCK_SCORE*(1.0f-(i-1-cc.x)/((float)(gc.x-cc.x)));
			}
		}
		
		// Evaluate Down score | Step1: find the shortest(straight) path to edge
		gc = determineGoalCoor(new Coordinate(cc.x, cc.y-1), EDGE.RIGHT, n);

		// Evaluate Down score | Step1: find the shortest(straight) path to edge
		for (int i = cc.x+1; i < gc.x; i++) {
//			System.out.println("Determining \"DOWN\": "+i+" "+gc.y+" with value: "+grid[i][gc.y].ordinal()+" | "+(i-cc.x)+" "+(gc.x-cc.x));
			if (grid[i][gc.y] == Piece.VSLIDER) {
				if (grid[i][gc.y] == Piece.VSLIDER) {
					DownScore+=PLAYER_SCORE*(1.0f-(i-1-cc.x)/((float)(gc.x-cc.x))); 
				} else if (grid[i][gc.y] == Piece.HSLIDER){
					DownScore+=OPPON_SCORE*(1.0f-(i-1-cc.x)/((float)(gc.x-cc.x)));
				}
			} else if (grid[i][gc.y] == Piece.HSLIDER) {
				if (grid[i][gc.y] == Piece.VSLIDER) {
					DownScore+=OPPON_SCORE*(1.0f-(i-1-cc.x)/((float)(gc.x-cc.x))); 
				} else if (grid[i][gc.y] == Piece.HSLIDER){
					DownScore+=PLAYER_SCORE*(1.0f-(i-1-cc.x)/((float)(gc.x-cc.x)));
				}
			} else if (grid[i][gc.y] == Piece.BLOCK) {
				DownScore+=BLOCK_SCORE*(1.0f-(i-1-cc.x)/((float)(gc.x-cc.x)));
			}
		}
		
		
//		for (int i=board.getN()-1; i>=0; i--) {
//			for (int j=0; j<board.getN(); j++) {
//				System.out.print(Board.SYMBOLS[grid[j][i].ordinal()] + " ");
//			}
//			System.out.println();
//		}
//		System.out.println(turn+" Player's piece at "+ cc.x+" "+cc.y +" gets");
//		System.out.println("UpScore: "+ UpScore +" | DownScore: "+DownScore);
		
//		System.out.println("========== Eval ==============");
		
		// Compare two score, go direction with lower score
		if (UpScore > DownScore) {
			return Direction.DOWN;
		} else if (UpScore < DownScore) {
			return Direction.UP;
		} else { // Suggest dont move, record in SmartPiece but can move "DOWN" if there's no way to go
			return null;
		}
		
	}
	
	
	public static Coordinate determineGoalCoor(Coordinate cc, EDGE edge, int n) {
		
		Coordinate gc = null;
		
		switch(edge) {
			case UP: gc = new Coordinate(cc.x, n-1); break;
			case DOWN: gc = new Coordinate(cc.x, 0); break;
			case LEFT: gc = new Coordinate(0, cc.y); break;
			case RIGHT: gc = new Coordinate(n-1, cc.y);break;
			default: break;
		}
		return gc;
	}
	
	// Preset scores
	public static final Float BLOCK_SCORE = 2.0f;
	public static final Float OPPON_SCORE = 0.5f;
	public static final Float PLAYER_SCORE = 0.3f;

}
