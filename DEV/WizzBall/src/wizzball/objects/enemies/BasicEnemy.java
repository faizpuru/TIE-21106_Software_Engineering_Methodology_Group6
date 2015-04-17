/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.objects.enemies;

import wizzball.Wizzball;
import wizzball.objects.collectable.BasicCollectable;

/**
 * Extend this class to create an enemy
 *
 */
public abstract class BasicEnemy extends BasicCollectable{

	
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
	}

	@Override
	public void effect() {
		parent.state = Wizzball.GAME_OVER;
	}

}
