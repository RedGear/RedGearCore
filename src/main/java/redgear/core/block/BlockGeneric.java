package redgear.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import redgear.core.util.StringHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This class is great for blocks that don't do anything special, or a a parent
 * for those that do
 * 
 * @author Blackhole
 * 
 */
public class BlockGeneric extends Block {
	/**
	 * Use this if your item has a typical icon
	 * 
	 * @param Id The typical ItemId
	 * @param name Name of item's icon
	 */
	public BlockGeneric(Material material, String name) {
		this(material, name, ItemBlock.class);
	}
	
	public BlockGeneric(Material material, String name, Class<? extends ItemBlock> item) {
		super(material);
		this.name = name;
		modName = StringHelper.parseModAsset();
		GameRegistry.registerBlock(this, item, name);
	}

	protected final String name;
	protected final String modName;

	/**
	 * Override this function if your item has an unusual icon/multiple icons
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon(modName + name);
	}
}
