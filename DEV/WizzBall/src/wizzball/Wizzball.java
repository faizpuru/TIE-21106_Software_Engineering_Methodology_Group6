// test from MY pc

package wizzball;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import wizzball.Spot;


public class Wizzball extends PApplet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MAX_SPEED=20;
	private static final int INCR_SPEED=2;

	PFont f;
	String typing = "";
	String player =  "";
	boolean firstStep = false;
	boolean enterTheGame = false;
	int count = 0;
	int x = 50;
	int rad = 60;        // Width of the shape
	float xpos, ypos;    // Starting position of shape    
	
	/*
	 * NEGATIVE SPEED --->  GOING UP
	 * POSITIVE SPEED ---> GOING DOWN
	 */
	float xspeed = (float) 15;  // Speed of the shape  
	float yspeed = (float) 15;  // Speed of the shape
	Spot sp1 = null;
	boolean isBounceUp = false;
	boolean isBounceDown = true;
	PImage img, floor, ceiling, saturn, stars;
	PVector vback, vmiddle, vfront;

	float yFont = 250;
	float zFont = -200;
	float xFont = 250;
	
	float gravity; //positive downwards ---  negative upwards
	boolean isRotated = false;

	public void setup() {
		img = loadImage("space_background.jpg");
		floor = loadImage("moonfloor.jpg");
		ceiling = loadImage("ceiling.jpg");
		stars = loadImage("starsBack.jpg");
		saturn = loadImage("saturn.png");
		size(500, 500, OPENGL);
		f = createFont("Arial",16,true);
		ellipseMode(RADIUS);
		xpos = width/2;
		ypos = height/2;
		
		sp1 = new Spot( this, xpos, ypos, 30 );

		
		gravity=(float) 0.5; //Setup gravity


		vback = new PVector(0, 0);
		vmiddle = new PVector(150, 140);
		//vfront = new PVector(0, 5); //just fixing the position of the image

		frameRate(24);
	}

	public void draw() {


		img.resize(width, height);
		stars.resize(width, height);
		floor.resize(width, (int) (height*0.2));
		ceiling.resize(width, (int) (height*0.1));
		saturn.resize(width/6, height/6);
		background(0);
		textFont(f,15);
		fill(200);
		stroke(153);
		text(" Hello, welcome to Wizzball game.\n Please, enter your name and press ENTER...\n" ,50 ,50 );
		text(typing, 50, 100 );
		if ( firstStep )
		{
			clear();
			
			background(stars);
			textMode(MODEL);
			fill(255,255,0);	
			textFont(f,25);
			rotateX(PI/6);
			isRotated = true;
			textAlign(CENTER);
			stroke(0, 20);
			//strokeWeight(5);

			text("Hello " + player+ ", you will enter the game.", xFont, yFont, zFont);
			text("You can move the character using arrows keys.", xFont, yFont+50, zFont);
			text("When the ball bounces up,", xFont, yFont+100, zFont);
			text("you can decelerate it using up arrow", xFont, yFont+150, zFont);
			text("When the ball is coming down,", xFont, yFont+200, zFont);
			text("you can accelerate it using down arrow.", xFont, yFont+250, zFont);
			text("Press TAB to continue...", xFont, yFont+300, zFont);

			yFont--;
			if (yFont < -300 )
				enterTheGame = true;
		}

		if (enterTheGame){
			if(isRotated)
				rotateX(-PI/6);
			strokeWeight(0);
		


			clear();
			paraDraw(img, vback, 1);
			paraDraw(saturn, vmiddle, 2);
			fill(255,0,0);
			
			
			if((gravity>0)){
				if(isBounceDown){
					float speedTmp=(float) Math.abs(yspeed+gravity); //Control MAX_SPEED		
					if(speedTmp<=MAX_SPEED){
					  yspeed = (float) (yspeed + gravity);
					}	
				}
				else{
					float speedTmp=(float) Math.abs(yspeed+gravity); //Control MAX_SPEED		
					if(speedTmp<=MAX_SPEED){
					  yspeed = (float) (yspeed + gravity);
					}				
				}
				
			}
			else if((gravity<0)){
				if(isBounceUp){
					float speedTmp=(float) Math.abs(yspeed+gravity); //Control MAX_SPEED		
					if(speedTmp<=MAX_SPEED){
						yspeed = (float) (yspeed +gravity);
					}	
				}
				else{
					float speedTmp=(float) Math.abs(yspeed+gravity); //Control MAX_SPEED		
					if(speedTmp<=MAX_SPEED){
						yspeed = (float) (yspeed +gravity);
					}	
				}
				
			}
			

			sp1.x = xpos;
			sp1.y = ypos;
			
			sp1.display();
			xpos = (float) (xpos + xspeed * 0.2) ;
			ypos = (float) (ypos + yspeed * 0.5 );
			image(floor, 0, (float) (height*0.8));
			image(ceiling,0, 0);
			
			//Floor collision 

			if (ypos > height*0.77  && yspeed > 0 ) { //Adjust this number for proper collision with floor

				yspeed = yspeed * -1;
				xspeed += sp1.rotationSpeed*50; //rotation effect

				changeBounce();
			}
			if (ypos < height*0.14 && yspeed < 0) { //Adjust this number for proper collision with ceiling

				yspeed = yspeed * -1;
				xspeed += sp1.rotationSpeed*50; //Rotation effect

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
			}        
		}
	}

	public void changeBounce(){
		isBounceUp = !isBounceUp;
		isBounceDown = !isBounceDown;
	}

	public void accelerate(){
		float speedTmp=Math.abs(yspeed)+3; //Control MAX_SPEED
		
		if(speedTmp<=MAX_SPEED){
			if(yspeed>0)
				yspeed = (float) ( yspeed  + 3 );
			else
				yspeed = (float) ( yspeed  - 3 );		
		}	
	}

	public void decelerate(){		
			if(yspeed<0){
				yspeed = (float) ( yspeed  + 3 );
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
		
		if(keyCode == RIGHT){
			sp1.accelerateRotation(0.1);
		}
		if(keyCode == LEFT){
			sp1.accelerateRotation(-0.1);
		}

		/*if ( keyCode == UP ) {
			if ( isBounceUp )
				accelerate();
			else if(isBounceDown)
				decelerate();
		} */
		/*if ( keyCode == DOWN  )
		{	
			if(isBounceUp)
				decelerate();
		}*/
		if(keyCode ==71){
			gravity=gravity*(-1);
		}
	}


	void paraDraw(PImage img, PVector pos, float vel){
		pos.sub(vel, 0, 0);

		int r = (int)pos.x+img.width;

		if(r < width) image(img, r, pos.y);
		if(pos.x < width) image(img, pos.x-img.width, pos.y); 
		if(pos.x < -img.width) pos.x = width;

		image(img, pos.x, pos.y);
		
	}
}
