package redgear.core.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubItem {
	/** Icon index in the icons table. */
	protected IIcon itemIcon;
	protected final String name;

	public SubItem(String name) {
		this.name = name;
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	public IIcon getIcon() {
		return itemIcon;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon(modName + name);
	}
}
