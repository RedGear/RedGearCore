package redgear.core.api.tile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IFacedTile {

	/**Returns the forge direction ordinal this block is facing.**/
	int getDirectionId();

	/**Returns the ForgeDirection this tile is facing. **/
	ForgeDirection getDirection();

	/**Sets the facing direction from ForgeDirection ordinal. **/
	boolean setDirection(int id);

	/**
	 * Set the facing direction of this tile
	 * 
	 * @param side Side to set direction to
	 * @return true if block changed, false if it didn't. 
	 */
	boolean setDirection(ForgeDirection side);
	
	/**
	 * Blocks should call this to allow the Tile to decide how to face itself. 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param entity
	 * @param stack
	 */
	void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack);
	
}
