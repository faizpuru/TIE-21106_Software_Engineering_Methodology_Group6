package Wizzball;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class Spot extends PApplet {
	
	 float x, y, radius;
	
	  
	  // First version of the Spot constructor;
	  // the fields are assigned default values
	  Spot() {
		  
	    radius = 40;
	    x = (float) (width*0.25);
	    y = (float) (height*0.5);
	  }
	  // Second version of the Spot constructor;
	  // the fields are assigned with parameters
	  Spot(float xpos, float ypos, float r) {
	    x = xpos;
	    y = ypos;
	    radius = r;
	  }
  
	 public void step(float stepx, float stepy) {
	    x += stepx;
	    y += stepy;
	    redraw();
	        
	  }
	 
	 public void draw()
	 {
		 ellipse(x, y, radius*2, radius*2);
		 redraw();
	 }
	  

}
