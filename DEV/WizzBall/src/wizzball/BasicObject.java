package wizzball;

import processing.core.PApplet;
import processing.core.PConstants;
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
		y = down ? (float) (parent.height * 0.8 - height) : (float) (parent.height * 0.1);

	}

	@SuppressWarnings("static-access")
	public void loadImage() {
		image = parent.createImage(100, 100, PConstants.RGB);
	}

	public void display() {
		parent.image(image, (x - width / 2 + parent.width / 2), y, width, height);
		/*
		 * parent.rect( (x - width / 2 + parent.width / 2), y, width, height);
		 */

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
		return xAbs - width / 2;
	}

	public float getRight() {
		return xAbs + width / 2; // - width;
	}

	public float getTop() {
		return y;
	}

	public float getBottom() {
		return y + height;
	}

	private boolean isTopCollide(Wizzball wizz) {
		if (wizz.xpos >= getLeft() && wizz.xpos <= getRight()) {
			if (getTop() >= wizz.ypos - wizz.sp1.radius && getTop() <= wizz.ypos + wizz.sp1.radius) {
				return true;
			}
		}
		return false;
	}

	private boolean isBottomCollide(Wizzball wizz) {
		if (wizz.xpos >= getLeft() && wizz.xpos <= getRight()) {
			if (getBottom() <= wizz.ypos + wizz.sp1.radius && getBottom() >= wizz.ypos - wizz.sp1.radius) {
				return true;
			}
		}
		return false;
	}

	private boolean isLeftCollide(Wizzball wizz) {
		if (wizz.xspeed > 0) {
			if (wizz.ypos + wizz.sp1.radius / 2 >= getTop() && wizz.ypos - wizz.sp1.radius / 2 <= getBottom()) {
				if (getLeft() <= wizz.xpos + wizz.sp1.radius / 2 && wizz.sp1.radius / 2 < x - width / 2) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isRightCollide(Wizzball wizz) {

		if (wizz.xspeed < 0) {
			if (wizz.ypos >= getTop() && wizz.ypos <= getBottom()) {
				if (getRight() >= wizz.xpos - wizz.sp1.radius / 2 && wizz.sp1.radius / 2 > x + width / 2) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isTopLeftCollide(Wizzball wizz) {
		return Math.sqrt(Math.pow((getLeft() - wizz.sp1.x), 2) + Math.pow((wizz.sp1.y - getTop()), 2)) <= wizz.sp1.radius;
	}

	private boolean isTopRightCollide(Wizzball wizz) {
		return Math.sqrt(Math.pow((getRight() - wizz.sp1.x), 2) + Math.pow((wizz.sp1.y - getTop()), 2)) <= wizz.sp1.radius;
	}

	private boolean isBottomLeftCollide(Wizzball wizz) {
		return Math.sqrt(Math.pow((getLeft() - wizz.sp1.x), 2) + Math.pow((wizz.sp1.y - getBottom()), 2)) <= wizz.sp1.radius;
	}

	private boolean isBottomRightCollide(Wizzball wizz) {
		return Math.sqrt(Math.pow((getRight() - wizz.sp1.x), 2) + Math.pow((wizz.sp1.y - getBottom()), 2)) <= wizz.sp1.radius;
	}

	boolean isCollide(Wizzball wizz, int edge) {
		if (edge == Wizzball.TOP) {
			return isTopCollide(wizz);
		} else if (edge == Wizzball.C_BOTTOM) {
			return isBottomCollide(wizz);
		} else if (edge == Wizzball.C_LEFT) {
			return isLeftCollide(wizz);
		} else if (edge == Wizzball.C_RIGHT) {
			return isRightCollide(wizz);
		} else if (edge == Wizzball.C_TOP_LEFT) {
			return isTopLeftCollide(wizz);
		} else if (edge == Wizzball.C_TOP_RIGHT) {
			return isTopRightCollide(wizz);
		} else if (edge == Wizzball.C_BOTTOM_LEFT) {
			return isBottomLeftCollide(wizz);
		} else if (edge == Wizzball.C_BOTTOM_RIGHT) {
			return isBottomRightCollide(wizz);
		} else {
			return false;
		}

	}

}
