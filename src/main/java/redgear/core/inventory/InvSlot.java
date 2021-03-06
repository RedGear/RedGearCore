package redgear.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Different from <{@link Slot}>, this class is used to store and sort the
 * stored ItemStacks of an inventory, as well as
 * provide rules about ISidedInventory behavior or stack filters.
 *
 * @author Blackhole
 *
 */
public class InvSlot extends Slot {

	private ItemStack contents;
	private int stackLimit = 64;
	private TransferRule playerRule = TransferRule.BOTH; //Rule defining what players can take or input through the GUI.
	private TransferRule machineRule = TransferRule.BOTH; //Rule defining what automated machines (hoppers, pipe, itemducts, ect.) can input or output through the block.

	public InvSlot(IInventory inventory, int x, int y) {
		super(inventory, inventory.getSizeInventory(), x, y);

	}

	public ItemStack addStack(ItemStack stack) {
		return addStack(stack, true);
	}

	public ItemStack addStack(ItemStack stack, boolean all) {
		return addStack(stack, all, true);
	}

	public ItemStack addStack(ItemStack stack, boolean all, boolean override) {
		if (canAddStack(stack, all, override))
			if (contents == null) {
				if (stack != null)
					contents = stack.copy();
				return null;
			} else {
				int canFit = contents.getMaxStackSize() - contents.stackSize;
				contents.stackSize += Math.min(canFit, stack.stackSize);
				if (canFit >= stack.stackSize)
					return null;
				else {
					stack.stackSize -= canFit;
					return stack;
				}

			}
		else
			return stack;
	}

	public boolean canAddStack(ItemStack stack) {
		return canAddStack(stack, true);
	}

	public boolean canAddStack(ItemStack stack, boolean all) {
		return canAddStack(stack, all, true);
	}

	public boolean canAddStack(ItemStack stack, boolean all, boolean override) {
		if (contents == null)
			if ((override || machineRule.canInput()) && stackAllowed(stack))
				return true;
			else
				return false;

		if (stack == null)
			return false;

		return contents.isStackable() && stack.isItemEqual(contents)
				&& ItemStack.areItemStackTagsEqual(stack, contents) && stackAllowed(stack)
				&& contents.stackSize < contents.getMaxStackSize() && (override || machineRule.canInput())
				&& (!all || contents.stackSize + stack.stackSize <= contents.getMaxStackSize());
	}
	
	public boolean isEmpty(){
		return !this.getHasStack();
	}
	
	public boolean isFull(){
		return contents != null && contents.stackSize >= contents.getMaxStackSize();
	}

	/**
	 * Can be used in children to filter the stacks.
	 *
	 * @param stack
	 * @return
	 */
	public boolean stackAllowed(ItemStack stack) {
		return true;
	}

	public boolean canExtract() {
		return machineRule.canOutput();
	}

	public InvSlot setMachineRule(TransferRule machineRule) {
		this.machineRule = machineRule;
		return this;
	}

	public InvSlot setPlayerRule(TransferRule playerRule) {
		this.playerRule = playerRule;
		return this;
	}
	
	public InvSlot setRules(TransferRule rule){
		this.machineRule = rule;
		this.playerRule = rule;
		return this;
	}

	public InvSlot setStackLimit(int stackLimit) {
		this.stackLimit = stackLimit;
		return this;
	}

	/**
	 * Helper funct to get the stack in the slot.
	 */
	@Override
	public ItemStack getStack() {
		return contents;
	}

	/**
	 * Helper method to put a stack in the slot.
	 */
	@Override
	public void putStack(ItemStack par1ItemStack) {
		contents = par1ItemStack;
		onSlotChanged();
	}

	/**
	 * Called when the stack in a Slot changes
	 */
	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		if (contents != null && contents.stackSize == 0)
			contents = null;
	}

	/**
	 * Returns the maximum stack size for a given slot (usually the same as
	 * getInventoryStackLimit(), but 1 in the case
	 * of armor slots)
	 */
	@Override
	public int getSlotStackLimit() {
		return stackLimit;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of
	 * the second int arg. Returns the new
	 * stack.
	 */
	@Override
	public ItemStack decrStackSize(int amount) {
		return decrStackSize(amount, true);
	}

	public ItemStack decrStackSize(int amount, boolean override) {
		ItemStack temp = contents;
		if (temp != null && (override || machineRule.canOutput()))
			if (temp.stackSize <= amount)
				contents = null;
			else {
				temp = temp.splitStack(amount);

				if (temp.stackSize == 0)
					contents = null;
			}
		return temp;
	}

	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		return playerRule.canOutput();
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for
	 * the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack stack) {
		return playerRule.canInput() && stackAllowed(stack);
	}
}
