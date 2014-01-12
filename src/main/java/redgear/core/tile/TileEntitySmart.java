package redgear.core.tile;

import net.minecraft.nbt.NBTTagCompound;

/**
 * TileEntity that saves the username of the person who placed it. Best if used
 * in conjunction with BlockSmart
 * 
 * @author Blackhole
 * 
 */
public class TileEntitySmart extends TileEntityGeneric {
	public String ownerName = "";

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("ownerName", ownerName);
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		ownerName = tag.getString("ownerName");
	}
}