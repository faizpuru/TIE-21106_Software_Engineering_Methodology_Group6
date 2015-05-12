/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.objects.enemies;

import processing.core.PImage;
import wizzball.game.Wizzball;

/**
 * Enemy who don't move
 *
 */
public class StaticEnemy extends BasicEnemy{


	
	/**
	 * @param p
	 * @param xpos
	 * @param ypos
	 * @param height
	 * @param width
	 * @param down
	 */
	public StaticEnemy(Wizzball p, float xpos, float ypos, float height, float width, boolean down,PImage img,
			int liv, int pt) {
		super(p, xpos, ypos, height, width, down,img,liv,pt);
	}

	/* (non-Javadoc)
	 * @see wizzball.objects.basics.BasicObject#loadImage()
	 */
	@Override
	public void loadImage() {
		image = Wizzball.enemy;
	}



	
	

}
