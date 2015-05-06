package wizzball.objects.enemies;

import wizzball.game.Wizzball;

public class Nasty extends MovingEnemy{

	public Nasty(Wizzball p, float xpos, float ypos, float height,
			float width, boolean down) {
		super(p, xpos, ypos, height, width, down);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see wizzball.objects.basics.BasicObject#loadImage()
	 */
	@Override
	public void loadImage() {
		image = parent.enemy;
	}

}
