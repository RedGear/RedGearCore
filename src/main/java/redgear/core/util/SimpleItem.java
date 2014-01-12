package redgear.core.util;

import java.io.Serializable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.api.item.ISimpleItem;
import redgear.core.api.util.HashHelper;
import redgear.core.world.Location;

/**
 * The SimpleItem class is similar to ItemStack, but is designed to be simpler, 
 * smaller, and is safe to be used in Hash-based data structures. 
 * @author Blackhole
 *
 */
public class SimpleItem implements ISimpleItem, Serializable {
	private static final long serialVersionUID = -7418985714979010403L;
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
	
	@Override
	public int getId(){
		return id;
	}
	
	@Override
	public int getMeta(){
		return meta;
	}
	
	@Override
	public ISimpleItem copy(){
		return new SimpleItem(this.id, this.meta);
	}
	
	@Override
	public boolean isValid(){
		return (isBlock() && Block.blocksList[id] != null) || (isItem() && Item.itemsList[id] != null);
	}
	
	@Override
	public boolean isBlock(){
		return id > 0 && id < Block.blocksList.length;
	}
	
	@Override
	public boolean isItem(){
		return id >= Block.blocksList.length && id < Item.itemsList.length;
	}
	
	@Override
	public Block getBlock(){
		return isBlock() ? Block.blocksList[id] : null;
	}
	
	@Override
	public Item getItem(){
		return isValid() ? Item.itemsList[id] : null;
	}
	
	@Override
	public ItemStack getStack(){
		return getStack(1);
	}
	
	@Override
	public ItemStack getStack(int amount){
		return new ItemStack(id, amount, meta);
	}
	
	@Override
	public String oreName(){
		return OreDictionary.getOreName(OreDictionary.getOreID(getStack()));
	}
	
	@Override
	public boolean isInOreDict(){
		return !oreName().equals("Unknown");
	}
	
	@Override
	public String getName(){
		return isValid() ? getStack().getDisplayName() : isInOreDict() ? oreName() : "Unknown";
	}
	
	@Override
	public String toString(){
		return getName();
	}
	
	@Override
	public int hashCode(){
		return HashHelper.hash(id, meta);
	}
	
	/*private int hash(int seed, int value){
		return (seed * 31) + value;
	}*/
	
	
	
	@Override
	public boolean equals(Object obj){
		return equals(obj, true);
	}
	
	@Override
	public boolean equals(Object obj, boolean oreDict){
		if(obj instanceof ISimpleItem)
			return equals((ISimpleItem) obj, oreDict);
		
		if(obj instanceof ItemStack)
			return equals(new SimpleItem((ItemStack) obj), oreDict);
		
		if(obj instanceof Block)
			return equals(new SimpleItem((Block) obj), oreDict);
		
		if(obj instanceof Item)
			return equals(new SimpleItem((Item) obj), oreDict);
		
		return false;
	}

	public boolean equals(ISimpleItem other, boolean oreDict){
		if(other == null)
			return false;
		
		if(id == other.getId() && (meta == OreDictionary.WILDCARD_VALUE || other.getMeta() == OreDictionary.WILDCARD_VALUE || meta == other.getMeta()))
			return true;
		
		if(oreDict){
			int oreId = OreDictionary.getOreID(getStack());
			
			if(oreId > -1 && oreId == OreDictionary.getOreID(other.getStack()))
				return true;
		}
		
		return false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag){
		tag.setInteger("id", id);
		tag.setInteger("meta", meta);
    }

    public SimpleItem(NBTTagCompound tag){
        id = tag.getInteger("id");
        meta = tag.getInteger("meta");
    }
    
    @Override
	public void writeToNBT(NBTTagCompound tag, String name){
    	NBTTagCompound subTag = new NBTTagCompound();
    	writeToNBT(subTag);
    	tag.setTag(name, subTag);
    }
    
    public SimpleItem(NBTTagCompound tag, String name){
    	this(tag.getCompoundTag(name));
    }
}