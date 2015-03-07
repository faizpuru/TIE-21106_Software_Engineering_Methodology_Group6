package wizzball;

import processing.core.*;

public class Platform {

	float x, y, width, height, xAbs;
	boolean down; // if down, platform on floor, !down, platform on ceiling
	PApplet parent;
	static PImage platform;

	// Second version of the platform constructor;
	// the fields are assigned with parameters
	Platform(PApplet p, float xpos, float height, float width, boolean down) {

		parent = p;
		xAbs = xpos;
		this.height = height;
		this.width = width;
		this.down = down;
		if (platform == null)
			platform = p.loadImage("platform.png");
		y = down ? (float) (parent.height * 0.8 - height)
				: (float) (parent.height * 0.1);

	}

	public void display() {
			parent.image(platform, (x - width / 2 + parent.width / 2), y,
					width, height);
			/*parent.rect( (x - width / 2 + parent.width / 2), y,
					width, height);*/
		
	}

	public void recalculatePlatformX(float xpos) {
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
