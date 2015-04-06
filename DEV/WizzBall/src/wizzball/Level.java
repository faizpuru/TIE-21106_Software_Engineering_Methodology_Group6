/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import processing.core.PApplet;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.collectable.Bonus;
import wizzball.objects.collectable.Life;
import wizzball.objects.collectable.PowerUp;
import wizzball.objects.collidable.Hole;
import wizzball.objects.collidable.Platform;
import wizzball.objects.enemies.StaticEnemy;

public class Level {

	int currentLevel = 1;
	int maximumTime = 0;
	int xBegin = 0;
	int xEnd = 0;
	public int nbBonus = 0;
	public Vector<BasicObject> objects = null;

	Wizzball wizz;

	public Level(Wizzball wizz) {
		this.wizz = wizz;
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
					maximumTime = wizz.millis() + PApplet.parseInt(words[1]) * 1000;
					nbLine++;
				}

				else {// Create platforms, stairs and holes
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
					
					if (words[0].equals("SE")) {
						objects.addElement(new StaticEnemy(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet.parseFloat(words[3]),
								true));
					}

					if (words[0].equals("L")) {
						objects.addElement(new Life(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet.parseFloat(words[3]),
								true));
					}
					
					if (words[0].equals("PU")) {
						objects.addElement(new PowerUp(wizz, PApplet.parseFloat(words[1]), PApplet.parseFloat(words[2]), PApplet.parseFloat(words[3]), PApplet.parseFloat(words[3]),
								true));
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
				System.out.println(strLine);
			}

			// Close the input stream
			br.close();

		} catch (IOException e) {
			System.out.println("SUCCESS");
		}
	}

}
