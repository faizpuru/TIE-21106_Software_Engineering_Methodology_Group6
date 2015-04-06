/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.objects.collidable;

import wizzball.Wizzball;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.basics.Collidable;

public class Platform extends BasicObject implements Collidable{

	boolean top = true, bottom = true, left = true, right = true,
			topLeft = true, topRight = true, bottomLeft = true, bottomRight = true;

	
	public Platform(Wizzball p, float xpos, float height, float width, boolean down) {
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
