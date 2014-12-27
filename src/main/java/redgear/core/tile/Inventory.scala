package redgear.core.tile

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import redgear.core.inventory.InvSlot
import scala.annotation.tailrec

trait Inventory extends ISidedInventory with Savable {

  var slots = List[InvSlot]()

  //def getInventory = this

  def addSlot(slot: InvSlot) = {
    slots = slots :+ slot
    slot
  }

  def addSlot(x: Int, y: Int): InvSlot = addSlot(new InvSlot(this, x, y))

  def getSlot(x: Int) = slots(x)

  def addStack(stack: ItemStack): ItemStack = {
    @tailrec
    def addStackTail(stack: ItemStack, slots: List[InvSlot]): ItemStack = {
      if (stack == null || slots.isEmpty)
        return null;
      else
        addStackTail(slots.head.addStack(stack), slots.tail)
    }

    addStackTail(stack, slots)

  }

  def addStack(stack: ItemStack, possibleSlots: Array[Int]): ItemStack = {
    for (i <- possibleSlots) {
      if (validSlot(i))
        slots(i).addStack(stack, false)
      if (stack == null) return null
    }
    stack
  }

  def addStack(slot: Int, stack: ItemStack): ItemStack = {
    if (validSlot(slot))
      slots(slot).addStack(stack)
    else
      stack
  }

  def addStack(slot: Int, stack: ItemStack, all: Boolean): ItemStack = {
    if (validSlot(slot))
      slots(slot).addStack(stack, all)
    else
      stack
  }

  def addStack(slot: Int, stack: ItemStack, all: Boolean, `override`: Boolean): ItemStack = {
    if (validSlot(slot))
      slots(slot).addStack(stack, all, `override`)
    else
      stack
  }

  def canAddStack(stack: ItemStack): Boolean = {
    var test: Boolean = false
    for (slot <- slots) {
      test = slot.canAddStack(stack, false)
      if (test) return test
    }
    test
  }

  def canAddStack(stack: ItemStack, possibleSlots: Array[Int]): Boolean = {
    var test: Boolean = false
    for (i <- possibleSlots) {
      if (validSlot(i))
        test = slots(i).canAddStack(stack, false)
      if (test) return test
    }
    test
  }

  def canAddStack(slot: Int, stack: ItemStack): Boolean = {
    if (validSlot(slot))
      slots(slot).canAddStack(stack)
    else
      false
  }

  def canAddStack(slot: Int, stack: ItemStack, all: Boolean): Boolean = {
    if (validSlot(slot))
      slots(slot).canAddStack(stack, all)
    else
      false
  }

  def canAddStack(slot: Int, stack: ItemStack, all: Boolean, `override`: Boolean): Boolean = {
    if (validSlot(slot))
      slots(slot).canAddStack(stack, all, `override`)
    else
      false
  }

  def stackAllowed(slot: Int, stack: ItemStack): Boolean = {
    if (validSlot(slot))
      slots(slot).stackAllowed(stack)
    else
      false
  }

  def validSlot(slot: Int): Boolean = {
    slot >= 0 && slot < getSizeInventory
  }

  override def getSizeInventory: Int = {
    slots.size
  }

  override def getStackInSlot(index: Int): ItemStack = {
    if (validSlot(index))
      slots(index).getStack
    else
      null
  }

  override def decrStackSize(index: Int, amount: Int): ItemStack = {
    if (validSlot(index))
      slots(index).decrStackSize(amount)
    else
      null
  }

  override def getStackInSlotOnClosing(index: Int): ItemStack = {
    val stack: ItemStack = getStackInSlot(index)
    if (stack != null) setInventorySlotContents(index, null)
    stack
  }

  override def setInventorySlotContents(index: Int, stack: ItemStack) {
    if (validSlot(index))
      slots(index).putStack(stack)
  }

  override def getInventoryName: String = ""

  override def getInventoryStackLimit: Int = 64

  override def hasCustomInventoryName: Boolean = true

  /**
   * Returns true as default, basically "anything can go in any slot" override
   * this function if that is different.
   * NOTE: This does not control what users can put in slots, only automation.
   */
  override def isItemValidForSlot(index: Int, itemstack: ItemStack): Boolean = {
    if (validSlot(index))
      slots(index).canAddStack(itemstack, false, false)
    else
      false
  }

  /**
   * Returns true as default. Override if tile isn't always accessible.
   */
  override def isUseableByPlayer(player: EntityPlayer): Boolean = true

  /**
   * openChest is never called and I don't know why you ever would call it or
   * what you'd do with it.
   */
  override def openInventory() {}

  override def closeInventory() {}

  abstract override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    val itemList: NBTTagList = new NBTTagList
    for (i <- 0 until slots.size) {
      val stack: ItemStack = getStackInSlot(i)
      if (stack != null) {
        val invTag: NBTTagCompound = new NBTTagCompound
        invTag.setByte("Slot", i.asInstanceOf[Byte])
        stack.writeToNBT(invTag)
        itemList.appendTag(invTag)
      }
    }
    tag.setTag("Inventory", itemList)
  }

  abstract override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    val tagList: NBTTagList = tag.getTagList("Inventory", 10)
    for (i <- 0 until tagList.tagCount) {
      val invTag: NBTTagCompound = tagList.getCompoundTagAt(i)
      val slot: Byte = invTag.getByte("Slot")
      if (slot >= 0 && slot < slots.size) slots(slot).putStack(ItemStack.loadItemStackFromNBT(invTag))
    }
  }

  override def getAccessibleSlotsFromSide(var1: Int) = (0 to slots.size).toArray

  override def canInsertItem(index: Int, itemstack: ItemStack, j: Int): Boolean = {
    if (validSlot(index))
      slots(index).canAddStack(itemstack, false, false)
    else
      false
  }

  override def canExtractItem(index: Int, itemstack: ItemStack, side: Int): Boolean = {
    if (validSlot(index))
      slots(index).canExtract
    else
      false
  }

  def getSlots: java.util.List[InvSlot] = slots.toSeq
}