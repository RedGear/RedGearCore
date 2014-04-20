package redgear.core.block;

import net.minecraft.tileentity.TileEntity;

public interface IHasTile {

	public TileEntity createTile();
	
	public boolean hasGui();
	
	public int guiId();
}
