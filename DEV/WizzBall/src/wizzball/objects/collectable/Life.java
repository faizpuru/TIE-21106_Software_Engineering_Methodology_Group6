package wizzball.objects.collectable;

import wizzball.Wizzball;

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
		image = parent.loadImage("heart.png");
	}
	
}
	