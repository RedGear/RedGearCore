package redgear.core.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import redgear.core.world.WorldLocation;

/**
 * TileEntityGeneric handles the basic necessities of TileEntities
 * These include a way of storing the direction a tile is facing, a name used
 * for saving, and basic packet and NBT data saving
 *
 * @author Blackhole
 *
 */

public abstract class TileEntityGeneric extends TileEntity {
	private boolean needsReSync = false;

	/**
	 * @return true if this Tile is NOT on the server side (worldObj could be
	 * null).
	 */
	protected boolean isClient() {
		return !isServer();
	}

	/**
	 * @return true if this Tile is on the server side.
	 */
	protected boolean isServer() {
		return worldObj != null && !worldObj.isRemote;
	}

	@Override
	public void updateEntity() {
		if (needsReSync) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			markDirty();
			needsReSync = false;
		}
	}

	public void forceSync() {
		needsReSync = true;
	}
	
	public WorldLocation getLocation(){
		return new WorldLocation(this);
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
	}
	
	/**
	 * Save info here that could be used to create a movable item form of this block. 
	 * Does not guarantee that this code will be called. 
	 * @param tag
	 */
	public void writeToItemNBT(NBTTagCompound tag){
		
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
	}
	
	/**
	 * Load info here that came from an item form of this block. 
	 * Does not guarantee that this code will be called. 
	 * @param tag
	 */
	public void loadFromItemNBT(NBTTagCompound tag){
		
	}

	@Override
	public final Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public final void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}
}
