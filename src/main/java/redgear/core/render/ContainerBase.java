package redgear.core.render;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public abstract class ContainerBase<T extends TileEntity> extends Container {
	public final T myTile;

	private int playerInvStart;
	private final int playerInvSize = 36;

	public ContainerBase(InventoryPlayer inventoryPlayer, T tile) {
		myTile = tile;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		if (myTile instanceof IInventory)
			return ((IInventory) myTile).isUseableByPlayer(player);
		else
			return true;
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		bindPlayerInventory(inventoryPlayer, 84);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer, int playerInvHeight) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, playerInvHeight + i * 18));

		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, playerInvHeight + 58));

		playerInvStart = inventorySlots.size() - playerInvSize;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			//places it into the tileEntity is possible since its in the player inventory
			if (slot >= playerInvStart && slot < playerInvStart + playerInvSize) {
				if (!mergeItemStack(stackInSlot, 0, playerInvStart, false))
					if (!mergeItemStack(stackInSlot, playerInvStart + playerInvSize, inventorySlots.size(), false))
						return null;
			}
			//merges the item into player inventory since its in the tileEntity
			else if (!mergeItemStack(stackInSlot, playerInvStart, playerInvStart + playerInvSize, false))
				return null;

			if (stackInSlot.stackSize == 0)
				slotObject.putStack(null);
			else
				slotObject.onSlotChanged();

			if (stackInSlot.stackSize == stack.stackSize)
				return null;

			slotObject.onPickupFromSlot(player, stackInSlot);
		}

		return stack;
	}

	@Override
	public boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverse) {
		boolean merged = false;
		int slotIndex = reverse ? endIndex - 1 : startIndex;

		if (stack == null)
			return false;

		if (stack.isStackable())
			while (stack.stackSize > 0 && (reverse ? slotIndex >= startIndex : slotIndex < endIndex)) {
				Slot slot = (Slot) inventorySlots.get(slotIndex);

				if (slot.isItemValid(stack)) {
					ItemStack slotStack = slot.getStack();

					if (slotStack != null && slotStack.getItem() == stack.getItem()
							&& (!stack.getHasSubtypes() || stack.getItemDamage() == slotStack.getItemDamage())
							&& ItemStack.areItemStackTagsEqual(stack, slotStack)) {
						int totalStackSize = slotStack.stackSize + stack.stackSize;
						int maxStackSize = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());
						if (totalStackSize <= maxStackSize) {
							stack.stackSize = 0;
							slotStack.stackSize = totalStackSize;
							slot.onSlotChanged();
							merged = true;
						} else if (slotStack.stackSize < maxStackSize) {
							stack.stackSize -= maxStackSize - slotStack.stackSize;
							slotStack.stackSize = maxStackSize;
							slot.onSlotChanged();
							merged = true;
						}
					}

				}
				slotIndex += reverse ? -1 : 1;
			}

		if (stack.stackSize > 0)//normal transfer :)
		{
			slotIndex = reverse ? endIndex - 1 : startIndex;

			while (stack.stackSize > 0 && (reverse ? slotIndex >= startIndex : slotIndex < endIndex)) {
				Slot slot = (Slot) inventorySlots.get(slotIndex);

				if (!slot.getHasStack() && slot.isItemValid(stack)) {
					int maxStackSize = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());
					if (stack.stackSize <= maxStackSize) {
						slot.putStack(stack.copy());
						slot.onSlotChanged();
						stack.stackSize = 0;
						merged = true;
					} else {
						slot.putStack(stack.splitStack(maxStackSize));
						slot.onSlotChanged();
						merged = true;
					}
				}

				slotIndex += reverse ? -1 : 1;
			}
		}

		return merged;
	}
}
