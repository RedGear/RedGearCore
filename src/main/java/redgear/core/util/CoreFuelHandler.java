package redgear.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CoreFuelHandler implements IFuelHandler {

	private static Map<SimpleItem, Integer> items = new HashMap<SimpleItem, Integer>();

	static {
		GameRegistry.registerFuelHandler(new CoreFuelHandler());
	}

	private CoreFuelHandler() {
	}

	public static void addFuel(SimpleItem fuel, int burnTime) {
		items.put(fuel, burnTime);
	}

	public static void addFuel(ItemStack fuel, int burnTime) {
		addFuel(new SimpleItem(fuel), burnTime);
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		SimpleItem value = new SimpleItem(fuel);

		for (Entry<SimpleItem, Integer> pair : items.entrySet())
			if (pair.getKey().equals(value))
				return pair.getValue().intValue();
		return 0;
	}
}
