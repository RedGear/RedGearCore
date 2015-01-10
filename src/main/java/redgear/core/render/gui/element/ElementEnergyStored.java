package redgear.core.render.gui.element;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import redgear.core.render.ContainerBase;
import redgear.core.render.GuiBase;
import redgear.core.render.RenderHelper;
import cofh.api.energy.IEnergyStorage;

public class ElementEnergyStored<T extends TileEntity, C extends ContainerBase<T>, G extends GuiBase<C>> extends ElementBase<T, C, G> {

	public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("redgear_core:textures/gui/elements/EnergyMeter.png");
	public static final int DEFAULT_SCALE = 42;

	protected IEnergyStorage storage;

	public ElementEnergyStored(G gui, int posX, int posY, IEnergyStorage storage) {
		super(gui, posX, posY);
		this.storage = storage;

		this.texture = DEFAULT_TEXTURE;
		this.sizeX = 8;
		this.sizeY = DEFAULT_SCALE;

		this.texW = 32;
		this.texH = 64;
	}

	@Override
	public void draw() {

		if (!visible) {
			return;
		}
		RenderHelper.bindTexture(texture);
		drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
		int qty = getScaled();
		drawTexturedModalRect(posX, posY + DEFAULT_SCALE - qty, 8, DEFAULT_SCALE - qty, sizeX, qty);
	}

	@Override
	public void addTooltip(List<String> list) {

		if (storage.getMaxEnergyStored() < 0) {
			list.add("Infinite RF");
		} else {
			list.add("" + storage.getEnergyStored() + " / " + storage.getMaxEnergyStored() + " RF");
		}
	}

	int getScaled() {
		if (storage.getMaxEnergyStored() < 0) {
			return sizeY;
		}
		return (int) (storage.getEnergyStored() * sizeY / storage.getMaxEnergyStored());
	}

}
