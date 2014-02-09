package redgear.core.util;

import java.io.Serializable;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.api.item.ISimpleItem;
import redgear.core.world.Location;

/**
 * The SimpleItem class is similar to ItemStack, but is designed to be simpler,
 * smaller, and is safe to be used in Hash-based data structures.
 * 
 * @author Blackhole
 * 
 */
public class SimpleItem implements ISimpleItem, Serializable {
	private static final long serialVersionUID = -8624739696061804763L;
	public final Item item;
	public final int meta;
	public final int stackSize;
	public final int oreID;

	public SimpleItem(ItemStack stack) {
		if (stack == null)
			stack = new ItemStack(Blocks.air);

		item = stack.getItem();
		meta = stack.getItemDamage();
		stackSize = stack.stackSize;
		oreID = OreDictionary.getOreID(stack);

	}

	public SimpleItem(Item item, int meta, int stackSize) {
		this(new ItemStack(item, stackSize, meta));
	}

	public SimpleItem(Item item, int meta) {
		this(new ItemStack(item, 1, meta));
	}

	public SimpleItem(Item item) {
		this(new ItemStack(item));
	}

	public SimpleItem(Block block) {
		this(new ItemStack(block));
	}

	public SimpleItem(Block block, int meta) {
		this(new ItemStack(block, 1, meta));
	}

	public SimpleItem(IBlockAccess world, int x, int y, int z) {
		this(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
	}

	public SimpleItem(IBlockAccess world, Location loc) {
		this(world, loc.chunkPosX, loc.chunkPosY, loc.chunkPosZ);
	}

	public SimpleItem(String oreName) {
		List<ItemStack> ores = OreDictionary.getOres(oreName);
		ItemStack stack = ores.isEmpty() ? new ItemStack(Blocks.air) : ores.get(0);

		item = stack.getItem();
		meta = stack.getItemDamage();
		stackSize = stack.stackSize;
		oreID = OreDictionary.getOreID(stack);

	}

	@Override
	public Item getItem() {
		return item;
	}
	
	@Override
	public Block getBlock(){
		return Block.getBlockFromItem(getItem());
	}

	@Override
	public int getMeta() {
		return meta;
	}

	@Override
	public int getOreID() {
		return oreID;
	}

	@Override
	public int getStackSize() {
		return stackSize;
	}

	@Override
	public ISimpleItem copy() {
		return new SimpleItem(getStack());
	}

	@Override
	public ItemStack getStack() {
		return getStack(stackSize);
	}

	@Override
	public ItemStack getStack(int amount) {
		return new ItemStack(item, amount, meta);
	}

	@Override
	public String oreName() {
		return OreDictionary.getOreName(OreDictionary.getOreID(getStack()));
	}

	@Override
	public boolean isInOreDict() {
		return oreID != -1;
	}

	@Override
	public String getName() {
		return getStack().getDisplayName();
	}

	@Override
	public int getItemId() {
		return Item.getIdFromItem(item);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		return oreID != -1 ? oreID : meta | getItemId() << 16;
	}

	@Override
	public boolean isItemEqual(ISimpleItem other) {
		if (other == null)
			return false;

		if (getItemId() == other.getItemId()
				&& (meta == OreDictionary.WILDCARD_VALUE || other.getMeta() == OreDictionary.WILDCARD_VALUE || meta == other
						.getMeta()))
			return true;

		return oreID == other.getOreID();
	}

	@Override
	public boolean isStackEqual(ISimpleItem other) {
		return isItemEqual(other) && stackSize == other.getStackSize();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ISimpleItem)
			return isItemEqual((ISimpleItem) obj);

		if (obj instanceof ItemStack)
			return isItemEqual(new SimpleItem((ItemStack) obj));

		if (obj instanceof Block)
			return isItemEqual(new SimpleItem((Block) obj));

		if (obj instanceof Item)
			return isItemEqual(new SimpleItem((Item) obj));

		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		getStack().writeToNBT(tag);
	}

	public SimpleItem(NBTTagCompound tag) {
		this(ItemStack.loadItemStackFromNBT(tag));
	}

	@Override
	public void writeToNBT(NBTTagCompound tag, String name) {
		NBTTagCompound subTag = new NBTTagCompound();
		writeToNBT(subTag);
		tag.setTag(name, subTag);
	}

	public SimpleItem(NBTTagCompound tag, String name) {
		this(tag.getCompoundTag(name));
	}
}