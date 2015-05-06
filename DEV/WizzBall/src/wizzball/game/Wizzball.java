/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.game;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.Table;
import processing.data.TableRow;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.basics.Collectable;
import wizzball.objects.basics.Collidable;
import wizzball.objects.collidable.Hole;
import wizzball.utilities.Timer;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

@SuppressWarnings("serial")
public class Wizzball extends PApplet {


	private static final int MAX_SPEED = 20;
	private static final double INCR_SPEED = 0.1;
	private static final int rotationEffect = 40;

	public static final int STARS_POINTS = 100;
	public static final int POWER_POINTS = 50;
	public static final int NASTIES_POINTS = 500;

	public static float gravity = (float) 0.5; // positive downwards --- negative upwards

	public static int C_TOP = 0, C_BOTTOM = 1, C_LEFT = 2, C_RIGHT = 3, C_TOP_LEFT = 4, C_TOP_RIGHT = 5, C_BOTTOM_LEFT = 6, C_BOTTOM_RIGHT = 7;

	public Level lvl;
	public Spot sp1;

	public Table table; // Scores table
	public boolean scoreSaved = false;

	private LinkedList<Integer> sequence = new LinkedList<Integer>();
	private LinkedList<Integer> KONAMI = new LinkedList<Integer>(Arrays.asList(97, 98, 39, 37, 39, 37, 40, 40, 38, 38));


	PImage img, floor, ceiling, saturn, stars1, starsOver, gameover, avatars, sound_on, sound_off, current_sound;

	Minim minim;
	AudioPlayer musicPlayer, bouncingPlayer, bonusPlayer, keyPlayer, gunPlayer, rayPlayer;

	PFont f, fontSW;
	float yFont = 250, zFont = -200;

	Star[] stars;// The array of stars
	PVector offset; // Global offset
	PVector vback = new PVector(0, 0);
	PVector vmiddle = new PVector(0, 0);
	PVector vfront = new PVector(0, 5);

	public static final int TYPING = 0, STORY = 1, GAME = 2, GAME_OVER = 3, MENU = 42, SETTINGS = 43, PAUSE = 44, SUCCESS = 45;
	public int state = MENU;

	String typing = "";
	String player = "";

	public float xpos = 0; // Starting position of shape
	public float ypos;
	// NEGATIVE SPEED ---> GOING UP POSITIVE SPEED ---> GOING DOWN
	public float xspeed = (float) 0; // Speed of the shape (initial = 0)
	float yspeed = (float) 5; // Speed of the shape
	private boolean isInGame = true;
	private boolean buttonGame, buttonSettings, buttonBall, buttonEyes, buttonMouth, buttonCustom, buttonBack, buttonSound, buttonRestart;

	public Timer timer = new Timer(this);
	private Hole trapInHole = null;
	private boolean pause = false;
	private boolean nyancatmode = false;

	public void setup() {
		initDisplayParameters();
		loadFonts();
		loadLevel();
		loadMusics();
		loadImages();
		frameRate(24);
		initSpot();
		initStars();
		initTable();

	}

	private void initTable() {
		File f = new File("tables/scores.csv");
		if (!f.exists()) {
			table = new processing.data.Table();
			table.addColumn("Id");
			table.addColumn("Name");
			table.addColumn("Score");
		} else
			table = loadTable("tables/scores.csv", "header, csv");
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
		restartTheLevel();
		sp1 = new Spot(this, xpos, ypos, 20);
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
		if (!lvl.loadLevel()) {
			state = SUCCESS;
		}
	}

	private void loadImages() {
		img = loadImage("space_background.jpg");
		floor = loadImage("moonfloor.jpg");
		ceiling = loadImage("ceiling.jpg");
		stars1 = loadImage("starsBack.jpg");
		saturn = loadImage("saturn.png");
		starsOver = loadImage("goscreen.png");
		gameover = loadImage("gover.png");
		avatars = loadImage("avatars.png");
		sound_on = loadImage("sound_on.png");
		sound_off = loadImage("sound_off.png");
		current_sound = sound_on;

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
		gunPlayer = minim.loadFile("musics/Gun.mp3");
		rayPlayer = minim.loadFile("musics/Ray_gun.mp3");

	}

