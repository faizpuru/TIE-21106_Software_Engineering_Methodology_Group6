package wizzball;

public class Hole extends BasicObject implements Collidable{
	boolean top = false, bottom = false, left = true, right = true,
			topLeft = true, topRight = true, bottomLeft = true, bottomRight = true;


	Hole(Wizzball p, float xpos, float height, float width, boolean down) {
		super(p, xpos, height, width, down);
	}
	
	@Override
	public void loadImage() {
		image = parent.loadImage("hole.png");
		
	}

	@Override
	public boolean[] getCollidablesEdges() {
		boolean[] collidableEdges = {top,bottom,left,right,topLeft,topRight,bottomLeft,bottomRight};
		return  collidableEdges;
	}

				



}
