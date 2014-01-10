package redgear.core.api.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This class is an interface for the SimpleItem class found in Core. 
 * The SimpleItem class is similar to ItemStack, but is designed to be simpler, 
 * smaller, and is safe to be used in Hash-based data structures. 
 * @author BlackHole
 */
public interface ISimpleItem {
	
	/**
	 * @return The Item id
	 */
	public int getId();
	
	/**
	 * @return The Meta value
	 */
	public int getMeta();

	/**
	 * @return True if the Block or Item represented can be found in the Block or Item lists. 
	 */
	public boolean isValid();

	/**
	 * @return True if the id is within the block ID range. Does not guarantee that block exists. Use {@link isValid()} for that. 
	 */
	public boolean isBlock();

	/**
	 * @return True if the id is within the item ID range and NOT in the block range. Does not guarantee that item exists. Use {@link isValid()} for that.  
	 */
	public boolean isItem();

	/**
	 * @return The Block represented, or null if it can't be found.
	 */
	public Block getBlock();

	/**
	 * @return The Item represented, or null if it can't be found. Will return the Item form of a Block.
	 */
	public Item getItem();

	/**
	 * @return New ItemStack reference with size of 1. Item may or may not exist.
	 */
	public ItemStack getStack();

	/**
	 * @return New ItemStack reference with given size. Item may or may not exist.
	 */
	public ItemStack getStack(int amount);

	/**
	 * @return The Ore Dictionary name this stack is registered with, or "Unknown" if it's not. 
	 */
	public String oreName();

	/**
	 * @return true if this stack is in the Ore Dictionary, false if it's not. 
	 */
	public boolean isInOreDict();

	/**
	 * @return The display name of the stack or "Unknown" if the item doesn't exist. 
	 */
	public String getName();

	/**
	 * SimpleItem's id and meta are immutable, which ensure that their hashcodes will never change,
	 * and this makes it safe to use SimpleItems inside Hash-based data structures, like HashMaps or HashSets.
	 * @return Unique HashCode created by the combination of the id and meta. 
	 */
	public int hashCode();

	/**
	 * @param obj Legal data types are ISimpleItem, ItemStack, Block, and Item
	 * @return True if the id and meta or oreDict match. Metas also count as matching if one is a wild card.
	 */
	public boolean equals(Object obj);

	/**
	 * @param obj Legal data types are ISimpleItem, ItemStack, Block, and Item
	 * @param oreDict Should ore dictionary names count towards matching? Omitting this argument results in a default of true.
	 * @return True if the id and meta or oreDict match if oreDict is true. Metas also count as matching if one is a wild card.
	 */
	public boolean equals(Object other, boolean oreDict);

	/**
	 * Saves id and meta to root of tag
	 * @param tag NBTTagCompound to add the ints 'id' and 'meta' to.
	 */
	public void writeToNBT(NBTTagCompound tag);

	/**
	 * Saves id and meta to a subtag with name to tag.
	 * @param tag Root tag to add the new subtag too.
	 * @param name Name of the subtag to add to root that will hold the ints 'id' and 'meta;
	 */
	public void writeToNBT(NBTTagCompound tag, String name);

}