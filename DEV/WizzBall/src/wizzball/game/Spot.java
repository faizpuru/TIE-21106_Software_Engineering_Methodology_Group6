/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.game;

import java.util.Vector;

import processing.core.PApplet;
import processing.core.PImage;
import wizzball.objects.weapons.BasicWeapon;
import wizzball.objects.weapons.LaserPistol;
import wizzball.objects.weapons.Pistol;

public class Spot {

	private static final int MAX_LIVES = 5;
	public float x;
	public float y;
	public float radius;
	private float tmpRadius = 0;
	Wizzball parent;
	PImage ball, eyes, mouth, custom;
	int ballCoord, eyesCoord, mouthCoord, customCoord;

	float currentAngle = 0;
	float rotationSpeed = (float) 0;
	float maxspeed = (float) 0.5;
	float friction = (float) 0.01;

	public int lives = 3;
	public int score = 0;
	public int acumulativeScore=0;
	public boolean power = false;
	private BasicWeapon weapon;
	private boolean appearing = true;

	// First version of the Spot constructor;
	// the fields are assigned default values
	public char getDirection() {
		if (rotationSpeed >= 0) {
			return 'r';
		}
		return 'l';
	}

	Spot(Wizzball p) {

		parent = p;
		radius = 20;
		x = (float) 0;
		y = (float) (p.height * 0.5);
	}

	// Second version of the Spot constructor;
	// the fields are assigned with parameters
	Spot(Wizzball p, float xpos, float ypos, float r) {

		parent = p;
		x = xpos;
		y = ypos;
		radius = r;
		ball = p.loadImage("Smiley.png");
		mouth = p.loadImage("mouth.png");
		eyes = p.loadImage("eyes.png");

	}

	public void initSpot() {
		lives = 3;
		score = 0;
		power = false;
	}

	public void display() {

		if (isAppearing()) {
			parent.pushMatrix();
			parent.translate(parent.width / 2, y);
			parent.image(ball, -tmpRadius, -tmpRadius, tmpRadius * 2, tmpRadius * 2);
			if (eyes != null)
				parent.image(eyes, -tmpRadius, -tmpRadius, tmpRadius * 2, tmpRadius * 2);
			if (mouth != null)
				parent.image(mouth, -tmpRadius, -tmpRadius, tmpRadius * 2, tmpRadius * 2);
			if (custom != null)
				parent.image(custom, -tmpRadius, -tmpRadius, tmpRadius * 2, tmpRadius * 2);
			parent.popMatrix();
			tmpRadius += radius / 10;
			if (tmpRadius >= radius) {
				appearing = false;
				tmpRadius = 0;
			}

		} else {
			if (Wizzball.gravity > 0) {
				currentAngle = currentAngle + rotationSpeed;
			} else {
				currentAngle = currentAngle - rotationSpeed;
			}
			parent.pushMatrix();
			parent.translate(parent.width / 2, y);
			parent.rotate(currentAngle);
			parent.image(ball, -radius, -radius, radius * 2, radius * 2);
			if (eyes != null)
				parent.image(eyes, -radius, -radius, radius * 2, radius * 2);
			if (mouth != null)
				parent.image(mouth, -radius, -radius, radius * 2, radius * 2);
			if (custom != null)
				parent.image(custom, -radius, -radius, radius * 2, radius * 2);

			parent.popMatrix();
			if (weapon != null) {
				if (weapon.isShooting()) {
					weapon.weaponEffectAndAnimation();
				}
			}

			friction();
		}

	}

	/**
	 * @return
	 */
	public boolean isAppearing() {
		return appearing;
	}

	/**
	 * 
	 */
	private void setImage(int coord, int image) {
		int xAvatar = coord % 8;
		int yAvatar = (coord - xAvatar) / 8;

		int w = 106;
		int h = 120;

		switch (image) {
		case 1:
			if (coord == -1) {
				ball = parent.loadImage("Smiley.png");
			} else {
				ball = parent.avatars.get(xAvatar * w + xAvatar, yAvatar * h + yAvatar, w, h);
				ball = ball.get(9, 15, 90, 90);
			}
			break;
		case 2:
			if (coord == -1) {
				eyes = parent.loadImage("eyes.png");
			} else {
				eyes = parent.avatars.get(xAvatar * w + xAvatar, yAvatar * h + yAvatar, w, h);
			}
			break;
		case 3:
			if (coord == -1) {
				mouth = parent.loadImage("mouth.png");
			} else {
				mouth = parent.avatars.get(xAvatar * w + xAvatar, yAvatar * h + yAvatar, w, h);
			}
			break;
		case 4:
			custom = parent.avatars.get(xAvatar * w + xAvatar, yAvatar * h + yAvatar, w, h);
			break;
		}

	}

