package redgear.core.api.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * A tile-based version of King Lemming's IBlockDebug. Designed to be used along side a block implementing IBlockDebug. 
 * @author BlackHole
 */
public interface ITileDebug {

	/**
	 * This function debugs a block.
	 * 
	 * @param side The side of the block.
	 * @param player Player doing the debugging.
	 */
	public void debugBlock(ForgeDirection side, EntityPlayer player);
}
