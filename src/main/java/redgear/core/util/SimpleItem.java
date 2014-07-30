package redgear.core.util;

import java.io.Serializable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.api.item.ISimpleItem;
import redgear.core.api.util.HashHelper;
import redgear.core.world.BlockLocation;
import redgear.core.world.Location;
import redgear.core.world.WorldLocation;

/**
 * The SimpleItem class is similar to ItemStack, but is designed to be simpler,
 * smaller, and is safe to be used in Hash-based data structures.
 *
 * Note: Setting the meta to OreDictionary.WILDCARD_VALUE works for equals(),
 * but NOT hashCode().
 *
 * In other words, you can't use WILDCARD if you intend to use hashing. Using a
 * List instead of a Set seems to work.
 *
 * @author Blackhole
 *
 */
public class SimpleItem implements ISimpleItem, Serializable {
	private static final long serialVersionUID = -8624739696061804763L;
	public final Item item;
	public final int meta;

	public SimpleItem(ItemStack stack) {
		if (stack == null)
			stack = new ItemStack(Blocks.air);

		item = stack.getItem();
		if (item == null)
			meta = 0;
		else
			meta = stack.getItemDamage();

	}

	public SimpleItem(Item item, int meta) {
		this(new ItemStack(item, 1, meta));
	}

	public SimpleItem(Item item) {
		this(new ItemStack(item, 1, 0));
	}

	public SimpleItem(Block block) {
		this(new ItemStack(block, 1, 0));
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

	public SimpleItem(ChunkPosition loc, IBlockAccess world) {
		this(new Location(loc), world);
	}

	public SimpleItem(Location loc, IBlockAccess world) {
		this(loc.getBlock(world), loc.getBlockMeta(world));
	}

	public SimpleItem(WorldLocation loc) {
		this(loc.getBlock(), loc.getBlockMeta());
	}

	@Override
	public Item getItem() {
		return item;
	}

	@Override
	public Block getBlock() {
		return Block.getBlockFromItem(getItem());
	}

	@Override
	public int getMeta() {
		return meta;
	}

	@Override
	public ISimpleItem copy() {
		return new SimpleItem(getStack());
	}

	@Override
	public ItemStack getStack() {
		return getStack(1);
	}

	@Override
	public ItemStack getStack(int amount) {
		return new ItemStack(item, amount, meta);
	}

	@Override
	public boolean isInOreDict() {
		return OreDictionary.getOreIDs(getStack()).length > 0;
	}

	@Override
	public String getName() {
		return Item.itemRegistry.getNameForObject(getItem());
	}

	@Override
	public String getDisplayName() {
		return getStack().getDisplayName();
	}

	@Override
	public String toString() {
		return getDisplayName();
	}

	public int getItemId() {
		return Item.getIdFromItem(getItem());
	}

	@Override
	public int hashCode() {
		return HashHelper.hash(getItemId(), meta);
	}

	@Override
	public boolean isItemEqual(ISimpleItem other, boolean omniDirect) {
		if (other == null)
			return false;

		if (getItem() == other.getItem()
				&& (getMeta() == OreDictionary.WILDCARD_VALUE || other.getMeta() == OreDictionary.WILDCARD_VALUE || getMeta() == other
						.getMeta()))
			return true;
		else if (omniDirect)
				return other.isItemEqual(this, false);
		else
			return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ISimpleItem)
			return isItemEqual((ISimpleItem) obj, false);

		if (obj instanceof ItemStack)
			return isItemEqual(new SimpleItem((ItemStack) obj), false);

		if (obj instanceof Block)
			return isItemEqual(new SimpleItem((Block) obj), false);

		if (obj instanceof Item)
			return isItemEqual(new SimpleItem((Item) obj), false);

		if (obj instanceof WorldLocation)
			return isItemEqual(new SimpleItem((WorldLocation) obj), false);

		if (obj instanceof BlockLocation)
			return isItemEqual(((BlockLocation) obj).block, false);

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