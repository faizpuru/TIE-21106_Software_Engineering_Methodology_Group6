/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.utilities;

import processing.core.PApplet;

/**
 * @author francois
 * @date 24/04/2015
 */
public class Timer {

	private PApplet parent;
	private int startTime;
	private int timeLeft;
	private boolean pause = true;

	public Timer(PApplet parent) {
		this.parent = parent;
	}

	public void start() {
		startTime = parent.millis();
		pause = false;
	}

	public void stop() {
		timeLeft = 0;
	}

	public void pause() {
		timeLeft = getSecondsLeft()*1000;
		pause = true;
	}

	public void unpause() {
		start();
	}

	public void init(int secs) {
		startTime = parent.millis();
		timeLeft = secs*1000;
	}

	public int getSecondsLeft() {
		if(pause)
			return timeLeft;
		return (timeLeft - (parent.millis() - startTime)) / 1000 + 1;
	}

	public boolean isPaused() {
		return pause;
	}
}
