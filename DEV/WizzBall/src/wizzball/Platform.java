package wizzball;
import processing.core.*;

public class Platform {
	
	 float x,width, height, xAbs;
	 boolean down; //if down, platform on floor, !down, platform on ceiling
	 PApplet parent;
	 
	 

	
	  
	  // First version of the platform constructor;
	  // the fields are assigned default values
	  Platform( PApplet p ) {
		  
		  parent = p;
		  height = 50;
		  width =20;
		  xAbs = 0;
		  x = 0;
		  down = false;
	  }
	  // Second version of the platform constructor;
	  // the fields are assigned with parameters
	  Platform( PApplet p, float xpos, float height, float width, boolean up) {
		  
		  parent = p;
		  xAbs = xpos;
		  this.height = height;
		  this.width = width;
		  this.down = up;

	  }
	  
	  public void display() {
		  if(down)  
			  parent.rect((x-width/2 + parent.width/2),(float) (parent.height*0.8-height),width,height);
		  else
			  parent.rect((x-width/2 + parent.width/2),(float) (parent.height*0.1),width,height);
		 }
	  
	  public void recalculatePlatformX(float xpos){
		  x = xAbs - xpos;
	  }
	  

}
