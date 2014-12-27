package redgear.core.tile

import cofh.api.energy.EnergyStorage
import net.minecraft.nbt.NBTTagCompound

/**
 * @author Blackhole
 *         Created on 10/12/2014.
 */
abstract class TileEntityElectricMachine(idleRate: Int, storage: EnergyStorage) extends TileEntityGeneric with Machine with Inventory with ElectricReceiver{

  def this(idleRate: Int, powerCapacity: Int) = this(idleRate, new EnergyStorage(powerCapacity))

  def this(idleRate: Int) = this(idleRate, 32000)

  def this() = this(4)

  override def updateEntity: Unit = {

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
