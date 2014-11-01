package redgear.core.tile

import net.minecraft.nbt.NBTTagCompound

/**
 * @author Blackhole
 *         Created on 10/12/2014.
 */
class TileEntitySmart extends TileEntityGeneric with Owned{


  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
  }

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
  }
}
