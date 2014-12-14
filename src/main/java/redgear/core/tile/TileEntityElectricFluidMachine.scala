package redgear.core.tile

import net.minecraft.nbt.NBTTagCompound
import cofh.api.energy.EnergyStorage
import redgear.core.asm.RedGearCore

abstract class TileEntityElectricFluidMachine(idleRate: Int, storage: EnergyStorage) extends TileEntityGeneric with Machine with Inventory with Tank with ElectricReceiver with Faced {
  
  def this(idleRate: Int, powerCapacity: Int) = this(idleRate, new EnergyStorage(powerCapacity))

  def this(idleRate: Int) = this(idleRate, 32000)

  def this() = this(4)
  
  override var tile: TileEntityGeneric = this

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