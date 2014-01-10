package redgear.core.api.world;

import java.util.Collection;

import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import redgear.core.api.item.ISimpleItem;

public interface ILocation {
	
	/**
	 * @return The X coord saved by this location
	 */
	public int getX();
	
	/**
	 * @return The Y coord saved by this location
	 */
	public int getY();
	
	/**
	 * @return The Z coord saved by this location
	 */
	public int getZ();

	/**
	 * @return A new location with the same coordinates.
	 */
	public ILocation copy();

	/**
	 * @param world World this location is in
	 * @param block Block to look for
	 * @param oreDict If true will check for ore dictionary compatibility as well as id and meta.
	 * @return True if the block at this location equals the block provided.
	 */
	public boolean check(IBlockAccess world, ISimpleItem block, boolean oreDict);

	/**
	 * @param world World this location is in
	 * @param block Block to look for
	 * @return True if the block at this location equals the block provided.
	 */
	public boolean check(IBlockAccess world, ISimpleItem block);
	
	/**
	 * @param world World this location is in
	 * @param block Block to look for
	 * @param oreDict If true will check for ore dictionary compatibility as well as id and meta.
	 * @param relative This location will be moved by the x, y and z provided by relative before checking. 
	 * @return True if the block at this location equals the block provided.
	 */
	public boolean check(IBlockAccess world, ISimpleItem block, boolean oreDict, ILocation relative);
	
	/**
	 * @param world World this location is in
	 * @param block Block to look for
	 * @param relative This location will be moved by the x, y and z provided by relative before checking. 
	 * @return True if the block at this location equals the block provided.
	 */
	public boolean check(IBlockAccess world, ISimpleItem block, ILocation relative);

	/**
	 * @param world World this location is in
	 * @param block Blocks to look for
	 * @param oreDict If true will check for ore dictionary compatibility as well as id and meta.
	 * @return True if the block at this location equals any of the blocks provided.
	 */
	public boolean check(IBlockAccess world, Collection<ISimpleItem> blocks, boolean oreDict);

	/**
	 * @param world World this location is in
	 * @param block Blocks to look for
	 * @return True if the block at this location equals any of the blocks provided.
	 */
	public boolean check(IBlockAccess world, Collection<ISimpleItem> blocks);

	/**
	 * @param world World this location is in
	 * @param block Blocks to look for
	 * @param relative This location will be moved by the x, y and z provided by relative before checking. 
	 * @return True if the block at this location equals any of the blocks provided.
	 */
	public boolean check(IBlockAccess world, Collection<ISimpleItem> blocks, ILocation relative);

	/**
	 * @param world World this location is in
	 * @param block Blocks to look for
	 * @param oreDict If true will check for ore dictionary compatibility as well as id and meta.
	 * @param relative This location will be moved by the x, y and z provided by relative before checking. 
	 * @return True if the block at this location equals any of the blocks provided.
	 */
	public boolean check(IBlockAccess world, Collection<ISimpleItem> blocks, boolean oreDict, ILocation relative);

	/**
	 * @param world World this location is in
	 * @param type Class of TileEntity to look for
	 * @param relative This location will be moved by the x, y and z provided by relative before checking. 
	 * @return True if the block at this location equals the block provided.
	 */
	public boolean check(IBlockAccess world, Class<? extends TileEntity> type, ILocation relative);

	/**
	 * @param world World this location is in
	 * @param type Class of TileEntity to look for
	 * @return True if the block at this location equals the block provided.
	 */
	public boolean check(IBlockAccess world, Class<? extends TileEntity> type);

	/**
	 * @param world World this location is in
	 * @param item Block to place in this location
	 */
	public void placeBlock(World world, ISimpleItem item);

	/**
	 * @param world World this location is in
	 * @param item Block to place in this location
	 * @param relative This location will be moved by the x, y and z provided by relative before placing a block. 
	 */
	public void placeBlock(World world, ISimpleItem item, ILocation relative);

	/**
	 * @param world World this location is in
	 * @param item Block to place in this location
	 * @param target Block to check for before placing
	 * @param oreDict If true will check for ore dictionary compatibility as well as id and meta.
	 * @return true if the target was found and replaced, false if the block at this location was not valid and replace not performed
	 */
	public boolean placeBlock(World world, ISimpleItem item, ISimpleItem target, boolean oreDict);

	/**
	 * @param world World this location is in
	 * @param item Block to place in this location
	 * @param target Block to check for before placing
	 * @return true if the target was found and replaced, false if the block at this location was not valid and replace not performed
	 */
	public boolean placeBlock(World world, ISimpleItem item, ISimpleItem target);

	/**
	 * @param world World this location is in
	 * @param item Block to place in this location
	 * @param target Block to check for before placing
	 * @param oreDict If true will check for ore dictionary compatibility as well as id and meta.
	 * @param relative This location will be moved by the x, y and z provided by relative before placing a block. 
	 * @return true if the target was found and replaced, false if the block at this location was not valid and replace not performed
	 */
	public boolean placeBlock(World world, ISimpleItem item, ISimpleItem target, boolean oreDict,
			ILocation relative);

