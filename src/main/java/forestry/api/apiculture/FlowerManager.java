package forestry.api.apiculture;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import forestry.api.genetics.IFlowerProvider;

public class FlowerManager {
	/**
	 * ItemStacks representing simple flower blocks. Meta-sensitive, processed by the basic {@link IFlowerProvider}.
	 */
	public static ArrayList<ItemStack> plainFlowers = new ArrayList<ItemStack>();
}
