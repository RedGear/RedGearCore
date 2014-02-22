package redgear.core.world;

import java.util.Collection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.api.item.ISimpleItem;
import redgear.core.util.SimpleItem;
import redgear.core.util.StringHelper;

public class BlockLocation extends WorldLocation {

	public final ISimpleItem block;

	public BlockLocation(BlockLocation other) {
		super(other);
		block = other.block;
	}

	public BlockLocation(WorldLocation other, ISimpleItem block) {
		super(other);
		this.block = block;
	}
	
	public BlockLocation(WorldLocation other) {
		super(other);
		this.block = new SimpleItem(other);
	}

	public BlockLocation(int x, int y, int z, World world, ISimpleItem block) {
		super(x, y, z, world);
		this.block = block;
	}

	public BlockLocation(ChunkPosition pos, World world, ISimpleItem block) {
		super(pos, world);
		this.block = block;
	}

	public BlockLocation(TileEntity tile, ISimpleItem block) {
		super(tile);
		this.block = block;
	}

	public BlockLocation(MovingObjectPosition pos, ISimpleItem block) {
		super(pos);
		this.block = block;
	}

	public BlockLocation(Vec3 vector, World world, ISimpleItem block) {
		super(vector, world);
		this.block = block;
	}

	public BlockLocation(NBTTagCompound tag, World world, ISimpleItem block) {
		super(tag, world);
		this.block = block;
	}

	public BlockLocation(NBTTagCompound tag, String name, World world, ISimpleItem block) {
		super(tag, name, world);
		this.block = block;
	}
	
	public BlockLocation(ChunkPosition pos, World world){
		this(pos, world, new SimpleItem(pos, world));
	}

	@Override
	protected BlockLocation create(int x, int y, int z) {
		return new BlockLocation(x, y, z, world, block);
	}

	/**
	 * @return A new location with the same coordinates.
	 */
	@Override
	public BlockLocation copy() {
		return new BlockLocation(this);
	}

	public boolean check() {
		return super.check(block);
	}

	public boolean check(ChunkPosition relative) {
		return super.check(block, relative);
	}

	public void placeBlock() {
		super.placeBlock(block);
	}

	public void placeBlock(ChunkPosition relative) {
		super.placeBlock(block, relative);
	}

	public boolean replaceBlock(ISimpleItem target) {
		return super.placeBlock(block, target);
	}

	public boolean replaceBlock(ISimpleItem target, ChunkPosition relative) {
		return super.placeBlock(block, target, relative);
	}

	public boolean replaceBlock(Collection<ISimpleItem> targets) {
		return super.placeBlock(block, targets);
	}

	public boolean replaceBlock(Collection<ISimpleItem> targets, ChunkPosition relative) {
		return super.placeBlock(block, targets, relative);
	}

	@Override
	public BlockLocation rotate(ForgeDirection direction, int degrees) {
		return (BlockLocation) super.rotate(direction, degrees);
	}

	@Override
	public BlockLocation reflect(ForgeDirection direction) {
		return (BlockLocation) super.reflect(direction);
	}

	@Override
	public BlockLocation translate(ChunkPosition other) {
		return (BlockLocation) super.translate(other);
	}

	@Override
	public BlockLocation translate(int direction, int amount) {
		return (BlockLocation) super.translate(direction, amount);
	}

	@Override
	public BlockLocation translate(int x, int y, int z) {
		return (BlockLocation) super.translate(x, y, z);
	}

	@Override
	public BlockLocation translate(ForgeDirection direction, int amount) {
		return (BlockLocation) super.translate(direction, amount);
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof BlockLocation)
			return super.equals(other) && this.block.equals(((BlockLocation) other).block);
		else
			return super.equals(other);
	}

	@Override
	public String toString() {
		return StringHelper.concat("BlockPlacerLocation [x=", chunkPosX, ", y=", chunkPosY, ", z=", chunkPosZ,
				", Dimension=", world.getWorldInfo().getWorldName(), ", block=", block, "]");
	}
}
