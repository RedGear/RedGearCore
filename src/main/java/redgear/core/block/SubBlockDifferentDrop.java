package redgear.core.block;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import redgear.core.world.WorldLocation;

public class SubBlockDifferentDrop extends SubBlock implements IDifferentDrop {

	ItemStack drop;

	public SubBlockDifferentDrop(String name, ItemStack drop) {
		super(name);
		this.drop = drop;
	}

	@Override
	public ArrayList<ItemStack> getDrops(WorldLocation loc, int meta, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>(1);
		ret.add(drop.copy());
		return ret;
	}
}
