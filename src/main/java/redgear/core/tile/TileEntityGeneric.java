package redgear.core.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * TileEntityGeneric handles the basic necessities of TileEntities
 * These include a way of storing the direction a tile is facing, a name used
 * for saving, and basic packet and NBT data saving
 * 
 * @author Blackhole
 * 
 */

public abstract class TileEntityGeneric extends TileEntity {
	private ForgeDirection direction = ForgeDirection.SOUTH; //Default
	private boolean redstoneState = false;
	private boolean needsReSync = false;

	public final int getDirectionId() {
		return getDirection().ordinal();
	}

	public ForgeDirection getDirection() {
		return direction;
	}

	public final boolean setDirection(int id) {
		return setDirection(ForgeDirection.getOrientation(id));
	}

	/**
	 * Set the facing direction of this tile
	 * 
	 * @param side Side to set direction to
	 * @return True if direction, false if it wasn't
	 */
	public boolean setDirection(ForgeDirection side) {
		boolean ans = false;

		if (side != ForgeDirection.UNKNOWN) {
			ans = direction != side;
			direction = side;
		}

		return ans;
	}

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

	/**
	 * Called when a player right clicks this tile while holding a Buildcraft
	 * wrench
	 * 
	 * @param player The Player who clicked.
	 * @param side The Side the player clicked on.
	 * @return true if the gui should open, false if it should not.
	 */
	public boolean wrenched(EntityPlayer player, ForgeDirection side) {
		return true;
	}

	/**
	 * Called when a player right clicks this tile while holding a Buildcraft
	 * wrench and holding shift
	 * 
	 * @param player The Player who clicked.
	 * @param side The Side the player clicked on
	 * @return true if the gui should open, false if it should not.
	 */
	public boolean wrenchedShift(EntityPlayer player, ForgeDirection side) {
		return true;
	}
	
	@Override
	public void updateEntity() {
		if(isServer() && needsReSync){
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			this.markDirty();
			needsReSync = false;
		}
	}
	
	protected void forceSync(){
		needsReSync = true;	
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setByte("direction", (byte) direction.ordinal());
		tag.setBoolean("redState", redstoneState);
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		direction = ForgeDirection.getOrientation(tag.getByte("direction"));
		redstoneState = tag.getBoolean("redState");
	}

	@Override
	public final Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public final void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g()); //TODO: Test this.
	}

	protected final boolean getRedstoneState() {
		return redstoneState;
	}

	/**
	 * Should be called by blocks to sync redstone state changes.
	 * 
	 * @param state
	 */
	public final void updateRedstone(boolean state) {
		if (redstoneState != state) {
			redstoneState = state;
			onRedstoneChange(state);
		}
	}

	/**
	 * Called whenever the redstone state of this block changes.
	 * True means redstone is now on (and was off).
	 * False means redstone is now off (and was on).
	 * 
	 * @param newState
	 */
	protected void onRedstoneChange(boolean newState) {
	}

	/**
	 * Use this to output a redstone signal to the given side
	 * 
	 * @return Strength of redstone 0 means none.
	 */
	public int redstoneSignal(ForgeDirection side) {
		return 0;
	}
}
