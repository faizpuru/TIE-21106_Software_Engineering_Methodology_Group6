package wizzball.objects.enemies;

import wizzball.game.Wizzball;

public class Bomb extends MovingEnemy {

	
	
	public Bomb(Wizzball p, float xpos, float ypos, float x2, float y2,
			float height, float width, boolean down) {
		super(p, xpos, ypos, x2, y2, height, width, down);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.basics.BasicObject#loadImage()
	 */
	@Override
	public void loadImage() {
		image = parent.nasty;
	}

}
