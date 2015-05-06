/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.objects.weapons;

import java.util.Vector;

import processing.core.PImage;
import wizzball.game.Wizzball;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.basics.Collidable;
import wizzball.objects.enemies.BasicEnemy;

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
		boolean right;
		float xBullet = 0;
		float yBullet = 0;
		static final float sBullet = 10;
		static final int limitX = 200;
		private PImage rainbow;

		/**
		 * X and Y
		 */
		public Bullet(float yB) {
			yBullet = yB;
			if(parent.sp1.getDirection()=='r'){
				right = true;
				xBullet = parent.sp1.radius;
			} else {
				right = false;
				xBullet = -parent.sp1.radius;
			} 
			rainbow = parent.loadImage("rainbow.jpg");

		}

		/**
		 * 
		 */
		private boolean isAlive() {
			return xBullet < 3 * parent.width / 8 || xBullet > -3*parent.width/8;
		}

		/**
		 * display bullet
		 */
		private void display() {
			parent.pushStyle();
			parent.strokeWeight(0);
			parent.fill(255);
			if (right) {
				if(!parent.nyancatmode)
				parent.rect(xBullet + parent.width / 2, yBullet, wBullet, hBullet, 3);
				else
					parent.image(rainbow, xBullet + parent.width / 2, yBullet, wBullet, hBullet);
				xBullet += sBullet - parent.xspeed*0.2;
			} else {
				if(!parent.nyancatmode)
				parent.rect(xBullet + wBullet + parent.width / 2, yBullet, wBullet, hBullet, 3);
				else
					parent.image(rainbow, xBullet + parent.width / 2, yBullet, wBullet, hBullet);
				
				xBullet -= sBullet + parent.xspeed*0.2;
			}
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
	@SuppressWarnings("unchecked")
	@Override
	protected void weaponEffect() {
		for (Bullet b : (Vector<Bullet>) bullets.clone()) {
			float sup = b.right ? 0 : wBullet;
			for (BasicObject o : parent.lvl.objects) {
				if (o instanceof Collidable || o instanceof BasicEnemy) {
					if (b.xBullet + sup + parent.xpos >= o.getLeft() && b.xBullet + sup + parent.xpos <= o.getRight()) {
						if (b.yBullet <= o.getBottom() && b.yBullet >= o.getTop()) {
							bullets.remove(b);
							if (o instanceof BasicEnemy) {
								((BasicEnemy) o).shoot();
							}
						}
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see wizzball.objects.basics.BasicObject#loadImage()
	 */
	@Override
	public void loadImage() {
		image = parent.loadImage("pistol.png");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.weapons.BasicWeapon#activateWeapon()
	 */
	@Override
	public void activateWeapon() {
		if (bullets.size() < maximumNbBullets)
			bullets.addElement(new Bullet(parent.ypos));
	}
}
