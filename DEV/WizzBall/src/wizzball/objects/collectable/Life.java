/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.objects.collectable;

import wizzball.game.Wizzball;

public class Life extends BasicCollectable {

	public Life(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, ypos, height, width, down);
	}
	
	@Override
	public void effect() {
		parent.sp1.incrementLives();
	}
	
	@Override
	public void loadImage() {
		image = parent.heart;
	}
	
}
	