package redgear.core.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import redgear.core.util.SimpleItem;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MetaBlock extends BlockGeneric {

	protected BiMap<Integer, SubBlock> blocks = HashBiMap.create();
	protected int counter = 0;

	public MetaBlock(Material material, String name) {
		super(material, name, MetaItemBlock.class);
	}

	public SimpleItem addMetaBlock(SubBlock newBlock) throws IndexOutOfBoundsException {
		if (counter > 15 && !(this instanceof ITileEntityProvider))
			throw new IndexOutOfBoundsException(
					"MetaBlocks can only have 16 values! (0-15) You can't register 17! Use a MetaTile OR use another MetaBlocks.");

		blocks.put(counter, newBlock);
		return new SimpleItem(this, counter++);
	}

	protected boolean indexCheck(int index) {
		return blocks.size() > index && blocks.get(index) != null;
	}

	public SubBlock getMetaBlock(int meta) {
		return blocks.get(meta);
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
		for (int i = 0; i < counter; i++)
			subItems.add(new ItemStack(this, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		for (SubBlock block : blocks.values())
			block.registerIcons(modName, par1IconRegister);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(int meta, int fortume, Random rand) {
		SubBlock called = getMetaBlock(meta);

		if (called instanceof IDifferentDrop)
			return ((IDifferentDrop) called).getQuantityDropped(meta, fortume, rand);
		else
			return 1;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		SubBlock called = getMetaBlock(meta);

		if (called instanceof IDifferentDrop)
			return ((IDifferentDrop) called).getItemDropped(meta, rand, fortune);
		else
			return super.getItemDropped(meta, rand, fortune);
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	@Override
	public int damageDropped(int meta) {
		SubBlock called = getMetaBlock(meta);

		if (called instanceof IDifferentDrop)
			return ((IDifferentDrop) called).getMetaDropped(meta);
		else
			return meta;
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	@Override
	public int getDamageValue(World par1World, int par2, int par3, int par4) {
		return par1World.getBlockMetadata(par2, par3, par4);
	}
}
