package redgear.core.world;

import java.util.Collection;

import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import redgear.core.util.SimpleItem;

public class Location{
	public int x;
	public int y;
	public int z;
	
	public Location(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Location(Location loc){
		this(loc.x, loc.y, loc.z);
	}
	
	public Location(TileEntity tile){
		this(tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	public Location(MovingObjectPosition pos){
		this(pos.blockX, pos.blockY, pos.blockZ);
	}
	
	public Location copy(){
		return new Location(this);
	}
	
	public boolean check(IBlockAccess world, SimpleItem block, boolean oreDict){
		if(block == null)
			return true;
		
		if(block.id == 0 && isAir(world)) //Special case for air-type blocks.
			return true;
		
		if(block.equals(new SimpleItem(world, this), oreDict))
			return true;
		
		return false;
	}
	
	public boolean check(IBlockAccess world, SimpleItem block){
		return check(world, block, true);
	}
	
	public boolean check(IBlockAccess world, SimpleItem block, boolean oreDict, Location relative){
		return copy().translate(relative).check(world, block, true);
	}
	
	public boolean check(IBlockAccess world, SimpleItem block, Location relative){
		return check(world, block, true, relative);
	}
	
	public boolean check(IBlockAccess world, Collection<SimpleItem> blocks, boolean oreDict){
		for(SimpleItem block : blocks)
			if(check(world, block, oreDict))
				return true;
		return false;
	}
	
	public boolean check(IBlockAccess world, Collection<SimpleItem> blocks){
		return check(world, blocks, true);
	}
	
	public boolean check(IBlockAccess world, Collection<SimpleItem> blocks, Location relative){
		return check(world, blocks, true, relative);
	}
	
	public boolean check(IBlockAccess world, Collection<SimpleItem> blocks, boolean oreDict, Location relative){
		return copy().translate(relative).check(world, blocks, oreDict);
	}
	
	public boolean check(IBlockAccess world, Class classes, Location relative){
		return copy().translate(relative).check(world,  classes);
	}
	
	public boolean check(IBlockAccess world, Class type){
		return type.isInstance(getTile(world));
	}
	
	public void placeBlock(World world, SimpleItem item){
		if(item != null && item.isValid() && item.isBlock())
			world.setBlock(x, y, z, item.id, item.meta, 2);
	}
	
	public void placeBlock(World world, SimpleItem item, Location relative){
		copy().translate(relative).placeBlock(world, item);
	}
	
	public boolean placeBlock(World world, SimpleItem item, SimpleItem target, boolean oreDict){
		if(check(world, target, oreDict)){
			placeBlock(world, item);
			return true;
		}
		else
			return false;
	}
	
	public boolean placeBlock(World world, SimpleItem item, SimpleItem target){
		return placeBlock(world, item, target, true);
	}
	
	public boolean placeBlock(World world, SimpleItem item, SimpleItem target, boolean oreDict, Location reletive){
		return copy().translate(reletive).placeBlock(world, item, target);
	}
	
	public boolean placeBlock(World world, SimpleItem item, SimpleItem target, Location reletive){
		return placeBlock(world, item, target, true, reletive);
	}
	
	public boolean placeBlock(World world, SimpleItem item, Collection<SimpleItem> targets, boolean oreDict){
		for(SimpleItem it : targets)
			if(placeBlock(world, item, it, oreDict))
				return true;
		return false;
	}
	
	public boolean placeBlock(World world, SimpleItem item, Collection<SimpleItem> targets){
		return placeBlock(world, item, targets, true);
	}
	
	public boolean placeBlock(World world, SimpleItem item, Collection<SimpleItem> targets, Location reletive){
		return placeBlock(world, item, targets, true, reletive);
	}
	
	public boolean placeBlock(World world, SimpleItem item, Collection<SimpleItem> targets, boolean oreDict, Location reletive){
		return copy().translate(reletive).placeBlock(world, item, targets, oreDict);
	}
	
	public int getBlockId(IBlockAccess world){
		return world.getBlockId(x, y, z);
	}
	
	public int getBlockMeta(IBlockAccess world){
		return world.getBlockMetadata(x, y, z);
	}
	
	public TileEntity getTile(IBlockAccess world){
		return world.getBlockTileEntity(x, y, z);
	}
	
	public Material getBlockMaterial(IBlockAccess world){
		return world.getBlockMaterial(x, y, z);
	}
	
	public boolean isAir(IBlockAccess world){
		return world.isAirBlock(x, y, z);
	}
	
	/**
	 * Rotates the map along a given axis
	 * @param direction ForgeDirection about which to rotate
	 * @param degrees Integer of clockwise 90 degree turns IE: 0 nothing, 1 clockwise once, 2 reversed, 3 counter clockwise. 
	 */
	public Location rotate(ForgeDirection direction, int degrees){
		degrees = Math.abs(degrees) % 4;// should ensure that degrees must be 0, 1, 2, or 3 and nothing else
		
		if(degrees == 0)
			return this;
		
		switch(direction){
		case DOWN:
			rotateY((degrees) + 2 % 4);
			break;
		case UP:
			rotateY(degrees);
			break;
		case NORTH:
			rotateZ((degrees + 2) % 4);
			break;
		case SOUTH:
			rotateZ(degrees);
			break;
		case WEST:
			rotateX((degrees + 2) % 4);
			break;
		case EAST:
			rotateX(degrees);
			break;
		case UNKNOWN:
		}
		
		return this;
	}
	
	public Location reflect(ForgeDirection direction){
		switch(direction){
		case DOWN:
		case UP:
			reflectY();
			break;
		case NORTH:
		case SOUTH:
			reflectZ();
			break;
		case WEST:
		case EAST:
			reflectX();
			break;
		case UNKNOWN:
		}
		
		return this;
	}
	
	public Location rotateX(int degrees){
		if(degrees > 0)
			rotateX(--degrees);
		int temp = -z;
		z = y;
		y = temp;
		
		return this;
	}
	
	public Location rotateY(int degrees){
		if(degrees > 0)
			rotateY(--degrees);
		int temp = -x;
		x = z;
		z = temp;
		
		return this;
	}
	
	public Location rotateZ(int degrees){
		if(degrees > 0)
			rotateZ(--degrees);
		int temp = -y;
		y = x;
		x = temp;
		
		return this;
	}
	
	public Location reflectX(){
		x = -x;
		return this;
	}
	
	public Location reflectY(){
		y = -y;
		return this;
	}
	
	public Location reflectZ(){
		z = -z;
		return this;
	}
	
	public Location translate(Location other){
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}
	
	public Location translaste(int direction, int amount){
		return translate(ForgeDirection.getOrientation(direction), amount);
	}
	
	public Location translate(int x, int y, int z){
		return translate(new Location(x, y, z));
	}
	
	public Location translate(ForgeDirection direction, int amount){
		return translate(direction.offsetX * amount, direction.offsetY * amount, direction.offsetZ * amount);
	}
	
	@Override
	public int hashCode(){
		return hash(hash(hash(1, x), y), z);
	}
	
	private int hash(int seed, int value){
		return (seed * 31) + value;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Location)
			return equals((Location) obj);
		else
			return false;
	}
	
	public boolean equals(Location other){
		return x == other.x && y == other.y && z == other.z;
	}
	
    public void writeToNBT(NBTTagCompound tag){
        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);
    }

    public Location (NBTTagCompound tag){
        this(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    }
}