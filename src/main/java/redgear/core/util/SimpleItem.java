package redgear.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.world.Location;

/**
 * The SimpleItem class is similar to ItemStack, but is designed to be simpler, smaller, and is designed to be used in Hash-based data structures. 
 * @author Blackhole
 *
 */
public class SimpleItem {
	public final int id;
	public final int meta;
	
	public SimpleItem(int id, int meta){
		this.id = id;
		this.meta = meta;
	}
	
	public SimpleItem(int id){
		this(id, OreDictionary.WILDCARD_VALUE);
	}
	
	public SimpleItem(ItemStack stack){
		this(stack.itemID, stack.getItemDamage());
	}
	
	public SimpleItem(Item item){
		this(new ItemStack(item));
	}
	
	public SimpleItem(Block block){
		this(new ItemStack(block));
	}
	
	public SimpleItem(IBlockAccess world, int x, int y, int z){
		this(world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z));
	}
	
	public SimpleItem(IBlockAccess world, Location loc){
		this(world, loc.x, loc.y, loc.z);
	}
	
	/**
	 * @return True if the Block or Item represented can be found in the Block or Item lists. 
	 */
	public boolean isValid(){
		return (isBlock() && Block.blocksList[id] != null) || (isItem() && Item.itemsList[id] != null);
	}
	
	/**
	 * @return True if the id is within the block ID range.
	 */
	public boolean isBlock(){
		return id > 0 && id < Block.blocksList.length;
	}
	
	/**
	 * @return True if the id is within the item ID range and NOT in the block range.
	 */
	public boolean isItem(){
		return id >= Block.blocksList.length && id < Item.itemsList.length;
	}
	
	/**
	 * @return The Block represented, or null if it can't be found.
	 */
	public Block getBlock(){
		return isBlock() ? Block.blocksList[id] : null;
	}
	
	/**
	 * @return The Item represented, or null if it can't be found. Will return the Item form of a Block.
	 */
	public Item getItem(){
		return isValid() ? Item.itemsList[id] : null;
	}
	
	/**
	 * @return New ItemStack reference with size of 1. Item may or may not exist.
	 */
	public ItemStack getStack(){
		return getStack(1);
	}
	
	/**
	 * @return New ItemStack reference with given size. Item may or may not exist.
	 */
	public ItemStack getStack(int amount){
		return new ItemStack(id, amount, meta);
	}
	
	/**
	 * @return The Ore Dictionary name this stack is registered with, or "Unknown" if it's not. 
	 */
	public String oreName(){
		return OreDictionary.getOreName(OreDictionary.getOreID(getStack()));
	}
	
	/**
	 * @return true if this stack is in the Ore Dictionary, false if it's not. 
	 */
	public boolean isInOreDict(){
		return !oreName().equals("Unknown");
	}
	
	/**
	 * @return The display name of the stack or "Unknown" if the item doesn't exist. 
	 */
	public String getName(){
		return isValid() ? getStack().getDisplayName() : isInOreDict() ? oreName() : "Unknown";
	}
	
	@Override
	public int hashCode(){
		return hash(hash(1, id), meta);
	}
	
	private int hash(int seed, int value){
		return (seed * 31) + value;
	}
	
	/**
	 * Legal data types are SimpleItem, ItemStack, Block, and Item
	 * @return True if the id and meta or oreDict match.
	 */
	@Override
	public boolean equals(Object obj){
		if(obj instanceof SimpleItem)
			return equals((SimpleItem) obj);
		
		if(obj instanceof ItemStack)
			return equals(new SimpleItem((ItemStack) obj));
		
		if(obj instanceof Block)
			return equals(new SimpleItem((Block) obj));
		
		if(obj instanceof Item)
			return equals(new SimpleItem((Item) obj));
		
		return false;
	}
	
	public boolean equals(SimpleItem item){
		return equals(item, true);
	}

	public boolean equals(SimpleItem other, boolean oreDict){
		if(other == null)
			return false;
		
		if(id == other.id && (meta == OreDictionary.WILDCARD_VALUE || other.meta == OreDictionary.WILDCARD_VALUE || meta == other.meta))
			return true;
		
		int oreId = OreDictionary.getOreID(getStack());
		
		if(oreDict && oreId > -1 && oreId == OreDictionary.getOreID(other.getStack()))
			return true;
		
		return false;
	}
	
	public void writeToNBT(NBTTagCompound tag){
		tag.setInteger("id", id);
		tag.setInteger("meta", meta);
    }

    public SimpleItem(NBTTagCompound tag){
        id = tag.getInteger("id");
        meta = tag.getInteger("meta");
    }
    
    public void writeToNBT(NBTTagCompound tag, String name){
    	NBTTagCompound subTag = new NBTTagCompound();
    	writeToNBT(subTag);
    	tag.setTag(name, subTag);
    }
    
    public SimpleItem(NBTTagCompound tag, String name){
    	this(tag.getCompoundTag(name));
    }
}