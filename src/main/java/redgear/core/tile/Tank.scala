package redgear.core.tile

import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._
import redgear.core.fluids.AdvFluidTank
import scala.collection.mutable.ArrayBuffer
import redgear.core.asm.RedGearCore

/**
 * Created by Blackhole on 10/11/2014.
 */
trait Tank extends IFluidHandler with Savable{

  var tanks = new ArrayBuffer[AdvFluidTank]
  var currMode = ejectMode.MACHINE
  var tile: TileEntityGeneric

  object ejectMode extends Enumeration{
    type ejectMode = Value

    val OFF = Value("OFF")
    val MACHINE = Value("MACHINE")
    val ALL = Value("ALL")

    def increment(lastMode: ejectMode): ejectMode = {
      if (lastMode eq OFF) MACHINE else if (lastMode eq MACHINE) ALL else OFF
    }

    def valueOf(ordinal: Int): ejectMode = {
      if (ordinal == 0) OFF else if (ordinal == 2) ALL else MACHINE
    }
  }

  /**
   * Adds the given LiquidTank to this tile
   *
   * @param newTank New Tank to add
   * @return index of the new tank used for adding side mappings
   */
  def addTank(newTank: AdvFluidTank): Int = {
    tanks += newTank
    tanks.size - 1
  }

  private def validTank(index: Int): Boolean = index >= 0 && index < tanks.size && tanks(index) != null

  def getTank(index: Int): AdvFluidTank = {
    if (validTank(index)) tanks(index)
    else null
  }

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int = {
    var filled: Int = 0
    for (tank <- tanks) {
      filled = tank.fillWithMap(resource, doFill)
      if (filled > 0) {
        if (doFill) tile.forceSync()
        return filled
      }
    }
    0
  }

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
    var removed: FluidStack = null
    for (tank <- tanks) {
      removed = tank.drainWithMap(resource, doDrain)
      if (removed != null && removed.amount > 0) {
        if (doDrain) tile.forceSync()
        return removed
      }
    }
    null
  }

  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack = {
    var removed: FluidStack = null
    for (tank <- tanks) {
      removed = tank.drainWithMap(maxDrain, doDrain)
      if (removed != null && removed.amount > 0) {
        if (doDrain) tile.forceSync()
        return removed
      }
    }
    null
  }

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  abstract override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    val tankList: NBTTagList = new NBTTagList
      for (i <- 0 to tanks.size) {
        val tank: AdvFluidTank = getTank(i)
        if (tank != null) {
          val invTag: NBTTagCompound = new NBTTagCompound
          invTag.setByte("tank", i.asInstanceOf[Byte])
          tankList.appendTag(tank.writeToNBT(invTag))
        }
    }
    tag.setTag("Tanks", tankList)
    tag.setInteger("ejectMode", currMode.id)
  }

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  abstract override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    val tagList: NBTTagList = tag.getTagList("Tanks", 10)
      for (i <- 0 to tagList.tagCount) {
        val invTag: NBTTagCompound = tagList.getCompoundTagAt(i)
        val slot: Byte = invTag.getByte("tank")
        val tank: AdvFluidTank = getTank(slot)
        if (tank != null) tank.readFromNBT(invTag)
      }
    currMode = ejectMode.valueOf(tag.getInteger("ejectMode"))
  }

  protected def incrementEjectMode() = currMode = ejectMode.increment(currMode)

  protected def getEjectMode: String = currMode.toString

  protected def ejectAllFluids: Boolean = tanks.foldLeft(false){(check: Boolean, tank: AdvFluidTank) => check || ejectFluidAllSides(tank)}

  protected def ejectFluidAllSides(tankIndex: Int): Boolean = {
    val temp: AdvFluidTank = getTank(tankIndex)
    if (temp != null) ejectFluidAllSides(temp)
    else false
  }

  protected def ejectFluidAllSides(tank: AdvFluidTank): Boolean = {
    var check: Boolean = false
    for (side <- ForgeDirection.VALID_DIRECTIONS) check |= ejectFluid(side, tank)
    check
  }

  protected def ejectFluid(side: ForgeDirection, tank: AdvFluidTank, maxDrain: Int): Boolean = {
    if (tank == null || tank.getFluid == null || (currMode eq ejectMode.OFF)) return false
    val otherTile: TileEntity = tile.getWorldObj.getTileEntity(tile.xCoord + side.offsetX, tile.yCoord + side.offsetY, tile.zCoord + side.offsetZ)
    if (otherTile != null && otherTile.isInstanceOf[IFluidHandler] && ((currMode eq ejectMode.ALL) || otherTile.isInstanceOf[Tank] || otherTile.isInstanceOf[TileEntityGeneric])) {
      val drain: FluidStack = tank.drainWithMap(maxDrain, false)
      if (drain == null) return false
      val fill: Int = otherTile.asInstanceOf[IFluidHandler].fill(side.getOpposite, drain, true)
      tank.drain(fill, true)
      return true
    }
    false
  }

  protected def ejectFluid(side: ForgeDirection, tankIndex: Int, maxDrain: Int): Boolean = ejectFluid(side, getTank(tankIndex), maxDrain)

  protected def ejectFluid(side: ForgeDirection, tank: AdvFluidTank): Boolean = ejectFluid(side, tank, tank.getCapacity)

  protected def ejectFluid(side: ForgeDirection, tankIndex: Int): Boolean = {
    val temp: AdvFluidTank = getTank(tankIndex)
    if (temp != null)
      ejectFluid(side, temp, temp.getCapacity)
    else
      false
  }

  protected def writeFluidStack(tag: NBTTagCompound, name: String, stack: FluidStack) {
    if (stack == null)
      return
    tag.setTag(name, stack.writeToNBT(new NBTTagCompound))
  }

  protected def readFluidStack(tag: NBTTagCompound, name: String): FluidStack = {
    val subTag: NBTTagCompound = tag.getCompoundTag(name)
    if (subTag == null)
      return null
    FluidStack.loadFluidStackFromNBT(subTag)
  }

  override def canFill(from: ForgeDirection, fluid: Fluid): Boolean = {
    for (tank <- tanks)
      if (tank.canAccept(fluid.getID)) return true
    false
  }

  override def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = {
    for (tank <- tanks)
      if (tank.canEject(fluid.getID)) return true
    false
  }

  override def getTankInfo(from: ForgeDirection) = tanks.map(tank => tank.getInfo).toArray
}
