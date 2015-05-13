/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.objects.enemies;

import processing.core.PImage;
import wizzball.game.Wizzball;
import wizzball.objects.collectable.BasicCollectable;

/**
 * Extend this class to create an enemy
 *
 */
public abstract class BasicEnemy extends BasicCollectable {

	PImage destroySprite;
	int numImage = 0;
	int lives,maxLives, damage, points;	

	/**
	 * @param p
	 * @param xpos
	 * @param ypos
	 * @param height
	 * @param width
	 * @param down
	 */
	public BasicEnemy(Wizzball p, float xpos, float ypos, float height, float width, boolean down, PImage img,
			int lives, int points) {
		super(p, xpos, ypos, height, width, down);
		destroySprite = parent.loadImage("explosion.png");
		this.lives = lives;
		maxLives = lives;
		this.points=points;
		image=img;
	}

	/**
	 * @return
	 */
	protected int getInitialLives() {
		return 1;
	}

	public void shoot(int damage){
		
		
		lives-=damage;
		if (lives<=0){
			destroy = true;
			parent.playExplosionSound();
			parent.sp1.score += points;
			parent.sp1.acumulativeScore+= points;
		}

	}
	
	@Override
	public void effect() {
		if (parent.sp1.power) {
			parent.sp1.score += points;
			parent.sp1.acumulativeScore+= points;
					
			parent.sp1.switchPower();
			parent.playExplosionSound();
		} else {
			parent.state = Wizzball.GAME_OVER;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.collectable.BasicCollectable#destroyAnimation()
	 */
	@Override
	protected void destroyAnimation() {
		if (numImage == 12) {
			delete();
		}

		image = getImage();
		numImage++;


	}

	/**
	 * 
	 * @return Correct image from the sprite
	 */
	private PImage getImage() {
		if(numImage == 10){
			numImage++;
		}
		int xAvatar = numImage % 4;
		int yAvatar = (numImage - xAvatar) / 4;

		int w = 283;
		int h = 237;
		
		

		return destroySprite.get(xAvatar * w + xAvatar, yAvatar * h + yAvatar, w, h);
	}

}
