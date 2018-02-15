package DesertAdventures;

public class Clock {

	long m_timeCycles;
	int m_timeScale;
	boolean m_isPaused;

	static long s_cyclesPerSecond = 1000000;

	static long secondsToCycles(int timeSeconds) {
		return ((long)timeSeconds * s_cyclesPerSecond);
	}

	// WARNING: Dangerous -- only use to convert small
	// durations into seconds.
	static int cyclesToSeconds(long timeCycles) {
		return (int) (timeCycles / s_cyclesPerSecond);
	}

	// Construct a clock. (Notice the use of 'explicit' to
	// prevent automatic conversion from int to Clock.)
	public Clock(int startTimeSeconds)
	{
		m_timeCycles = System.nanoTime();
		m_timeScale=  1;
		m_isPaused= false;
	}

	// Return the current time in cycles. NOTE that we do
	// not return absolute time measurements in floating
	// point seconds, because a 32-bit float doesn't have
	// enough precision. See calcDeltaSeconds().
	long getTimeCycles() {
		return m_timeCycles;
	}

	// Determine the difference between this clock's
	// absolute time and that of another clock, in
	// seconds. We only return time deltas as floating
	// point seconds, due to the precision limitations of 360 7. The Game Loop and
	// Real-Time Simulation
	// a 32-bit float.
	int calcDeltaSeconds(Clock other) {
		long dt = m_timeCycles - other.m_timeCycles;
		return cyclesToSeconds(dt);
	}

	// This function should be called once per frame,
	// with the real measured frame time delta in seconds.
	void update(int dtRealSeconds) {
		if (!m_isPaused) {
			long dtScaledCycles = secondsToCycles(dtRealSeconds * m_timeScale);
			m_timeCycles += dtScaledCycles;
		}
	//	m_timeCycles += dtScaledCycles;
	}

	void setPaused(boolean wantPaused) {
		m_isPaused = wantPaused;
	}

	boolean isPaused() {
		return m_isPaused;
	}

	void setTimeScale(int scale) {
		m_timeScale = scale;
	}

	int getTimeScale() {
		return m_timeScale;
	}

	void singleStep() {
		if (m_isPaused) {
			// Add one ideal frame interval; don't forget
			// to scale it by our current time scale!

			//long dtScaledCycles = secondsToCycles((1.0f / 30.0f) * m_timeScale);

			//m_timeCycles += dtScaledCycles;
		}

	}
}
