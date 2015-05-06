/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.objects.collidable;

import wizzball.game.Wizzball;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.basics.Collidable;

public class Platform extends BasicObject implements Collidable {

	boolean top = true, bottom = true, left = true, right = true, topLeft = true, topRight = true, bottomLeft = true, bottomRight = true;

	public Platform(Wizzball p, float xpos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
	}

	@Override
	public void loadImage() {
		image = parent.platformIm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.basics.BasicObject#display()
	 */
	@Override
	public void display() {
		super.display();
		if (down)
			y = (float) (parent.height * 0.8) - height;
		else
			y = (float) (parent.height * 0.1);
	}

	@Override
	public boolean[] getCollidablesEdges() {
		boolean[] collidableEdges = { top, bottom, left, right, topLeft, topRight, bottomLeft, bottomRight };
		return collidableEdges;
	}

}
