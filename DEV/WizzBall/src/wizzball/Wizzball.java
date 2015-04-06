package wizzball;

import java.util.Vector;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.basics.Collectable;
import wizzball.objects.basics.Collidable;
import wizzball.objects.collidable.Hole;

@SuppressWarnings("serial")
public class Wizzball extends PApplet {

	private static final int MAX_SPEED = 20;
	private static final double INCR_SPEED = 0.1;
	private static final int rotationEffect = 40;
	public static float gravity = (float) 0.5; // positive downwards --- negative upwards

	public static int C_TOP = 0, C_BOTTOM = 1, C_LEFT = 2, C_RIGHT = 3, C_TOP_LEFT = 4, C_TOP_RIGHT = 5, C_BOTTOM_LEFT = 6, C_BOTTOM_RIGHT = 7;

	public Level lvl;
	public Spot sp1;

	PImage img, floor, ceiling, saturn, stars1, starsOver, gameover;

	Minim minim;
	AudioPlayer musicPlayer, bouncingPlayer, bonusPlayer, keyPlayer;

	PFont f, fontSW;
	float yFont = 250, zFont = -200;

	Star[] stars;// The array of stars
	PVector offset; // Global offset

	int actualTime; // Timer

	private static final int TYPING = 0, STORY = 1, GAME = 2, GAME_OVER = 3;
	int state = TYPING;

	String typing = "";
	String player = "";

	public float xpos = 0; // Starting position of shape
	public float ypos;
	// NEGATIVE SPEED ---> GOING UP POSITIVE SPEED ---> GOING DOWN
	public float xspeed = (float) 0; // Speed of the shape (initial = 0)
	float yspeed = (float) 5; // Speed of the shape

	public void setup() {
		initDisplayParameters();
		loadFonts();
		loadLevel();
		loadMusics();
		loadImages();
		frameRate(24);
		initSpot();
		initStars();
	}

	private void loadFonts() {
		f = createFont("Arial", 16, true);
		fontSW = loadFont("fonts/StarJedi-48.vlw");

	}

	private void initDisplayParameters() {
		ellipseMode(RADIUS);
		size(500, 500, OPENGL);
		smooth();
	}

	private void initSpot() {
		initPositionAndSpeed();
		sp1 = new Spot(this, xpos, ypos, 15);
	}

	private void initStars() {
		stars = new Star[200];
		for (int i = 0; i < stars.length; i++)
			stars[i] = new Star();

		// Initialize the offset
		offset = new PVector(width / 2, height / 2);
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
		keyPlayer = minim.loadFile("musics/keySound.mp3");

	}

	public void draw() {

		loopThemeMusic();
		imagesResizing();

		switch (state) {
		case TYPING:
			displayNameScreen();
			break;
		case STORY:
			displayStoryScreen();
			break;
		case GAME:
			displayGame();
			break;
		case GAME_OVER:
			if(sp1.lives>0){
				sp1.lives--;
				initPositionAndSpeed();
				state = GAME;
				return;
			}
			displayGameOver();
			break;
		}

	}

	@SuppressWarnings("unchecked")
	private void displayGame() {

		frameRate(25);
		stroke(0);
		strokeWeight(5);
		background(0);
		textAlign(LEFT);
		textFont(fontSW, 14);

		clear();
		image(floor, 0, (float) (height * 0.8));
		image(ceiling, 0, 0);
		displayStars();

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
			state = GAME_OVER;
		}

		int countdown = (lvl.maximumTime - passedTime) / 1000;

		sp1.x = xpos;
		sp1.y = ypos;

		sp1.display();

		// Display platforms to the good position
		for (BasicObject p : (Vector<BasicObject>)lvl.objects.clone()) {
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

		text("Stars left: " + lvl.nbBonus, 50, 100);
		text("Time left: " + countdown, 50, 85);
		pushStyle();;
		if(sp1.lives==0)
			fill(240,7,30);
		text("Lives : " + sp1.lives, 50, 70);
		popStyle();
		
		
	}

	private void displayStoryScreen() {
		pushStyle();
		pushMatrix();

		frameRate(40);
		background(0);
		displayStars();
		rotateX(PI / 4);

		stroke(0);
		strokeWeight(5);
		directionalLight(250, 207, 63, 0, -200, -200);

		textFont(fontSW, 20);
		textAlign(CENTER);
		int xFont = width / 2;
		text("Hello " + player + ", you will enter the game.", xFont, yFont, zFont);
		text("enter the GAME.", xFont, yFont + 40, zFont);
		text("You can rotate the character ", xFont, yFont + 80, zFont);
		text("using arrows keys.", xFont, yFont + 120, zFont);
		text("Use G to change the ", xFont, yFont + 160, zFont);
		text("GRAVITY ", xFont, yFont + 200, zFont);
		text("Press TAB to continue...", xFont, yFont + 240, zFont);

		yFont--;
		if (yFont < -300) {
			state = GAME;
			actualTime = millis();
		}

		popStyle();
		popMatrix();

	}

