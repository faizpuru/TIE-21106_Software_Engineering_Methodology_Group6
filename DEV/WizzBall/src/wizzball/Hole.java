package wizzball;
import processing.core.*;

public class Hole {
	
	
	float x, y, width, height, xAbs;
	boolean down = false;
	PApplet parent;
	static PImage hole ;
		
		Hole( PApplet p, float xpos, float height, float width, boolean down ){
			parent = p;
			this.xAbs = xpos;
			this.height = height;
			this.width = width;
			this.down = down;
			if ( hole == null )
				hole = p.loadImage("hole.png");
			y = down ? (float) (parent.height * 0.8 - height)
					: (float) (parent.height * 0.1);
			
		}
		
		public void recalculateHoleX(float xpos) {
			x = xAbs - xpos;
		}
		
		public void display() {
			parent.image(hole, (x - width / 2 + parent.width / 2), y,
					width, height);
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
