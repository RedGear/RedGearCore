package redgear.core.render;

import java.awt.Color;

public class ProgressBar {

	public final int id;
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	public int total = 0;
	public int value = 0;
	
	public ProgressBar(int id, int x, int y, int width, int height, int value){
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.value = value;
	}
	
	public ProgressBar(int id, int x, int y, int width, int height){
		this(id, x, y, width, height, 0);
	}
	
	public int getX1(){
		return x;
	}
	
	public int getY1(){
		return y + height - (int) ((double)height * ((double) value / (double)total));
	}
	
	public int getX2(){
		return x + width;
	}
	
	public int getY2(){
		return y + height;
	}
	
	public int getColor(){
		return Color.GREEN.getRGB();
	}
}
