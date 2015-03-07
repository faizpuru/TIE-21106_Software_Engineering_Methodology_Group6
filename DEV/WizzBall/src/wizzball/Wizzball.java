// test from MY pc

package wizzball;

import java.util.Vector;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;

public class Wizzball extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MAX_SPEED = 20;
	private static final double INCR_SPEED = 0.1;

	PFont f;
	String typing = "";
	String player = "";
	boolean firstStep = false;
	boolean enterTheGame = false;
	boolean gameOver = false;
	int count = 0;
	int x = 50;
	int rad = 60; // Width of the shape
	float xpos, ypos; // Starting position of shape

	// Timer
	int actualTime;
	int totalTime = 60000;

	/*
	 * NEGATIVE SPEED ---> GOING UP POSITIVE SPEED ---> GOING DOWN
	 */
	float xspeed = (float) 0; // Speed of the shape (initial = 0)
	float yspeed = (float) 5; // Speed of the shape
	Spot sp1 = null;
	Vector<Platform> platforms = null;
	PImage img, floor, ceiling, saturn, stars, starsOver, gameover;
	PVector vback, vmiddle, vfront;
	int rotationEffect = 40;

	float yFont = 250;
	float zFont = -200;
	float xFont = 250;

	float v = 0; // background velocity

	float gravity; // positive downwards --- negative upwards

	Minim minim;
	AudioPlayer musicPlayer;

	public void setup() {

		minim = new Minim(this);
		musicPlayer = minim.loadFile("music.mp3");

		img = loadImage("space_background.jpg");
		floor = loadImage("moonfloor.jpg");
		ceiling = loadImage("ceiling.jpg");
		stars = loadImage("starsBack.jpg");
		saturn = loadImage("saturn.png");
		starsOver = loadImage("goscreen.png");
		gameover = loadImage("logo.png");

		size(500, 500, OPENGL);
		f = createFont("Arial", 16, true);
		ellipseMode(RADIUS);
		xpos = 0;
		ypos = height / 2;

		sp1 = new Spot(this, xpos, ypos, 15);

		
		createPlatforms();

		gravity = (float) 0.5; // Setup gravity

		vback = new PVector(0, 0);
		vmiddle = new PVector(150, 140);
		// vfront = new PVector(0, 5); //just fixing the position of the image

		frameRate(24);
	}

	private void createPlatforms() {
		platforms = new Vector<Platform>();
		
		//Stair
		for(int i = 0 ; i < 10 ; ++i){
			platforms.addElement(new Platform(this, i*50, 10+20*i, 50, true));
		}
		
		
		platforms.addElement(new Platform(this, 550, 190, 50, false));
		platforms.addElement(new Platform(this, 650, 190, 50, true));
		platforms.addElement(new Platform(this, 750, 190, 50, false));
		platforms.addElement(new Platform(this, 850, 190, 50, true));
		platforms.addElement(new Platform(this, 950, 190, 50, false));

		
	}

	public void draw() {

		if (!musicPlayer.isPlaying())
			musicPlayer.play();

		img.resize(width, height);
		stars.resize(width, height);
		floor.resize(width, (int) (height * 0.2));
		ceiling.resize(width, (int) (height * 0.1));
		saturn.resize(width / 6, height / 6);
		background(0);
		textFont(f, 15);
		fill(200);
		stroke(153);
		text(" Hello, welcome to Wizzball game.\n Please, enter your name and press ENTER...\n", 50, 50);
		text(typing, 50, 100);
		if (firstStep) {
			clear();

			background(stars);
			textMode(MODEL);
			fill(255, 255, 0);
			textFont(f, 25);
			pushMatrix();
			rotateX(PI / 6);
			popMatrix();
			textAlign(CENTER);
			stroke(0, 20);
			// strokeWeight(5);

			text("Hello " + player + ", you will enter the game.", xFont, yFont, zFont);
			text("You can move the character using arrows keys.", xFont, yFont + 50, zFont);
			text("When the ball bounces up,", xFont, yFont + 100, zFont);
			text("you can decelerate it using up arrow", xFont, yFont + 150, zFont);
			text("When the ball is coming down,", xFont, yFont + 200, zFont);
			text("you can accelerate it using down arrow.", xFont, yFont + 250, zFont);
			text("Press TAB to continue...", xFont, yFont + 300, zFont);

			yFont--;
			if (yFont < -300){
				enterTheGame = true;
				actualTime= millis();
				firstStep = false;
			}

		}

		if (enterTheGame) {

			strokeWeight(0);

			clear();
			paraDraw(img, vback, v);
			// paraDraw(saturn, vmiddle, 2);
			fill(255, 0, 0);

			// /CONTROL OF THE GRAVITY

			float speedTmp = (float) Math.abs(yspeed + gravity); // Control
																	// MAX_SPEED
			if (speedTmp <= MAX_SPEED) {
				yspeed = (float) (yspeed + gravity);
			}

			// /CONTROL THE TIME

			// Calculate how much time has passed
			int passedTime = millis() - actualTime;

			if (passedTime > totalTime) { // After 60 seconds..
				println(" GAME ENDED! ");
				//actualTime = millis(); // Restart timer
				gameOver = true;
				enterTheGame=false;
			}

			int countdown = (totalTime - passedTime) / 1000;

			text("Time left: " + countdown, 50, 100);

			sp1.x = xpos;
			sp1.y = ypos;

			sp1.display();

			// Display platforms to the good position
			for (Platform p : platforms) {
				if (p.isDisplay()) {
					p.display();
					p.recalculatePlatformX(xpos);
				}
			}

			xpos = (float) (xpos + xspeed * 0.2);
			ypos = (float) (ypos + yspeed * 0.5);

			// The ball can't go under the floor
			ypos = (float) (ypos < height * 0.1 + sp1.radius ? height * 0.1 + sp1.radius : ypos);
			ypos = (float) (ypos > height * 0.8 - sp1.radius ? height * 0.8 - sp1.radius : ypos);

			image(floor, 0, (float) (height * 0.8));
			image(ceiling, 0, 0);

			v = xpos - sp1.x;

			managePlatformsCollision();
			text("distance : " + xpos, 50, 70);

		}
		/*
		 * if(gameOver){ clear(); strokeWeight(0);
		 * 
		 * clear(); background(starsOver); image(gameover, 25, 200); fill(255,0,0);
		 * 
		 * 
		 * }
		 * GAME OVER SCREEN
		 */
		if(gameOver){ 
			  
			  clear(); 
			  paraDraw(starsOver, vback, v);

			  image(gameover,width/4, height / 4);
		 }
		 
		
		// Floor collision

		if (ypos >= (height * 0.8 - sp1.radius) && yspeed > 0) { // Adjust this
																	// number
																	// for
																	// proper
																	// collision
																	// with
																	// floor

			yspeed = yspeed * -1;
			xspeed += sp1.rotationSpeed * rotationEffect; // rotation effect
			xspeed = abs(xspeed) > MAX_SPEED ? (xspeed / abs(xspeed)) * MAX_SPEED : xspeed;
		} else if (ypos <= (height * 0.1 + sp1.radius) && yspeed < 0) { // Adjust
																		// this
			// number for
			// proper
			// collision
			// with ceiling

			yspeed = yspeed * -1;
			xspeed += sp1.rotationSpeed * rotationEffect; // Rotation effect
			xspeed = abs(xspeed) > MAX_SPEED ? (xspeed / abs(xspeed)) * MAX_SPEED : xspeed;

		}

		/*
		 * else if (ypos < 0) { yspeed = yspeed * -1; }
		 * 
		 * /* if ( xpos > width-sp1.radius || xpos < sp1.radius) { xspeed *= -1; yspeed += sp1.rotationSpeed*rotationEffect; //Rotation effect }
		 */
	}

	private void managePlatformsCollision() {
		for (Platform p : platforms) {
			if (p.isDisplay()) {
				
				//corners to implement
				//if()
				
				//Top and bottom
				if(xpos >= p.getLeft() && xpos <= p.getRight()){
					if(p.getTop()>=ypos - sp1.radius && p.getTop()<=ypos + sp1.radius){
						ypos = p.getTop() - sp1.radius;
						yspeed *= -1;
						xspeed += sp1.rotationSpeed * rotationEffect; // rotation effect
						xspeed = abs(xspeed) > MAX_SPEED ? (xspeed / abs(xspeed)) * MAX_SPEED : xspeed;

					}
					
					if(p.getBottom()<=ypos + sp1.radius && p.getBottom()>=ypos - sp1.radius){
						ypos = p.getBottom() + sp1.radius;
						yspeed *= -1;
						xspeed += sp1.rotationSpeed * rotationEffect; // rotation effect
						xspeed = abs(xspeed) > MAX_SPEED ? (xspeed / abs(xspeed)) * MAX_SPEED : xspeed;

					}
				}

				// /Left collision
				if (xspeed > 0) {
					if (ypos + sp1.radius/2 >= p.getTop() && ypos - sp1.radius/2 <= p.getBottom()) {
						if (p.getLeft() <= xpos + sp1.radius /2 && sp1.radius/2  < p.x - p.width/2 ) {
							xpos = p.getLeft()  - sp1.radius/2;
							xspeed *= -1;
						}
					}

					// /Right collision
				} else if (xspeed < 0) {
					if (ypos >= p.getTop() && ypos <= p.getBottom()) {
						if (p.getRight() >= xpos - sp1.radius /2 && sp1.radius/2  > p.x + p.width/2 ) {
							xpos = p.getRight()  + sp1.radius/2;
							xspeed *= -1;
						}
					}
				}
			}
		}
	}

	public void accelerate() {
		float speedTmp = Math.abs(yspeed) + 3; // Control MAX_SPEED

		if (speedTmp <= MAX_SPEED) {
			if (yspeed > 0)
				yspeed = (float) (yspeed + 3);
			else
				yspeed = (float) (yspeed - 3);
		}
	}

	/*
	 * public void decelerate() { if (yspeed < 0) { yspeed = (float) (yspeed + 3); if (yspeed > 0) changeBounce(); } }
	 */

	public void keyPressed() {

		if (key == '\n' && count == 0) {

			count += 1;
			player = typing;
			typing = "";
			firstStep = true;
		} else {
			if (keyCode == BACKSPACE) {
				typing = typing.substring(0, typing.length() - 1);
			} else if (key != CODED)
				typing += key;
		}
		if (keyCode == TAB) {
			count += 1;
			enterTheGame = true;
		}

		if (keyCode == RIGHT) {
			sp1.accelerateRotation(INCR_SPEED);
		}
		if (keyCode == LEFT) {
			sp1.accelerateRotation(-INCR_SPEED);
		}

		/*
		 * if ( keyCode == UP ) { if ( isBounceUp ) accelerate(); else if(isBounceDown) decelerate(); }
		 */
		/*
		 * if ( keyCode == DOWN ) { if(isBounceUp) decelerate(); }
		 */
		if (keyCode == 71) {
			gravity = gravity * (-1);
		}
	}

	void paraDraw(PImage img, PVector pos, float vel) {
		pos.sub(vel, 0, 0);

		int r = (int) pos.x + img.width;

		if (r < width)
			image(img, r, pos.y);
		if (pos.x < width)
			image(img, pos.x - img.width, pos.y);
		if (pos.x < -img.width)
			pos.x = width;

		image(img, pos.x, pos.y);

	}

	public float getLimitX(char side) {
		if (side == 'l')
			return xpos - width / 2;
		if (side == 'r')
			return xpos + width / 2;

		return 0;
	}

}
