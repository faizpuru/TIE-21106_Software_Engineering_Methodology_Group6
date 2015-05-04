/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.objects.collectable;

import wizzball.game.Wizzball;

public class Bonus extends BasicCollectable {

	public Bonus(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, ypos, height, width, down);
	}

	@Override
	public void loadImage() {
		image = parent.loadImage("bonus.png");
	}

	@Override
	public void effect() {
		parent.lvl.nbBonus--;
		parent.sp1.score+=Wizzball.STARS_POINTS;
		parent.sp1.acumulativeScore+=Wizzball.STARS_POINTS;

	}
}
