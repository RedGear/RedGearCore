package redgear.core.render.gui.element;

import net.minecraftforge.fluids.IFluidTank;
import redgear.core.render.GuiBase;
import redgear.core.render.RenderHelper;

public class ElementFluidTankWithGlass extends ElementFluidTank{

	public ElementFluidTankWithGlass(GuiBase gui, int posX, int posY, IFluidTank tank) {
		super(gui, posX, posY, tank);
	}

	@Override
	public void draw() {

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
