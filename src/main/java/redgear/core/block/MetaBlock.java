package redgear.core.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import redgear.core.util.SimpleItem;
import redgear.core.world.WorldLocation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MetaBlock<S extends SubBlock> extends BlockGeneric {

	protected BiMap<Integer, S> blocks = HashBiMap.create();

	public MetaBlock(Material material, String name) {
		super(material, name, MetaItemBlock.class);
	}

	public SimpleItem addMetaBlock(S newBlock) throws IndexOutOfBoundsException {
		if (blocks.size() > 15 && !(this instanceof ITileEntityProvider))
			throw new IndexOutOfBoundsException(
					"MetaBlocks can only have 16 values! (0-15) You can't register 17! Use a MetaTile OR use another MetaBlocks.");

		blocks.put(blocks.size(), newBlock);
		SimpleItem item = new SimpleItem(this, blocks.size() - 1);
		GameRegistry.registerCustomItemStack(this.name + "." + newBlock.name, item.getStack());
		return item;
	}

	protected boolean indexCheck(int index) {
		return blocks.size() > index && blocks.get(index) != null;
	}

	public S getMetaBlock(int meta) {
		return blocks.get(meta);
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

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public IIcon getIcon(int side, int meta) {
		if(indexCheck(meta))
			return getMetaBlock(meta).getIcon(side);
		else 
			return getMetaBlock(0).getIcon(side);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, @SuppressWarnings("rawtypes") List subItems) {
		for (int i = 0; i < blocks.size(); i++)
			subItems.add(new ItemStack(this, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		for (S block : blocks.values())
			block.registerIcons(modName, par1IconRegister);
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
		S called = getMetaBlock(meta);
		WorldLocation loc = new WorldLocation(x, y, z, world);

		if (called instanceof IDifferentDrop)
			return ((IDifferentDrop) called).getDrops(loc, meta, fortune);
		else{
			ArrayList<ItemStack> ret = new ArrayList<ItemStack>(1);
			ret.add(new ItemStack(this, 1, meta));
			return ret;
		}

		
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	@Override
	public int getDamageValue(World par1World, int par2, int par3, int par4) {
		return par1World.getBlockMetadata(par2, par3, par4);
	}
}
