package redgear.core.item;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class SubItemBucket extends SubItem {

	public final Fluid fluid;

	public SubItemBucket(String name, Fluid fluid) {
		super(name);
		this.fluid = fluid;
	}

	public SubItemBucket(String name, int fluidId) {
		this(name, FluidRegistry.getFluid(fluidId));
	}

}
