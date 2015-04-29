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

	/**
	 * @param p
	 * @param xpos
	 * @param ypos
	 * @param height
	 * @param width
	 * @param down
	 */
	public BasicEnemy(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, ypos, height, width, down);
		destroySprite = parent.loadImage("explosion.png");
	}

	public void shoot(){
		parent.sp1.score += Wizzball.NASTIES_POINTS;
		destroy = true;

	}
	
	@Override
	public void effect() {
		if (parent.sp1.power) {
			parent.sp1.score += Wizzball.NASTIES_POINTS;
			parent.sp1.switchPower();
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
		if (numImage == 25) {
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
		int xAvatar = numImage % 5;
		int yAvatar = (numImage - xAvatar) / 5;

		int w = 64;
		int h = 64;

		return destroySprite.get(xAvatar * w + xAvatar, yAvatar * h + yAvatar, w, h);
	}

}
