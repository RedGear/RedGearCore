package redgear.core.util;

import net.minecraft.block.Block;
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
 * @author Blackhole
 *
 */
public class SimpleItem implements ISimpleItem {
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
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#isValid()
	 */
	@Override
	public boolean isValid(){
		return (isBlock() && Block.blocksList[id] != null) || (isItem() && Item.itemsList[id] != null);
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#isBlock()
	 */
	@Override
	public boolean isBlock(){
		return id > 0 && id < Block.blocksList.length;
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#isItem()
	 */
	@Override
	public boolean isItem(){
		return id >= Block.blocksList.length && id < Item.itemsList.length;
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#getBlock()
	 */
	@Override
	public Block getBlock(){
		return isBlock() ? Block.blocksList[id] : null;
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#getItem()
	 */
	@Override
	public Item getItem(){
		return isValid() ? Item.itemsList[id] : null;
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#getStack()
	 */
	@Override
	public ItemStack getStack(){
		return getStack(1);
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#getStack(int)
	 */
	@Override
	public ItemStack getStack(int amount){
		return new ItemStack(id, amount, meta);
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#oreName()
	 */
	@Override
	public String oreName(){
		return OreDictionary.getOreName(OreDictionary.getOreID(getStack()));
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#isInOreDict()
	 */
	@Override
	public boolean isInOreDict(){
		return !oreName().equals("Unknown");
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#getName()
	 */
	@Override
	public String getName(){
		return isValid() ? getStack().getDisplayName() : isInOreDict() ? oreName() : "Unknown";
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#hashCode()
	 */
	
	@Override
	public int hashCode(){
		return hash(hash(1, id), meta);
	}
	
	private int hash(int seed, int value){
		return (seed * 31) + value;
	}
	
	
	
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
		
		int oreId = OreDictionary.getOreID(getStack());
		
		if(oreDict && oreId > -1 && oreId == OreDictionary.getOreID(other.getStack()))
			return true;
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag){
		tag.setInteger("id", id);
		tag.setInteger("meta", meta);
    }

    public SimpleItem(NBTTagCompound tag){
        id = tag.getInteger("id");
        meta = tag.getInteger("meta");
    }
    
    /* (non-Javadoc)
	 * @see redgear.core.util.ISimpleItem#writeToNBT(net.minecraft.nbt.NBTTagCompound, java.lang.String)
	 */
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