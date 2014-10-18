package redgear.core.tile

import net.minecraft.nbt.NBTTagCompound

/**
 * Created by Blackhole on 10/12/2014.
 */
trait Savable {
  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  def writeToNBT(tag: NBTTagCompound)

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  def readFromNBT(tag: NBTTagCompound)
}
