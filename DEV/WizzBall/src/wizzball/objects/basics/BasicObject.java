/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.objects.basics;

import processing.core.PConstants;
import processing.core.PImage;
import wizzball.game.Wizzball;
import wizzball.objects.enemies.BasicEnemy;
import wizzball.objects.weapons.BombPistol;

public class BasicObject {

	protected float x;
	protected float y;
	protected float width;
	protected float height;
	protected float xAbs;
	public boolean down; // if down, platform on floor, !down, platform on ceiling
	protected Wizzball parent;
	protected PImage image;

	// Second version of the platform constructor;
	// the fields are assigned with parameters
	protected BasicObject(Wizzball p, float xpos, float height, float width, boolean down) {

		parent = p;
		xAbs = xpos;
		this.height = height;
		this.width = width;
		this.down = down;
		if (image == null)
			loadImage();
		y = down ? (float) (parent.height * 0.8 - height) : (float) (parent.height * 0.1);

	}
	

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

		if ((xAbs +   width  > left )  &&  (xAbs -  width < right))
			return true;
		
		return false;
	}
	
	private float getRadius(){
		boolean bomb = (parent.sp1.getActiveWeapon()!=null && parent.sp1.getActiveWeapon() instanceof BombPistol && parent.sp1.getActiveWeapon().isShooting());
		if(this instanceof BasicEnemy && bomb){
			return ((BombPistol)parent.sp1.getActiveWeapon()).sizeBullet;
		} else {
			return parent.sp1.radius;
		}
		
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

	private boolean isTopCollide() {
		if (parent.xpos >= getLeft() && parent.xpos <= getRight()) {
			if (getTop() >= parent.ypos - getRadius() && getTop() <= parent.ypos + getRadius()) {
				return true;
			}
		}
		return false;
	}

	private boolean isBottomCollide() {
		if (parent.xpos >= getLeft() && parent.xpos <= getRight()) {
			if (getBottom() <= parent.ypos + getRadius() && getBottom() >= parent.ypos - getRadius()) {
				return true;
			}
		}
		return false;
	}

	private boolean isLeftCollide() {
		if (parent.xspeed > 0) {
			if (parent.ypos + getRadius() >= getTop() && parent.ypos - getRadius() <= getBottom()) {
				if (getLeft() <= parent.xpos + getRadius() && getRadius() < x - width / 2) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isRightCollide() {

		if (parent.xspeed < 0) {
			if (parent.ypos >= getTop() && parent.ypos <= getBottom()) {
				if (getRight() >= parent.xpos - getRadius() && getRadius() > x + width / 2) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isTopLeftCollide() {
		return Math.sqrt(Math.pow((getLeft() - parent.sp1.x), 2) + Math.pow((parent.sp1.y - getTop()), 2)) <= getRadius();
	}

	private boolean isTopRightCollide() {
		return Math.sqrt(Math.pow((getRight() - parent.sp1.x), 2) + Math.pow((parent.sp1.y - getTop()), 2)) <= getRadius();
	}

	private boolean isBottomLeftCollide() {
		return Math.sqrt(Math.pow((getLeft() - parent.sp1.x), 2) + Math.pow((parent.sp1.y - getBottom()), 2)) <= getRadius();
	}

	private boolean isBottomRightCollide() {
		return Math.sqrt(Math.pow((getRight() - parent.sp1.x), 2) + Math.pow((parent.sp1.y - getBottom()), 2)) <= getRadius();
	}

	public boolean isCollide(int edge) {
		if (edge == Wizzball.C_TOP) {
			return isTopCollide();
		} else if (edge == Wizzball.C_BOTTOM) {
			return isBottomCollide();
		} else if (edge == Wizzball.C_LEFT) {
			return isLeftCollide();
		} else if (edge == Wizzball.C_RIGHT) {
			return isRightCollide();
		} else if (edge == Wizzball.C_TOP_LEFT) {
			return isTopLeftCollide();
		} else if (edge == Wizzball.C_TOP_RIGHT) {
			return isTopRightCollide();
		} else if (edge == Wizzball.C_BOTTOM_LEFT) {
			return isBottomLeftCollide();
		} else if (edge == Wizzball.C_BOTTOM_RIGHT) {
			return isBottomRightCollide();
		} else {
			return false;
		}

	}
	
	public float getX(){
		return xAbs;
	}

}
