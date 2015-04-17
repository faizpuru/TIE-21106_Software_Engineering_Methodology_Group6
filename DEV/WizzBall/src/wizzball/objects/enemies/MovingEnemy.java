/**
 * TUT _ Tampere
 * TIE-21106_Software_Engineering_Methodology
 * Group 6
 */
package wizzball.objects.enemies;

import wizzball.Wizzball;

/**
 * Enemy who don't move
 *
 */
public class MovingEnemy extends BasicEnemy {

	float x1, y1, x2, y2;
	private float directorCoeff;
	boolean direction = true;
	boolean vertical = false;

	/**
	 * @param p
	 * @param xpos
	 * @param ypos
	 * @param height
	 * @param width
	 * @param down
	 */
	public MovingEnemy(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, ypos, height, width, down);

	}

	public MovingEnemy(Wizzball p, float xpos, float ypos, float x2, float y2, float height, float width, boolean down) {
		
		
		super(p, xpos, ypos, height, width, down);
		if(!isGreater(xpos,x2)){
			this.x1 = xpos;
			this.y1 = ypos;
			this.x2 = x2;
			this.y2 = y2;
		} else {
			this.x1 = xpos;
			this.y1 = ypos;
			this.x2 = x2;
			this.y2 = y2;
			direction = !direction;
		}	
		

		directorCoeff = (y2 - y1) / (x2 - x1);
		
		if(x1==x2){
			vertical  = true;
		}

	}
	
	/**
	 * 
	 */
	public boolean isGreater(float a, float b) {
		if(a>b){
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.basics.BasicObject#loadImage()
	 */
	@Override
	public void loadImage() {
		image = parent.loadImage("static_enemy.png");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.collectable.BasicCollectable#display()
	 */
	@Override
	public void display() {

		super.display();

		if (!vertical) {
			if (xAbs > x2) {
				direction = !direction;
				xAbs = x2;
			} else if (xAbs < x1) {
				direction = !direction;
				xAbs = x1;
			}
		} else {
			if(isGreater(y1, y2)){
				if(y>y1){
					direction = !direction;
					y = y1;
				} else if(y<y2){
					direction = !direction;
					y = y2;
				}
			} else {
				if(y>y2){
					direction = !direction;
					y = y2;
				} else if(y<y1){
					direction = !direction;
					y = y1;
				}
			}
		}

		
		
		if (direction) {
			if (!vertical) {
				xAbs = xAbs + 1;
				y = y + directorCoeff;
			} else {
				y++;
			}

		} else {
			if (!vertical) {
				xAbs = xAbs - 1;
				y = y - directorCoeff;
			} else {
				y--;
			}
		}
	}

}
