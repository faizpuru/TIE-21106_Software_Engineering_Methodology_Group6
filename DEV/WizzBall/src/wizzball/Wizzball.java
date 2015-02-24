// test from MY pc

package wizzball;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import wizzball.Spot;

public class Wizzball extends PApplet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	PFont f;
	String typing = "";
	String player =  "";
	boolean firstStep = false;
	boolean enterTheGame = false;
	int count = 0;
	int x = 50;
	int rad = 60;        // Width of the shape
	float xpos, ypos;    // Starting position of shape    
	float xspeed = (float) 5;  // Speed of the shape
	float yspeed = (float) 5;  // Speed of the shape
	Spot sp1 = null;
	boolean isBounceUp = false;
	boolean isBounceDown = true;
	PImage img;
	PImage floor;
	PImage ceiling;

	float yFont = 250;
	float zFont = -200;
	float xFont = 250;

	public void setup() {
		img = loadImage("space_background.jpg");
		floor = loadImage("MoonFloor.jpg");
		ceiling = loadImage("ceiling.jpg");
		size(500, 500, OPENGL);
		f = createFont("Arial",16,true);
		ellipseMode(RADIUS);
		xpos = width/2;
		ypos = height/2;

	}

	public void draw() {
		img.resize(width, height);
		floor.resize(width, (int) (height*0.2));
		ceiling.resize(width, (int) (height*0.1));
		background(0);
		textFont(f,16);
		fill(200);
		stroke(153);
		text(" Hello, welcome to Wizzball game.\n Please, enter your name and press ENTER...\n" ,50 ,50 );
		text( typing, 50, 100 );
		if ( firstStep )
		{
			clear();
			

			textMode(MODEL);
			textFont(f,20);
			rotateX(PI/6);
			textAlign(CENTER);
			stroke(0);
			strokeWeight(5);



			text("Hello " + player + " , you will enter the game.", xFont, yFont, zFont);
			text("You can move the character using arrows keys.", xFont, yFont+50, zFont);
			text("When the ball bounces up,", xFont, yFont+100, zFont);
			text("you can decelerate it using up arrow", xFont, yFont+150, zFont);
			text("When the ball is coming down,", xFont, yFont+200, zFont);
			text("you can accelerate it using down arrow.", xFont, yFont+250, zFont);
			text("Press TAB to continue...", xFont, yFont+300, zFont);

			yFont--;
		}

		if (enterTheGame){
			rotateX(-PI/6);
			strokeWeight(0);
			
			
			clear();
			background(img); 
			sp1 = new Spot( this, xpos, ypos, 5 );
			sp1.display();
			xpos = (float) (xpos + xspeed * 0.2) ;
			ypos = (float) (ypos + yspeed * 0.5 );
			image(floor, 0, (float) (height*0.8));
			image(ceiling,0, 0);


			//Floor collision 

			if (ypos > height*0.77   ) { //Adjust this number for proper collision with floor

				yspeed = yspeed * -1;
				changeBounce();
			}
			if (ypos < height*0.14 ) { //Adjust this number for proper collision with ceiling

				yspeed = yspeed * -1;
				changeBounce();
			}
			else if ( ypos < 0 )
			{
				yspeed = yspeed * -1;
				changeBounce();
			}

			if ( xpos > width || xpos < 0)
			{
				xspeed *= -1;
				text("bravo" + player ,20 ,20);
			}        
		}
	}

	public void changeBounce(){
		isBounceUp = !isBounceUp;
		isBounceDown = !isBounceDown;
	}

	public void accelerate(){
		if(yspeed>0)
			yspeed = (float) ( yspeed  + 3 );
		else
			yspeed = (float) ( yspeed  - 3 );
	}

	public void decelerate(){
		if(yspeed>0){
			yspeed = (float) ( yspeed  - 3 );
			if(yspeed<0)
				changeBounce();
		}	else{
			yspeed = (float) ( yspeed + 3 );
			if(yspeed>0)
				changeBounce();
		}
	}

	public void keyPressed() {

		if ( key == '\n' && count == 0 ){

			count += 1;
			player = typing;
			typing = "";
			firstStep = true;
		}
		else
		{
			if (keyCode == BACKSPACE) {
				typing = typing.substring(0, typing.length() - 1);
			} 
			else
				if (key != CODED) typing += key; 
		}
		if(keyCode == TAB){
			count += 1;
			enterTheGame=true;
		}

		if ( keyCode == UP ) {
			if ( isBounceUp )
				accelerate();
			else if(isBounceDown)
				decelerate();
		} 
		if ( keyCode == DOWN  )
		{
			if ( isBounceDown )
				accelerate();
			else if(isBounceUp)
				decelerate();
		}
	}
}
