/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.objects.weapons;

import java.util.Vector;

import processing.core.PApplet;
import processing.core.PImage;
import wizzball.game.Wizzball;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.enemies.BasicEnemy;
import wizzball.utilities.Timer;

/**
 * @author francois
 *
 */
public class Pistol extends BasicWeapon {

	/**
	 * @param p
	 * @param xpos
	 * @param ypos
	 * @param height
	 * @param width
	 * @param down
	 */

	private int wBullet = 10, hBullet = 5;
	Vector<Bullet> bullets;
	static private int maximumNbBullets = 15;

	class Bullet {
		float xBullet = 0;
		float yBullet = 0;
		static final float sBullet = 5;
		static final int limitX = 200;

		/**
		 * X and Y
		 */
		public Bullet(float yB) {
			yBullet = yB;
			xBullet = parent.sp1.radius;

		}

		/**
		 * 
		 */
		private boolean isAlive( ) {
			return xBullet<parent.width/2;
		}

		/**
		 * display bullet
		 */
		private void display() {
			parent.pushStyle();
			parent.strokeWeight(0);
			parent.fill(100);
			parent.rect(xBullet+parent.width/2, yBullet, wBullet, hBullet,3);
			xBullet += sBullet;
			parent.popStyle();
		}
	}

	public Pistol(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, ypos, height, width, down);
		bullets = new Vector<Pistol.Bullet>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.weapons.BasicWeapon#weaponAnimation()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void weaponAnimation() {
		for (Bullet b : (Vector<Bullet>) bullets.clone()) {
			if (b.isAlive()) {
				b.display();
			} else {
				bullets.remove(b);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.weapons.BasicWeapon#isShooting()
	 */
	@Override
	public boolean isShooting() {
		return bullets.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.weapons.BasicWeapon#weaponEffect()
	 */
	@Override
	protected void weaponEffect() {

	}

	
	/* (non-Javadoc)
	 * @see wizzball.objects.weapons.BasicWeapon#activateWeapon()
	 */
	@Override
	public void activateWeapon() {
		if(bullets.size()<maximumNbBullets )
		bullets.addElement(new Bullet(parent.ypos));
	}
}
