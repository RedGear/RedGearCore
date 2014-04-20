package redgear.core.tile;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ITileFactory{
	
	TileEntity createTile();
	
	@SideOnly(Side.CLIENT)
	Object createGui(InventoryPlayer inventoryPlayer, TileEntity tile);
	
	Object createContainer(InventoryPlayer inventoryPlayer, TileEntity tile);

}
