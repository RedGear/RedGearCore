package redgear.core.util;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import redgear.core.api.item.ISimpleItem;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CoreFuelHandler implements IFuelHandler {

	private static List<Pair<ISimpleItem, Integer>> items = new LinkedList<Pair<ISimpleItem, Integer>>();

	static {
		GameRegistry.registerFuelHandler(new CoreFuelHandler());
	}

	private CoreFuelHandler() {
	}

	public static void addFuel(ISimpleItem fuel, int burnTime) {
		items.add(Pair.of(fuel, burnTime));
	}

	public static void addFuel(ItemStack fuel, int burnTime) {
		addFuel(new SimpleItem(fuel), burnTime);
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		SimpleItem value = new SimpleItem(fuel);

		for (Pair<ISimpleItem, Integer> pair : items)
			if (pair.getKey().equals(value))
				return pair.getValue().intValue();
		return 0;
	}
}
