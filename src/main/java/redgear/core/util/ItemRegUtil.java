package redgear.core.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.asm.RedGearCore;
import redgear.core.mod.Mods;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegUtil {

	public static ItemStack findStack(Mods mod, String name) {
		return findStack(mod, name, OreDictionary.WILDCARD_VALUE);
	}

	public static ItemStack findStack(Mods mod, String name, int meta) {
		return findStack(mod.getId(), name, meta);
	}

	public static ItemStack findStack(String mod, String name, int meta) {
		ItemStack value = findStack(mod, name);

		if (value != null)
			value.setItemDamage(meta);

		return value;
	}

	public static ItemStack findStack(String mod, String name) {
		if (mod.equalsIgnoreCase("minecraft") || Loader.isModLoaded(mod))
			return GameRegistry.findItemStack(mod, name, 1);
		else {
			RedGearCore.inst.logDebug("ItemRegUtil: Mod: ", mod, " was not found, skipping item: ", name);
			return null;
		}
	}

	public static ItemStack findStack(String name) {
		String mod;
		String item;
		int meta;

		int colon = name.indexOf(":");

		if (colon > 0) {
			mod = name.substring(0, colon);
			name = name.substring(colon + 1);

			colon = name.indexOf("@");

			if (colon > 0) {
				item = name.substring(0, colon);
				meta = Integer.parseInt(name.substring(colon + 1));
			} else {
				item = name;
				meta = OreDictionary.WILDCARD_VALUE;
			}

			return findStack(mod, item, meta);

		} else
			return null;
	}

	public static SimpleItem findItem(Mods mod, String name) {
		return new SimpleItem(findStack(mod, name));
	}

	public static SimpleItem findItem(Mods mod, String name, int meta) {
		return new SimpleItem(findStack(mod, name, meta));
	}

	public static SimpleItem findItem(String mod, String name) {
		return new SimpleItem(findStack(mod, name));
	}

	public static SimpleItem findItem(String mod, String name, int meta) {
		return new SimpleItem(findStack(mod, name, meta));
	}

	public static SimpleItem findItem(String name) {
		return new SimpleItem(findStack(name));
	}
}
