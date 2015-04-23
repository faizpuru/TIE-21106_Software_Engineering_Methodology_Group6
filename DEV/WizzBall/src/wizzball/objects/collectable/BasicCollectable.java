/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */

package wizzball.objects.collectable;

import wizzball.Wizzball;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.basics.Collectable;

/**
 * BasicCollectable is a very easy way to create a collectable object
 * To use it : - Override loadimage to change the image 
 * 			   - Override effect and add an effect
 */
public abstract class BasicCollectable extends BasicObject implements Collectable {
	float maxSize;
	boolean smaller = true;
	double smallerFactor = 0.9;
	boolean destroy = false;

	private BasicCollectable(Wizzball p, float xpos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
	}

	public BasicCollectable(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
		y = ypos;
		maxSize = height;
	}
	
	@Override
	public void collect(){
		destroy = true;
	}

	@Override
	public void effect() {
	}

	protected void delete() {
		parent.lvl.objects.remove(this);
		parent.playBonusSound();
		effect();
	}

	@Override
	public void display() {

		parent.image(image, (x - width / 2 + parent.width / 2), y, width, width);

		if (destroy) {
			destroyAnimation();	
			return;
		}

		if (smaller) {
			width -= maxSize / 200;
		} else {
			width += maxSize / 200;
		}

		if (width == maxSize * smallerFactor || width == maxSize) {
			smaller = !smaller;
		}

	}

	/**
	 * Override to change the animation 
	 */
	protected void destroyAnimation() {
		width -= maxSize / 5;
		x = parent.xpos;
		y = parent.ypos;
		if (width < maxSize / 10) {
			delete();
		}
		return;		// TODO Auto-generated method stub
		
	}
}
