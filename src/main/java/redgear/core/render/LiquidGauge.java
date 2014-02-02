package redgear.core.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class LiquidGauge extends GuiRegion {

	public int liquidID = 0;
	public int liquidAmount = 0;
	public final int tankCapacity;
	public final int tankId;

	public LiquidGauge(int tankCapacity, int x, int y, int width, int height, int tankId) {
		super(x, y, width, height);

		this.tankCapacity = tankCapacity;
		this.tankId = tankId;
	}

	@Override
	public int getY1() {
		return getY() + getHeight() - (int) (getHeight() * ((double) liquidAmount / (double) tankCapacity));
	}

	@Override
	public void draw(GuiGeneric gui) {
		if (liquidAmount == 0)
			return;

		Fluid fluid = FluidRegistry.getFluid(liquidID);
		if (fluid == null)
			return;
		Icon liquidIcon = fluid.getIcon();

		if (liquidIcon == null && fluid.getBlockID() > 0) //frustrating
			liquidIcon = Block.blocksList[fluid.getBlockID()].getBlockTextureFromSide(0);

		if (liquidIcon == null)
			return;

		gui.drawRectangleIcon(getX1(), getY1(), getX2(), getY2(), liquidIcon, TextureMap.locationBlocksTexture);

	}

}