	private void saveScore() {
		TableRow newRow = table.addRow();
		newRow.setInt("Id", table.lastRowIndex());
		newRow.setString("Name", player);
		newRow.setInt("Score", sp1.acumulativeScore);
		saveTable(table, "tables/scores.csv", "csv");
	}

	public void draw() {

		
		
		if (sequence.equals(KONAMI)) {
			nyancatMode();
		}

		loopThemeMusic();
		imagesResizing();

		switch (state) {
		case MENU:
			displayMenu();
			break;
		case SETTINGS:
			displaySettings();
			break;
		case TYPING:
			displayNameScreen();
			break;
		case STORY:
			displayStoryScreen();
			break;
		case GAME:
			if (!lvl.isLoading())
				displayGame();
			break;
		case PAUSE:
			displayPause();
			break;
		case GAME_OVER:
			if (sp1.lives > 0) {
				sp1.lives--;
				timer.pause();
				restartTheLevel();
				state = GAME;
				return;
			}
			if (!scoreSaved) {
				saveScore();
				scoreSaved = true;
			}
			displayGameOver();
			break;
		case SUCCESS:
			displaySuccessScreen();
			break;
		}

	}

	/**
	 * 
	 */
	private void nyancatMode() {
		
		if(!nyancatmode ){
		
		sp1.ball = loadImage("nyancat.png");
		sp1.mouth = null;
		sp1.eyes = null;
		sp1.custom = null;
		
		musicPlayer = minim.loadFile("musics/nyancat.mp3");
		nyancatmode = true;
		}

	}

	@SuppressWarnings("unchecked")
	private void displayGame() {

		clear();
		frameRate(25);
		stroke(0);
		strokeWeight(5);
		if(!nyancatmode){
			background(0);
		} else {
			background(249,58,166);
		}
		textAlign(LEFT);
		textFont(fontSW, 14);



		paraDrawCeiling(ceiling, 500, xpos);
		paraDrawFloor(floor, 500, xpos);

		displayStars();

		if (!isInGame) {
			ypos = height + 2 * sp1.radius;
			xpos -= lvl.xEnd / 40;
			if (xpos < 0) {
				reinitPositionAndSpeed();
				sp1.appearAnimation();
				timer.unpause();
			}
		}

		if (trapInHole != null) {
			xpos = trapInHole.getX();
			ypos = ypos - 2;

			if (ypos <= (trapInHole.getTop() + sp1.radius)) {
				state = GAME_OVER;
				trapInHole = null;
			}
		}

		// /CONTROL OF THE GRAVITY

		float speedTmp = (float) Math.abs(yspeed + gravity); // Control
		// MAX_SPEED
		if (speedTmp <= MAX_SPEED) {
			yspeed = (float) (yspeed + gravity);
		}

		// /CONTROL THE

		if (timer.getSecondsLeft() <= 0) { // After level time..
			state = GAME_OVER;
		}

		sp1.x = xpos;
		sp1.y = ypos;

		if (isInGame) {
			sp1.display();
		}

		// Display platforms to the good position
		for (BasicObject p : (Vector<BasicObject>) lvl.objects.clone()) {
			if (p.isDisplay()) {
				p.display();
				p.recalculatePositionX(xpos);
			}

		}
		if (trapInHole == null && state != GAME_OVER && !sp1.isAppearing()) {
			xpos = (float) (xpos + xspeed * 0.2);
			ypos = (float) (ypos + yspeed * 0.5);

			manageFloorCollision();
			manageObjectsCollision();
		}

		if (sp1.score >= 300) {
			sp1.lives += 1;
			sp1.score = 0;
		}

		if (achieveLevel()) {
			nextLevel();
		}

		displayTextBoxGame();

	}

	/**
	 * 
	 */
	private void reinitPositionAndSpeed() {
		xpos = 0;
		ypos = height / 2;
		xspeed = 0;
		yspeed = 5;
		gravity = (float) 0.5;
		sp1.appearAnimation();
		isInGame = true;
	}

