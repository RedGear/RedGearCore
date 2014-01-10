package redgear.core.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.util.SimpleItem;

public class MultiBlockMap {

	private HashMap<Location, Collection<ISimpleItem>> points;
	
	public MultiBlockMap(){
		this(1);
	}
	
	public MultiBlockMap(int size){
		points = new HashMap<Location, Collection<ISimpleItem>>(size);
	}
	
	public void addLocation(Location loc, Collection<ISimpleItem> items){
		points.put(loc, items);
	}
	
	public void addLocation(int x, int y, int z, ItemStack stack){
		addLocation(x, y, z, new SimpleItem(stack));
	}
	
	public void addLocation(int x, int y, int z, ISimpleItem stack){
		HashSet<ISimpleItem>temp = new HashSet<ISimpleItem>(1);
		temp.add(stack);
		addLocation(x, y, z, temp);
	}
	
	public void addLocation(int x, int y, int z, Collection<ISimpleItem> stack){
		addLocation(new Location(x, y, z), stack);
	}
	
	public boolean check(IBlockAccess world, int x, int y, int z){
		return check(world, new Location(x, y, z));
	}
	
	public boolean check(IBlockAccess world, Location other){
		for(Entry<Location, Collection<ISimpleItem>> bit : points.entrySet())
			if(!bit.getKey().check(world, bit.getValue(), other))
				return false; //if one is wrong, don't bother continuing
		return true;
	}
	
	/**
	 * Rotates the map along a given axis
	 * @param direction ForgeDirection about which to rotate
	 * @param degrees Integer of clockwise 90 degree turns IE: 0 nothing, 1 clockwise once, 2 reversed, 3 counter clockwise. 
	 */
	public void rotate(ForgeDirection direction, int degrees){
		for(Entry<Location, Collection<ISimpleItem>> bit : points.entrySet())
			bit.getKey().rotate(direction, degrees);
	}
	
	public void reflect(ForgeDirection direction){
		for(Entry<Location, Collection<ISimpleItem>> bit : points.entrySet())
			bit.getKey().reflect(direction);
	}
	
	public void translate(int x, int y, int z){
		translate(new Location(x, y, z));
	}
	
	public void translate(Location other){
		for(Entry<Location, Collection<ISimpleItem>> bit : points.entrySet())
			bit.getKey().translate(other);
	}
	
	public void translaste(int direction, int amount){
		translate(ForgeDirection.getOrientation(direction), amount);
	}
	
	public void translate(ForgeDirection direction, int amount){
		translate(direction.offsetX * amount, direction.offsetY * amount, direction.offsetZ * amount);
	}	
}