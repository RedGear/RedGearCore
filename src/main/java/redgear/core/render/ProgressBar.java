package redgear.core.render;

import java.awt.Color;

public class ProgressBar extends GuiRegion implements GuiElement{

	public final int id;
	public int total = 0;
	public int value = 0;

	public ProgressBar(int id, int x, int y, int width, int height, int value) {
		super(x, y, width, height);
		this.id = id;
		this.value = value;
	}

	public ProgressBar(int id, int x, int y, int width, int height) {
		this(id, x, y, width, height, 0);
	}

	@Override
	public int getY1() {
		if (total > 0)
			return getY() + getHeight() - (int) (getHeight() * ((double) value / (double) total)); //sanity check. 
		else
			return getY2();
	}

	public int getColor() {
		return Color.GREEN.getRGB();
	}

	@Override
	public void draw(GuiGeneric gui) {
		if (getY1() != getY2())
			gui.drawRectangleSolid(getX1(), getY1(), getX2(), getY2(), getColor());
	}
}
