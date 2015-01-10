package redgear.core.render.gui.element;

import net.minecraft.tileentity.TileEntity;
import redgear.core.render.ContainerBase;
import redgear.core.render.GuiBase;
import redgear.core.tile.Machine;

public class ElementWorkScaled<T extends TileEntity & Machine, C extends ContainerBase<T>, G extends GuiBase<C>> extends ElementDualScaled<T, C, G>{

	public ElementWorkScaled(G gui, int posX, int posY) {
		super(gui, posX, posY);
	}
	
	
	
	@Override
	public void draw() {
		this.setQuantity(getScaledWork());
		super.draw();
	}
	
	private int getScaledWork() {
		T tile = getTile();
		
		if (tile.getWork() == 0 || tile.getWorkTotal() == 0)
			return 0;
		else
			return (int) ((float) tile.getWork() / (float) tile.getWorkTotal() * texH);
	}

}
