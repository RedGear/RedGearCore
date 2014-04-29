package redgear.core.tile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * TileEntityGeneric handles the basic necessities of TileEntities
 * These include a way of storing the direction a tile is facing, a name used
 * for saving, and basic packet and NBT data saving
 *
 * @author Blackhole
 *
 */

public abstract class TileEntityGeneric extends TileEntity implements IFacedTile {
	private ForgeDirection direction = ForgeDirection.SOUTH; //Default
	private boolean needsReSync = false;

	@Override
	public final int getDirectionId() {
		return getDirection().ordinal();
	}

	@Override
	public ForgeDirection getDirection() {
		return direction;
	}

	@Override
	public final boolean setDirection(int id) {
		return setDirection(ForgeDirection.getOrientation(id));
	}

	@Override
	public boolean setDirection(ForgeDirection side) {
		boolean ans = false;

		if (side != ForgeDirection.UNKNOWN) {
			ans = direction != side;
			direction = side;
		}

		return ans;
	}

	private static final int[] directionMap = {2, 5, 3, 4 };

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		setDirection(directionMap[MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3]);
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

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setByte("direction", (byte) direction.ordinal());
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		direction = ForgeDirection.getOrientation(tag.getByte("direction"));
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