	private void displaySettings() {
		buttonEyes = false;
		buttonBall = false;
		buttonCustom = false;
		buttonMouth = false;
		buttonBack = false;
		buttonSound = false;

		pushStyle();
		background(50);
		fill(50);
		textFont(fontSW, 40);
		textAlign(CENTER);
		stroke(40);
		strokeWeight(5);

		pushStyle();
		fill(200, 226, 9, 240);
		text("SETTINGS", width / 2, 50);
		popStyle();

		float h = 40;
		float border = 10;
		float y1 = height - (h + border);
		float y2 = y1 - (h + border);
		float y3 = y2 - (h + border);
		float y4 = y3 - (h + border);

		pushStyle();
		if (mouseY <= 60 && mouseX <= 60) {
			fill(100, 100, 100);
			buttonBack = true;
		}
		triangle(10, 35, 60, 10, 60, 60);
		popStyle();

		image(current_sound, width - 10 - 60, 10, 60, 60);
		if (mouseX >= width - 70 && mouseY <= 70) {
			buttonSound = true;
		}

		float tmpRadius = sp1.radius;
		float tmpx = sp1.x;
		float tmpy = sp1.y;

		sp1.radius = (y4 - 40 - 100) / 2;
		sp1.x = width / 2 - sp1.radius;
		sp1.y = sp1.radius + 40 + 50;
		sp1.display();

		if (mouseX >= 50 && mouseX <= width - 50) {
			if (mouseY >= y1 && mouseY <= y1 + h) {
				buttonCustom = true;
			} else if (mouseY >= y2 && mouseY <= y2 + h) {
				buttonMouth = true;
			} else if (mouseY >= y3 && mouseY <= y3 + h) {
				buttonEyes = true;
			} else if (mouseY >= y4 && mouseY <= y4 + h) {
				buttonBall = true;
			}
		}

		pushStyle();
		if (buttonBall)
			fill(100, 100, 100);
		rect(50, y4, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonEyes)
			fill(100, 100, 100);
		rect(50, y3, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonMouth)
			fill(100, 100, 100);
		rect(50, y2, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonCustom)
			fill(100, 100, 100);
		rect(50, y1, width - 100, h, 10);
		popStyle();

		fill(200, 226, 9, 240);
		textFont(fontSW, 30);

		text("Body", width / 2, y4 + h - 10);
		text("Eyes", width / 2, y3 + h - 10);
		text("Mouth", width / 2, y2 + h - 10);
		text("Custom", width / 2, y1 + h - 10);

		popStyle();

		sp1.x = tmpx;
		sp1.y = tmpy;
		sp1.radius = tmpRadius;

	}

	private void displayPause() {
		pushStyle();

		float y1 = height / 2 - 75 - 42.5f;
		float y2 = height / 2 + 75 - 42.5f;
		float y3 = height / 2 - 42.5f;
		float h = 60;
		buttonGame = false;
		buttonSettings = false;
		buttonRestart = false;

		fill(0);
		background(0);
		textFont(fontSW, 40);
		textAlign(CENTER);
		stroke(40);
		strokeWeight(5);

		if (mouseX >= 50 && mouseX <= width - 50) {
			if (mouseY >= y1 && mouseY <= y1 + h) {
				buttonGame = true;
			} else if (mouseY >= y2 && mouseY <= y2 + h) {
				buttonSettings = true;
			} else if (mouseY >= y3 && mouseY <= y3 + h) {
				buttonRestart = true;
			}
		}

		pushStyle();
		if (buttonGame)
			fill(100, 100, 100);
		rect(50, y1, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonRestart)
			fill(100, 100, 100);
		rect(50, y3, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonSettings)
			fill(100, 100, 100);
		rect(50, y2, width - 100, h, 10);
		popStyle();

		fill(200, 226, 9, 240);
		text("RESUME", width / 2, height / 2 - 75);
		text("RESTART", width / 2, height / 2);
		text("SETTINGS", width / 2, height / 2 + 75);

		popStyle();
	}

	private void displayMenu() {

		pushStyle();

		float y1 = height / 2 - 75 - 42.5f;
		float y2 = height / 2 + 75 - 42.5f;
		float h = 60;
		buttonGame = false;
		buttonSettings = false;

		fill(0);
		background(0);
		textFont(fontSW, 40);
		textAlign(CENTER);
		stroke(40);
		strokeWeight(5);

		if (mouseX >= 50 && mouseX <= width - 50) {
			if (mouseY >= y1 && mouseY <= y1 + h) {
				buttonGame = true;
			} else if (mouseY >= y2 && mouseY <= y2 + h) {
				buttonSettings = true;
			}
		}

		pushStyle();
		if (buttonGame)
			fill(100, 100, 100);
		rect(50, y1, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonSettings)
			fill(100, 100, 100);
		rect(50, y2, width - 100, h, 10);
		popStyle();

		fill(200, 226, 9, 240);
		text("PLAY", width / 2, height / 2 - 75);
		text("SETTINGS", width / 2, height / 2 + 75);

		popStyle();
	}

