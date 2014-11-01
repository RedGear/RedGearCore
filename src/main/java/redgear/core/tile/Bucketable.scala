package redgear.core.tile

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{FluidContainerRegistry, FluidStack}
import redgear.core.fluids.FluidUtil
import redgear.core.util.ItemStackUtil

/**
 * @author Blackhole
 *         Created on 10/12/2014.
 */
trait Bucketable extends Tank {
  def bucket(player: EntityPlayer, index: Int, container: ItemStack): Boolean = {
    if (FluidContainerRegistry.isFilledContainer(container))
      fill(player, index, container)
    else if (FluidContainerRegistry.isEmptyContainer(container))
      empty(player, index, container)
    else
      false
  }

  def fill(player: EntityPlayer, index: Int, container: ItemStack): Boolean = {
    if (container == null) return false
    val contents: FluidStack = FluidContainerRegistry.getFluidForFilledItem(container)
    if (contents != null && fill(ForgeDirection.UNKNOWN, contents, false) == contents.amount) {
      fill(ForgeDirection.UNKNOWN, contents, true)
      if (player.capabilities.isCreativeMode) return true
      val ans: ItemStack = container.getItem.getContainerItem(container)
      player.inventory.decrStackSize(index, 1)
      if (ans != null) if (!player.inventory.addItemStackToInventory(ans)) ItemStackUtil.dropItemStack(player.worldObj, player.posX.asInstanceOf[Int], player.posY.asInstanceOf[Int], player.posZ.asInstanceOf[Int], ans)
      player.inventory.markDirty
      return true
    }
    else return false
  }

  def empty(player: EntityPlayer, index: Int, container: ItemStack): Boolean = {
    for (tank <- tanks) {
      val contents: FluidStack = tank.getFluid
      if (contents != null) {
        val filled: ItemStack = FluidContainerRegistry.fillFluidContainer(contents.copy, container.copy)
        if (filled != null) {
          val capacity: Int = FluidUtil.getContainerCapacity(contents, filled)
          if (tank.canDrainWithMap(capacity)) {
            tank.drainWithMap(capacity, true)
            if (player.capabilities.isCreativeMode) return true
            player.inventory.decrStackSize(index, 1)
            if (!player.inventory.addItemStackToInventory(filled)) ItemStackUtil.dropItemStack(player.worldObj, player.posX.asInstanceOf[Int], player.posY.asInstanceOf[Int], player.posZ.asInstanceOf[Int], filled)
            player.inventory.markDirty
            return true
          }
        }
      }
    }
    false
  }

}
