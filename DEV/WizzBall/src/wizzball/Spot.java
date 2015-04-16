/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball;

import processing.core.PImage;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

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
	public int score = 0;

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
		if (lives < MAX_LIVES) {
			lives++;
		}

	}

	public void changeColour() {
		// TODO Auto-generated method stub
		SetInvertir(ball);
		
	}
	 public void SetInvertir(PImage f){
	        this.ball = f;
	        Color color;
	        for(int i=0;i<ball.width;i++){
	          for(int j=0;j< ball.height;j++){
	                //se obtiene el color del pixel
	                color = new Color(ball.get(i, j));
	                //se extraen los valores RGB
	               int r = color.getRed(); 
	               int g = color.getGreen();
	               int b = color.getBlue();
	                //se coloca en la nueva imagen con los valores invertidos
	                ball.set(i, j, new Color(255-r,255-g,255-b).getRGB());                                                                    
	          }
	        }        
	    }
	    
}
