package redgear.core.tile

import net.minecraft.nbt.NBTTagCompound

abstract class TileEntityTank(override val idleRate: Int) extends TileEntityGeneric with Inventory with Tank with Bucketable with Machine with Faced {

  override def updateEntity: Unit = {
    super.updateEntity
  }

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