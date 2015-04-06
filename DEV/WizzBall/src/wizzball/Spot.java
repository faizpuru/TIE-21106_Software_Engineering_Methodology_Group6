package wizzball;

import processing.core.*;

public class Spot {

	private static final int MAX_LIVES = 5;
	public float x;
	public float y;
	public float radius;
	Wizzball parent;
	PImage ball;

	float currentAngle = 0;
	float rotationSpeed = (float) 0;
	float maxspeed = (float) 0.5;
	float friction = (float) 0.01;

	public int lives = 3;

	// First version of the Spot constructor;
	// the fields are assigned default values
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

	}

	public void display() {
		if (Wizzball.gravity > 0) {
			currentAngle = currentAngle + rotationSpeed;
		} else {
			currentAngle = currentAngle - rotationSpeed;
		}
		parent.pushMatrix();
		parent.translate(parent.width / 2, y);
		parent.rotate(currentAngle);
		parent.image(ball, -radius, -radius, radius * 2, radius * 2);
		parent.popMatrix();

		friction();

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
		if(lives<MAX_LIVES){
			lives++;
		}
		
	}
}
