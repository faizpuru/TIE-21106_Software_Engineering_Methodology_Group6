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
 * 
 */
public class Timer {

	private PApplet parent;
	private int startTime;
	private int timeLeft;
	private boolean pause = true;
	private int initialTime;

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
		timeLeft = getMilliSecondsLeft();
		pause = true;
	}

	public void unpause() {
		start();
	}

	public void init(int secs) {
		startTime = parent.millis();
		timeLeft = secs * 1000;
		initialTime = secs;
	}

	public int getSecondsLeft() {
		if (pause)
			return timeLeft / 1000;
		return (timeLeft - (parent.millis() - startTime)) / 1000 + 1;
	}
	
	private int getMilliSecondsLeft(){
		return (timeLeft - (parent.millis() - startTime));
	}

	public boolean isPaused() {
		return pause;
	}

	public void display(int x, int y, int size) {
		float percent = 1 - (float) getSecondsLeft() / (float) initialTime;
		float angle = (float) (2*PApplet.PI * percent) - (PApplet.PI/2);

		parent.pushStyle();
		parent.strokeWeight(2);
		parent.stroke(100);
		parent.smooth(4);

		if (percent >= 0.75){
			parent.stroke(0);
			parent.fill(255, 50, 50);
		}

		parent.ellipse(x, y, size, size);
		parent.line(x, y, x + size * PApplet.cos((float) angle), y + size * PApplet.sin((float) angle));
		parent.popStyle();
	}
}
