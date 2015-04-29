/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.objects.weapons;

import wizzball.game.Wizzball;
import wizzball.objects.collectable.BasicCollectable;

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
	
	public BasicWeapon(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, ypos, height, width, down);
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see wizzball.objects.collectable.BasicCollectable#effect()
	 * Add the weapon to the player
	 */
	@Override
	public void effect() {
		parent.sp1.switchWeapon(this);
	}
	
	public void weaponEffectAndAnimation(){
		weaponEffect();
		weaponAnimation();
	}
	
	/**
	 * Override to create the effect of the weapon
	 */
	protected void weaponEffect(){
		
	}
	
	/**
	 * Override to change the animation of the weapon
	 */
	protected void weaponAnimation(){
		
	}
	
	/**
	 * Override to create the effect of the weapon
	 */
	public void activateWeapon(){
		isShooting=true;
	}

	/**
	 * @return
	 */
	public boolean isShooting() {
		return isShooting;
	}
	
	
	
	

}
