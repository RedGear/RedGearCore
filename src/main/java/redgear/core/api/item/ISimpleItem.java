package redgear.core.api.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This class is an interface for the SimpleItem class found in Core.
 * The SimpleItem class is similar to ItemStack, but is designed to be simpler,
 * smaller, and is safe to be used in Hash-based data structures.
 * 
 * Note: Setting the meta to OreDictionary.WILDCARD_VALUE works for equals(), but NOT hashCode(). 
 * 
 * In other words, you can't use WILDCARD if you intend to use hashing. Using a List instead of a Set seems to work. 
 * 
 * 
 * @author BlackHole
 */
public interface ISimpleItem {

	/**
	 * @return The Item
	 */
	Item getItem();
	
	/**
	 * @return The Block form of this item if it exists or air if it doesn't. 
	 */
	Block getBlock();

	/**
	 * @return The Meta value
	 */
	int getMeta();

	/**
	 * @return A new SimpleItem with the same Item and meta as this.
	 */
	ISimpleItem copy();

	/**
	 * @return New ItemStack reference with size of 1.
	 */
	ItemStack getStack();

	/**
	 * @return New ItemStack reference with given size.
	 */
	ItemStack getStack(int amount);

	/**
	 * @return The Ore Dictionary name this stack is registered with, or
	 * "Unknown" if it's not.
	 */
	String oreName();

	/**
	 * @return true if this stack is in the Ore Dictionary, false if it's not.
	 */
	boolean isInOreDict();

	/**
	 * @return The display name of the stack or "Unknown" if the item doesn't
	 * exist.
	 */
	String getName();

	/**
	 * SimpleItem's id and meta are immutable, which ensure that their hashcodes
	 * will never change,
	 * and this makes it safe to use SimpleItems inside Hash-based data
	 * structures, like HashMaps or HashSets.
	 * 
	 * Note: This does NOT work with wild card metas. 
	 * 
	 * <pre>
	 * {@code
	 * new SimpleItem(Blocks.wool, OreDictionary.WILDCARD_VALUE).hashCode() != new SimpleItem(Blocks.wool, 14);
	 * }
	 * </pre>
	 *
	 * 
	 * @return Unique HashCode created by the combination of the id and meta.
	 */
	@Override
	int hashCode();

	/**
	 * @param obj Legal data types are ISimpleItem, ItemStack, Block, and Item
	 * @return True if the id and meta or oreDict match. Metas also count as
	 * matching if one is a wild card.
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * Saves id and meta to root of tag
	 * 
	 * @param tag NBTTagCompound to add the ints 'id' and 'meta' to.
	 */
	void writeToNBT(NBTTagCompound tag);

	/**
	 * Saves id and meta to a subtag with name to tag.
	 * 
	 * @param tag Root tag to add the new subtag too.
	 * @param name Name of the subtag to add to root that will hold the ints
	 * 'id' and 'meta'
	 */
	void writeToNBT(NBTTagCompound tag, String name);

	int getOreID();

	int getItemId();

	boolean isItemEqual(ISimpleItem other);

	String getDisplayName();

	

}