	/**
	 * @param world World this location is in
	 * @param item Block to place in this location
	 * @param target Block to check for before placing
	 * @param relative This location will be moved by the x, y and z provided by relative before placing a block. 
	 * @return true if the target was found and replaced, false if the block at this location was not valid and replace not performed
	 */
	public boolean placeBlock(World world, ISimpleItem item, ISimpleItem target, ILocation relative);

	/**
	 * @param world World this location is in
	 * @param item Block to place in this location
	 * @param target Blocks to check for before placing
	 * @param oreDict If true will check for ore dictionary compatibility as well as id and meta.
	 * @param relative This location will be moved by the x, y and z provided by relative before placing a block. 
	 * @return true if the target was found and replaced, false if the block at this location was not valid and replace not performed
	 */
	public boolean placeBlock(World world, ISimpleItem item, Collection<ISimpleItem> targets, boolean oreDict);

	/**
	 * @param world World this location is in
	 * @param item Block to place in this location
	 * @param target Blocks to check for before placing
	 * @return true if the target was found and replaced, false if the block at this location was not valid and replace not performed
	 */
	public boolean placeBlock(World world, ISimpleItem item, Collection<ISimpleItem> targets);

	/**
	 * @param world World this location is in
	 * @param item Block to place in this location
	 * @param target Blocks to check for before placing
	 * @param relative This location will be moved by the x, y and z provided by relative before placing a block. 
	 * @return true if the target was found and replaced, false if the block at this location was not valid and replace not performed
	 */
	public boolean placeBlock(World world, ISimpleItem item, Collection<ISimpleItem> targets, ILocation relative);

	/**
	 * @param world World this location is in
	 * @param item Block to place in this location
	 * @param target Blocks to check for before placing
	 * @param oreDict If true will check for ore dictionary compatibility as well as id and meta.
	 * @return true if the target was found and replaced, false if the block at this location was not valid and replace not performed
	 */
	public boolean placeBlock(World world, ISimpleItem item, Collection<ISimpleItem> targets, boolean oreDict,
			ILocation relative);

	/**
	 * @param world World this location is in
	 * @return ID of block in this location.
	 */
	public int getBlockId(IBlockAccess world);

	/**
	 * @param world World this location is in
	 * @return Meta of block in this location.
	 */
	public int getBlockMeta(IBlockAccess world);

	/**
	 * @param world World this location is in
	 * @return Tile of block in this location, or null if there is none.
	 */
	public TileEntity getTile(IBlockAccess world);

	/**
	 * @param world World this location is in
	 * @return BlockMaterial of block in this location.
	 */
	public Material getBlockMaterial(IBlockAccess world);

	/**
	 * @param world World this location is in
	 * @return true if the block is air, false otherwise.
	 */
	public boolean isAir(IBlockAccess world);

	/**
	 * Rotates this location along a given axis from the origin
	 * @param direction ForgeDirection about which to rotate
	 * @param degrees Integer of clockwise 90 degree turns IE: 0 nothing, 1 clockwise once, 2 reversed, 3 counter clockwise. 
	 * @return This object
	 */
	public ILocation rotate(ForgeDirection direction, int degrees);

	/**
	 * Reflects this location across the axis provided at the origin
	 * @param direction Direction to reflect (reverse)
	 * @return This object
	 */
	public ILocation reflect(ForgeDirection direction);

	/**
	 * Moves this location by the x, y and z values in other.
	 * @param other Another location to move with.
	 * @return This object
	 */
	public ILocation translate(ILocation other);

	/**
	 * Moves this location in the forge direction by the amount
	 * @param direction ForgeDirection ID to move along
	 * @param amount Amount of blocks to move
	 * @return This object
	 */
	public ILocation translate(int direction, int amount);

	/**
	 * Moves this location by the x, y and z provided.
	 * @param x
	 * @param y
	 * @param z
	 * @return This object
	 */
	public ILocation translate(int x, int y, int z);

	/**
	 * Moves this location in the ForgeDirection by the amount
	 * @param direction ForgeDirection to move along
	 * @param amount Amount of blocks to move
	 * @return This object
	 */
	public ILocation translate(ForgeDirection direction, int amount);

	/**
	 * @return Unique HashCode of this location
	 */
	public int hashCode();

	/**
	 * @param other Another Location
	 * @return true if this location's x, y and z match other's x, y and z
	 */
	public boolean equals(ILocation other);
	/**
	 * Saves this location to the tag.
	 * @param tag NBTTagCompound to save this location to.
	 */
	public void writeToNBT(NBTTagCompound tag);

	/**
	 * Saves this location to a subtag with name to tag
	 * @param tag Root tag to add the new subtag to
	 * @param name Name of the subtag to be added to tag that will save this location to.
	 */
	public void writeToNBT(NBTTagCompound tag, String name);

}