package redgear.core.item;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class SubItemBucket extends SubItem {

	protected final Fluid fluid;
	
	public SubItemBucket(String name, String displayName, Fluid fluid) {
		super(name, displayName);
		this.fluid = fluid;
	}
	
	public SubItemBucket(String name, String displayName, int fluidId){
		this(name, displayName, FluidRegistry.getFluid(fluidId));
	}

}
