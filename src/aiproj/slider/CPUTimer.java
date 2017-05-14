package aiproj.slider;

/** Small helper class for nanosecond CPU usage timing */
public class CPUTimer {
	long start = 0; // nanosecond time we started timing
	java.lang.management.ThreadMXBean thread =
		java.lang.management.ManagementFactory.getThreadMXBean();
	
	public CPUTimer() {
		thread.setThreadCpuTimeEnabled(true);
	}

	/** Restart the clock to start timing again from now */
	public void start() {
		start = thread.getCurrentThreadCpuTime();
	}

	/**
	 * Return time since started (clock() can be called multiple times per 
	 * start(), but start() should be called at least once before clock())
	 */
	public long clock() {
		return thread.getCurrentThreadCpuTime() - start;
	}
}