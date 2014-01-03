package redgear.core.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import redgear.core.util.SimpleItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MetaItem extends ItemGeneric {

	protected final Map<Integer, SubItem> items = new HashMap<Integer, SubItem>();

	public MetaItem(int itemID, String name) {
		super(itemID, name);
		setUnlocalizedName(name);
		hasSubtypes = true;
	}

	public SimpleItem addMetaItem(SubItem newItem) {
		items.put(items.size(), newItem);
		ItemStack temp = new ItemStack(this, 1, items.size() - 1);
		//LanguageRegistry.addName(temp, newItem.displayName);
		return new SimpleItem(temp);
	}

	public boolean indexCheck(int index) {
		return items.size() > index && items.get(index) != null;
	}

	public SubItem getMetaItem(int meta) {
		return items.get(meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Gets an icon index based on an item's damage value
	 */
	public Icon getIconFromDamage(int index) {
		if (indexCheck(index))
			return items.get(index).getIcon();
		else
			return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int unknown, CreativeTabs tab, List subItems) {
		for (int i = 0; i < items.size(); i++)
			subItems.add(new ItemStack(this, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + "." + items.get(itemstack.getItemDamage()).name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		for (SubItem item : items.values())
			item.registerIcons(modName, par1IconRegister);
	}
}
