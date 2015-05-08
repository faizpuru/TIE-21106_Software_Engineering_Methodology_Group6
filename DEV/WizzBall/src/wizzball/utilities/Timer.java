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

	public int getSecondsLeft() {
		if (pause)
			return timeLeft / 1000;
		return (timeLeft - (parent.millis() - startTime)) / 1000 + 1;
	}

	private int getMilliSecondsLeft() {
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
		parent.strokeWeight(10);

		parent.smooth(8);

		parent.ellipse(x, y, size+1, size);

		parent.stroke(255, 25, 25);
		parent.arc((float) x, (float) y, (float) size, (float) size, -PApplet.PI / 2, (float) angle);

		parent.pushStyle();
		parent.stroke(0);
		for (int i = 0; i <= 4; i++) {
			parent.strokeWeight(2);
			float ang = i * (PApplet.PI / 2);
			parent.line(x + (size * 0.8f + 3) * PApplet.cos((float) ang), y + (size * 0.8f + 3) * PApplet.sin((float) ang), x + (size + 3) * PApplet.cos((float) ang), y
					+ (size + 3) * PApplet.sin((float) ang));
			parent.strokeWeight(1);
			for (int j = 0; j < 3; j++) {
				ang = ang + PApplet.PI / 8;
				parent.line(x + (size * 0.8f + 3) * PApplet.cos((float) ang), y + (size * 0.8f + 3) * PApplet.sin((float) ang), x + (size + 3) * PApplet.cos((float) ang), y
						+ (size + 3) * PApplet.sin((float) ang));
			}
		}

		float s = 2 * size + 22;
		parent.image(parent.watch, x - s / 2, y - s / 2 - 3, s, s);
		parent.popStyle();

		parent.popStyle();

		parent.pushStyle();
		parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parent.fill(200, 250, 38);
		int sFont = 30;
		parent.textFont(parent.fontDigital, sFont);
		parent.text(getSecondsLeft(), x, y);
		parent.popStyle();
	}
}
