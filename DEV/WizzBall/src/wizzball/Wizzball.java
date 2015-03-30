package wizzball;

import java.util.Vector;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;

public class Wizzball extends PApplet {

	public static int TOP = 0, C_BOTTOM = 1, C_LEFT = 2, C_RIGHT = 3, C_TOP_LEFT = 4, C_TOP_RIGHT = 5, C_BOTTOM_LEFT = 6, C_BOTTOM_RIGHT = 7;

	private static final long serialVersionUID = 1L;

	Level lvl;

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
	PImage img, floor, ceiling, saturn, stars1, starsOver, gameover;
	PVector vback = new PVector(0, 0), vmiddle = new PVector(150, 140), vfront;
	int rotationEffect = 40;

	float yFont = 250;
	float zFont = -200;
	float xFont = 250;

	float gravity = (float) 0.5; // positive downwards --- negative upwards

	Minim minim;
	AudioPlayer musicPlayer, bouncingPlayer, bonusPlayer;

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

	private void loadLevel() {
		if (lvl == null) {
			lvl = new Level(this);
		}
		lvl.loadLevel();
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
		bonusPlayer = minim.loadFile("musics/bonus.wav");
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

			if (passedTime > lvl.maximumTime) { // After 60 seconds..
				gameOver();
			}

			int countdown = (lvl.maximumTime - passedTime) / 1000;

			text("Time left: " + countdown, 50, 100);

			sp1.x = xpos;
			sp1.y = ypos;

			sp1.display();

			// Display platforms to the good position
			for (BasicObject p : lvl.objects) {
				if (p.isDisplay()) {
					p.display();
					p.recalculatePositionX(xpos);
				}

			}

			xpos = (float) (xpos + xspeed * 0.2);
			ypos = (float) (ypos + yspeed * 0.5);


			manageFloorCollision();
			manageObjectsCollision();

			if (achieveLevel()) {
				nextLevel();
			}
			text("distance : " + xpos, 50, 70);

		}
		
		if (gameOver) {
			displayGameOver();
		}
	}

	private boolean achieveLevel() {
		return xpos >= lvl.xEnd && lvl.nbBonus == 0;
	}

	private void nextLevel() {
		lvl.currentLevel++;
		// Reinitialize position
		xpos = 0;
		// ypos = height / 2;
		gravity = (float) 0.5;

		// reinitialize speed
		xspeed = 0;
		yspeed = 5;

		// Load a new level
		loadLevel();
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

	public void playBonusSound() {
		bonusPlayer.rewind();
		bonusPlayer.play();
	}

	private void manageFloorCollision() {
		// The ball can't go under the floor
		ypos = (float) (ypos < height * 0.1 + sp1.radius ? height * 0.1 + sp1.radius : ypos);
		ypos = (float) (ypos > height * 0.8 - sp1.radius ? height * 0.8 - sp1.radius : ypos);

		if (ypos >= (height * 0.8 - sp1.radius) && yspeed > 0) { // Adjust this number for proper collision with floor
			ybounce();
		} else if (ypos <= (height * 0.1 + sp1.radius) && yspeed < 0) { // Adjust this number for proper collision with ceiling

			ybounce();
		}
	}

	@SuppressWarnings("unchecked")
	private void manageObjectsCollision() {
		Vector<BasicObject> tmp = (Vector<BasicObject>) lvl.objects.clone();
		for (BasicObject p : tmp) {
			if (p.isDisplay()) {

				// racine_carre((x_point - x_centre)� + (y_centre - y_point)) < rayon
				if (p.isCollide(C_TOP_LEFT)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect(this);
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_TOP_LEFT])) {
						xpos = p.getLeft() - sp1.radius;
						ypos = p.getTop() - sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_TOP_RIGHT)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect(this);
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_TOP_RIGHT])) {
						xpos = p.getRight() + sp1.radius;
						ypos = p.getTop() - sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_BOTTOM_LEFT)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect(this);
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM_LEFT])) {
						xpos = p.getLeft() - sp1.radius;
						ypos = p.getBottom() + sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_BOTTOM_RIGHT)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect(this);
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM_RIGHT])) {
						xpos = p.getRight() + sp1.radius;
						ypos = p.getBottom() + sp1.radius;
						bounceCorner();
					}
					continue;
				}
				// Top and bottom
				if (p.isCollide(TOP)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect(this);
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[TOP])) {
						ypos = p.getTop() - sp1.radius;
						ybounce();
					} else if (p instanceof Hole) {
						gameOver();
					}
				}

				if (p.isCollide(C_BOTTOM)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect(this);
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM])) {
						ypos = p.getBottom() + sp1.radius;
						ybounce();
					} else if (p instanceof Hole) {
						gameOver();
					}
				}
			}

			// /Left collision
			if (p.isCollide(C_LEFT)) {
				if (p instanceof Collectable) {
					((Collectable) p).effect(this);
				} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_LEFT])) {
					xpos = p.getLeft() - sp1.radius / 2;
					xbounce();
				}
			}

			// /Right collision
			if (p.isCollide(C_RIGHT)) {
				if (p instanceof Collectable) {
					((Collectable) p).effect(this);
				} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_RIGHT])) {
					xpos = p.getRight() + sp1.radius / 2;
					xbounce();
				}
			}
		}
	}

	private void gameOver() {
		gameOver = true;
		enterTheGame = false;
	}
	
	private void displayGameOver(){
		clear();
		image(gameover, width / 4, height / 4);
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
