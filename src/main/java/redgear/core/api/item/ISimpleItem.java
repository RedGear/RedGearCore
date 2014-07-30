package redgear.core.api.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This class is an interface for the SimpleItem class found in Core.
 * The SimpleItem class is similar to ItemStack, but is designed to be simpler,
 * smaller, and has lots of helpful features. 
 * 
 * Note: Setting the meta to OreDictionary.WILDCARD_VALUE works for equals(), but NOT hashCode(). 
 * 
 * In other words, you can't use WILDCARD if you intend to use hashing. Using a List instead of a Set seems to work. 
 * 
 * Also, equals() and hashCode() are NOT guaranteed between different implementations. In fact it's best to assume they are not. 
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
	 * @return true if this stack is in the Ore Dictionary, false if it's not.
	 */
	boolean isInOreDict();

	/**
	 * @return The display name of the stack or "Unknown" if the item doesn't
	 * exist.
	 */
	String getName();

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

	/**
	 * Compares this ISimpleItem with another. 
	 * If omniDirectional is true then additionally it will return true if the other is equal to this. 
	 * 
	 * In other words 
	 * 
	 * return this.equals(other) || other.equals(this)
	 * 
	 * @param other Other ISimpleItem to compare to
	 * @param omniDirect If true other.equals(this) will be considered.
	 * @return this.equals(other) || (omniDirectional && other.equals(this)
	 */
	boolean isItemEqual(ISimpleItem other, boolean omniDirect);

	String getDisplayName();
}