package redgear.core.tile;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Use this on a tile to tell it's containing block of any outputting redstone signals. 
 * Block must use this in it's isProvidingStrongPower.
 * @author BlackHole
 */
public interface IRedstoneEmiter {
	
	int getRedstoneSignal(ForgeDirection side);

}
