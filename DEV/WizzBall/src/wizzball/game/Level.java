/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PImage;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.collectable.Bonus;
import wizzball.objects.collectable.Life;
import wizzball.objects.collectable.PowerUp;
import wizzball.objects.collidable.Hole;
import wizzball.objects.collidable.Platform;
import wizzball.objects.enemies.MovingEnemy;
import wizzball.objects.enemies.StaticEnemy;
import wizzball.objects.weapons.LaserPistol;
import wizzball.objects.weapons.Pistol;
import wizzball.game.Wizzball;


public class Level {

	PImage image;
	int currentLevel = 1 ;
	//starting level;
	int maximumTime = 0;
	int xBegin = 0;
	int xEnd = 0;
	public int nbBonus = 0;
	public Vector<BasicObject> objects = null;

	Wizzball wizz;
	private boolean loading;

	public Level(Wizzball wizz) {
		this.wizz = wizz;
	}

	public boolean isLoading() {
		return loading;
	}

	public int getLevel() {
		return this.currentLevel;
	}

	/**
	 * Load the level corresponding to the txt file
	 */
	@SuppressWarnings("unchecked")
	public void loadLevel() {

		loading = true;

		// Open the file
		try {
			FileInputStream fstream = new FileInputStream("data/levels/level" + currentLevel + ".txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			try {
				image = wizz.loadImage("level" + currentLevel + "_background.jpg");
				image.resize(500, 500);
			} catch (Exception e) {
				image = null;
			}
			// initialize platforms and holes containers
			if (objects == null)
				objects = new Vector<BasicObject>();
			else
				objects.clear();

			int nbLine = 0;
			String[] words;

			// Read File Line By Line

			nbBonus = 0;

			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				words = strLine.split(" ");

				// Initialize begin, end and time for the level
				if (nbLine == 0) {
					xBegin = PApplet.parseInt(words[1]);
					nbLine++;
				} else if (nbLine == 1) {
					xEnd = PApplet.parseInt(words[1]);
					objects.addElement(new Platform(wizz, xEnd + 50, (float) (wizz.height * 0.7), 40, true));
					objects.addElement(new Platform(wizz, xBegin, (float) (wizz.height * 0.7), 40, true));
					nbLine++;
				} else if (nbLine == 2) {
					wizz.timer.init(PApplet.parseInt(words[1]));
					nbLine++;
				}

				else {// Create platforms, stairs and holes
					for (int i = 0; i < words.length; i++) {
						try {

							words[i] = words[i].split(":")[1];
						} catch (Exception e) {
							// No description
						}
					}
					if (words[0].equals("P")) {
						boolean down = false;
						if (words[4].equals("+"))
							down = true;
						objects.addElement(new Platform(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), down));
					}

					if (words[0].equals("H")) {
						boolean down = false;
						if (words[4].equals("+"))
							down = true;
						objects.addElement(new Hole(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), down));
					}
					if (words[0].equals("B")) {
						nbBonus++;
						objects.addElement(new Bonus(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet.parseFloat(words[3]),
								true));
					}

					if (words[0].equals("LW")) {
						objects.addElement(new LaserPistol(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet
								.parseFloat(words[3]), true));
					}

					if (words[0].equals("PW")) {
						objects.addElement(new Pistol(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet.parseFloat(words[3]),
								true));
					}

					if (words[0].equals("SA")) {
						objects.addElement(new StaticEnemy(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet
								.parseFloat(words[3]),true,Wizzball.staticAlien,Wizzball.STALIEN_LIVES,Wizzball.STALIEN_POINTS));
					}

					if (words[0].equals("BO")) {
						objects.addElement(new MovingEnemy(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet.parseFloat(words[4]),
								PApplet.parseFloat(words[5]), PApplet.parseFloat(words[5]), true,Wizzball.enemy,Wizzball.BOMB_LIVES,Wizzball.BOMB_POINTS));
					}

					if (words[0].equals("N")) {
						objects.addElement(new MovingEnemy(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet.parseFloat(words[4]),
								PApplet.parseFloat(words[5]), PApplet.parseFloat(words[5]), true,Wizzball.nasty,Wizzball.NASTY_LIVES,Wizzball.NASTY_POINTS));
					}
					
					if (words[0].equals("MA")) {
						objects.addElement(new MovingEnemy(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet.parseFloat(words[4]),
								PApplet.parseFloat(words[5]), PApplet.parseFloat(words[5]), true,Wizzball.movingAlien,Wizzball.MVALIEN_LIVES,Wizzball.MVALIEN_POINTS));
					}

					if (words[0].equals("L")) {
						objects.addElement(new Life(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet.parseFloat(words[3]),
								true));
					}

					if (words[0].equals("PU")) {
						objects.addElement(new PowerUp(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]),
								PApplet.parseFloat(words[3]), true));
					}

					if (words[0].equals("S")) {
						boolean down = false;
						if (words[7].equals("+"))
							down = true;

						boolean direcStairs = false;
						if (words[6].equals("+"))
							direcStairs = true;

						int steps = PApplet.parseInt(words[1]);
						float begin = PApplet.parseFloat(words[2]);
						float end = PApplet.parseFloat(words[3]);
						float heightMin = PApplet.parseFloat(words[4]);
						float heightMax = PApplet.parseFloat(words[5]);

						float width = (PApplet.abs(end - begin)) / (steps - 1);
						float heightIncrement = (heightMax - heightMin) / steps;

						if (direcStairs) {
							for (int i = 0; i < steps; ++i) {
								objects.addElement(new Platform(wizz, begin + i * width, heightMin + heightIncrement * i, width, down));
							}
						} else {
							for (int i = 0; i < steps; ++i) {
								objects.addElement(new Platform(wizz, begin + i * width, heightMax - heightIncrement * i, width, down));
							}
						}

					}
				}
			}

			// Close the input stream
			br.close();

			for (BasicObject p : (Vector<BasicObject>) objects.clone()) {
				if (p.isDisplay()) {
					p.recalculatePositionX(wizz.xpos);
				}

			}

			loading = false;

		} catch (IOException e) {
			wizz.state = Wizzball.SUCCESS;
		}
	}

	/**
	 * @return background
	 */
	public PImage getImage() {
		// TODO Auto-generated method stub
		return image;
	}
}
