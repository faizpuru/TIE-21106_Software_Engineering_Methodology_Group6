/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.objects.weapons;

import wizzball.game.Wizzball;
import wizzball.objects.collectable.BasicCollectable;
import wizzball.utilities.Timer;

/**
 * @author francois
 * @date 28.04.2015
 */
public abstract class BasicWeapon extends BasicCollectable {

	/**
	 * @param p
	 * @param xpos
	 * @param ypos
	 * @param height
	 * @param width
	 * @param down
	 */
	boolean isShooting = false;
	Timer t;

	public BasicWeapon(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, ypos, height, width, down);
		t = new Timer(p);
		t.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.collectable.BasicCollectable#effect() Add the weapon to the player
	 */
	@Override
	public void effect() {
		parent.sp1.switchWeapon(this);
	}

	public void weaponEffectAndAnimation() {
			weaponEffect();
			weaponAnimation();
	}
	
	public boolean isAllowedToShoot(){
		if (t.getMilliSecondsLeft() <= 0) {
			t.initMillis(getTimeBetweenTwoShoot());
			return true;
		}
		return false;
	}

	/**
	 * Override to create the effect of the weapon
	 */
	protected void weaponEffect() {

	}

	/**
	 * Override to change the animation of the weapon
	 */
	protected void weaponAnimation() {

	}

	/**
	 * Override to create the effect of the weapon
	 */
	public void activateWeapon() {
		isShooting = true;
	}

	/**
	 * @return
	 */
	protected int getTimeBetweenTwoShoot() {
		return 0;
	}

	/**
	 * @return
	 */
	public boolean isShooting() {
		return isShooting;
	}

}
