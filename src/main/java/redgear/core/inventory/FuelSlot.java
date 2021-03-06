package redgear.core.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class FuelSlot  extends InvSlot{

	public FuelSlot(IInventory inventory, int x, int y) {
		super(inventory, x, y);
		this.setMachineRule(TransferRule.INPUT);
	}
	
	/**
	 * Can be used in children to filter the stacks. 
	 * @param stack
	 * @return
	 */
	public boolean stackAllowed(ItemStack stack){
		return GameRegistry.getFuelValue(stack) > 0;
	}

}
