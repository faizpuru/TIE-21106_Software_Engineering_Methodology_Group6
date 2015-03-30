package wizzball;

import processing.core.*;

public class Platform extends BasicObject implements Collidable{

	boolean top = true, bottom = true, left = true, right = true,
			topLeft = true, topRight = true, bottomLeft = true, bottomRight = true;

	
	Platform(PApplet p, float xpos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
	}

	@Override
	public void loadImage() {
		image =  parent.loadImage("platform.png");
	}

	@Override
	public boolean[] getCollidablesEdges() {
		boolean[] collidableEdges = {top,bottom,left,right,topLeft,topRight,bottomLeft,bottomRight};
		return  collidableEdges;
	}

}
