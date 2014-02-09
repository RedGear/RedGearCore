package redgear.core.world;

import java.util.Collection;

import redgear.core.util.SimpleItem;
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

	/**
	 * @return A new location with the same coordinates.
	 */
	public Location copy() {
		return new Location(this);
	}

	public boolean check(IBlockAccess world, Block block) {
		if (block == null)
			return true;

		if (block.equals(Blocks.air) && isAir(world)) //Special case for air-type blocks.
			return true;

		if (block.equals(getBlock(world)))
			return true;

		return false;
	}

	public boolean check(IBlockAccess world, Block block, ChunkPosition relative) {
		return copy().translate(relative).check(world, block);
	}

	public boolean check(IBlockAccess world, Collection<Block> blocks) {
		for (Block block : blocks)
			if (check(world, block))
				return true;
		return false;
	}

	public boolean check(IBlockAccess world, Collection<Block> blocks, ChunkPosition relative) {
		return copy().translate(relative).check(world, blocks);
	}

	public boolean check(IBlockAccess world, Class<? extends TileEntity> classes, ChunkPosition relative) {
		return copy().translate(relative).check(world, classes);
	}

	public boolean check(IBlockAccess world, Class<? extends TileEntity> type) {
		return type.isInstance(getTile(world));
	}

	public void placeBlock(World world, Block block) {
		world.setBlock(chunkPosX, chunkPosY, chunkPosZ, block);
	}

	public void placeBlock(World world, Block block, ChunkPosition relative) {
		copy().translate(relative).placeBlock(world, block);
	}

	public boolean placeBlock(World world, Block block, Block target) {
		if (check(world, target)) {
			placeBlock(world, block);
			return true;
		} else
			return false;
	}

	public boolean placeBlock(World world, Block item, Block target, ChunkPosition relative) {
		return copy().translate(relative).placeBlock(world, item, target);
	}

	public boolean placeBlock(World world, Block block, Collection<Block> targets) {
		for (Block it : targets)
			if (placeBlock(world, block, it))
				return true;
		return false;
	}

	public boolean placeBlock(World world, Block item, Collection<Block> targets, ChunkPosition relative) {
		return copy().translate(relative).placeBlock(world, item, targets);
	}
	
	public void placeBlock(World world, SimpleItem block) {
		placeBlock(world, block.getBlock());
	}

	public void placeBlock(World world, SimpleItem block, ChunkPosition relative) {
		placeBlock(world, block.getBlock(), relative);
	}

	public boolean placeBlock(World world, SimpleItem block, SimpleItem target) {
		return placeBlock(world, block.getBlock(), target.getBlock());
	}

	public boolean placeBlock(World world, SimpleItem item, SimpleItem target, ChunkPosition relative) {
		return placeBlock(world, item.getBlock(), target.getBlock(), relative);
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
		placeBlock(world, Blocks.air);
	}

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

		return new Location(chunkPosX, chunkPosZ, chunkPosY);
	}

	private Location rotateY(int degrees) {
		if (degrees > 0)
			rotateY(--degrees);

		return new Location(chunkPosZ, chunkPosY, chunkPosX);
	}

	private Location rotateZ(int degrees) {
		if (degrees > 0)
			rotateZ(--degrees);

		return new Location(chunkPosY, chunkPosX, chunkPosZ);
	}

	private Location reflectX() {
		return new Location(-chunkPosX, chunkPosY, chunkPosZ);
	}

	private Location reflectY() {
		return new Location(chunkPosX, -chunkPosY, chunkPosZ);
	}

	private Location reflectZ() {
		return new Location(chunkPosX, chunkPosY, -chunkPosZ);
	}

	public Location translate(ChunkPosition other) {
		return new Location(chunkPosX + other.chunkPosX, chunkPosY + other.chunkPosY, chunkPosZ + other.chunkPosZ);
	}

	public Location translate(int direction, int amount) {
		return translate(ForgeDirection.getOrientation(direction), amount);
	}

	public Location translate(int x, int y, int z) {
		return translate(new Location(x, y, z));
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