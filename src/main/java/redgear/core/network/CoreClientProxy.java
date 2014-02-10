package redgear.core.network;

import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;

public class CoreClientProxy extends CoreCommonProxy {
	
	CoreIconRegistry icoReg;

	@Override
	public Side getSide() {
		return Side.CLIENT;
	}

	@Override
	public void addIcon(String name) {
		icoReg.addIcon(name);
	}
	
	public IIcon getIcon(String name) {
		return icoReg.getIcon(name);
	}

	@Override
	public void addFluid(String iconName, Fluid fluid) {
		icoReg.addFluid(iconName, fluid);
	}

	@Override
	public void init() {
		super.init();
		icoReg = CoreIconRegistry.init();
	}
}
