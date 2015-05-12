/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.objects.collidable;

import processing.core.PImage;
import processing.core.PShape;
import wizzball.game.Wizzball;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.basics.Collidable;

public class Platform extends BasicObject implements Collidable {

	boolean top = true, bottom = true, left = true, right = true, topLeft = true, topRight = true, bottomLeft = true, bottomRight = true;

	public Platform(Wizzball p, float xpos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
		image = image.get((int)(image.width/2 - width), (int) (image.height/2-height), (int)width*2, (int)height*2);

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
		//parent.image(image, (x - width / 2 + parent.width / 2), y, width, height);
		
		parent.pushStyle();
		parent.strokeWeight(2);
		
		PShape shape = parent.createShape();
		shape.beginShape();
		shape.vertex(0,0);
		shape.vertex(width,0);
		shape.vertex(width,height);
		shape.vertex(0,height);		
		shape.endShape(Wizzball.CLOSE);
		

		parent.shape(shape,(x - width / 2 + parent.width / 2), y, width, height);
		parent.popStyle();
		parent.image(image, (x - width / 2 + parent.width / 2), y, width, height);
		
		
		
		


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
