package redgear.core.tile

import cofh.api.energy.EnergyStorage
import cofh.api.energy.IEnergyHandler
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import cofh.api.energy.IEnergyReceiver

/**
 * Created by Blackhole on 10/11/2014.
 */
trait ElectricReceiver extends IEnergyReceiver with Savable with TryEnergy{
  self: TileEntityGeneric => 
  
  protected var storage: EnergyStorage = new EnergyStorage(32000)

  /**
   * If the amount of energy requested is found, then it will be used and
   * return true. If there isn't enough power it will do nothing and return
   * false.
   *
   * @param energyUse amount of power requested
   * @return true if there is enough power, false if there is not
   */
  abstract override protected def tryUseEnergy(energyUse: Int): Boolean = {
    if (storage.getEnergyStored > energyUse) {
      storage.extractEnergy(energyUse, false)
      return true
    }
    else return false
  }

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  abstract override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    storage.writeToNBT(tag)
  }

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  abstract override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    storage.readFromNBT(tag)
  }

  /**
   * Add energy to an IEnergyHandler, internal distribution is left entirely to the IEnergyHandler.
   *
   * @param from
	 * Orientation the energy is received from.
   * @param maxReceive
	 * Maximum amount of energy to receive.
   * @param simulate
	 * If TRUE, the charge will only be simulated.
   * @return Amount of energy that was (or would have been, if simulated) received.
   */
  def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean): Int = {
    forceSync
    return storage.receiveEnergy(maxReceive, simulate)
  }

  /**
   * Returns true if the Handler functions on a given side - if a Tile Entity can receive or send energy on a given side, this should return true.
   */
  def canConnectEnergy(from: ForgeDirection): Boolean = {
    return true
  }

  /**
   * Returns the amount of energy currently stored.
   */
  def getEnergyStored(from: ForgeDirection): Int = {
    return storage.getEnergyStored
  }

  /**
   * Returns the maximum amount of energy that can be stored.
   */
  def getMaxEnergyStored(from: ForgeDirection): Int = {
    return storage.getMaxEnergyStored
  }
}
