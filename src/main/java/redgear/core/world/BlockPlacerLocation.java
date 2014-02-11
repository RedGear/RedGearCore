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

public class BlockPlacerLocation extends WorldLocation {

	public final ISimpleItem block;

	public BlockPlacerLocation(BlockPlacerLocation other) {
		super(other);
		block = other.block;
	}

	public BlockPlacerLocation(WorldLocation other, ISimpleItem block) {
		super(other);
		this.block = block;
	}
	
	public BlockPlacerLocation(WorldLocation other) {
		super(other);
		this.block = new SimpleItem(other);
	}

	public BlockPlacerLocation(int x, int y, int z, World world, ISimpleItem block) {
		super(x, y, z, world);
		this.block = block;
	}

	public BlockPlacerLocation(ChunkPosition pos, World world, ISimpleItem block) {
		super(pos, world);
		this.block = block;
	}

	public BlockPlacerLocation(TileEntity tile, ISimpleItem block) {
		super(tile);
		this.block = block;
	}

	public BlockPlacerLocation(MovingObjectPosition pos, ISimpleItem block) {
		super(pos);
		this.block = block;
	}

	public BlockPlacerLocation(Vec3 vector, World world, ISimpleItem block) {
		super(vector, world);
		this.block = block;
	}

	public BlockPlacerLocation(NBTTagCompound tag, World world, ISimpleItem block) {
		super(tag, world);
		this.block = block;
	}

	public BlockPlacerLocation(NBTTagCompound tag, String name, World world, ISimpleItem block) {
		super(tag, name, world);
		this.block = block;
	}
	
	public BlockPlacerLocation(ChunkPosition pos, World world){
		this(pos, world, new SimpleItem(pos, world));
	}

	@Override
	protected BlockPlacerLocation create(int x, int y, int z) {
		return new BlockPlacerLocation(x, y, z, world, block);
	}

	/**
	 * @return A new location with the same coordinates.
	 */
	@Override
	public BlockPlacerLocation copy() {
		return new BlockPlacerLocation(this);
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
	public BlockPlacerLocation rotate(ForgeDirection direction, int degrees) {
		return (BlockPlacerLocation) super.rotate(direction, degrees);
	}

	@Override
	public BlockPlacerLocation reflect(ForgeDirection direction) {
		return (BlockPlacerLocation) super.reflect(direction);
	}

	@Override
	public BlockPlacerLocation translate(ChunkPosition other) {
		return (BlockPlacerLocation) super.translate(other);
	}

	@Override
	public BlockPlacerLocation translate(int direction, int amount) {
		return (BlockPlacerLocation) super.translate(direction, amount);
	}

	@Override
	public BlockPlacerLocation translate(int x, int y, int z) {
		return (BlockPlacerLocation) super.translate(x, y, z);
	}

	@Override
	public BlockPlacerLocation translate(ForgeDirection direction, int amount) {
		return (BlockPlacerLocation) super.translate(direction, amount);
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof BlockPlacerLocation)
			return super.equals(other) && this.block.equals(((BlockPlacerLocation) other).block);
		else
			return super.equals(other);
	}

	@Override
	public String toString() {
		return StringHelper.concat("BlockPlacerLocation [x=", chunkPosX, ", y=", chunkPosY, ", z=", chunkPosZ,
				", Dimension=", world.getWorldInfo().getWorldName(), ", block=", block, "]");
	}
}
