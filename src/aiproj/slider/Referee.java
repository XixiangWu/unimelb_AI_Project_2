/* * * * * * * * *
 * Slider game driver class 'Referee' along with some internal helper classes
 * Note: you should build your own Board representation classes; you will not
 * have access to the Referee.Board class when we test your project after 
 * submission
 *
 * created for COMP30024 Artificial Intelligence 2017
 * by Matt Farrugia <matt.farrugia@unimelb.edu.au>
 */
package aiproj.slider;

import aiproj.slider.exception.IllegalMoveException;
import aiproj.slider.gameobject.Board;
import aiproj.slider.gameobject.Player;

/** 
 * Referee class: Driver for a game of Slider
 * Run this class on the command line using a command like:
 * java aiproj.slider.Referee 6 your.package.PlayerName your.package.PlayerName
 * to play your program PlayerName against itself on a board of size N=6
 * See the specification for more detialed instructions
 */
public class Referee {

	/** Enumeration of all of the possible states of a board position */
	public static enum Piece { BLANK, BLOCK, HSLIDER, VSLIDER, }
	
	/** Load provided classes, and play a game of Slider */
	public static void main(String[] args) {

		/* * * *
		 * first, read and validate command line options
		 */
		Options options = new Options(args);
		
		/* * * *
		 * then, set up the board and initialise the players
		 */

		// create a new board
		Board board = new Board(options.dimension);
		
		// set up timer and time array for profiling
		CPUTimer timer = new CPUTimer(); // nanosecond CPU usage timer
		long[] times = new long[]{0, 0}; // cumulative time spent by each player
		
		// and initialise the players
		SliderPlayer[] players = new SliderPlayer[2];
		try {
			timer.start();
			players[Player.H] = (SliderPlayer)options.playerH.newInstance();
			players[Player.H].init(options.dimension, board.toString(), 'H');
			times[Player.H] += timer.clock();

			timer.start();
			players[Player.V] = (SliderPlayer)options.playerV.newInstance();
			players[Player.V].init(options.dimension, board.toString(), 'V');
			times[Player.V] += timer.clock();	
		} catch (IllegalAccessException | InstantiationException e) {
			System.err.println("player instantiation error: " + e.getMessage());
			System.exit(1);
		}
		
		/* * * *
		 * now, play the game!
		 */

		int turn = Player.H;
		Move previousMove = null;
		String message = null;
		
		render(board);

		// game loop
		while (!board.finished()) {

			// delay
			sleep(options.delay);

			// calculate and time move
			timer.start();
			players[turn].update(previousMove);
			previousMove = players[turn].move();
			times[turn] += timer.clock();

			// validate and perform move
			try {
				board.move(previousMove, Player.pieces[turn]);
			} catch (IllegalMoveException e) {
				// exit game due to violation, leading to loss for players[turn]
				message = e.getMessage();
				break;
			}

			// other player's turn next
			turn = Player.other(turn);
			
			render(board);
		}
		

		/* * * *
		 * game over! finally, display the results
		 */

		if(board.finished()) {
			System.out.println("winner: " + board.winner());
			System.out.println("times:");
			System.out.println(" horizontal ~"+ times[Player.H]/1000000 +"ms");
			System.out.println(" vertical   ~"+ times[Player.V]/1000000 +"ms");
		} else {
			System.out.println("illegal move: "
				+ (turn==Player.H ? "horizontal" : "vertical"));
			System.out.println(" " + message);
			System.out.println(" (move: " + previousMove + ")");
		}
	}

	/** Helper function for rendering a board */
	private static void render(Board board) {
		System.out.println(board);
	}

	/** Helper function for delaying time miliseconds between turns */
	private static void sleep(int time) {
		if (time > 0) {
			try {
				Thread.sleep(time);
			} catch(InterruptedException e) {
				// if interrupted, not much we can do. sleep time is over
			}
		}
	}

}


