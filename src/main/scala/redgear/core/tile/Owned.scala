package redgear.core.tile

import net.minecraft.nbt.NBTTagCompound

/**
 * @author Blackhole
 *         Created on 10/12/2014.
 */
trait Owned extends Savable{
  var ownerName: String = ""

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  abstract override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setString("ownerName", ownerName)
  }

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  abstract override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    ownerName = tag.getString("ownerName")
  }
}
