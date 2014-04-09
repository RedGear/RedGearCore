package redgear.core.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class IntentoryStorage implements ISidedInventory {
	private final List<InvSlot> slots = new ArrayList<InvSlot>();

	public int addSlot(InvSlot slot) {
		slots.add(slot);
		return slot.getSlotIndex();
	}

	public int addSlot(int x, int y) {
		return addSlot(new InvSlot(this, x, y));
	}

	public ItemStack addStack(ItemStack stack) {
		for (InvSlot slot : slots) {
			stack = slot.addStack(stack, false);
			if (stack == null)
				return null;
		}

		return stack;
	}

	public ItemStack addStack(ItemStack stack, int[] possibleSlots) {
		for (int i : possibleSlots) {
			stack = slots.get(i).addStack(stack, false);
			if (stack == null)
				return null;
		}

		return stack;
	}

	public ItemStack addStack(int slot, ItemStack stack) {
		return slots.get(slot).addStack(stack);
	}

	public ItemStack addStack(int slot, ItemStack stack, boolean all) {
		return slots.get(slot).addStack(stack, all);
	}

	public ItemStack addStack(int slot, ItemStack stack, boolean all, boolean override) {
		return slots.get(slot).addStack(stack, all, override);
	}

	public boolean canAddStack(ItemStack stack) {
		boolean test = false;
		for (InvSlot slot : slots) {
			test = slot.canAddStack(stack, false);
			if (test) //shortcut ends loop if it's true
				return test;
		}

		return test;
	}

	public boolean canAddStack(ItemStack stack, int[] possibleSlots) {
		boolean test = false;
		for (int i : possibleSlots) {
			test = slots.get(i).canAddStack(stack, false);
			if (test)
				return test;
		}

		return test;
	}

	public boolean canAddStack(int slot, ItemStack stack) {
		return slots.get(slot).canAddStack(stack);
	}

	public boolean canAddStack(int slot, ItemStack stack, boolean all) {
		return slots.get(slot).canAddStack(stack, all);
	}

	public boolean canAddStack(int slot, ItemStack stack, boolean all, boolean override) {
		return slots.get(slot).canAddStack(stack, all, override);
	}

	public boolean stackAllowed(int slot, ItemStack stack) {
		return slots.get(slot).stackAllowed(stack);
	}

	public boolean validSlot(int slot) {
		return slot > 0 && slot < slots.size();
	}

	@Override
	public int getSizeInventory() {
		return slots.size();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return slots.get(index).getStack();
	}

	@Override
	public ItemStack decrStackSize(int index, int amount) {
		return slots.get(index).decrStackSize(amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack stack = getStackInSlot(index);

		if (stack != null)
			setInventorySlotContents(index, null);

		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		slots.get(slot).putStack(stack);
	}

	@Override
	public String getInventoryName() {
		return "";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64; // I guess it's possible to force a slot to not allow the full 64 items .....
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true; //TODO: When I add languages, make sure to change this to false
	}

	/**
	 * Returns true as default, basically "anything can go in any slot" override
	 * this function if that is different.
	 * NOTE: This does not control what users can put in slots, only automation.
	 */

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return slots.get(slot).canAddStack(itemstack, false, false);
	}

	/**
	 * Returns true as default. Override if tile isn't always accessible.
	 */

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	/**
	 * openChest is never called and I don't know why you ever would call it or
	 * what you'd do with it.
	 */
	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	public void writeToNBT(NBTTagCompound tag) {
		NBTTagList itemList = new NBTTagList();

		for (int i = 0; i < slots.size(); i++) {
			ItemStack stack = getStackInSlot(i);

			if (stack != null) {
				NBTTagCompound invTag = new NBTTagCompound();
				invTag.setByte("Slot", (byte) i);
				stack.writeToNBT(invTag);
				itemList.appendTag(invTag);
			}
		}

		tag.setTag("Inventory", itemList);
	}

	public void readFromNBT(NBTTagCompound tag) {
		NBTTagList tagList = tag.getTagList("Inventory", 10);

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound invTag = tagList.getCompoundTagAt(i);
			byte slot = invTag.getByte("Slot");

			if (slot >= 0 && slot < slots.size())
				slots.get(slot).putStack(ItemStack.loadItemStackFromNBT(invTag));
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		int[] temp = new int[slots.size()];

		for (int i = 0; i < slots.size(); i++)
			temp[i] = i;

		return temp;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return slots.get(i).canAddStack(itemstack, false, false);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
		return slots.get(slot).canExtract();
	}

	public List<InvSlot> getSlots() {
		return slots;
	}

	@Override
	public void markDirty() {

	}
}