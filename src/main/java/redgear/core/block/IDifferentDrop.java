package redgear.core.block;

import java.util.Random;

import net.minecraft.item.Item;

public interface IDifferentDrop {

	public Item getItemDropped(int meta, Random rand, int fortune);
	
	public int getQuantityDropped(int meta, int fortume, Random rand);
	
	public int getMetaDropped(int meta);
}
