package redgear.core.block;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SubBlockDifferentDrop extends SubBlock implements IDifferentDrop {

	ItemStack drop;

	public SubBlockDifferentDrop(String name, ItemStack drop) {
		super(name);
		this.drop = drop;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return drop.getItem();
	}

	@Override
	public int getQuantityDropped(int meta, int fortume, Random rand) {
		return drop.stackSize;
	}

	@Override
	public int getMetaDropped(int meta) {
		return drop.getItemDamage();
	}
}
