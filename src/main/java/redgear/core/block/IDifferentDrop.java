package redgear.core.block;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import redgear.core.world.WorldLocation;

public interface IDifferentDrop {

	public ArrayList<ItemStack> getDrops(WorldLocation loc, int meta, int fortune);
	
}
