package aiproj.slider;

/** Helper class for storing and validating command-line arguments */
public class Options {

	public final int delay;				 // time in ms to delay rendering
	public final int dimension;			 // dimension of board to play on
	public final Class playerH, playerV; // class names of players to play

	public Options(String[] args) {

		// are there enough arguments?
		if (args.length < 3) {
			printUsageInfoAndExit();
		}

		// check if we also have an optional delay
		if (args.length > 3) {
			this.delay = Integer.parseInt(args[3]);
		} else {
			this.delay = 0; // default to zero (which will be ignored)
		}

		// check dimension of board
		this.dimension = Integer.parseInt(args[0]);
		if (! (dimension > 3) ) {
			System.err.println("invalid dimension: should be > 3");
			System.exit(1);
		}

		// attempt to locate classes provided by name
		String playerHClassName = args[1];
		String playerVClassName = args[2];
		Class playerH = null;
		Class playerV = null;
		try {
			playerH = Class.forName(playerHClassName);
			playerV = Class.forName(playerVClassName);
		} catch (ClassNotFoundException e) {
			System.err.println("invalid class name: "+ e.getMessage());
			System.exit(1);
		}
		this.playerH = playerH;
		this.playerV = playerV;
	}
	

	static void printUsageInfoAndExit() {
		System.err.println("usage: java Referee N playerH playerV [delay]");
		System.err.println("       N - dimension of board to use (N > 3)");
		System.err.println(" playerH - fully qualified name of H player");
		System.err.println(" playerV - fully qualified name of V player");
		System.err.println("   delay - (optional) ms delay between turns");
		System.exit(1);
	}
}