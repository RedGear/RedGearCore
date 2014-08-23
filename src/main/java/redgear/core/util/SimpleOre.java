package redgear.core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.api.item.ISimpleItem;
import redgear.core.world.BlockLocation;
import redgear.core.world.WorldLocation;

public class SimpleOre implements ISimpleItem, Serializable {
	private static final long serialVersionUID = -7330362966957142907L;

	public final String oreName;
	private final int oreId;
	private ISimpleItem target = null;

	public SimpleOre(String oreName, ISimpleItem target) {
		this.oreName = oreName;
		oreId = OreDictionary.getOreID(oreName);
		setTarget(target);
	}

	public SimpleOre(String oreName) {
		this(oreName, (ISimpleItem) null);
	}

	public SimpleOre(String oreName, ItemStack target) {
		this(oreName, target == null ? null : new SimpleItem(target));
	}

	public SimpleOre(String oreName, Item target) {
		this(oreName, target, 0);
	}

	public SimpleOre(String oreName, Item target, int targetMeta) {
		this(oreName, target == null ? null : new SimpleItem(target, targetMeta));
	}
	
	public SimpleOre(NBTTagCompound tag) {
		this(tag.getString("oreName"));
	}
	
	public SimpleOre(NBTTagCompound tag, String name) {
		this(tag.getCompoundTag(name));
	}

	public SimpleOre setTarget(ISimpleItem target) {
		if (target != null && !(target instanceof SimpleOre))
			for (int i : OreDictionary.getOreIDs(target.getStack()))
				if (i == oreId) {
					this.target = target;
					return this;
				}
		return this;
	}

	public ISimpleItem getTarget() {
		if (target != null)
			return target;
		else {
			Iterator<ItemStack> i = OreDictionary.getOres(oreName).iterator();
			if (i.hasNext()) {
				setTarget(new SimpleItem(i.next()));
				return target;
			} else
				return null;
		}
	}
	
	public List<ISimpleItem> getSimpleItems(){
		List<ItemStack> stacks = getStacks();
		List<ISimpleItem> items = new ArrayList<ISimpleItem>(stacks.size() > 0 ? stacks.size() : 0);
		
		for(ItemStack stack : stacks)
			items.add(new SimpleItem(stack));
		
		return items;
	}
	
	public List<ItemStack> getStacks(){
		return OreDictionary.getOres(oreName);
	}

	@Override
	public Item getItem() {
		ISimpleItem stack = getTarget();
		return stack == null ? null : stack.getItem();
	}

	@Override
	public Block getBlock() {
		Item item = getItem();

		return item == null ? null : Block.getBlockFromItem(item);
	}

	@Override
	public int getMeta() {
		ISimpleItem stack = getTarget();

		return stack == null ? null : stack.getMeta();
	}

	@Override
	public ISimpleItem copy() {
		return new SimpleOre(oreName, target);
	}

	@Override
	public ItemStack getStack() {
		return getStack(1);
	}

	@Override
	public ItemStack getStack(int amount) {
		ISimpleItem item = getTarget();
		return item == null ? null : item.getStack(amount);
	}

	public String getOreName() {
		return oreName;
	}

	@Override
	public boolean isInOreDict() {
		return true;
	}

	@Override
	public String getName() {
		return getOreName();
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setString("oreName", oreName);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag, String name) {
		NBTTagCompound subTag = new NBTTagCompound();
		writeToNBT(subTag);
		tag.setTag(name, subTag);
	}

	public int getOreID() {
		return oreId;
	}

	@Override
	public boolean isItemEqual(ISimpleItem other, boolean omniDirect) {
		if (other.isInOreDict())
			for (int i : OreDictionary.getOreIDs(other.getStack()))
				if (i == oreId)
					return true;

		if (omniDirect)
			return other.isItemEqual(this, false);
		else
			return false;
	}

	@Override
	public String getDisplayName() {
		ISimpleItem item = getTarget();
		return item == null ? "UNKNOWN" : item.getDisplayName();
	}
	
	@Override
	public int hashCode(){
		return oreId;
	}
	
	@Override
	public boolean equals(Object obj){
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

}
