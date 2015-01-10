package redgear.core.render.gui.element;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fluids.IFluidTank;
import redgear.core.render.ContainerBase;
import redgear.core.render.GuiBase;
import redgear.core.render.RenderHelper;

import java.util.List;

public class ElementFluidTank<T extends TileEntity, C extends ContainerBase<T>, G extends GuiBase<C>> extends ElementBase<T, C, G> {

	public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("redgear_core:textures/gui/elements/FluidTank.png");
	public static final int DEFAULT_SCALE = 60;

	protected IFluidTank tank;
	protected int gaugeType;

	public ElementFluidTank(G gui, int posX, int posY, IFluidTank tank) {
		super(gui, posX, posY);
		this.tank = tank;

		this.texture = DEFAULT_TEXTURE;
		this.texW = 64;
		this.texH = 64;

		this.sizeX = 16;
		this.sizeY = DEFAULT_SCALE;
	}

	public ElementFluidTank(G gui, int posX, int posY, IFluidTank tank, String texture) {
		super(gui, posX, posY);
		this.tank = tank;

		this.texture = new ResourceLocation(texture);
		this.texW = 64;
		this.texH = 64;

		this.sizeX = 16;
		this.sizeY = DEFAULT_SCALE;
	}

	public ElementFluidTank<T, C, G> setGauge(int gaugeType) {
		this.gaugeType = gaugeType;
		return this;
	}

	@Override
	public void draw() {

		if (!visible) {
			return;
		}
		int amount = getScaled();

		gui.drawFluid(posX, posY + sizeY - amount, tank.getFluid(), sizeX, amount);
		RenderHelper.bindTexture(texture);
		drawTexturedModalRect(posX, posY, 32 + gaugeType * 16, 1, sizeX, sizeY);
	}

	@Override
	public void addTooltip(List<String> list) {
		if (tank.getFluid() != null && tank.getFluidAmount() > 0) {
            if(ForgeVersion.getBuildVersion() >= 1162)
			    list.add(tank.getFluid().getFluid().getLocalizedName(tank.getFluid()));
            else
                list.add(tank.getFluid().getFluid().getLocalizedName());
		}
		list.add("" + tank.getFluidAmount() + " / " + tank.getCapacity() + " mB");
	}

	@Override
	public boolean handleMouseClicked(int x, int y, int mouseButton) {

		return false;
	}

	int getScaled() {

		return tank.getFluidAmount() * sizeY / tank.getCapacity();
	}

}
