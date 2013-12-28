package redgear.core.block;

import net.minecraft.tileentity.TileEntity;

public interface IHasTile {

	public Class <? extends TileEntity > getTile();
	
	public boolean hasGui();
	
	public int guiId();
}