	private void displayNameScreen() {
		pushStyle();
		pushMatrix();
		stroke(0);
		strokeWeight(5);
		directionalLight(250, 207, 63, 0, -200, -200);
		background(0);
		textAlign(CENTER);
		textFont(fontSW, 18);
		fill(200);
		stroke(153);
		text(" Hello, welcome to Wizzball game.\n Please, enter your name and press ENTER...\n", width / 2, 50);
		textFont(fontSW, 30);
		text(typing, width / 2, height / 2);
		popStyle();
		popMatrix();
	}

	private void imagesResizing() {
		// img.resize(width, height);
		stars1.resize(width, height);
		floor.resize(width, (int) (height * 0.2));
		ceiling.resize(width, (int) (height * 0.1));
		saturn.resize(width / 6, height / 6);
	}

	private void loopThemeMusic() {
		if (!musicPlayer.isPlaying())
			musicPlayer.play();
		// In the end play again the song
		if ((millis() % musicPlayer.length()) / 100 == 0) {
			musicPlayer.rewind();
			musicPlayer.play();
		}

	}

	private void displayStars() {
		// Display the stars

		for (int i = 0; i < stars.length; i++)
			stars[i].display();

		// Modify the offset, using the center of the screen as a form of joystick
		// Something should be changed HERE to use the position of the ball as the joystick

		PVector angle = new PVector(sp1.x - width / 2, sp1.y - height / 2);
		angle.normalize();
		angle.mult(dist(width / 2, height / 2, sp1.x, sp1.y) / 50);

		offset.add(angle);
	}

	private boolean achieveLevel() {
		return xpos >= lvl.xEnd && lvl.nbBonus == 0;
	}

	private void nextLevel() {
		lvl.currentLevel++;
		initPositionAndSpeed(); // Reinitialize position
		loadLevel(); // Load a new level

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

	public void playKeySound() {
		keyPlayer.rewind();
		keyPlayer.play();
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

	private void manageObjectsCollision() {
		for (BasicObject p : lvl.objects) {
			if (p.isDisplay()) {

				// racine_carre((x_point - x_centre)� + (y_centre - y_point)) < rayon
				if (p.isCollide(C_TOP_LEFT)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_TOP_LEFT])) {
						xpos = p.getLeft() - sp1.radius;
						ypos = p.getTop() - sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_TOP_RIGHT)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_TOP_RIGHT])) {
						xpos = p.getRight() + sp1.radius;
						ypos = p.getTop() - sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_BOTTOM_LEFT)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM_LEFT])) {
						xpos = p.getLeft() - sp1.radius;
						ypos = p.getBottom() + sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_BOTTOM_RIGHT)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM_RIGHT])) {
						xpos = p.getRight() + sp1.radius;
						ypos = p.getBottom() + sp1.radius;
						bounceCorner();
					}
					continue;
				}
				// Top and bottom
				if (p.isCollide(C_TOP)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_TOP])) {
						ypos = p.getTop() - sp1.radius;
						ybounce();
					} else if (p instanceof Hole) {
						state = GAME_OVER;
					}
				}

				if (p.isCollide(C_BOTTOM)) {
					if (p instanceof Collectable) {
						((Collectable) p).effect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM])) {
						ypos = p.getBottom() + sp1.radius;
						ybounce();
					} else if (p instanceof Hole) {
						state = GAME_OVER;
					}
				}
			}

			// /Left collision
			if (p.isCollide(C_LEFT)) {
				if (p instanceof Collectable) {
					((Collectable) p).effect();
				} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_LEFT])) {
					xpos = p.getLeft() - sp1.radius;
					xbounce();
				}
			}

			// /Right collision
			if (p.isCollide(C_RIGHT)) {
				if (p instanceof Collectable) {
					((Collectable) p).effect();
				} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_RIGHT])) {
					xpos = p.getRight() + sp1.radius;
					xbounce();
				}
			}
		}
	}

	private void displayGameOver() {
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

	public void keyPressed() {
		if (state == TYPING) {
			if (key == '\n') {
					player = typing;
					typing = "";
					state = STORY;
			} else {
				if (keyCode == BACKSPACE) {
					if (typing.length() > 1) {
						typing = typing.substring(0, typing.length() - 1);
					} else {
						typing = "";
					}
				} else if (key != CODED) {
					typing += key;
					playKeySound();
				}
			}
		}
		if (keyCode == TAB) {
			state = GAME;
			initPositionAndSpeed();
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

	private void initPositionAndSpeed() {
		xpos = 0;
		ypos = height / 2;
		xspeed = 0;
		yspeed = 5;
		gravity = (float) 0.5;
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
