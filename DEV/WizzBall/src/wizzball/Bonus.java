package wizzball;

public class Bonus extends BasicObject implements Collectable {

	float maxSize;
	boolean smaller = true;
	double smallerFactor = 0.9;
	boolean destroy = false;

	Bonus(Wizzball p, float xpos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
	}

	public Bonus(Wizzball p, float xpos, float ypos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
		y = ypos;
		maxSize = height;
	}

	@Override
	public void loadImage() {
		image = parent.loadImage("bonus.png");
	}

	@Override
	public void effect() {
		destroy = true;
	}

	private void delete() {
		parent.lvl.objects.remove(this);
		parent.lvl.nbBonus--;
		parent.playBonusSound();
	}

	@Override
	public void display() {

		parent.image(image, (x - width / 2 + parent.width / 2), y, width, width);

		if (destroy) {
			width -= maxSize / 5;
			x = parent.xpos;
			y = parent.ypos;
			if (width < maxSize/10) {
				delete();
			}
			return;
		}

		if (smaller) {
			width -= maxSize / 200;
		} else {
			width += maxSize / 200;
		}

		if (width == maxSize * smallerFactor || width == maxSize) {
			smaller = !smaller;
		}

	}

}
