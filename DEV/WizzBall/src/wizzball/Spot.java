package wizzball;
import processing.core.PApplet;

public class Spot {
	
	 float x, y, radius;
	 PApplet parent;
	 

	
	  
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
	  }
  
	  public void display() {
		  
			 parent.ellipse(x, y, radius*2, radius*2);
			 parent.redraw();
			 
		 }
	 public void step(float stepx, float stepy) {
		 
		 x += stepx;
		 y += stepy;
		 parent.redraw();
	        
	  }
	 
}
