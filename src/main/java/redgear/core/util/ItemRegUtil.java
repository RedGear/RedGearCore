package redgear.core.util;

import net.minecraft.item.ItemStack;
import redgear.core.mod.Mods;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegUtil {

	public static SimpleItem findItem(Mods mod, String name) {
		return findItem(mod, name, -1);
	}

	public static SimpleItem findItem(Mods mod, String name, int meta) {
		if (mod.isIn()) {
			ItemStack test = GameRegistry.findItemStack(mod.getId(), name, 1);

			if (test != null) {
				if (meta >= 0)
					test.setItemDamage(meta);
				return new SimpleItem(test.getItem());
			}
		}

		return null;
	}
}
