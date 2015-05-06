/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.objects.collectable;

import wizzball.game.Wizzball;

public class PowerUp extends BasicCollectable {

	public PowerUp(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, ypos, height, width, down);
	}
	
	@Override
	public void effect() {
		parent.sp1.switchPower();
		parent.sp1.score+=Wizzball.POWER_POINTS;
		parent.sp1.acumulativeScore+=Wizzball.POWER_POINTS;
	}
	
	@Override
	public void loadImage() {
		image = parent.powerup;
	}
	
}
	