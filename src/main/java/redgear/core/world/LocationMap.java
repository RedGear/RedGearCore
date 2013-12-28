package redgear.core.world;

import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import redgear.core.util.SimpleItem;

public class LocationMap {

	private HashSet<Location> points = new HashSet<Location>();
	
	public Iterator getIterator(){
		return points.iterator();
	}
	
	public final boolean addLocation(Location addition){
		return points.add(addition);
	}
	
	public boolean check(IBlockAccess world, SimpleItem block, Location other){
		Iterator<Location> bit = getIterator();
		while(bit.hasNext())
			if(!bit.next().check(world, block, other))
				return false;
		return true;
	}
	
	public boolean check(IBlockAccess world, SimpleItem block, int x, int y, int z){
		return check(world, block, new Location(x, y, z));
	}
	
	public boolean check(IBlockAccess world, SimpleItem block){
		return check(world, block, new Location(0, 0, 0));
	}
	
	public LocationMap copy(){
		LocationMap copy = new LocationMap();
		copy.points = (HashSet<Location>) points.clone();
		return copy;
	}
	
	/**
	 * Rotates the map along a given axis
	 * @param direction ForgeDirection about which to rotate
	 * @param degrees Integer of clockwise 90 degree turns IE: 0 nothing, 1 clockwise once, 2 reversed, 3 counter clockwise. 
	 */
	public void rotate(ForgeDirection direction, int degrees){
		Iterator<Location> bit = getIterator();
		while(bit.hasNext())
			bit.next().rotate(direction, degrees);
	}
	
	public void reflect(ForgeDirection direction){
		Iterator<Location> bit = getIterator();
		while(bit.hasNext())
			bit.next().reflect(direction);
	}
	
	public void translate(int x, int y, int z){
		translate(new Location(x, y, z));
	}
	
	public void translate(Location other){
		Iterator<Location> bit = getIterator();
		while(bit.hasNext())
			bit.next().translate(other);
	}
	
	public void translaste(int direction, int amount){
		translate(ForgeDirection.getOrientation(direction), amount);
	}
	
	public void translate(ForgeDirection direction, int amount){
		translate(direction.offsetX * amount, direction.offsetY * amount, direction.offsetZ * amount);
	}
	
	public void merge(LocationMap other){
		Iterator<Location> otherI = other.getIterator();
		while(otherI.hasNext())
			addLocation(otherI.next());
	}
}
