/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.utilities;

import processing.core.PApplet;
import wizzball.game.Wizzball;

/**
 * @author francois
 * @date 24/04/2015
 * 
 */
public class Timer {

	private Wizzball parent;
	private int startTime;
	private int timeLeft;
	private boolean pause = true;
	private int initialTime;

	public Timer(Wizzball parent) {
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
	
	public void initMillis(int millis) {
		startTime = parent.millis();
		timeLeft = millis;
		initialTime = millis;
	}

	public int getSecondsLeft() {
		if (pause)
			return timeLeft / 1000;
		return (timeLeft - (parent.millis() - startTime)) / 1000 + 1;
	}

	public int getMilliSecondsLeft() {
		return (timeLeft - (parent.millis() - startTime));
	}

	public boolean isPaused() {
		return pause;
	}

	public void display(int x, int y, int size) {
		float percent = 1 - (float) getSecondsLeft() / (float) initialTime;
		float angle = (float) (2 * PApplet.PI * percent) - (PApplet.PI / 2);

		parent.pushStyle();

		parent.fill(10);
		parent.stroke(230);
		parent.strokeWeight(8);

		parent.ellipse(x, y, size+1, size);
		parent.strokeWeight(9);

		parent.stroke(255, 25, 25);
		parent.arc((float) x, (float) y, (float) size, (float) size, -PApplet.PI / 2, (float) angle);

		parent.stroke(0);		

		parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parent.fill(200, 250, 38);
		int sFont = size;
		parent.textFont(parent.f, sFont);
		parent.text(getSecondsLeft(), x, y);
		parent.popStyle();
	}
}
