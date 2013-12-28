package redgear.core.render;

public class LiquidGauge {
	
	public int liquidID = 0;
	public int liquidAmount = 0;
	public final int tankCapacity;
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	public final int tankId;
	
	public LiquidGauge(int tankCapacity, int x, int y, int width, int height, int tankId){
		this.tankCapacity = tankCapacity;
		
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tankId = tankId;
	}
	
}
