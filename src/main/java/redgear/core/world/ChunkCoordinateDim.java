package redgear.core.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ChunkCoordinateDim extends ChunkCoordinate {
	
	public final int dimension;

	public ChunkCoordinateDim(int x, int z, int dimension){
		super(x, z);
		this.dimension = dimension;
	}
	
	public ChunkCoordinateDim(int x, int z, World world){
		this(x, z, world.provider.dimensionId);
	}
	
	public ChunkCoordinateDim(ChunkCoordinate other, int dimension){
		this(other.x, other.z, dimension);
	}
	
	public ChunkCoordinateDim(ChunkCoordinateDim other){
		this(other.x, other.z, other.dimension);
	}
	
	public ChunkCoordinateDim(Location loc, int dimension){
		this(new ChunkCoordinate(loc), dimension);
	}
	
	public ChunkCoordinateDim (NBTTagCompound tag){
        this(tag.getInteger("x"), tag.getInteger("z"), 0);
    }
	
	@Override
	public int hashCode(){
		return hash(hash(hash(1, x), z), dimension);
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof ChunkCoordinateDim)
			return equals((ChunkCoordinateDim) obj);
		else
			if(obj instanceof ChunkCoordinate)
				return equals((ChunkCoordinate) obj);
			else
				return false;
	}
	
	public boolean equals(ChunkCoordinateDim other){
		return x == other.x && z == other.z && dimension == other.dimension;
	}

}
