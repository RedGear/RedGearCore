package redgear.core.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

public class ChunkCoordinate {

	public int x;
	public int z;

	public ChunkCoordinate(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public ChunkCoordinate(ChunkCoordinate other) {
		this(other.x, other.z);
	}

	public ChunkCoordinate(Location loc) {
		this(loc.chunkPosX >> 4, loc.chunkPosZ >> 4);
	}

	public ChunkCoordinate(Chunk chunk) {
		this(chunk.xPosition, chunk.zPosition);
	}

	public ChunkCoordinate translate(ChunkCoordinate relative) {
		x += relative.x;
		z += relative.z;
		return this;
	}

	public ChunkCoordinate translate(int x, int z) {
		return translate(new ChunkCoordinate(x, z));
	}

	public ChunkCoordinate translaste(int direction, int amount) {
		return translate(ForgeDirection.getOrientation(direction), amount);
	}

	public ChunkCoordinate translate(ForgeDirection direction, int amount) {
		return translate(direction.offsetX * amount, direction.offsetZ * amount);
	}

	public boolean checkExists(World world) {
		return world.getChunkProvider().chunkExists(x, z);
	}

	public Chunk getChunk(World world) {
		return world.getChunkFromBlockCoords(x, z);
	}

	@Override
	public int hashCode() {
		return hash(hash(7, x), z);
	}

	protected int hash(int seed, int value) {
		return seed * 31 + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ChunkCoordinate)
			return equals((ChunkCoordinate) obj);
		else
			return false;
	}

	public boolean equals(ChunkCoordinate other) {
		return x == other.x && z == other.z;
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("x", x);
		tag.setInteger("z", z);
	}

	public ChunkCoordinate(NBTTagCompound tag) {
		this(tag.getInteger("x"), tag.getInteger("z"));
	}
}
