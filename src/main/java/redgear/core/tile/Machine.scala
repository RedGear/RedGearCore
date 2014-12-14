package redgear.core.tile

import net.minecraft.nbt.NBTTagCompound
import scala.beans.BeanProperty

/**
 * Created by Blackhole on 10/11/2014.
 */
trait Machine extends Savable with Updateable with TryEnergy{
  @BeanProperty
  val idleRate: Int = 20
  @BeanProperty
  var idle: Int = 0
  @BeanProperty
  var energyRate: Int = 0
  @BeanProperty
  var standby: Int = 0
  @BeanProperty
  var workTotal: Int = 0
  @BeanProperty
  var work: Int = 0

  var tile: TileEntityGeneric

  abstract override def updateEntity {
    super.updateEntity
    if (tile isClient) return
    if (standby > 0) {
      standby -= 1
      return
    }
    var check: Boolean = false
    idle -= 1;

    if (idle <= 0) {
      idle = idleRate
      check |= doPreWork
      if (work == 0) {
        val add: Int = checkWork
        if (add > 0) {
          addWork(add)
          check = true
        }
      }
    }
    if (work > 0 && tryUseEnergy(energyRate)) {
      check |= doWork
      if ( {
        work -= 1;
        work
      } <= 0) {
        workTotal = 0
        check |= doPostWork
      }
    }
    if (check) tile forceSync
  }

  def addWork(work: Int) {
    this.work += work
    workTotal = work
  }

  protected def stopWork {
    workTotal = 0
    work = 0
  }

  /**
   * Called every time the idle timer ticks. Use this for free things like
   * filling/emptying buckets
   */
  protected def doPreWork: Boolean

  /**
   * Called after PreWork if work is 0, check for work here and it will be
   * added before calling work.
   */
  protected def checkWork: Int

  protected def doWork: Boolean

  /**
   * If the amount of energy requested is found, then use it and return true,
   * otherwise do nothing and return false
   *
   * @param energy Amount of energy needed to work
   * @return true if there is enough power, false if there is not
   */
  abstract override protected def tryUseEnergy(energy: Int): Boolean = true

  /**
   * Called when work is done.
   */
  protected def doPostWork: Boolean

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  abstract override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("idle", idle)
    tag.setLong("energyRate", energyRate)
    tag.setInteger("work", work)
    tag.setInteger("workTotal", workTotal)
  }

  /**
   * Don't forget to override this function in all children if you want more
   * vars!
   */
  abstract override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    idle = tag.getInteger("idle")
    energyRate = tag.getInteger("energyRate")
    work = tag.getInteger("work")
    workTotal = tag.getInteger("workTotal")
  }
}
