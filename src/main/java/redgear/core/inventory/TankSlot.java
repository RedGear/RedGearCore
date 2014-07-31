package redgear.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import redgear.core.tile.TileEntityInventory;

public class TankSlot extends InvSlot{
	private final boolean fillSlot;
	private final boolean either;

	public TankSlot(TileEntityInventory inventory, int x, int y, boolean fillSlot, boolean either) {
		super(inventory, x, y);
		this.fillSlot = fillSlot;
		this.either = either;
	}

	public TankSlot(TileEntityInventory inventory, int x, int y, boolean fillSlot) { 
		this(inventory, x, y, fillSlot, false);
	}
	
	public TankSlot(TileEntityInventory inventory, int x, int y, boolean fillSlot, TransferRule rule) { 
		this(inventory, x, y, fillSlot, false);
		this.setMachineRule(rule);
		this.setPlayerRule(rule);
	}
	
	/**
	 * Can be used in children to filter the stacks. 
	 * @param stack
	 * @return
	 */
	public boolean stackAllowed(ItemStack stack){
		return either ? FluidContainerRegistry.isContainer(stack) : fillSlot ? FluidContainerRegistry.isFilledContainer(stack) : FluidContainerRegistry.isEmptyContainer(stack);
	}
}
