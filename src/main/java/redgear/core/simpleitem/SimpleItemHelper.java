package redgear.core.simpleitem;

import net.minecraft.item.ItemStack;
import redgear.core.api.item.ISimpleItem;
import redgear.core.mod.Mods;

public interface SimpleItemHelper {

	public static final SimpleItemHelper helper = new SimpleItemHelperImpl();

	public ItemStack findStack(String mod, String name, int meta);

	public ItemStack findStack(String mod, String name);

	public ISimpleItem parseFromJson(String rawJson);

	public String saveToJson(ISimpleItem item);

	public boolean isInOreDict(String name);

	public ISimpleItem findItem(Mods mod, String name);

	public ISimpleItem findItem(Mods mod, String name, int meta);

	public ISimpleItem findItem(String mod, String name);

	public ISimpleItem findItem(String mod, String name, int meta);

	public ISimpleItem findItem(String name);

	public ISimpleItem wrap(ItemStack stack);

	public ISimpleItem parse(Object obj);
}
