package wizzball;

public class Bonus extends BasicObject implements Collectable {

	Bonus(Wizzball p, float xpos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
	}
	
	public Bonus(Wizzball p, float xpos, float ypos, float height, float width, boolean down){
		super(p, xpos, height, width, down);
		y = ypos;
	}
	
	@Override
	public void loadImage() {
		image =  parent.loadImage("bonus.png");
	}

	@Override
	public void effect(Wizzball wizz) {
			wizz.lvl.objects.remove(this);
			wizz.lvl.nbBonus--;
			wizz.playBonusSound();
		
	}

}
