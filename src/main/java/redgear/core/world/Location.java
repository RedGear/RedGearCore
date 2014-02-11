package redgear.core.world;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.util.SimpleItem;

public class Location extends ChunkPosition {

	public Location(int x, int y, int z) {
		super(x, y, z);
	}

	public Location(ChunkPosition pos) {
		this(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
	}

	public Location(TileEntity tile) {
		this(tile.xCoord, tile.yCoord, tile.zCoord);
	}

	public Location(MovingObjectPosition pos) {
		this(pos.blockX, pos.blockY, pos.blockZ);
	}

	public Location(Vec3 vector) {
		super(vector);
	}

	public int getX() {
		return chunkPosX;
	}

	public int getY() {
		return chunkPosY;
	}

	public int getZ() {
		return chunkPosZ;
	}
	
	protected Location create(int x, int y, int z){
		return new Location(x, y, z);
	}

	/**
	 * @return A new location with the same coordinates.
	 */
	public Location copy() {
		return new Location(this);
	}

	public boolean check(IBlockAccess world, ISimpleItem block) {
		if (block == null)
			return true;

		if (block.equals(Blocks.air) && isAir(world)) //Special case for air-type blocks.
			return true;

		if (block.equals(new SimpleItem(getBlock(world))))
			return true;

		return false;
	}

	public boolean check(IBlockAccess world, ISimpleItem block, ChunkPosition relative) {
		return copy().translate(relative).check(world, block);
	}

	public boolean check(IBlockAccess world, Collection<ISimpleItem> blocks) {
		for (ISimpleItem block : blocks)
			if (check(world, block))
				return true;
		return false;
	}

	public boolean check(IBlockAccess world, Collection<ISimpleItem> blocks, ChunkPosition relative) {
		return copy().translate(relative).check(world, blocks);
	}

	public boolean check(IBlockAccess world, Class<? extends TileEntity> classes, ChunkPosition relative) {
		return copy().translate(relative).check(world, classes);
	}

	public boolean check(IBlockAccess world, Class<? extends TileEntity> type) {
		return type.isInstance(getTile(world));
	}

	public void placeBlock(World world, ISimpleItem block) {
		world.setBlock(chunkPosX, chunkPosY, chunkPosZ, block.getBlock(), block.getMeta(), 3);
	}

	public void placeBlock(World world, ISimpleItem block, ChunkPosition relative) {
		copy().translate(relative).placeBlock(world, block);
	}

	public boolean placeBlock(World world, ISimpleItem block, ISimpleItem target) {
		if (check(world, target)) {
			placeBlock(world, block);
			return true;
		} else
			return false;
	}

	public boolean placeBlock(World world, ISimpleItem item, ISimpleItem target, ChunkPosition relative) {
		return copy().translate(relative).placeBlock(world, item, target);
	}

	public boolean placeBlock(World world, ISimpleItem block, Collection<ISimpleItem> targets) {
		for (ISimpleItem it : targets)
			if (placeBlock(world, block, it))
				return true;
		return false;
	}

	public boolean placeBlock(World world, ISimpleItem item, Collection<ISimpleItem> targets, ChunkPosition relative) {
		return copy().translate(relative).placeBlock(world, item, targets);
	}


	public Block getBlock(IBlockAccess world) {
		return world.getBlock(chunkPosX, chunkPosY, chunkPosZ);
	}

	public int getBlockMeta(IBlockAccess world) {
		return world.getBlockMetadata(chunkPosX, chunkPosY, chunkPosZ);
	}

	public TileEntity getTile(IBlockAccess world) {
		return world.getTileEntity(chunkPosX, chunkPosY, chunkPosZ);
	}

	public Material getMaterial(IBlockAccess world) {
		return getBlock(world).getMaterial();
	}

	public boolean isAir(IBlockAccess world) {
		return world.isAirBlock(chunkPosX, chunkPosY, chunkPosZ);
	}
	
	public void setAir(World world){
		placeBlock(world, new SimpleItem(Blocks.air));
	}
	
	public boolean isSideSolid(IBlockAccess world, ForgeDirection side){
		return world.isSideSolid(chunkPosX, chunkPosY, chunkPosZ, side, false);
	}

	public Location rotate(ForgeDirection direction, int degrees) {
		degrees = Math.abs(degrees) % 4;// should ensure that degrees must be 0, 1, 2, or 3 and nothing else

		if (degrees == 0)
			return this;

		switch (direction) {
		case DOWN:
			return rotateY(degrees + 2 % 4);
		case UP:
			return rotateY(degrees);
		case NORTH:
			return rotateZ((degrees + 2) % 4);
		case SOUTH:
			return rotateZ(degrees);
		case WEST:
			return rotateX((degrees + 2) % 4);
		case EAST:
			return rotateX(degrees);
		case UNKNOWN:
		default:
			return this.copy();
		}
	}

	public Location reflect(ForgeDirection direction) {
		switch (direction) {
		case DOWN:
		case UP:
			return reflectY();
		case NORTH:
		case SOUTH:
			return reflectZ();
		case WEST:
		case EAST:
			return reflectX();
		case UNKNOWN:
		default:
			return this.copy();
		}
	}

	private Location rotateX(int degrees) {
		if (degrees > 0)
			rotateX(--degrees);

		return create(chunkPosX, chunkPosZ, chunkPosY);
	}

	private Location rotateY(int degrees) {
		if (degrees > 0)
			rotateY(--degrees);

		return create(chunkPosZ, chunkPosY, chunkPosX);
	}

	private Location rotateZ(int degrees) {
		if (degrees > 0)
			rotateZ(--degrees);

		return create(chunkPosY, chunkPosX, chunkPosZ);
	}

	private Location reflectX() {
		return create(-chunkPosX, chunkPosY, chunkPosZ);
	}

	private Location reflectY() {
		return create(chunkPosX, -chunkPosY, chunkPosZ);
	}

	private Location reflectZ() {
		return create(chunkPosX, chunkPosY, -chunkPosZ);
	}

	public Location translate(ChunkPosition other) {
		return create(chunkPosX + other.chunkPosX, chunkPosY + other.chunkPosY, chunkPosZ + other.chunkPosZ);
	}

	public Location translate(int direction, int amount) {
		return translate(ForgeDirection.getOrientation(direction), amount);
	}

	public Location translate(int x, int y, int z) {
		return translate(create(x, y, z));
	}

	public Location translate(ForgeDirection direction, int amount) {
		return translate(direction.offsetX * amount, direction.offsetY * amount, direction.offsetZ * amount);
	}

	@Override
	public String toString() {
		return "Location [x=" + chunkPosX + ", y=" + chunkPosY + ", z=" + chunkPosZ + "]";
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("x", chunkPosX);
		tag.setInteger("y", chunkPosY);
		tag.setInteger("z", chunkPosZ);
	}

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