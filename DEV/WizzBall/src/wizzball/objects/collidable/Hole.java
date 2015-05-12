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
		int factor = 1;
		int xCrop = (int) parent.random(factor*width,image.width-factor*width);
		int yCrop = (int) parent.random(factor*height,image.height-factor*height);
		image = image.get((int)(xCrop - width*factor), (int) (yCrop-height*2), (int)width*2*factor, (int)height*2*factor);
	}
	
	/* (non-Javadoc)
	 * @see wizzball.objects.basics.BasicObject#display()
	 */
	@Override
	public void display() {
		parent.pushMatrix();
		parent.pushStyle();
		parent.fill(255);
		parent.translate(x + parent.width/2, y + height);
		int sign = -1;

		if(down){
			parent.rotate(Wizzball.PI);
			sign = 1;
		}
		
		parent.strokeWeight(1);
		parent.rect((- width / 2 + width/20), (float) (-height*0.1), width-width/10, (float) (sign*height*0.9));
		parent.pushStyle();
		parent.fill(240,60,60);
		parent.rect((- width / 2), 0, width, (float) (sign*height*0.1));
		parent.popStyle();
		parent.image(image, (- width / 2 + width/20), (float) (-height*0.1), width-width/10, (float) (sign*height*0.9));
		parent.popMatrix();
		parent.popStyle();
	}
	
	@Override
	public void loadImage() {
		image = parent.hole;
		
	}

	@Override
	public boolean[] getCollidablesEdges() {
		boolean[] collidableEdges = {top,bottom,left,right,topLeft,topRight,bottomLeft,bottomRight};
		return  collidableEdges;
	}

				



}
