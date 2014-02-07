package redgear.core.render;


public class DrawSnippet extends GuiRegion implements GuiElement {

	private final int snipX;
	private final int snipY;

	/**
	 * Copies a piece off this texture and draws the snippet somewhere else. IE:
	 * Furnace progress bar or burn time.
	 * 
	 * @param x - X Coord where the snippet WILL BE DRAWN.
	 * @param y - Y Coord where the snippet WILL BE DRAWN.
	 * @param width - Width of the snippet. TO and FROM are the same.
	 * @param height - Height of the snippet. TO and FROM are the same.
	 * @param snipX - X Coord of where to snip FROM.
	 * @param snipY - Y Coord of where to snip FROM.
	 */
	public DrawSnippet(int x, int y, int width, int height, int snipX, int snipY) {
		super(x, y, width, height);
		this.snipX = snipX;
		this.snipY = snipY;
	}

	public int getSnipX() {
		return snipX;
	}

	public int getSnipY() {
		return snipY;
	}

	@Override
	public void draw(GuiGeneric gui) {
		gui.drawRectangleSnip(getX1(), getY1(), getX2(), getY2(), getSnipX(), getSnipY(), gui.guiLocation);
	}
}