	/**
	 * Reduce the rotation speed with the time
	 */
	private void friction() {
		if (rotationSpeed > 0)
			rotationSpeed -= friction;
		if (rotationSpeed < 0)
			rotationSpeed += friction;

	}

	public void step(float stepx, float stepy) {

		x += stepx;
		y += stepy;
		parent.redraw();

	}

	/**
	 * Positiv value = turn right, negativ = turn leff
	 */
	public void accelerateRotation(double d) {
		if ((d > 0 && rotationSpeed + d < maxspeed) || (d < 0 && rotationSpeed + d > -maxspeed))
			rotationSpeed += d;
	}

	public void incrementLives() {
		if (lives < MAX_LIVES) {
			lives++;
		}

	}

	public void switchPower() {
		changeColour();
		power = !power;
	}

	/**
	 * 
	 */
	private void changeColour() {
		ball.filter(PApplet.INVERT);
	}

	/**
	 * 
	 */
	public void changeBall() {
		Vector<Integer> v = new Vector<Integer>();
		for (int i = 0; i < 4; i++) {
			v.add(i);
		}

		int nextIndex = 0;

		if (!(ballCoord == -1)) {
			nextIndex = v.indexOf(ballCoord) + 1;
			nextIndex = nextIndex <= v.size() - 1 ? nextIndex : -1;
		}
		if (nextIndex == -1) {
			ballCoord = -1;
		} else {
			ballCoord = v.get(nextIndex);
		}
		setImage(ballCoord, 1);

	}

	public void changeEyes() {
		Vector<Integer> v = new Vector<Integer>();
		for (int i = 5; i <= 23; i++) {
			v.add(i);
		}
		v.add(175);
		int nextIndex = 0;

		if (!(eyesCoord == -1)) {
			nextIndex = v.indexOf(eyesCoord) + 1;
			nextIndex = nextIndex <= v.size() - 1 ? nextIndex : -1;
		}
		if (nextIndex == -1) {
			eyesCoord = -1;
		} else {
			eyesCoord = v.get(nextIndex);
		}
		setImage(eyesCoord, 2);

	}

	public void changeMouth() {
		Vector<Integer> v = new Vector<Integer>();
		for (int i = 25; i <= 36; i++) {
			v.add(i);
		}
		v.add(175);

		int nextIndex = 0;

		if (!(mouthCoord == -1)) {
			nextIndex = v.indexOf(mouthCoord) + 1;
			nextIndex = nextIndex <= v.size() - 1 ? nextIndex : -1;
		}
		if (nextIndex == -1) {
			mouthCoord = -1;
		} else {
			mouthCoord = v.get(nextIndex);
		}
		setImage(mouthCoord, 3);
	}

	public void changeCustom() {
		Vector<Integer> v = new Vector<Integer>();
		for (int i = 152; i <= 156; i++) {
			v.add(i);
		}
		v.add(159);
		v.add(161);
		v.add(165);
		v.add(166);
		v.add(170);
		v.add(174);
		v.add(175);

		int nextIndex = v.indexOf(customCoord) + 1;
		nextIndex = nextIndex <= v.size() - 1 ? nextIndex : 0;
		customCoord = v.get(nextIndex);
		setImage(customCoord, 4);
	}

	/**
	 * @param basicWeapon
	 */
	public void switchWeapon(BasicWeapon weapon) {
		this.weapon = weapon;
	}

	/**
	 * 
	 */
	public void activateWeapon() {
		if (weapon != null) {
			weapon.activateWeapon();
			if(weapon instanceof Pistol){
				parent.playGunSound();
			}
			else if(weapon instanceof LaserPistol){
				parent.playRaySound();
			}
		}
	}

	public void appearAnimation() {
		appearing = true;
	}

}
