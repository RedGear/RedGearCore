package redgear.core.tile;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRedstoneTile {
	
	/**Use this to tell the tile the OUTSIDE redstone state from other blocks.**/
	void updateRedstone(int state);
	
	/**Use this to tell the block the INSIDE redstone state from the tile.**/
	int redstoneSignal(ForgeDirection side) ;

}
