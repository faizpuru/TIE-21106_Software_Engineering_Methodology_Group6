package wizzball;
import processing.core.*;

public class Spot {
	
	 float x, y, radius;
	 PApplet parent;
	 PImage ball;
	 
	 static float currentAngle = 0;
	 static float rotationSpeed = (float) 0.2;
	 float maxspeed = (float) 0.5;
	 float friction = (float) 0.01;
	 

	
	  
	  // First version of the Spot constructor;
	  // the fields are assigned default values
	  Spot( PApplet p ) {
		  
		  parent = p;
		  radius = 40;
		  x = (float) (p.width*0.25);
		  y = (float) (p.height*0.5);
	  }
	  // Second version of the Spot constructor;
	  // the fields are assigned with parameters
	  Spot( PApplet p, float xpos, float ypos, float r) {
		  
		  parent = p;
		  x = xpos;
		  y = ypos;
		  radius = r;
		  ball = p.loadImage("Smiley.png");

	  }
	  
	  public void display() {
		currentAngle = currentAngle+rotationSpeed;
		parent.translate(x, y);

		parent.rotate(currentAngle);

		parent.image(ball,-radius,-radius,radius*2,radius*2);

		parent.rotate(-currentAngle);
		parent.translate(-x, -y);
		
		friction();




			 
		 }
	  
	
	 /**
	 * Reduce the rotation speed with the time
	 */
	private void friction() {
		 if(rotationSpeed>0)
			 rotationSpeed -= friction;
		 if(rotationSpeed<0)
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
		if((d >0 && rotationSpeed+d < maxspeed) || (d < 0 && rotationSpeed+d > -maxspeed))
			rotationSpeed +=d;	 
	}
}