	/**
	 * 
	 */
	private void displayTextBoxGame() {

		text("Score: " + sp1.score, 50, 55);
		text("Stars left: " + lvl.nbBonus, 50, 100);
		text("Time left: " + timer.getSecondsLeft(), 50, 85);
		timer.display(25, 85, 15);
		pushStyle();

		if (sp1.lives == 0)
			fill(240, 7, 30);
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
			timer.start();
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
		ceiling.resize(width * 9, (int) (height * 0.1));
		floor.resize(width * 9, (int) (height * 0.2));
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
		timer.pause();
		lvl.currentLevel++;
		restartTheLevel(); // Reinitialize position
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

	public void playGunSound() {
		gunPlayer.rewind();
		gunPlayer.play();
	}

	public void playRaySound() {
		rayPlayer.rewind();
		rayPlayer.play();
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
						((Collectable) p).collect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_TOP_LEFT])) {
						xpos = p.getLeft() - sp1.radius;
						ypos = p.getTop() - sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_TOP_RIGHT)) {
					if (p instanceof Collectable) {
						((Collectable) p).collect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_TOP_RIGHT])) {
						xpos = p.getRight() + sp1.radius;
						ypos = p.getTop() - sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_BOTTOM_LEFT)) {
					if (p instanceof Collectable) {
						((Collectable) p).collect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM_LEFT])) {
						xpos = p.getLeft() - sp1.radius;
						ypos = p.getBottom() + sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_BOTTOM_RIGHT)) {
					if (p instanceof Collectable) {
						((Collectable) p).collect();
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
						((Collectable) p).collect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_TOP])) {
						ypos = p.getTop() - sp1.radius;
						ybounce();
					} else if (p instanceof Hole) {
						trapInHole = (Hole) p;
					}
				}

				if (p.isCollide(C_BOTTOM)) {
					if (p instanceof Collectable) {
						((Collectable) p).collect();
					} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM])) {
						ypos = p.getBottom() + sp1.radius;
						ybounce();
					} else if (p instanceof Hole) {
						trapInHole = (Hole) p;
					}
				}
			}

			// /Left collision
			if (p.isCollide(C_LEFT)) {
				if (p instanceof Collectable) {
					((Collectable) p).collect();
				} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_LEFT])) {
					xpos = p.getLeft() - sp1.radius;
					xbounce();
				}
			}

			// /Right collision
			if (p.isCollide(C_RIGHT)) {
				if (p instanceof Collectable) {
					((Collectable) p).collect();
				} else if (p instanceof Collidable && (((Collidable) (p)).getCollidablesEdges()[C_RIGHT])) {
					xpos = p.getRight() + sp1.radius;
					xbounce();
				}
			}
		}
	}

	private void displaySuccessScreen() {
		state = SUCCESS;
		buttonRestart = false;

		clear();
		pushStyle();
		background(50);
		fill(50);
		textAlign(CENTER);
		int w = width / 2 - gameover.width / 2;
		int h = height / 4 - gameover.height / 2;
		textFont(fontSW, 40);
		stroke(40);
		strokeWeight(5);

		pushStyle();
		fill(200, 226, 9, 240);
		text("SUCCESS", w, h);
		popStyle();

		pushStyle();
		if (mouseY >= height - 70) {
			fill(100, 100, 100);
			buttonRestart = true;
		}
		rect(20, height - 70, width - 40, 60);
		popStyle();

		fill(200, 226, 9, 240);
		text("RESTART", width / 2, height - 25);

		popStyle();

	}

	private void displayGameOver() {
		buttonRestart = false;

		clear();
		pushStyle();
		background(50);
		fill(50);
		textAlign(CENTER);
		int w = width / 2 - gameover.width / 2;
		int h = height / 4 - gameover.height / 2;
		fill(249, 241, 241);
		text("id       " + "Name      " + "Score    ", w + 218, h + 200);
		text("--------------------------------", w + 215, h + 215);

		for (TableRow row : table.rows()) {

			int id = row.getInt("Id");
			String name = row.getString("Name");
			int score = row.getInt("Score");

			text(id + " --- " + name + " --- " + score, w + 200, h + 235 + id * 15);
		}
		fill(50);
		textFont(fontSW, 40);
		stroke(40);
		strokeWeight(5);

		image(gameover, w, h);

		pushStyle();
		if (mouseY >= height - 70) {
			fill(100, 100, 100);
			buttonRestart = true;
		}
		rect(20, height - 70, width - 40, 60);
		popStyle();

		fill(200, 226, 9, 240);
		text("RESTART", width / 2, height - 25);

		popStyle();
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
	 * (non-Javadoc)
	 * 
	 * @see processing.core.PApplet#mouseClicked()
	 */
	@Override
	public void mouseClicked() {
		super.mouseClicked();

		switch (state) {
		case MENU:
			if (buttonGame) {
				state = TYPING;
			} else if (buttonSettings) {
				sp1.appearAnimation();
				state = SETTINGS;
			}
			break;
		case SETTINGS:
			if (buttonBall) {
				sp1.changeBall();
			} else if (buttonMouth) {
				sp1.changeMouth();
			} else if (buttonEyes) {
				sp1.changeEyes();
			} else if (buttonCustom) {
				sp1.changeCustom();
			} else if (buttonBack) {
				if (pause) {
					state = PAUSE;
				} else {
					state = MENU;
				}
			} else if (buttonSound) {
				switchSound();
			}
			break;

		case PAUSE:
			if (buttonGame) {
				state = GAME;
				timer.unpause();
			} else if (buttonSettings) {
				state = SETTINGS;
			} else if (buttonRestart) {
				timer.unpause();
				restartGame();
			}
			break;

		case GAME_OVER:
			if (buttonRestart) {
				restartGame();
			}
			break;

		case SUCCESS:
			if (buttonRestart) {
				restartGame();
			}
			break;
		}
	}

	/**
	 * restart the game
	 */
	private void restartGame() {
		lvl = new Level(this);
		sp1.initSpot();
		loadLevel();
		state = GAME;
	}

	/**
	 * 
	 */
	private void switchSound() {
		if (current_sound.equals(sound_on)) {
			current_sound = sound_off;
			musicPlayer.mute();
		} else {
			current_sound = sound_on;
			musicPlayer.unmute();
		}

	}

	public void keyPressed() {

		
		int keyCode = this.keyCode;

		if (sequence.size() == 10) {
			sequence.removeLast();
		}
		if(key == CODED){
			sequence.push(keyCode);
		} else {
			sequence.push((int) key);
		}

		switch (state) {
		case TYPING:
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
			break;
		case GAME:
			if (key == ESC) {
				key = 0; // Avoid killing process
				pause = true;
				state = PAUSE;
				timer.pause();
			}
			if (keyCode == RIGHT) {
				sp1.accelerateRotation(INCR_SPEED);
			}
			if (keyCode == LEFT) {
				sp1.accelerateRotation(-INCR_SPEED);
			}
			if (key == ' ') {
				sp1.activateWeapon();
			}

			if (keyCode == 71) {
				gravity = gravity * (-1);
			}

			// To debug -- DELETE IT AFTER
			if (keyCode == TAB) {
				timer.pause();
				state = GAME;
				restartTheLevel();
			}
			break;

		case STORY:
			if (keyCode == TAB) {
				state = GAME;
				timer.start();
			}

		default:
			if (key == ESC) {
				key = 0; // Avoid killing process
			}

			// To debug -- DELETE IT AFTER
			if (keyCode == TAB) {
				state = GAME;
				restartTheLevel();
			}
			break;
		}
	}

	private void restartTheLevel() {
		isInGame = false;
	}

	void paraDrawCeiling(PImage img, float imgX, float imgY) {

		if (imgX > 0)
			image(img, -imgY - 1000, 0);
	}

	void paraDrawFloor(PImage img, float imgX, float imgY) {

		if (imgX > 0)
			image(img, -imgY - 1000, height - img.height);
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
			if (y < height * 0.1)
				y += (int) (height * 0.7);
			if (y > height * 0.8)
				y -= (int) (height * 0.7);
			// Display the point
			point(x, y);

			popStyle();
		}
	}

}
