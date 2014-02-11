package redgear.core.block;

import java.util.ArrayList;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.asm.RedGearCore;
import redgear.core.tile.TileEntityGeneric;
import redgear.core.util.SimpleItem;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MetaTile extends MetaBlock implements ITileEntityProvider {
	private final int[] directionMap = {2, 5, 3, 4 };

	public MetaTile(Material par2Material, String name) {
		super(par2Material, name);
		isBlockContainer = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		try {
			return getMetaTile(meta).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f, float g,
			float t) {
		SubBlock block = getMetaBlock(world.getBlockMetadata(x, y, z));

		if (block != null) {

			if (!world.isRemote && player.getHeldItem() != null && player.getHeldItem().getItem() != null
					&& IToolWrench.class.isAssignableFrom(player.getHeldItem().getItem().getClass())) {
				TileEntity tile = world.getTileEntity(x, y, z);
				if (tile != null && TileEntityGeneric.class.isAssignableFrom(tile.getClass())) {
					TileEntityGeneric tileGeneric = (TileEntityGeneric) tile;
					boolean test;

					if (player.isSneaking())
						test = tileGeneric.wrenchedShift(player, ForgeDirection.getOrientation(side));
					else
						test = tileGeneric.wrenched(player, ForgeDirection.getOrientation(side));

					if (!test)//if tile returns true, continue to the gui. false means stop. 
						return true;
				}
			}

			if (block instanceof IHasTile && ((IHasTile) block).hasGui() && !player.isSneaking()) {
				player.openGui(RedGearCore.inst, ((IHasTile) block).guiId(), world, x, y, z);
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

		if (tile instanceof TileEntityGeneric)
			((TileEntityGeneric) tile).setDirection(directionMap[MathHelper
					.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3]);
	}

	/**
	 * Make sure to pass a MetaTileBlock, not just MetaBlock, otherwise it will
	 * crash.
	 * 
	 * @param newBlock The new MetaTileBlock for this MetaTile
	 * @throws ClassCastException if the passed MetaBlock is not a MetaTileBlock
	 */
	@Override
	public SimpleItem addMetaBlock(SubBlock newBlock) throws ClassCastException {
		if (!(newBlock instanceof SubTile))
			throw new ClassCastException("MetaTile can only except MetaTileBlocks!");
		return addMetaBlock((SubTile) newBlock);
	}

	public SimpleItem addMetaBlock(SubTile newBlock) {
		GameRegistry.registerTileEntity(newBlock.tile, newBlock.tile.getName());
		return super.addMetaBlock(newBlock);
	}

	@Override
	protected boolean indexCheck(int index) {
		return blocks.size() > index && blocks.get(index) != null;
	}

	public Class<? extends TileEntity> getMetaTile(int meta) {
		return ((SubTile) blocks.get(meta)).tile;
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	@Override
	public int getDamageValue(World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile != null)
			for (Entry<Integer, SubBlock> test : blocks.entrySet())
				if (test.getValue() instanceof SubTile && ((SubTile) test.getValue()).tile.equals(tile.getClass()))
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
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>(1);

		int count = quantityDropped(metadata, fortune, world.rand);
		for (int i = 0; i < count; i++) {
			Item item = getItemDropped(metadata, world.rand, fortune);
			if (item != null)
				ret.add(new ItemStack(item, 1, getDamageValue(world, x, y, z)));
		}
		return ret;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return getMetaBlock(world.getBlockMetadata(x, y, z)).getBlockTexture(world, x, y, z, side);
	}

	/**
	 * Called when the block receives a BlockEvent - see World.addBlockEvent. By
	 * default, passes it on to the tile
	 * entity at this location. Args: world, x, y, z, blockID, EventID, event
	 * parameter
	 */
	@Override
	public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
		super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
		TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
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
		if (!world.isRemote)
			checkRedstone(world, x, y, z);
	}

	private void checkRedstone(World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof TileEntityGeneric)
			((TileEntityGeneric) tile).updateRedstone(world.isBlockIndirectlyGettingPowered(x, y, z));
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

		if (tile instanceof TileEntityGeneric)
			return ((TileEntityGeneric) tile).redstoneSignal(ForgeDirection.getOrientation(side).getOpposite());
		else
			return 0;
	}
}
