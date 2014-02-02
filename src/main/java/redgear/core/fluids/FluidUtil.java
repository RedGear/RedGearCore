package redgear.core.fluids;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import redgear.core.asm.RedGearCore;
import redgear.core.network.CoreIconRegistry;
import redgear.core.util.StringHelper;

public class FluidUtil {

	public static ItemStack getEmptyContainer(ItemStack filledContainer) {
		if (!FluidContainerRegistry.isFilledContainer(filledContainer))
			return null;

		FluidContainerData[] data = FluidContainerRegistry.getRegisteredFluidContainerData();

		for (FluidContainerData point : data)
			if (point.filledContainer.isItemEqual(filledContainer))
				return point.emptyContainer;

		return null;
	}

	public static int getContainerCapacity(FluidStack fluid, ItemStack container) {
		if (FluidContainerRegistry.isFilledContainer(container))
			return FluidContainerRegistry.getFluidForFilledItem(container).amount;

		if (FluidContainerRegistry.isEmptyContainer(container))
			return FluidContainerRegistry.getFluidForFilledItem(FluidContainerRegistry.fillFluidContainer(fluid,
					container)).amount;

		return 0;
	}

	public static int getContainerCapacity(ItemStack container) {
		return getContainerCapacity(new FluidStack(FluidRegistry.WATER, Integer.MAX_VALUE), container);
	}

	public static Fluid createFluid(String name) {
		return createFluid(name, name);
	}

	public static Fluid createFluid(String fluidName, String iconName) {
		Fluid fluid = new Fluid(fluidName);
		if (!FluidRegistry.registerFluid(fluid))
			fluid = FluidRegistry.getFluid(fluid.getName()); //fluid already exists
		else {
			fluid.setUnlocalizedName(StringHelper.parseUnLocalName(fluidName));
			RedGearCore.proxy.addFluid(StringHelper.parseModName() + ":" + iconName, fluid);
		}
		return fluid;
	}
}
