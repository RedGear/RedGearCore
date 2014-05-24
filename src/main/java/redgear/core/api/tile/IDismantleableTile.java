package redgear.core.api.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * A version of King Lemming's IDismantleable designed for Tiles, namely by not passing the unnecessary world or coords. 
 * Designed to be used with a Block that implements IDismantleable and asks the Tile for implementation.
 * @author BlackHole
 */
public interface IDismantleableTile {

	/**
	 * Dismantles the tile. A return of null will tell the block to drop the default block drop. 
	 */
	public ItemStack dismantleBlock(EntityPlayer player, boolean holdingWrench, boolean crouching);

	/**
	 * Return true if the tile can be dismantled.
	 */
	public boolean canDismantle(EntityPlayer player, boolean holdingWrench, boolean crouching);
}
