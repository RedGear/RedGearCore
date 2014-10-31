package redgear.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import redgear.core.api.item.ISimpleItem;

/**
 * @author Blackhole
 *         Created on 10/24/2014.
 */
public class WildcardItem implements ISimpleItem {
    public WildcardItem(ItemStack stack) {
		// TODO Auto-generated constructor stub
	}

	@Override
    public Item getItem() {
        return null;
    }

    @Override
    public Block getBlock() {
        return null;
    }

    @Override
    public int getMeta() {
        return 0;
    }

    @Override
    public ISimpleItem copy() {
        return null;
    }

    @Override
    public ItemStack getStack() {
        return null;
    }

    @Override
    public ItemStack getStack(int amount) {
        return null;
    }

    @Override
    public boolean isInOreDict() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void writeToNBT(NBTTagCompound tag, String name) {

    }

    @Override
    public boolean isItemEqual(ISimpleItem other, boolean omniDirect) {
        return false;
    }

    @Override
    public String getDisplayName() {
        return null;
    }
}
