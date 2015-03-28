package wizzball;
import processing.core.*;

public class Hole extends BasicObject{

	Hole(PApplet p, float xpos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
	}
	
	@Override
	public void loadImage() {
		image = parent.loadImage("hole.png");
		
	}

				



}
