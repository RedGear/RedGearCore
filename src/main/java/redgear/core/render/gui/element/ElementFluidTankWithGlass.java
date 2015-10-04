package redgear.core.render.gui.element;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import redgear.core.asm.RedGearCore;
import redgear.core.render.ContainerBase;
import redgear.core.render.GuiBase;
import redgear.core.render.RenderHelper;

public class ElementFluidTankWithGlass<T extends TileEntity, C extends ContainerBase<T>, G extends GuiBase<C>> extends ElementFluidTank<T, C, G>{

	public ElementFluidTankWithGlass(G gui, int posX, int posY, IFluidTank tank) {
		super(gui, posX, posY, tank);
	}

	@Override
	public void draw() {

//		FluidStack fluid = tank.getFluid();

//		RedGearCore.inst.logDebug("Drawing tank filled with: " + (fluid == null ? "null" : fluid.getLocalizedName()));

		if (!visible) {
			return;
		}
		
		int amount = getScaled();
		
		RenderHelper.bindTexture(texture);
		drawTexturedModalRect(posX, posY, 0, 1, sizeX, sizeY);
		gui.drawFluid(posX, posY + sizeY - amount, tank.getFluid(), sizeX, amount);
		RenderHelper.bindTexture(texture);
		drawTexturedModalRect(posX, posY, 17, 1, sizeX, sizeY);
		drawTexturedModalRect(posX, posY, 33 + gaugeType * 16, 1, sizeX - 1, sizeY);
	}
}
