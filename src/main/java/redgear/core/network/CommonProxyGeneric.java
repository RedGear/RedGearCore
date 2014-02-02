package redgear.core.network;

import redgear.core.util.CoreFuelHandler;
import redgear.core.util.CoreTradeHandler;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;

public class CommonProxyGeneric {

	public Side getSide() {
		return Side.SERVER;
	}

	public void addIcon(String name) {

	}

	public void addFluid(String iconName, Fluid fluid) {

	}

	public void init() {
		CoreFuelHandler.init();
		CoreTradeHandler.init();
		CoreGuiHandler.init();
	}
}
