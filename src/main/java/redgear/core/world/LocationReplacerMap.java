package redgear.core.world;

import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LocationReplacerMap extends LocationMap {
	
	private HashSet<LocationReplacer> points = new HashSet<LocationReplacer>();
	
	public LocationReplacerMap(){}
	
	public Iterator getIterator(){
		return points.iterator();
	}
	
	public boolean addLocation(LocationReplacer addition){
		return super.addLocation(addition);
	}
	
	public boolean check(IBlockAccess world, Location other){
		for(LocationReplacer bit : points)
			if(!bit.check(world, true, other))
				return false;
		return true;
	}
	
	public boolean check(IBlockAccess world, int x, int y, int z){
		return check(world, new Location(x, y, z));
	}
	
	public boolean check(IBlockAccess world){
		return check(world,  new Location(0, 0, 0));
	}
	
	public LocationReplacerMap copy(){
		LocationReplacerMap copy = new LocationReplacerMap();
		copy.points = (HashSet<LocationReplacer>) points.clone();
		return copy;
	}
	
	public void placeBlock(World world, boolean useOreDict){
		for(LocationReplacer bit : points)
			bit.placeBlock(world, useOreDict);
	}
	
	public void placeBlock(World world){
		placeBlock(world, true);
	}
	
	public void placeBlock(World world, Location relative){
		placeBlock(world, true, relative);
	}
	
	public void placeBlock(World world, boolean useOreDict, Location relative){
		for(LocationReplacer bit : points)
			bit.placeBlock(world, useOreDict, relative);
	}
	
	public void writeToNBT(NBTTagCompound tag){
		tag.setInteger("count", points.size());
    	int i = 0;
    	for(LocationReplacer bit : points){
    		NBTTagCompound subTag = new NBTTagCompound();
    		bit.writeToNBT(subTag);
    		tag.setCompoundTag("loc" + i++, subTag);
    	}
    }

    public LocationReplacerMap (NBTTagCompound tag){
    	int count = tag.getInteger("count");
        
        for(int i = 0; i < count; i++){
        	NBTTagCompound subTag = tag.getCompoundTag("loc" + i);
        	points.add(new LocationReplacer(subTag));
        }
    }
	
}
