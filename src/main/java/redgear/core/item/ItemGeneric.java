package redgear.core.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import redgear.core.util.StringHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This class is great for items that don't do anything special, or a a parent
 * for those that do
 * 
 * @author Blackhole
 * 
 */
public class ItemGeneric extends Item {
	protected String name;
	protected String modName;

	/**
	 * Use this if your item has a typical icon
	 * 
	 * @param Id The typical ItemId
	 * @param name Name of item's icon
	 */
	public ItemGeneric(int Id, String name) {
		super(Id);
		this.name = name;
		GameRegistry.registerItem(this, name);
		modName = StringHelper.parseModAsset();
	}

	/**
	 * Override this function if your item has an unusual icon/multiple icons
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon(modName + name);
	}
}
