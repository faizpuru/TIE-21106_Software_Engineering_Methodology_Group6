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
import processing.core.PShape;
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

	/*
	 * POINTS
	 */

	public static final int STARS_POINTS = 100;
	public static final int POWER_POINTS = 50;
	public static final int NASTIES_POINTS = 200;
	public static final int BOMBS_POINTS = 150;

	/*
	 * LIVES
	 */

	public final static int NASTY_LIVES = 3;
	public final static int BOMB_LIVES = 2;
	public final static int STALIEN_LIVES = 5;
	public final static int MVALIEN_LIVES = 4;



	/*
	 * WEAPONS DAMAGE
	 */

	public final int GUN_DAMAGE = 1;
	public final int RAY_DAMAGE = 3;

	public static float gravity = (float) 0.5; // positive downwards ---
												// negative upwards

	public static int C_TOP = 0, C_BOTTOM = 1, C_LEFT = 2, C_RIGHT = 3,
			C_TOP_LEFT = 4, C_TOP_RIGHT = 5, C_BOTTOM_LEFT = 6,
			C_BOTTOM_RIGHT = 7;

	public Level lvl;
	public Spot sp1;

	public Table table; // Scores table
	public boolean scoreSaved = false;

	private LinkedList<Integer> sequence = new LinkedList<Integer>();
	private LinkedList<Integer> KONAMI = new LinkedList<Integer>(Arrays.asList(
			97, 98, 39, 37, 39, 37, 40, 40, 38, 38));

	PImage coin, level1, level2, level3, img, floor, backg, ceiling, saturn, stars1,
			starsOver, gameover, avatars, sound_on, sound_off, current_sound,heart1;
	
	PImage loading1, loading2, loading3, loading4, loading5, loading6;
	
	public static PImage nasty;
	public PImage rainbow;
	public PImage bonus;
	public PImage heart;
	public PImage powerup;
	public PImage hole;
	public PImage platformIm;
	public static PImage enemy;
	public PImage lasergun;
	public PImage laserSprite;
	public PImage watch;
	public static PImage staticAlien;
	public static PImage movingAlien;

	Minim minim;
	AudioPlayer musicPlayer, bouncingPlayer, bonusPlayer, keyPlayer, gunPlayer,
			rayPlayer, explosionPlayer;

	public PFont f, fontSW, fontDigital;

	Star[] stars;// The array of stars
	PVector offset; // Global offset
	PVector vback = new PVector(0, 0);
	PVector vmiddle = new PVector(0, 0);
	PVector vfront = new PVector(0, 5);

	public static final int TYPING = 0, STORY = 1, GAME = 2, GAME_OVER = 3,
			MENU = 42, SETTINGS = 43, PAUSE = 44, SUCCESS = 45;
	private float FRAMERATE = 28;
	public int state = MENU;

	String typing = "";
	String player = "";
	String loadingPoints = ".";

	public float xpos = 0; // Starting position of shape
	public float ypos;
	// NEGATIVE SPEED ---> GOING UP POSITIVE SPEED ---> GOING DOWN
	public float xspeed = (float) 0; // Speed of the shape (initial = 0)
	float yspeed = (float) 5; // Speed of the shape
	private boolean isInGame = true;
	private boolean buttonGame, buttonSettings, buttonBall, buttonEyes,
			buttonMouth, buttonCustom, buttonBack, buttonSound, buttonRestart;

	public Timer timer = new Timer(this);
	private Hole trapInHole = null;
	private boolean pause = false;
	public boolean nyancatmode = false;
	private boolean buttonGameKeyboard = false;
	private boolean buttonSettingsKeyboard = false;
	private boolean buttonRestartKeyboard = false;
	private boolean buttonBallKeyboard = false;
	private boolean buttonEyesKeyboard = false;
	private boolean buttonMouthKeyboard = false;
	private boolean buttonCustomKeyboard = false;
	private boolean multithreading = false;
	private boolean keyLeft = false, keyRight = false;
	public static int loading = 0;

	@Override
	public void init() {
		super.init();
		String a = getParameter("multithreading");
		if (a != null && a.equals("1")) {
			multithreading = true;
		}
	}

	public void setup() {
		initDisplayParameters();
		initLoadingImages();
		loadFonts();
		if (multithreading) {
			thread("initialization");
		} else {
			initialization();
		}

	}

	/**
	 * 
	 */
	private void initLoadingImages() {
		loading1 = loadImage("loading/Load1.png");
		loading2 = loadImage("loading/BarEmpty.png");
		loading3 = loadImage("loading/Load3.png");
		loading4 = loadImage("loading/Full1.png");
		loading5 = loadImage("loading/BarFull.png");
		loading6 = loadImage("loading/Full3.png");

	}

	public void initialization() {
		loadMusics();
		loadImages();
		initSpot();
		initStars();
		loadLevel();
		initTable();
		loading++;
		System.out.println(loading);
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
		f = loadFont("fonts/draw.vlw");
		loading++;
		fontSW = loadFont("fonts/font.vlw");
		loading++;
		fontDigital = loadFont("fonts/digital.vlw");
		loading++;

	}

	private void initDisplayParameters() {
		frameRate(FRAMERATE);
		ellipseMode(RADIUS);
		size(500, 500, P2D);
		smooth();
	}
	

	private void initSpot() {
		loading++;
		sp1 = new Spot(this, xpos, ypos, 20);
		reinitPositionAndSpeed();
		loading++;
	}

	private void initStars() {
		stars = new Star[200];
		for (int i = 0; i < stars.length; i++)
			stars[i] = new Star();

		// Initialize the offset
		offset = new PVector(width / 2, height / 2);
		loading++;
	}

	private void loadLevel() {
		if (lvl == null) {
			lvl = new Level(this);
		}
		if (multithreading) {
			thread("testThread");
		} else {
			testThread();
		}

	}

	public void testThread() {
		lvl.loadLevel();
	}

	private void loadImages() {		
		coin = loadImage("coin.png");
		loading++;
		img = loadImage("space_background.jpg");
		loading++;
		floor = loadImage("Background1.png");
		floor = floor.get(0, 356, floor.width, 144);
		loading++;
		backg = loadImage("Background1.png");
		backg = backg.get(0,0,backg.width,356);
		ceiling = loadImage("up.png");
		loading++;
		stars1 = loadImage("starsBack.jpg");
		loading++;
		saturn = loadImage("saturn.png");
		loading++;
		starsOver = loadImage("goscreen.png");
		loading++;
		gameover = loadImage("gover.png");
		loading++;
		avatars = loadImage("avatars.png");
		loading++;
		sound_on = loadImage("sound_on.png");
		loading++;
		sound_off = loadImage("sound_off.png");
		loading++;
		current_sound = sound_on;
		loading++;
		rainbow = loadImage("rainbow.jpg");
		loading++;
		bonus = loadImage("bonus.png");
		loading++;
		heart = loadImage("heart.png");
		heart1 = loadImage("heart.png");
		loading++;
		powerup = loadImage("powerup.png");
		loading++;
		hole = loadImage("hole.png");
		loading++;
		platformIm = loadImage("Ground_Bubbles.png");
		loading++;
		enemy = loadImage("static_enemy.png");
		loading++;
		staticAlien = loadImage("alien.png");
		loading++;
		movingAlien = loadImage("static_enemy.png");
		loading++;
		lasergun = loadImage("lasergun.png");
		loading++;
		nasty = loadImage("nasty.png");
		loading++;
		laserSprite = loadImage("laser.png");
		loading++;
		watch = loadImage("watch.png");
		loading++;

	}

	@Override
	public PImage loadImage(String filename) {
		return super.loadImage("images/" + filename);
	}

	private void loadMusics() {
		minim = new Minim(this);
		musicPlayer = minim.loadFile("musics/music.mp3");
		loading++;
		bouncingPlayer = minim.loadFile("musics/bounce.mp3");
		loading++;
		bonusPlayer = minim.loadFile("musics/bonus.wav");
		loading++;
		keyPlayer = minim.loadFile("musics/keySound.mp3");
		loading++;
		gunPlayer = minim.loadFile("musics/Gun.mp3");
		loading++;
		rayPlayer = minim.loadFile("musics/Ray_gun.mp3");
		loading++;
		explosionPlayer = minim.loadFile("musics/Explosion.mp3");
		loading++;

		minim.stop();

	}
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#resize(java.awt.Dimension)
	 */
	
	
	private void saveScore() {
		TableRow newRow = table.addRow();
		newRow.setInt("Id", table.lastRowIndex());
		newRow.setString("Name", player);
		newRow.setInt("Score", sp1.acumulativeScore);
		saveTable(table, "tables/scores.csv", "csv");
	}

	public void draw() {

		if (loading < 37) {

			displayLoading();
			return;
		}

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

			if (!lvl.isLoading()) {
				displayGame();
			} else {
				pushStyle();
				background(50);
				textAlign(CENTER);
				fill(255);
				text("Loading", width / 2, height / 2);
				popStyle();
			}
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
	private void displayLoading() {
		pushStyle();
		background(50);
		textAlign(CENTER);
		fill(255);
		textFont(fontSW);
		text("WIZZBALL", width / 2, height / 2 - 10);		
		int progress = loading/(38/11);
		float x = 20+width/15;
		if(progress <=0){
			image(loading1,20, height/2,width/15,height/10);
		} else {
			image(loading4,20, height/2,width/15,height/10);
		}
		
		if(progress<38){
			image(loading3,width-20-width/15, height/2,width/15,height/10);
		} else {
			image(loading6,width-20-width/15, height/2,width/15,height/10);
		}

		while(x<progress*width/15){
			image(loading5,x,height/2,width/15, height/10);
			x += width/15;
		}
		
		while(x < width-20-width/15){
			image(loading2,x,height/2,width/15, height/10);
			x += width/15;
		}


		popStyle();		
	}

	/**
	 * 
	 */
	private void nyancatMode() {

		if (!nyancatmode) {

			frameRate(40);
			sp1.ball = loadImage("nyancat.png");
			sp1.mouth = null;
			sp1.eyes = null;
			sp1.custom = null;

			musicPlayer.close();
			musicPlayer = minim.loadFile("musics/nyancat.mp3");
			nyancatmode = true;
		}

	}

	@SuppressWarnings("unchecked")
	private void displayGame() {

		clear();
		stroke(0);
		strokeWeight(5);
		
		pushMatrix();
		translate(-xpos/5, 0);
		image(backg, 0, (float) (height*0.1));
		popMatrix();
		
		pushMatrix();
		int pos = (int) (xpos/ceiling.width);
		translate(-xpos, 0);
		image(ceiling, pos*ceiling.width, 0);
		image(ceiling, (pos-1)*ceiling.width, 0);
		image(ceiling, (pos+1)*ceiling.width, 0);

		popMatrix();
		
		
		if (keyRight) {
			sp1.accelerateRotation(INCR_SPEED);
		}
		if (keyLeft) {
			sp1.accelerateRotation(-INCR_SPEED);
		}
		
		/*if (!nyancatmode) {
			if (lvl.getImage() != null) {
				background(lvl.getImage());
			} else {
				background(50);
			}
		} else {
			background(239, 89, 123);
		}*/
//		displayStars();

		textAlign(LEFT);
		textFont(f, 14);

		//paraDrawCeiling(ceiling, 500, xpos);
		paraDrawFloor(floor, 500, xpos);

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
			sp1.lives = 0;
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

		if (sp1.score >= 800) {
			sp1.incrementLives();
			sp1.score = 0;
		}

		if (achieveLevel()) {
			nextLevel();
		}

		//displayTextBoxGame();

	}

	/**
	 * 
	 */
	private void reinitPositionAndSpeed() {
		sp1.deleteBullet();
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
		textFont(f, 40);
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
		if (buttonBall || buttonBallKeyboard)
			fill(100, 100, 100);
		rect(50, y4, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonEyes || buttonEyesKeyboard)
			fill(100, 100, 100);
		rect(50, y3, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonMouth || buttonMouthKeyboard)
			fill(100, 100, 100);
		rect(50, y2, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonCustom || buttonCustomKeyboard)
			fill(100, 100, 100);
		rect(50, y1, width - 100, h, 10);
		popStyle();

		fill(200, 226, 9, 240);
		textFont(f, 30);

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
		background(50);
		textFont(f, 40);
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
		if (buttonGame || buttonGameKeyboard)
			fill(100, 100, 100);
		rect(50, y1, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonRestart || buttonRestartKeyboard)
			fill(100, 100, 100);
		rect(50, y3, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonSettings || buttonSettingsKeyboard)
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
		background(50);
		textFont(f, 40);
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
		if (buttonGame || buttonGameKeyboard)
			fill(100, 100, 100);
		rect(50, y1, width - 100, h, 10);
		popStyle();

		pushStyle();
		if (buttonSettings || buttonSettingsKeyboard)
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

		pushStyle();

		fill(10);
		strokeWeight(2);
		stroke(200);

		int h0 = height;
		int h1 = height - 10;
		int h2 = height - 83;

		int offset = 20;
		int w2 = 125;
		int w3 = 70;

		int yA = 0;

		PShape shape4 = createShape();
		shape4.beginShape();
		shape4.vertex(0, h0);
		shape4.vertex(0, h1);
		yA += 18;
		shape4.vertex(yA, h1);
		yA += offset;
		shape4.vertex(yA, h2);
		yA += w2;
		shape4.vertex(yA, h2);
		yA += offset;
		shape4.vertex(yA, h1);
		yA = width - 7 - 2 * offset - w3;
		shape4.vertex(yA, h1);
		yA += offset;
		shape4.vertex(yA, h2);
		yA += w3;
		shape4.vertex(yA, h2);
		yA += offset;
		shape4.vertex(yA, h1);
		shape4.vertex(width, h1);
		shape4.vertex(width, h0);
		shape4.endShape(CLOSE);

		int h4 = 23;
		int w1 = 200;

		PShape shape = createShape();
		shape.beginShape();
		shape.vertex(width / 2 - w1 / 2, 0);
		shape.vertex(width / 2 + w1 / 2, 0);
		shape.vertex(width / 2 + w1 / 2 - 10, h4);
		shape.vertex(width / 2 - w1 / 2 + 10, h4);
		shape.endShape(CLOSE);

		shape(shape4);
		shape(shape);
		popStyle();

		pushStyle();
		int sFont = 20;
		textAlign(CENTER);
		fill(255);
		textFont(f, sFont);
		text(("Level " + lvl.currentLevel), width / 2, h4 / 2 + sFont / 2 - 3);
		popStyle();

		float xb = 18+offset+5;
		
		coin.resize(20, 20);
		heart1.resize(20, 20);
		image(coin, xb, h2 + 10);
		for (int i = 0; i < sp1.lives; i++) {
			image(heart1, xb + i * 20, h2 +30);
		}
		
		textAlign(LEFT,CENTER);

		text(sp1.acumulativeScore, xb+ coin.width + 5, h2 +20);
		text("Stars left: " + lvl.nbBonus, xb, h2 + 60);
		timer.display(width - 61, (int) (height * 0.92), 25);
	}

	private void displayStoryScreen() {
		pushStyle();
		pushMatrix();

		background(50);

		stroke(0);
		strokeWeight(5);
		textFont(f, 20);
		textAlign(CENTER);
		int xFont = width / 2;
		text("Hello " + player + ", you will enter the game"
				+ "\n You can rotate the character using arrows keys."
				+ "\n Use G to change the GRAVITY", xFont, height/4);
		text("Press TAB to continue...", xFont, height-20);


		popStyle();
		popMatrix();

	}

	private void displayNameScreen() {
		pushStyle();
		pushMatrix();
		stroke(0);
		strokeWeight(5);
		background(50);
		textAlign(CENTER);
		textFont(f, 18);
		fill(200);
		stroke(153);
		text(" Hello, welcome to Wizzball game.\n Please, enter your name and press ENTER...\n",
				width / 2, 50);
		textFont(f, 30);
		text(typing, width / 2, height / 2);
		popStyle();
		popMatrix();
	}

	private void imagesResizing() {
		// img.resize(width, height);
		stars1.resize(width, height);
		ceiling.resize(ceiling.width, (int) (height * 0.1));
		floor.resize(width * 9, (int) (height * 0.2));
		if (lvl.getImage() != null)
			lvl.getImage().resize(width, height);
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
		xspeed = abs(xspeed) > MAX_SPEED ? (xspeed / abs(xspeed)) * MAX_SPEED
				: xspeed;
		playBounceSound();

	}

	private void xbounce() {
		xspeed = xspeed * -1;
		yspeed += sp1.rotationSpeed * rotationEffect; // rotation effect
		yspeed = abs(yspeed) > MAX_SPEED ? (yspeed / abs(yspeed)) * MAX_SPEED
				: yspeed;
		playBounceSound();

	}

	private void bounceCorner() {
		xspeed = xspeed * -1;
		yspeed += sp1.rotationSpeed * rotationEffect; // rotation effect
		yspeed = abs(yspeed) > MAX_SPEED ? (yspeed / abs(yspeed)) * MAX_SPEED
				: yspeed;

		yspeed = yspeed * -1;
		xspeed += sp1.rotationSpeed * rotationEffect; // rotation effect
		xspeed = abs(xspeed) > MAX_SPEED ? (xspeed / abs(xspeed)) * MAX_SPEED
				: xspeed;

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

	public void playExplosionSound() {
		explosionPlayer.rewind();
		explosionPlayer.play();
	}

	private void manageFloorCollision() {
		// The ball can't go under the floor
		ypos = (float) (ypos < height * 0.1 + sp1.radius ? height * 0.1
				+ sp1.radius : ypos);
		ypos = (float) (ypos > height * 0.8 - sp1.radius ? height * 0.8
				- sp1.radius : ypos);

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
																		// number
																		// for
																		// proper
																		// collision
																		// with
																		// ceiling

			ybounce();
		}
	}

	private void manageObjectsCollision() {
		for (BasicObject p : lvl.objects) {
			if (p.isDisplay()) {

				// racine_carre((x_point - x_centre)� + (y_centre - y_point)) <
				// rayon
				if (p.isCollide(C_TOP_LEFT)) {
					if (p instanceof Collectable) {
						((Collectable) p).collect();
					} else if (p instanceof Collidable
							&& (((Collidable) (p)).getCollidablesEdges()[C_TOP_LEFT])) {
						xpos = p.getLeft() - sp1.radius;
						ypos = p.getTop() - sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_TOP_RIGHT)) {
					if (p instanceof Collectable) {
						((Collectable) p).collect();
					} else if (p instanceof Collidable
							&& (((Collidable) (p)).getCollidablesEdges()[C_TOP_RIGHT])) {
						xpos = p.getRight() + sp1.radius;
						ypos = p.getTop() - sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_BOTTOM_LEFT)) {
					if (p instanceof Collectable) {
						((Collectable) p).collect();
					} else if (p instanceof Collidable
							&& (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM_LEFT])) {
						xpos = p.getLeft() - sp1.radius;
						ypos = p.getBottom() + sp1.radius;
						bounceCorner();
					}
					continue;
				}

				if (p.isCollide(C_BOTTOM_RIGHT)) {
					if (p instanceof Collectable) {
						((Collectable) p).collect();
					} else if (p instanceof Collidable
							&& (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM_RIGHT])) {
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
					} else if (p instanceof Collidable
							&& (((Collidable) (p)).getCollidablesEdges()[C_TOP])) {
						ypos = p.getTop() - sp1.radius;
						ybounce();
					} else if (p instanceof Hole) {
						trapInHole = (Hole) p;
					}
				}

				if (p.isCollide(C_BOTTOM)) {
					if (p instanceof Collectable) {
						((Collectable) p).collect();
					} else if (p instanceof Collidable
							&& (((Collidable) (p)).getCollidablesEdges()[C_BOTTOM])) {
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
				} else if (p instanceof Collidable
						&& (((Collidable) (p)).getCollidablesEdges()[C_LEFT])) {
					xpos = p.getLeft() - sp1.radius;
					xbounce();
				}
			}

			// /Right collision
			if (p.isCollide(C_RIGHT)) {
				if (p instanceof Collectable) {
					((Collectable) p).collect();
				} else if (p instanceof Collidable
						&& (((Collidable) (p)).getCollidablesEdges()[C_RIGHT])) {
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
		textFont(f, 40);
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
		text("    Name      " + "Score    ", w + 218, h + 200);
		text("------------------------------", w + 215, h + 215);

		for (TableRow row : table.rows()) {

			int id = row.getInt("Id");
			String name = row.getString("Name");
			int score = row.getInt("Score");

			text("     " + name + "      " + score, w + 200, h + 235 + id * 15);
		}
		textFont(f, 40);
		fill(50);
		stroke(40);
		strokeWeight(5);

		pushStyle();
		textFont(fontSW, 50);
		fill(200, 226, 9, 240);
		text("GAME\nOVER",width/2,height/4);
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
				scoreSaved = false;
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
		xpos = 0;
		ypos = width / 2;
		xspeed = 0;
		yspeed = 5;
		gravity = 0.5f;
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

		/*
		 * Store the sequence -> Easter Egg
		 */

		if (sequence.size() == 10) {
			sequence.removeLast();
		}
		if (key == CODED) {
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
			if (keyCode == RIGHT && !keyRight) {
				sp1.accelerateRotation(INCR_SPEED);
				keyRight = true;
			}
			if (keyCode == LEFT && !keyLeft) {
				sp1.accelerateRotation(-INCR_SPEED);
				keyLeft = true;
			}
			if (key == ' ') {
				if (sp1.isAllowedToShoot()) {
					sp1.activateWeapon();
				}
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
			break;

		case MENU:
			if (keyCode == DOWN || keyCode == UP) {
				if (buttonGameKeyboard == false
						&& buttonSettingsKeyboard == false) {
					buttonGameKeyboard = true;
				}

				else if (buttonGameKeyboard == true
						&& buttonSettingsKeyboard == false) {
					buttonGameKeyboard = false;
					buttonSettingsKeyboard = true;
				} else if (buttonGameKeyboard == false
						&& buttonSettingsKeyboard == true) {
					buttonGameKeyboard = true;
					buttonSettingsKeyboard = false;
				}
			}

			else if (keyCode == ENTER || key == ' ') {
				if (buttonGameKeyboard) {
					buttonGameKeyboard = false;
					state = TYPING;
				} else if (buttonSettingsKeyboard) {
					state = SETTINGS;
					buttonSettingsKeyboard = false;
				}
			}

			break;

		case PAUSE:
			if (keyCode == DOWN || keyCode == UP) {
				if (buttonGameKeyboard == false
						&& buttonSettingsKeyboard == false
						&& buttonRestartKeyboard == false) {
					buttonGameKeyboard = true;
				}

				else if (buttonGameKeyboard == true) {
					buttonGameKeyboard = false;
					buttonRestartKeyboard = keyCode == DOWN;
					buttonSettingsKeyboard = !(keyCode == DOWN);
				} else if (buttonRestartKeyboard == true) {
					buttonSettingsKeyboard = (keyCode == DOWN);
					buttonGameKeyboard = !(keyCode == DOWN);
					buttonRestartKeyboard = false;
				} else if (buttonSettingsKeyboard == true) {
					buttonGameKeyboard = (keyCode == DOWN);
					buttonRestartKeyboard = !(keyCode == DOWN);
					buttonSettingsKeyboard = false;
				}
			}

			else if (keyCode == ENTER || key == ' ') {
				if (buttonGameKeyboard) {
					state = GAME;
					timer.unpause();
					buttonGameKeyboard = false;
				} else if (buttonSettingsKeyboard) {
					state = SETTINGS;
					buttonSettingsKeyboard = false;
				} else if (buttonRestartKeyboard) {
					buttonRestartKeyboard = false;
					timer.unpause();
					restartGame();
				}
			} else if (key == ESC) {
				key = 0; // Avoid killing process
				buttonGameKeyboard = false;
				buttonSettingsKeyboard = false;
				buttonRestartKeyboard = false;
				timer.unpause();
				state = GAME;
			}

			break;

		case SETTINGS:
			if (keyCode == DOWN || keyCode == UP) {
				if (!buttonBallKeyboard && !buttonEyesKeyboard
						&& !buttonMouthKeyboard && !buttonCustomKeyboard) {
					buttonBallKeyboard = true;
				} else if (buttonBallKeyboard == true) {
					buttonBallKeyboard = false;
					buttonEyesKeyboard = keyCode == DOWN;
					buttonCustomKeyboard = !(keyCode == DOWN);
				} else if (buttonEyesKeyboard == true) {
					buttonMouthKeyboard = (keyCode == DOWN);
					buttonBallKeyboard = !(keyCode == DOWN);
					buttonEyesKeyboard = false;
				} else if (buttonMouthKeyboard == true) {
					buttonCustomKeyboard = (keyCode == DOWN);
					buttonEyesKeyboard = !(keyCode == DOWN);
					buttonMouthKeyboard = false;
				} else if (buttonCustomKeyboard == true) {
					buttonBallKeyboard = (keyCode == DOWN);
					buttonMouthKeyboard = !(keyCode == DOWN);
					buttonCustomKeyboard = false;
				}
			}

			else if (keyCode == ENTER || key == ' ') {
				if (buttonBallKeyboard) {
					sp1.changeBall();
				} else if (buttonEyesKeyboard) {
					sp1.changeEyes();
				} else if (buttonMouthKeyboard) {
					sp1.changeMouth();
				} else if (buttonCustomKeyboard) {
					sp1.changeCustom();
				}
			} else if (key == ESC) {
				key = 0; // Avoid killing process
				if (pause) {
					state = PAUSE;
				} else {
					state = MENU;
				}
			}

			break;

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
	
	
	@Override
	public void keyReleased() {
		if(keyCode == LEFT){
			keyLeft = false;
		} if(keyCode == RIGHT){
			keyRight = false;
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
			loc = new PVector(random(width * map(size, 1, 7, 7, 1)),
					random(height * map(size, 1, 7, 7, 1)));
			bright = (int) random(75, 150);
		}

		void display() {
			pushStyle();

			// Setup the style
			stroke(bright);
			strokeWeight(size);

			// Find the actual location and constrain it to within the bounds of
			// the screen
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
