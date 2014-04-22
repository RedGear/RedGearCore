package redgear.core.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import redgear.core.util.SimpleItem;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MetaItem<S extends SubItem> extends ItemGeneric {

	protected final Map<Integer, S> items = new HashMap<Integer, S>();

	public MetaItem(String name) {
		super(name);
		setUnlocalizedName(name);
		hasSubtypes = true;
	}

	public SimpleItem addMetaItem(S newItem) {
		items.put(items.size(), newItem);
		SimpleItem item = new SimpleItem(this, items.size() - 1);
		GameRegistry.registerCustomItemStack(this.name + "." + newItem.name, item.getStack());
		return item;
	}

	public boolean indexCheck(int index) {
		return items.size() > index && items.get(index) != null;
	}

	public S getMetaItem(int meta) {
		return items.get(meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Gets an icon index based on an item's damage value
	 */
	public IIcon getIconFromDamage(int index) {
		if (indexCheck(index))
			return items.get(index).getIcon();
		else
			return null;
	}

	@SuppressWarnings({"unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List subItems) {
		for (int i = 0; i < items.size(); i++)
			subItems.add(new ItemStack(this, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + "." + items.get(itemstack.getItemDamage()).name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		for (S item : items.values())
			item.registerIcons(modName, par1IconRegister);
	}
}
