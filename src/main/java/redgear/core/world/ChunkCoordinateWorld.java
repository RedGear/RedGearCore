package redgear.core.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkCoordinateWorld extends ChunkCoordinate {
	
	public final World world;
	
	public ChunkCoordinateWorld(int x, int z, World world){
		super(x, z);
		this.world = world;
	}
	
	public ChunkCoordinateWorld(Chunk chunk) {
		this(chunk.xPosition, chunk.zPosition, chunk.worldObj);
	}
	
	public ChunkCoordinateWorld(ChunkCoordinate other, World world){
		this(other.x, other.z, world);
	}
	
	public ChunkCoordinateWorld(ChunkCoordinateWorld other){
		this(other.x, other.z, other.world);
	}
	
	public ChunkCoordinateWorld(Location loc, World world){
		this(new ChunkCoordinate(loc), world);
	}
	
	public ChunkCoordinateWorld (NBTTagCompound tag, World world){
        this(tag.getInteger("x"), tag.getInteger("z"), world);
    }
	
	@Override
	public int hashCode(){
		return hash(hash(hash(7, x), z), world.provider.dimensionId);
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof ChunkCoordinateWorld)
			return equals((ChunkCoordinateWorld) obj);
		else
			if(obj instanceof ChunkCoordinate)
				return equals((ChunkCoordinate) obj);
			else
				return false;
	}
	
	public boolean equals(ChunkCoordinateWorld other){
		return x == other.x && z == other.z && world == other.world;
	}

}
