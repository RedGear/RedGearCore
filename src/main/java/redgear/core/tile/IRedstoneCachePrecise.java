package redgear.core.tile;

/**
 * Unlike {@link IRedstoneCache} IRedstoneCachePrecise can accept and retrieve the strength of the redstone signal. 
 * @author BlackHole
 */
public interface IRedstoneCachePrecise {

	public void setPower(int power);

	public int getPower();
}
