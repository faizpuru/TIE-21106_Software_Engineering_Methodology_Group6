package wizzball.objects.collectable;

import wizzball.Wizzball;

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
	}
}
