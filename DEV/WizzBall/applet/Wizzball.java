package Wizzball;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PShape;
import Wizzball.Spot;

public class Wizzball extends PApplet {
	
	PFont f;
	String typing = "";
	String player =  "";
	boolean firstStep = false;
	boolean enterTheGame = false;
	int count = 0;
	PShape square;
	int x = 50;
	int rad = 60;        // Width of the shape
	float xpos, ypos;    // Starting position of shape    
	float xspeed = (float) 2.8;  // Speed of the shape
	float yspeed = (float) 2.2;  // Speed of the shape
	int xdirection = 1;  // Left or Right
	int ydirection = 1;  // Top to Bottom
	int speed = 5;
	Spot sp1 = new Spot();
	
	
	
	public void setup() {
		
		size(500, 500,P3D);
		f = createFont("Arial",16,true);
		square = createShape(RECT, 0, 0, 600, 80);
		square.setFill(color(0, 0, 255));
		ellipseMode(RADIUS);
		xpos = width/2;
		ypos = height/2;
		
	}

	public void draw() {
		
		  background(0);  
		  textFont(f,16);
		  fill(200 );
		  stroke(153);
		  text(" Hello, welcome to Wizzball game.\n Please, enter your name and press TAB...\n" ,50 ,50 );
		  text( typing, 50, 100 );
		  if ( firstStep )
		  {
			clear();
		    text("Hello " + player + " , you will enter the game.\n You can move the character using arrows keys.\n When the ball bounces up,\n you can decelerate it using up arrow \n When the ball is coming down,\n you can accelerate it using down arrow.\n Press RETURN to continue...",50 ,50 );
		  }
		  if ( enterTheGame )  
		  {
			  
			  clear();
			  //sp1 = new Spot(/*xpos, ypos, 5*/);
			  text("hello", 50, 50);
			  ellipse(0, 0, 40, 40);
			  redraw();
			  //sp1.draw();
			  
			  /*
			  xpos = xpos + speed;
			  ypos = ypos + speed;
			  shape(square, 0, 420);
		
			  if (ypos > height || ypos < 0) {
				  // If the object reaches either edge, multiply speed by -1 to turn it around.
				  speed = speed * -1;
			  }
			  if ((ypos < height) ) {
				  // If the object reaches either edge, multiply speed by -1 to turn it around.
				  speed = speed * 1;
			  }                 
		        */                        
		 }
	}
	
	public void keyPressed() {
		
		  if ( key == '\n' && count == 0 ){
			count += 1;
		    player = typing ;
		    typing = "";
		    //clear();
		    firstStep = true;
		  }
		  else
		  {
			  typing = typing + key; 
		  }
		  if ( key == TAB  ){
		    enterTheGame = true;
		    count += 1;
		  }
		  /*
		  if (keyCode == UP){
		      speed = speed -3;
		    } 
		  
		  if (keyCode == DOWN) {
		      speed = speed + 3;
		    } 
		  else{
		    typing += key;
		    println(key);
		  }
		  */
		}
}
