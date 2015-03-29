package wizzball;

import processing.core.PApplet;

public class Bonus extends BasicObject {

	Bonus(PApplet p, float xpos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
	}
	
	public Bonus(PApplet p, float xpos, float ypos, float height, float width, boolean down){
		super(p, xpos, height, width, down);
		y = ypos;
	}
	
	@Override
	public void loadImage() {
		image =  parent.loadImage("bonus.png");
	}

}
