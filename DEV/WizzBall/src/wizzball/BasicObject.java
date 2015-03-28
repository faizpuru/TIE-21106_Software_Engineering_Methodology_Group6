package wizzball;

import processing.core.PApplet;
import processing.core.PImage;

public class BasicObject {

	float x, y, width, height, xAbs;
	boolean down; // if down, platform on floor, !down, platform on ceiling
	PApplet parent;
	PImage image;

	// Second version of the platform constructor;
	// the fields are assigned with parameters
	BasicObject(PApplet p, float xpos, float height, float width, boolean down) {

		parent = p;
		xAbs = xpos;
		this.height = height;
		this.width = width;
		this.down = down;
		if (image == null)
			loadImage();
		y = down ? (float) (parent.height * 0.8 - height)
				: (float) (parent.height * 0.1);

	}

	@SuppressWarnings("static-access")
	public void loadImage() {
		image = parent.createImage(100, 100, parent.RGB);
	}

	public void display() {
			parent.image(image, (x - width / 2 + parent.width / 2), y,
					width, height);
			/*parent.rect( (x - width / 2 + parent.width / 2), y,
					width, height);*/
		
	}

	public void recalculatePositionX(float xpos) {
		x = xAbs - xpos;
	}

	public boolean isDisplay() {

		float left = ((Wizzball) parent).getLimitX('l');
		float right = ((Wizzball) parent).getLimitX('r');

		if (xAbs - width / 2 > left)
			return true;
		if (xAbs + width / 2 < right)
			return true;
		return false;
	}

	public float getLeft() {
		return xAbs - width / 2 ;
	}

	public float getRight() {
		return xAbs + width / 2 ; //- width;
	}

	public float getTop() {
		return y;
	}

	public float getBottom() {
		return y + height;
	}

	
}
