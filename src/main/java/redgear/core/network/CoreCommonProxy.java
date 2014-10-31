package redgear.core.network;

import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.fluids.Fluid;
import redgear.core.util.CoreTradeHandler;

public class CoreCommonProxy {

	public Side getSide() {
		return Side.SERVER;
	}

	public void addIcon(String name) {

	}

	public void addFluid(String iconName, Fluid fluid) {

	}

    public void init(){
        CoreTradeHandler.init();
        CoreGuiHandler.init();
    }
}
