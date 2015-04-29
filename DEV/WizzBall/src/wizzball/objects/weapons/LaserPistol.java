/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.objects.weapons;

import processing.core.PApplet;
import processing.core.PImage;
import wizzball.game.Wizzball;
import wizzball.objects.basics.BasicObject;
import wizzball.objects.enemies.BasicEnemy;

/**
 * @author francois
 *
 */
public class LaserPistol extends BasicWeapon {

	/**
	 * @param p
	 * @param xpos
	 * @param ypos
	 * @param height
	 * @param width
	 * @param down
	 */

	static PImage sprite;
	private int numSprite = 0;
	private int wBullet = 97, hBullet = 53;
	private int numberImages = 7;

	public LaserPistol(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, ypos, height, width, down);
		if (sprite == null) {
			sprite = parent.loadImage("laser.png");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.weapons.BasicWeapon#weaponAnimation()
	 */
	@Override
	public void weaponAnimation() {

		if (isShooting()) {
			parent.pushMatrix();
			if (parent.sp1.getDirection() == 'l') {
				parent.translate(parent.width / 2, parent.ypos);
				parent.rotate(PApplet.PI);
				parent.image(sprite.get(numSprite * wBullet, 0, wBullet - 1, hBullet), parent.sp1.radius, -parent.sp1.radius);
			} else {
				parent.image(sprite.get(numSprite * wBullet, 0, wBullet - 1, hBullet), parent.width / 2 + parent.sp1.radius, parent.ypos - parent.sp1.radius);
			}

			parent.popMatrix();
			numSprite++;
			if (numSprite >= numberImages) {
				isShooting = false;
				numSprite = 0;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.weapons.BasicWeapon#weaponEffect()
	 */
	@Override
	protected void weaponEffect() {
		for (BasicObject o : parent.lvl.objects) {
			if (o instanceof BasicEnemy) {
				float t = o.getTop();
				float b = o.getBottom();
				float l = o.getLeft();
				float r = o.getRight();

				float topBullet = parent.ypos - hBullet / 2;
				float bottomBullet = parent.ypos + hBullet / 2;

				if (parent.sp1.getDirection() == 'r') {
					if (bottomBullet >= t && topBullet <= b) {
						if (l > parent.xpos && l < parent.xpos + parent.sp1.radius + wBullet) {
							((BasicEnemy) o).shoot();
						}
					}
				} else {
					if (bottomBullet >= t && topBullet <= b) {
						if (r < parent.xpos && r > parent.xpos - parent.sp1.radius - wBullet) {
							((BasicEnemy) o).shoot();
						}
					}
				}

			}
		}
	}

}
