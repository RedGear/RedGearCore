package redgear.core.world;

import java.io.Serializable;
import java.util.Collection;

import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.api.util.HashHelper;
import redgear.core.api.world.ILocation;
import redgear.core.api.world.IPoint;
import redgear.core.util.SimpleItem;

public class Location implements ILocation, Serializable {
	private static final long serialVersionUID = 2273703778552009616L;
	public final int x;
	public final int y;
	public final int z;

	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location(IPoint point) {
		this(point.getX(), point.getY(), point.getZ());
	}

	public Location(TileEntity tile) {
		this(tile.xCoord, tile.yCoord, tile.zCoord);
	}

	public Location(MovingObjectPosition pos) {
		this(pos.blockX, pos.blockY, pos.blockZ);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public Location copy() {
		return new Location(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#check(net.minecraft.world.IBlockAccess,
	 * redgear.core.util.ISimpleItem, boolean)
	 */
	@Override
	public boolean check(IBlockAccess world, ISimpleItem block, boolean oreDict) {
		if (block == null)
			return true;

		if (block.getId() == 0 && isAir(world)) //Special case for air-type blocks.
			return true;

		if (block.equals(new SimpleItem(world, this), oreDict))
			return true;

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#check(net.minecraft.world.IBlockAccess,
	 * redgear.core.util.ISimpleItem)
	 */
	@Override
	public boolean check(IBlockAccess world, ISimpleItem block) {
		return check(world, block, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#check(net.minecraft.world.IBlockAccess,
	 * redgear.core.util.ISimpleItem, boolean, redgear.core.world.Location)
	 */
	@Override
	public boolean check(IBlockAccess world, ISimpleItem block, boolean oreDict, ILocation relative) {
		return copy().translate(relative).check(world, block, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#check(net.minecraft.world.IBlockAccess,
	 * redgear.core.util.ISimpleItem, redgear.core.world.Location)
	 */
	@Override
	public boolean check(IBlockAccess world, ISimpleItem block, ILocation relative) {
		return check(world, block, true, relative);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#check(net.minecraft.world.IBlockAccess,
	 * java.util.Collection, boolean)
	 */
	@Override
	public boolean check(IBlockAccess world, Collection<ISimpleItem> blocks, boolean oreDict) {
		for (ISimpleItem block : blocks)
			if (check(world, block, oreDict))
				return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#check(net.minecraft.world.IBlockAccess,
	 * java.util.Collection)
	 */
	@Override
	public boolean check(IBlockAccess world, Collection<ISimpleItem> blocks) {
		return check(world, blocks, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#check(net.minecraft.world.IBlockAccess,
	 * java.util.Collection, redgear.core.world.Location)
	 */
	@Override
	public boolean check(IBlockAccess world, Collection<ISimpleItem> blocks, ILocation relative) {
		return check(world, blocks, true, relative);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#check(net.minecraft.world.IBlockAccess,
	 * java.util.Collection, boolean, redgear.core.world.Location)
	 */
	@Override
	public boolean check(IBlockAccess world, Collection<ISimpleItem> blocks, boolean oreDict, ILocation relative) {
		return copy().translate(relative).check(world, blocks, oreDict);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#check(net.minecraft.world.IBlockAccess,
	 * java.lang.Class, redgear.core.world.Location)
	 */
	@Override
	public boolean check(IBlockAccess world, Class<? extends TileEntity> classes, ILocation relative) {
		return copy().translate(relative).check(world, classes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#check(net.minecraft.world.IBlockAccess,
	 * java.lang.Class)
	 */
	@Override
	public boolean check(IBlockAccess world, Class<? extends TileEntity> type) {
		return type.isInstance(getTile(world));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#placeBlock(net.minecraft.world.World,
	 * redgear.core.util.ISimpleItem)
	 */
	@Override
	public void placeBlock(World world, ISimpleItem item) {
		if (item != null && item.isValid() && item.isBlock())
			world.setBlock(x, y, z, item.getId(), item.getMeta(), 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#placeBlock(net.minecraft.world.World,
	 * redgear.core.util.ISimpleItem, redgear.core.world.Location)
	 */
	@Override
	public void placeBlock(World world, ISimpleItem item, ILocation relative) {
		copy().translate(relative).placeBlock(world, item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#placeBlock(net.minecraft.world.World,
	 * redgear.core.util.ISimpleItem, redgear.core.util.ISimpleItem, boolean)
	 */
	@Override
	public boolean placeBlock(World world, ISimpleItem item, ISimpleItem target, boolean oreDict) {
		if (check(world, target, oreDict)) {
			placeBlock(world, item);
			return true;
		} else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#placeBlock(net.minecraft.world.World,
	 * redgear.core.util.ISimpleItem, redgear.core.util.ISimpleItem)
	 */
	@Override
	public boolean placeBlock(World world, ISimpleItem item, ISimpleItem target) {
		return placeBlock(world, item, target, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#placeBlock(net.minecraft.world.World,
	 * redgear.core.util.ISimpleItem, redgear.core.util.ISimpleItem, boolean,
	 * redgear.core.world.Location)
	 */
	@Override
	public boolean placeBlock(World world, ISimpleItem item, ISimpleItem target, boolean oreDict, ILocation relative) {
		return copy().translate(relative).placeBlock(world, item, target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#placeBlock(net.minecraft.world.World,
	 * redgear.core.util.ISimpleItem, redgear.core.util.ISimpleItem,
	 * redgear.core.world.Location)
	 */
	@Override
	public boolean placeBlock(World world, ISimpleItem item, ISimpleItem target, ILocation relative) {
		return placeBlock(world, item, target, true, relative);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#placeBlock(net.minecraft.world.World,
	 * redgear.core.util.ISimpleItem, java.util.Collection, boolean)
	 */
	@Override
	public boolean placeBlock(World world, ISimpleItem item, Collection<ISimpleItem> targets, boolean oreDict) {
		for (ISimpleItem it : targets)
			if (placeBlock(world, item, it, oreDict))
				return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#placeBlock(net.minecraft.world.World,
	 * redgear.core.util.ISimpleItem, java.util.Collection)
	 */
	@Override
	public boolean placeBlock(World world, ISimpleItem item, Collection<ISimpleItem> targets) {
		return placeBlock(world, item, targets, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#placeBlock(net.minecraft.world.World,
	 * redgear.core.util.ISimpleItem, java.util.Collection,
	 * redgear.core.world.Location)
	 */
	@Override
	public boolean placeBlock(World world, ISimpleItem item, Collection<ISimpleItem> targets, ILocation relative) {
		return placeBlock(world, item, targets, true, relative);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#placeBlock(net.minecraft.world.World,
	 * redgear.core.util.ISimpleItem, java.util.Collection, boolean,
	 * redgear.core.world.Location)
	 */
	@Override
	public boolean placeBlock(World world, ISimpleItem item, Collection<ISimpleItem> targets, boolean oreDict,
			ILocation relative) {
		return copy().translate(relative).placeBlock(world, item, targets, oreDict);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * redgear.core.world.ILocation#getBlockId(net.minecraft.world.IBlockAccess)
	 */
	@Override
	public int getBlockId(IBlockAccess world) {
		return world.getBlockId(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * redgear.core.world.ILocation#getBlockMeta(net.minecraft.world.IBlockAccess
	 * )
	 */
	@Override
	public int getBlockMeta(IBlockAccess world) {
		return world.getBlockMetadata(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * redgear.core.world.ILocation#getTile(net.minecraft.world.IBlockAccess)
	 */
	@Override
	public TileEntity getTile(IBlockAccess world) {
		return world.getBlockTileEntity(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#getBlockMaterial(net.minecraft.world.
	 * IBlockAccess)
	 */
	@Override
	public Material getBlockMaterial(IBlockAccess world) {
		return world.getBlockMaterial(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#isAir(net.minecraft.world.IBlockAccess)
	 */
	@Override
	public boolean isAir(IBlockAccess world) {
		return world.isAirBlock(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * redgear.core.world.ILocation#rotate(net.minecraftforge.common.ForgeDirection
	 * , int)
	 */
	@Override
	public Location rotate(ForgeDirection direction, int degrees) {
		degrees = Math.abs(degrees) % 4;// should ensure that degrees must be 0, 1, 2, or 3 and nothing else

		if (degrees == 0)
			return this;

		switch (direction) {
		case DOWN:
			rotateY(degrees + 2 % 4);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * redgear.core.world.ILocation#reflect(net.minecraftforge.common.ForgeDirection
	 * )
	 */
	@Override
	public Location reflect(ForgeDirection direction) {
		switch (direction) {
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

	private Location rotateX(int degrees) {
		if (degrees > 0)
			rotateX(--degrees);

		return new Location(x, z, y);
	}

	private Location rotateY(int degrees) {
		if (degrees > 0)
			rotateY(--degrees);

		return new Location(z, y, x);
	}

	private Location rotateZ(int degrees) {
		if (degrees > 0)
			rotateZ(--degrees);

		return new Location(y, x, z);
	}

	private Location reflectX() {
		return new Location(-x, y, z);
	}

	private Location reflectY() {
		return new Location(x, -y, z);
	}

	private Location reflectZ() {
		return new Location(x, y, -z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#translate(redgear.core.world.Location)
	 */
	@Override
	public ILocation translate(ILocation other) {
		return new Location(x + other.getX(), y + other.getY(), z + other.getZ());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#translaste(int, int)
	 */
	@Override
	public ILocation translate(int direction, int amount) {
		return translate(ForgeDirection.getOrientation(direction), amount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#translate(int, int, int)
	 */
	@Override
	public ILocation translate(int x, int y, int z) {
		return translate(new Location(x, y, z));
	}

	@Override
	public ILocation translate(ForgeDirection direction, int amount) {
		return translate(direction.offsetX * amount, direction.offsetY * amount, direction.offsetZ * amount);
	}

	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	@Override
	public int hashCode() {
		return HashHelper.hash(x, y, z);
	}

	/*
	 * hash(hash(hash(1, x), y), z);
	 * 
	 * private int hash(int seed, int value){
	 * return (seed * 31) + value;
	 * }
	 */

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IPoint)
			return equals((IPoint) obj);
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see redgear.core.world.ILocation#equals(redgear.core.world.Location)
	 */
	@Override
	public boolean equals(IPoint other) {
		return x == other.getX() && y == other.getY() && z == other.getZ();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * redgear.core.world.ILocation#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("x", x);
		tag.setInteger("y", y);
		tag.setInteger("z", z);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag, String name) {
		NBTTagCompound subTag = new NBTTagCompound();
		writeToNBT(subTag);
		tag.setTag(name, subTag);
	}

	public Location(NBTTagCompound tag) {
		this(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
	}

	public Location(NBTTagCompound tag, String name) {
		this(tag.getCompoundTag(name));
	}
}