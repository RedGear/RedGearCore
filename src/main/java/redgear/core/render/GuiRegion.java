package redgear.core.render;

abstract class GuiRegion {
	private final int x;
	private final int y;
	private final int width;
	private final int height;

	public GuiRegion(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX1() {
		return getX();
	}

	public int getY1() {
		return getY();
	}

	public int getX2() {
		return getX() + getWidth();
	}

	public int getY2() {
		return getY() + getHeight();
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}
}
