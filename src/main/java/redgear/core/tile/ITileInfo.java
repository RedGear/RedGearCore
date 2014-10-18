package redgear.core.tile;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * A Tile-based version of King Lemming's IBlockInfo. Designed to be used with Blocks that implement IBlockInfo
 * @author BlackHole
 *
 */
public interface ITileInfo {

	/**
	 * This function appends information to a list provided to it.
	 * @param side The side of the block that is being queried.
	 * @param player Player doing the querying - this can be NULL.
	 * @param info The list that the information should be appended to.
	 * @param debug If true, the Block should return "debug" information.
	 */
	public void getBlockInfo(ForgeDirection side, EntityPlayer player, List<IChatComponent> info, boolean debug);
}
