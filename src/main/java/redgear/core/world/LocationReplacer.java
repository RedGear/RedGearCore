package redgear.core.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import redgear.core.util.SimpleItem;

public class LocationReplacer extends Location {
	
	public final SimpleItem block;
	public final SimpleItem target;

	public LocationReplacer(int x, int y, int z, SimpleItem block, SimpleItem target) {
		super(x, y, z);
		this.block = block;
		this.target = target;
	}
	
	public LocationReplacer(int x, int y, int z, SimpleItem block) {
		this(x, y, z, block, null);
	}
	
	public LocationReplacer(Location loc, SimpleItem block, SimpleItem target){
		this(loc.x, loc.y, loc.z, block);
	}

	public LocationReplacer(Location loc, SimpleItem block){
		this(loc, block, null);
	}
	
	public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        NBTTagCompound blockTag = new NBTTagCompound();
        block.writeToNBT(blockTag);
        tag.setTag("block", blockTag);
        
        NBTTagCompound targetTag = new NBTTagCompound();
        target.writeToNBT(targetTag);
        tag.setTag("target", targetTag);
    }

    public LocationReplacer (NBTTagCompound tag){
        super(tag);
        
        block = new SimpleItem(tag.getCompoundTag("block"));
        target = new SimpleItem(tag.getCompoundTag("target"));
    }
	
	public boolean check(IBlockAccess world, boolean oreDict){
		return super.check(world, block, oreDict);
	}
	
	public boolean check(IBlockAccess world){
		return check(world, block, true);
	}
	
	public boolean check(IBlockAccess world, boolean oreDict, Location relative){
		return copy().translate(relative).check(world, block, true);
	}
	
	public boolean check(IBlockAccess world, Location relative){
		return check(world, block, true, relative);
	}
	
	public boolean placeBlock(World world, boolean useOreDict){
		return placeBlock(world, block, target, useOreDict);
	}
	
	public boolean placeBlock(World world){
		return placeBlock(world, true);
	}
	
	public boolean placeBlock(World world, Location relative){
		return placeBlock(world, true, relative);
	}
	
	public boolean placeBlock(World world, boolean useOreDict, Location relative){
		return placeBlock(world, block, target, useOreDict, relative);
	}
	
	@Deprecated
	/**
	 * private and deprecated because it makes no logical sense in this situation. 
	 */
	private LocationReplacer(TileEntity tile){
		this(tile.xCoord, tile.yCoord, tile.zCoord, null);
	}
}
