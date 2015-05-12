/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.objects.collidable;

import wizzball.game.Wizzball;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.basics.Collidable;

public class Hole extends BasicObject implements Collidable{
	boolean top = false, bottom = false, left = true, right = true,
			topLeft = true, topRight = true, bottomLeft = true, bottomRight = true;


	public Hole(Wizzball p, float xpos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
	}
	
	@Override
	public void loadImage() {
		image = parent.hole;
		
	}
	
	/* (non-Javadoc)
	 * @see wizzball.objects.basics.BasicObject#display()
	 */
	@Override
	public void display() {
		parent.pushMatrix();
		parent.translate(x + parent.width/2, y + height);
		int sign = -1;

		if(down){
			parent.rotate(Wizzball.PI);
			sign = 1;
		}
		parent.image(image, (- width / 2), 0, width, sign*height);
		parent.popMatrix();
	}

	@Override
	public boolean[] getCollidablesEdges() {
		boolean[] collidableEdges = {top,bottom,left,right,topLeft,topRight,bottomLeft,bottomRight};
		return  collidableEdges;
	}

				



}
