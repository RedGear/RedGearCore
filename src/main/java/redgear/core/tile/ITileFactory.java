package redgear.core.tile;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import redgear.core.machine.IMachine;
import redgear.core.render.ContainerBase;
import redgear.core.render.GuiBase;

public interface ITileFactory<T extends TileEntity, C extends ContainerBase<T>, G extends GuiBase<C>> {
	
	T createTile();
	
	IMachine createMachine();
	
	G createGui(InventoryPlayer inventoryPlayer, TileEntity tile);
	
	C createContainer(InventoryPlayer inventoryPlayer, TileEntity tile);

}
