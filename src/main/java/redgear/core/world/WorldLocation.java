package redgear.core.world;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.util.StringHelper;

public class WorldLocation extends Location {

	public final World world;

	public WorldLocation(WorldLocation other) {
		super(other);
		world = other.world;
	}

	public WorldLocation(int x, int y, int z, World world) {
		super(x, y, z);
		this.world = world;
	}

	public WorldLocation(ChunkPosition pos, World world) {
		super(pos);
		this.world = world;
	}

	public WorldLocation(TileEntity tile) {
		super(tile);
		world = tile.getWorldObj();
	}

	public WorldLocation(MovingObjectPosition pos) {
		super(pos);
		world = pos.entityHit.worldObj;
	}

	public WorldLocation(Vec3 vector, World world) {
		super(vector);
		this.world = world;
	}

	public WorldLocation(NBTTagCompound tag, World world) {
		super(tag);
		this.world = world;
	}

	public WorldLocation(NBTTagCompound tag, String name, World world) {
		super(tag, name);
		this.world = world;
	}

	@Override
	protected WorldLocation create(int x, int y, int z) {
		return new WorldLocation(x, y, z, world);
	}

	/**
	 * @return A new location with the same coordinates.
	 */
	@Override
	public WorldLocation copy() {
		return new WorldLocation(this);
	}

	public boolean check(ISimpleItem block) {
		return super.check(world, block);
	}

	public boolean check(ISimpleItem block, ChunkPosition relative) {
		return super.check(world, block, relative);
	}

	public boolean check(Collection<ISimpleItem> blocks) {
		return super.check(world, blocks);
	}

	public boolean check(Collection<ISimpleItem> blocks, ChunkPosition relative) {
		return super.check(world, blocks, relative);
	}

	public boolean check(Class<? extends TileEntity> classes, ChunkPosition relative) {
		return super.check(world, classes, relative);
	}

	public boolean check(Class<? extends TileEntity> type) {
		return super.check(world, type);
	}

	public void placeBlock(ISimpleItem block) {
		super.placeBlock(world, block);
	}

	public void placeBlock(ISimpleItem block, ChunkPosition relative) {
		super.placeBlock(world, block, relative);
	}

	public boolean placeBlock(ISimpleItem block, ISimpleItem target) {
		return super.placeBlock(world, block, target);
	}

	public boolean placeBlock(ISimpleItem item, ISimpleItem target, ChunkPosition relative) {
		return super.placeBlock(world, item, target, relative);
	}

	public boolean placeBlock(ISimpleItem block, Collection<ISimpleItem> targets) {
		return super.placeBlock(world, block, targets);
	}

	public boolean placeBlock(ISimpleItem item, Collection<ISimpleItem> targets, ChunkPosition relative) {
		return super.placeBlock(world, item, targets, relative);
	}

	public Block getBlock() {
		return super.getBlock(world);
	}

	public int getBlockMeta() {
		return super.getBlockMeta(world);
	}

	public TileEntity getTile() {
		return super.getTile(world);
	}

	public Material getMaterial() {
		return super.getMaterial(world);
	}

	public boolean isAir() {
		return super.isAir(world);
	}

	public void setAir() {
		super.setAir(world);
	}

	public boolean isSideSolid(ForgeDirection side) {
		return super.isSideSolid(world, side);
	}

	@Override
	public WorldLocation rotate(ForgeDirection direction, int degrees) {
		return (WorldLocation) super.rotate(direction, degrees);
	}

	@Override
	public WorldLocation reflect(ForgeDirection direction) {
		return (WorldLocation) super.reflect(direction);
	}

	@Override
	public WorldLocation translate(ChunkPosition other) {
		return (WorldLocation) super.translate(other);
	}

	@Override
	public WorldLocation translate(int direction, int amount) {
		return (WorldLocation) super.translate(direction, amount);
	}

	@Override
	public WorldLocation translate(int x, int y, int z) {
		return (WorldLocation) super.translate(x, y, z);
	}

	@Override
	public WorldLocation translate(ForgeDirection direction, int amount) {
		return (WorldLocation) super.translate(direction, amount);
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof WorldLocation)
			return super.equals(other) && this.world.provider.dimensionId == ((WorldLocation) other).world.provider.dimensionId;
		else
			return super.equals(other);
	}
	
	@Override
	public String toString() {
		return  StringHelper.concat("WorldLocation [x=", chunkPosX, ", y=", chunkPosY, ", z=", chunkPosZ, ", Dimension=", world.getWorldInfo().getWorldName(), "]");
	}
}
