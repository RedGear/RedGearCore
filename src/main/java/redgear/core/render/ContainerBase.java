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
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		if (!(myTile instanceof IInventory))
			return null;
		IInventory invTile = (IInventory) myTile;

		//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			//merges the item into player inventory since its in the tileEntity
			if (slot < 9) {
				if (!mergeItemStack(stackInSlot, invTile.getSizeInventory(), invTile.getSizeInventory() + 36, true))
					return null;
			}
			//places it into the tileEntity is possible since its in the player inventory
			else if (!mergeItemStack(stackInSlot, 0, invTile.getSizeInventory() - 1, false))
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
}
