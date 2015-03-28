package wizzball;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
	float xpos = 0, ypos; // Starting position of shape

	// Timer
	int actualTime;
	int totalTime = 600000;
	int begin = 0;
	int end = 0;
	// The array of stars
	Star[] stars;

	// Global offset
	PVector offset;

	/*
	 * NEGATIVE SPEED ---> GOING UP POSITIVE SPEED ---> GOING DOWN
	 */
	float xspeed = (float) 0; // Speed of the shape (initial = 0)
	float yspeed = (float) 5; // Speed of the shape
	Spot sp1 = null;
	Vector<Platform> platforms = null;
	Vector<Hole> holes = null;
	PImage img, floor, ceiling, saturn, stars1, starsOver, gameover;
	PVector vback = new PVector(0, 0), vmiddle = new PVector(150, 140), vfront;
	int rotationEffect = 40;

	float yFont = 250;
	float zFont = -200;
	float xFont = 250;

	float v = 0; // background velocity

	float gravity = (float) 0.5; // positive downwards --- negative upwards

	Minim minim;
	AudioPlayer musicPlayer, bouncingPlayer;
	private int currentLevel = 1;

	public void setup() {
		loadLevel();
		loadMusics();
		loadImages();
		frameRate(24);
		sp1 = new Spot(this, xpos, ypos, 15);
		size(500, 500, OPENGL);
		f = createFont("Arial", 16, true);
		ellipseMode(RADIUS);
		ypos = height / 2;

		// vfront = new PVector(0, 5); //just fixing the position of the image

		stars = new Star[200];
		for (int i = 0; i < stars.length; i++)
			stars[i] = new Star();

		// Initialize the offset
		offset = new PVector(width / 2, height / 2);

		smooth();

	}

	/**
	 * Load the level corresponding to the txt file
	 */
	public void loadLevel() {
		// Open the file
		try {
			FileInputStream fstream = new FileInputStream("data/levels/level" + currentLevel + ".txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			//initialize platforms and holes
			if (platforms == null)
				platforms = new Vector<Platform>();
			else platforms.clear();
			
			if(holes == null)
			holes = new Vector<Hole>();
			else holes.clear();
			
			int nbLine  = 0;
			String[] words;

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				words = strLine.split(" ");
				
				//Initialize begin, end and time for the level
				if(nbLine == 0){
					begin = parseInt(words[1]);
					nbLine++ ;
				} else if(nbLine == 1){
					end = parseInt(words[1]);
					platforms.addElement(new Platform(this, end, (float)(this.height*0.7), 40, true));
					platforms.addElement(new Platform(this, begin,(float)(this.height*0.7), 40, true));
					nbLine++;
				} else if(nbLine == 2){
					totalTime = parseInt(words[1])*1000;
					nbLine++;
				}
				
				else{//Create platforms, stairs and holes
					if(words[0].equals("P")){
						boolean down = false;
						if(words[4].equals("+")) down = true;
						platforms.addElement(new Platform(this, parseFloat(words[1]), parseFloat(words[2]), parseFloat(words[3]), down));
					}
					
					if(words[0].equals("H")){
						boolean down = false;
						if(words[4].equals("+")) down = true;
						holes.addElement(new Hole(this, parseFloat(words[1]), parseFloat(words[2]), parseFloat(words[3]), down));
					}
					
					
					if(words[0].equals("S")){
						boolean down = false;
						if(words[7].equals("+")) down = true;
						
						boolean direcStairs = false;
						if(words[6].equals("+")) direcStairs = true;
						
						
						int steps = parseInt(words[1]);
						float begin = parseFloat(words[2]);
						float end = parseFloat(words[3]);
						float heightMin = parseFloat(words[4]);
						float heightMax = parseFloat(words[5]);
						
						float width = (abs(end-begin))/(steps-1);
						float heightIncrement = (heightMax-heightMin)/steps;
						

						
						if(direcStairs){
							for(int i = 0 ; i < steps ; ++i){
								platforms.addElement(new Platform(this, begin+i*width, heightMin+heightIncrement*i, width, down));
							}
						} else {
							for(int i = 0 ; i < steps ; ++i){
								platforms.addElement(new Platform(this, begin+i*width, heightMax-heightIncrement*i, width, down));
							}
						}
						
					}
				}
				System.out.println(strLine);
			}

			// Close the input stream
			br.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadImages() {
		img = loadImage("space_background.jpg");
		floor = loadImage("moonfloor.jpg");
		ceiling = loadImage("ceiling.jpg");
		stars1 = loadImage("starsBack.jpg");
		saturn = loadImage("saturn.png");
		starsOver = loadImage("goscreen.png");
		gameover = loadImage("logo.png");
	}

	@Override
	public PImage loadImage(String filename) {
		return super.loadImage("images/" + filename);
	}

	private void loadMusics() {
		minim = new Minim(this);
		musicPlayer = minim.loadFile("musics/music.mp3");
		bouncingPlayer = minim.loadFile("musics/bounce.mp3");
	}


	public void draw() {

		if (!musicPlayer.isPlaying())
			musicPlayer.play();

		// img.resize(width, height);
		stars1.resize(width, height);
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

			background(stars1);
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
			if (yFont < -300) {
				enterTheGame = true;
				actualTime = millis();
				firstStep = false;
			}

		}

		if (enterTheGame) {

			clear();

			image(floor, 0, (float) (height * 0.8));
			image(ceiling, 0, 0);

			// Display the stars

			for (int i = 0; i < stars.length; i++)
				stars[i].display();

			// Modify the offset, using the center of the screen as a form of joystick
			// Something should be changed HERE to use the position of the ball as the joystick

			PVector angle = new PVector(sp1.x - width / 2, sp1.y - height / 2);
			angle.normalize();
			angle.mult(dist(width / 2, height / 2, sp1.x, sp1.y) / 50);

			offset.add(angle);

			// strokeWeight(0);

			// paraDraw(floor, vback, v);
			// paraDraw(ceiling, vmiddle, 2);
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
				// actualTime = millis(); // Restart timer
				gameOver = true;
				enterTheGame = false;
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

			for (Hole h : holes) {
				if (h.isDisplay()) {
					h.display();
					h.recalculateHoleX(xpos);
				}
			}
			xpos = (float) (xpos + xspeed * 0.2);
			ypos = (float) (ypos + yspeed * 0.5);

			// The ball can't go under the floor
			ypos = (float) (ypos < height * 0.1 + sp1.radius ? height * 0.1 + sp1.radius : ypos);
			ypos = (float) (ypos > height * 0.8 - sp1.radius ? height * 0.8 - sp1.radius : ypos);

			v = xpos - sp1.x;

			managePlatformsCollision();
			manageHolesCollision();
			text("distance : " + xpos, 50, 70);

		}
		/*
		 * if(gameOver){ clear(); strokeWeight(0);
		 * 
		 * clear(); background(starsOver); image(gameover, 25, 200); fill(255,0,0);
		 * 
		 * 
		 * } GAME OVER SCREEN
		 */
		if (gameOver) {

			clear();
			// paraDraw(starsOver, vback, v);

			image(gameover, width / 4, height / 4);
		}

		// Floor collision

		if (ypos >= (height * 0.8 - sp1.radius) && yspeed > 0) { // Adjust this
			// number
			// for
			// proper
			// collision
			// with
			// floor

			ybounce();
		} else if (ypos <= (height * 0.1 + sp1.radius) && yspeed < 0) { // Adjust
			// this
			// number for
			// proper
			// collision
			// with ceiling

			ybounce();

		}

		/*
		 * else if (ypos < 0) { yspeed = yspeed * -1; }
		 * 
		 * /* if ( xpos > width-sp1.radius || xpos < sp1.radius) { xspeed *= -1; yspeed += sp1.rotationSpeed*rotationEffect; //Rotation effect }
		 */
	}

	private void ybounce() {
		yspeed = yspeed * -1;
		xspeed += sp1.rotationSpeed * rotationEffect; // rotation effect
		xspeed = abs(xspeed) > MAX_SPEED ? (xspeed / abs(xspeed)) * MAX_SPEED : xspeed;
		playBounceSound();

	}

	private void xbounce() {
		xspeed = xspeed * -1;
		yspeed += sp1.rotationSpeed * rotationEffect; // rotation effect
		yspeed = abs(yspeed) > MAX_SPEED ? (yspeed / abs(yspeed)) * MAX_SPEED : yspeed;
		playBounceSound();

	}

	private void bounceCorner() {
		xspeed = xspeed * -1;
		yspeed += sp1.rotationSpeed * rotationEffect; // rotation effect
		yspeed = abs(yspeed) > MAX_SPEED ? (yspeed / abs(yspeed)) * MAX_SPEED : yspeed;

		yspeed = yspeed * -1;
		xspeed += sp1.rotationSpeed * rotationEffect; // rotation effect
		xspeed = abs(xspeed) > MAX_SPEED ? (xspeed / abs(xspeed)) * MAX_SPEED : xspeed;

		playBounceSound();

	}

	private void playBounceSound() {
		bouncingPlayer.rewind();
		bouncingPlayer.play();
	}

	private void managePlatformsCollision() {
		for (Platform p : platforms) {
			if (p.isDisplay()) {

				// corners to implement
				// racine_carre((x_point - x_centre)� + (y_centre - y_point)) < rayon
				if (Math.sqrt(Math.pow((p.getLeft() - sp1.x), 2) + Math.pow((sp1.y - p.getTop()), 2)) <= sp1.radius) {
					xpos = p.getLeft() - sp1.radius;
					ypos = p.getTop() - sp1.radius;
					bounceCorner();
					continue;
				}

				if (Math.sqrt(Math.pow((p.getRight() - sp1.x), 2) + Math.pow((sp1.y - p.getTop()), 2)) <= sp1.radius) {
					xpos = p.getRight() + sp1.radius;
					ypos = p.getTop() - sp1.radius;
					bounceCorner();
					continue;
				}

				if (Math.sqrt(Math.pow((p.getLeft() - sp1.x), 2) + Math.pow((sp1.y - p.getBottom()), 2)) <= sp1.radius) {
					xpos = p.getLeft() - sp1.radius;
					ypos = p.getBottom() + sp1.radius;
					bounceCorner();
					continue;
				}

				if (Math.sqrt(Math.pow((p.getRight() - sp1.x), 2) + Math.pow((sp1.y - p.getBottom()), 2)) <= sp1.radius) {
					xpos = p.getRight() + sp1.radius;
					ypos = p.getBottom() + sp1.radius;
					bounceCorner();
					continue;
				}
				// Top and bottom
				if (xpos >= p.getLeft() && xpos <= p.getRight()) {
					if (p.getTop() >= ypos - sp1.radius && p.getTop() <= ypos + sp1.radius) {
						ypos = p.getTop() - sp1.radius;
						ybounce();

					}

					if (p.getBottom() <= ypos + sp1.radius && p.getBottom() >= ypos - sp1.radius) {
						ypos = p.getBottom() + sp1.radius;
						ybounce();

					}
				}

				// /Left collision
				if (xspeed > 0) {
					if (ypos + sp1.radius / 2 >= p.getTop() && ypos - sp1.radius / 2 <= p.getBottom()) {
						if (p.getLeft() <= xpos + sp1.radius / 2 && sp1.radius / 2 < p.x - p.width / 2) {
							xpos = p.getLeft() - sp1.radius / 2;
							xbounce();
						}
					}

					// /Right collision
				} else if (xspeed < 0) {
					if (ypos >= p.getTop() && ypos <= p.getBottom()) {
						if (p.getRight() >= xpos - sp1.radius / 2 && sp1.radius / 2 > p.x + p.width / 2) {
							xpos = p.getRight() + sp1.radius / 2;
							xbounce();
						}
					}
				}
			}
		}
	}

	private void manageHolesCollision() {
		for (Hole h : holes) {
			if (h.isDisplay()) {
				if (Math.sqrt(Math.pow((h.getLeft() - sp1.x), 2) + Math.pow((sp1.y - h.getTop()), 2)) <= sp1.radius) {
					xpos = h.getLeft() - sp1.radius;
					ypos = h.getTop() - sp1.radius;
					bounceCorner();
					continue;
				}

				if (Math.sqrt(Math.pow((h.getRight() - sp1.x), 2) + Math.pow((sp1.y - h.getTop()), 2)) <= sp1.radius) {
					xpos = h.getRight() + sp1.radius;
					ypos = h.getTop() - sp1.radius;
					bounceCorner();
					continue;
				}

				if (Math.sqrt(Math.pow((h.getLeft() - sp1.x), 2) + Math.pow((sp1.y - h.getBottom()), 2)) <= sp1.radius) {
					xpos = h.getLeft() - sp1.radius;
					ypos = h.getBottom() + sp1.radius;
					bounceCorner();
					continue;
				}

				if (Math.sqrt(Math.pow((h.getRight() - sp1.x), 2) + Math.pow((sp1.y - h.getBottom()), 2)) <= sp1.radius) {
					xpos = h.getRight() + sp1.radius;
					ypos = h.getBottom() + sp1.radius;
					bounceCorner();
					continue;
				}
				// Top and bottom
				if (xpos >= h.getLeft() && xpos <= h.getRight()) {
					if (h.getTop() > ypos - sp1.radius && h.getTop() < ypos + sp1.radius) {
						gameOver = true;
						enterTheGame = false;

					}

					if (h.getBottom() < ypos + sp1.radius && h.getBottom() > ypos - sp1.radius) {
						gameOver = true;
						enterTheGame = false;

					}
				}
				// /Left collision
				if (xspeed > 0) {
					if (ypos + sp1.radius / 2 >= h.getTop() && ypos - sp1.radius / 2 <= h.getBottom()) {
						if (h.getLeft() <= xpos + sp1.radius / 2 && sp1.radius / 2 < h.x - h.width / 2) {
							xpos = h.getLeft() - sp1.radius / 2;
							xbounce();
						}
					}

					// /Right collision
				} else if (xspeed < 0) {
					if (ypos >= h.getTop() && ypos <= h.getBottom()) {
						if (h.getRight() >= xpos - sp1.radius / 2 && sp1.radius / 2 > h.x + h.width / 2) {
							xpos = h.getRight() + sp1.radius / 2;
							xbounce();
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

	// Star class
	class Star {
		// Location
		PVector loc;
		// Size
		int size;
		// Brightness
		int bright;

		Star() {
			// Randomize all of the values
			size = (int) random(1, 4);
			loc = new PVector(random(width * map(size, 1, 7, 7, 1)), random(height * map(size, 1, 7, 7, 1)));
			bright = (int) random(75, 150);
		}

		void display() {
			pushStyle();

			// Setup the style
			stroke(bright);
			strokeWeight(size);

			// Find the actual location and constrain it to within the bounds of the screen
			int x = (int) (((loc.x - offset.x) * size / 8)) % width;
			int y = (int) (((loc.y - offset.y) * size / 8)) % height;
			if (x < 0)
				x += width;
			if (y < 0)
				y += height;

			// Display the point
			point(x, y);

			popStyle();
		}
	}

}
