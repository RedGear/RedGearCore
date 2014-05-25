package redgear.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import redgear.core.api.tile.IBucketableTank;
import redgear.core.api.tile.IDismantleableTile;
import redgear.core.api.tile.IFacedTile;
import redgear.core.api.tile.IRedstoneCachePrecise;
import redgear.core.api.tile.IRedstoneEmiter;
import redgear.core.api.tile.ITileDebug;
import redgear.core.api.tile.ITileInfo;
import redgear.core.api.tile.IWrenchableTile;
import redgear.core.asm.RedGearCore;
import redgear.core.world.WorldLocation;
import buildcraft.api.tools.IToolWrench;
import cofh.api.block.IBlockDebug;
import cofh.api.block.IBlockInfo;
import cofh.api.block.IDismantleable;
import cofh.api.tileentity.IRedstoneCache;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MetaTile extends MetaBlock<SubTile> implements ITileEntityProvider, IBlockDebug, IBlockInfo, IDismantleable {

	public MetaTile(Material par2Material, String name) {
		super(par2Material, name);
		isBlockContainer = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if (indexCheck(meta))
			return getMetaBlock(meta).createTile();
		else
			return getMetaBlock(0).createTile();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f, float g,
			float t) {
		SubTile block = getMetaBlock(world.getBlockMetadata(x, y, z));

		if (block != null) {

			//if (!world.isRemote)
			if (player.getHeldItem() != null && player.getHeldItem().getItem() != null)
				if (holdingWrench(player)) {
					TileEntity tile = world.getTileEntity(x, y, z);
					if (tile instanceof IWrenchableTile) {
						IWrenchableTile wrenchable = (IWrenchableTile) tile;
						boolean test;

						if (player.isSneaking())
							test = wrenchable.wrenchedShift(player, ForgeDirection.getOrientation(side));
						else
							test = wrenchable.wrenched(player, ForgeDirection.getOrientation(side));

						if (!test)//if tile returns true, continue to the gui. false means stop.
							return true;
					}
				}

				else if (FluidContainerRegistry.isContainer(player.getHeldItem())) {
					TileEntity tile = world.getTileEntity(x, y, z);
					if (tile instanceof IBucketableTank
							&& ((IBucketableTank) tile).bucket(player, player.inventory.currentItem,
									player.getHeldItem()))
						return true;

				}

			if (block.hasGui() && !player.isSneaking()) {
				player.openGui(RedGearCore.inst, block.guiId(), world, x, y, z);
				return true;
			}
		}
		return false;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		if (entity == null)
			return;

		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof IFacedTile)
			((IFacedTile) tile).onBlockPlacedBy(world, x, y, z, entity, stack);
		
		checkRedstone(world, x, y, z);
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	@Override
	public int getDamageValue(World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile != null)
			for (Entry<Integer, SubTile> test : blocks.entrySet())
				if (test.getValue().createTile().getClass().equals(tile.getClass()))
					return test.getKey();
		return 0;
	}

	/**
	 * This returns a complete list of items dropped from this block.
	 *
	 * @param world The current world
	 * @param x X Position
	 * @param y Y Position
	 * @param z Z Position
	 * @param metadata Current metadata
	 * @param fortune Breakers fortune level
	 * @return A ArrayList containing all items this block drops
	 */
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		SubTile called = getMetaBlock(meta);
		WorldLocation loc = new WorldLocation(x, y, z, world);

		if (called instanceof IDifferentDrop)
			return ((IDifferentDrop) called).getDrops(loc, meta, fortune);
		else {
			ArrayList<ItemStack> ret = new ArrayList<ItemStack>(1);
			ret.add(new ItemStack(this, 1, meta));
			return ret;
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		if (indexCheck(meta))
			return getMetaBlock(meta).getBlockTexture(world, x, y, z, side);
		else
			return getMetaBlock(0).getBlockTexture(world, x, y, z, side);
	}

	/**
	 * Get the rotations that can apply to the block at the specified
	 * coordinates. Null means no rotations are possible.
	 * Note, this is up to the block to decide. It may not be accurate or
	 * representative.
	 *
	 * @param worldObj The world
	 * @param x X position
	 * @param y Y position
	 * @param z Z position
	 * @return An array of valid axes to rotate around, or null for none or
	 * unknown
	 */
	@Override
	public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z) {
		return ForgeDirection.VALID_DIRECTIONS;
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor block
	 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		//if (!world.isRemote)
			checkRedstone(world, x, y, z);
	}

	private void checkRedstone(World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof IRedstoneCachePrecise)
			((IRedstoneCachePrecise) tile).setPower(world.getStrongestIndirectPower(x, y, z));
		else if (tile instanceof IRedstoneCache)
			((IRedstoneCache) tile).setPowered(world.isBlockIndirectlyGettingPowered(x, y, z));
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the
	 * specified side. Args: World, X, Y, Z,
	 * side. Note that the side is reversed - eg it is 1 (up) when checking the
	 * bottom of the block.
	 */
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof IRedstoneEmiter)
			return ((IRedstoneEmiter) tile).getRedstoneSignal(ForgeDirection.getOrientation(side).getOpposite());
		else
			return 0;
	}

	@Override
	public void debugBlock(IBlockAccess world, int x, int y, int z, ForgeDirection side, EntityPlayer player) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof ITileDebug)
			((ITileDebug) tile).debugBlock(side, player);
	}

	@Override
	public void getBlockInfo(IBlockAccess world, int x, int y, int z, ForgeDirection side, EntityPlayer player,
			List<String> info, boolean debug) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof ITileInfo)
			((ITileInfo) tile).getBlockInfo(side, player, info, debug);
	}

	@Override
	public ItemStack dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof IDismantleableTile) {
			NBTTagCompound tag = new NBTTagCompound();
			ItemStack stack = ((IDismantleableTile) tile).dismantleBlock(player, tag, holdingWrench(player),
					player.isSneaking());

			if (stack == null)
				stack = new ItemStack(this, 1, world.getBlockMetadata(x, y, z));

			if (!tag.hasNoTags())
				stack.stackTagCompound = tag;

			if (returnBlock)
				player.inventory.addItemStackToInventory(stack);
			else
				this.dropBlockAsItem(world, x, y, z, stack);

			world.setBlockToAir(x, y, z);

			return stack;
		} else
			return null;
	}

	@Override
	public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof IDismantleableTile)
			return ((IDismantleableTile) tile).canDismantle(player, holdingWrench(player), player.isSneaking());
		else
			return false;
	}

	protected boolean holdingWrench(EntityPlayer player) {
		return player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IToolWrench;
	}
}
