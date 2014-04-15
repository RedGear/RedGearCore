package redgear.core.fluids;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import redgear.core.inventory.IntentoryStorage;
import redgear.core.render.LiquidGauge;

public class FluidStorage implements IFluidHandler {
	private final List<AdvFluidTank> tanks = new ArrayList<AdvFluidTank>();

	/**
	 * Adds the given LiquidTank to this tile
	 * 
	 * @param newTank New Tank to add
	 * @return index of the new tank used for adding side mappings
	 */
	public int addTank(AdvFluidTank newTank, int x, int y, int width, int height) {
		tanks.add(newTank.addLiquidGauge(x, y, width, height, tanks.size()));
		return tanks.size() - 1;
	}

	public int tanks() {
		return tanks.size();
	}

	private boolean validTank(int index) {
		return index >= 0 && index < tanks.size() && tanks.get(index) != null;
	}

	public AdvFluidTank getTank(int index) {
		if (validTank(index))
			return tanks.get(index);
		else
			return null;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int filled = 0;

		for (AdvFluidTank tank : tanks) {
			filled = tank.fillWithMap(resource, doFill);

			if (filled > 0)
				return filled;
		}

		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		FluidStack removed = null;

		for (AdvFluidTank tank : tanks) {
			removed = tank.drainWithMap(resource, doDrain);

			if (removed.amount > 0)
				return removed;
		}

		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack removed = null;

		for (AdvFluidTank tank : tanks) {
			removed = tank.drainWithMap(maxDrain, doDrain);

			if (removed != null && removed.amount > 0)
				return removed;
		}

		return null;
	}

	public void writeToNBT(NBTTagCompound tag) {
		NBTTagList tankList = new NBTTagList();

		for (int i = 0; i < tanks.size(); i++) {
			AdvFluidTank tank = getTank(i);

			if (tank != null) {
				NBTTagCompound invTag = new NBTTagCompound();
				invTag.setByte("tank", (byte) i);
				tankList.appendTag(tank.writeToNBT(invTag));
			}
		}

		tag.setTag("Tanks", tankList);
	}

	public void readFromNBT(NBTTagCompound tag) {
		NBTTagList tagList = tag.getTagList("Tanks", 10);

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound invTag = tagList.getCompoundTagAt(i);
			byte slot = invTag.getByte("tank");
			AdvFluidTank tank = getTank(slot);
			if (tank != null)
				tank.readFromNBT(invTag);
		}
	}

	public void writeFluidStack(NBTTagCompound tag, String name, FluidStack stack) {
		if (stack == null)
			return;

		tag.setTag(name, stack.writeToNBT(new NBTTagCompound()));
	}

	public FluidStack readFluidStack(NBTTagCompound tag, String name) {
		NBTTagCompound subTag = tag.getCompoundTag(name);

		if (subTag == null)
			return null;

		return FluidStack.loadFluidStackFromNBT(subTag);
	}

	public boolean fillTank(IntentoryStorage inventory, int slotFullIndex, int slotEmptyIndex, AdvFluidTank tank) {
		ItemStack fullSlot = inventory.getStackInSlot(slotFullIndex);

		if (fullSlot == null || tank == null || !inventory.validSlot(slotEmptyIndex))
			return false;

		FluidStack contents = FluidContainerRegistry.getFluidForFilledItem(fullSlot);
		ItemStack emptyContainer = FluidUtil.getEmptyContainer(fullSlot);

		if (tank.canFillWithMap(contents, true) && inventory.canAddStack(slotEmptyIndex, emptyContainer)) {
			tank.fillWithMap(contents, true);

			if (emptyContainer != null)
				inventory.addStack(slotEmptyIndex, emptyContainer);

			inventory.decrStackSize(slotFullIndex, 1);
			return true;
		}

		return false;
	}

	public boolean fillTank(IntentoryStorage inventory, int slotFullIndex, int slotEmptyIndex, int tankIndex) {
		return fillTank(inventory, slotFullIndex, slotEmptyIndex, getTank(tankIndex));
	}

	public boolean emptyTank(IntentoryStorage inventory, int slotEmptyIndex, int slotFullIndex, AdvFluidTank tank) {
		ItemStack emptySlot = inventory.getStackInSlot(slotEmptyIndex);

		if (emptySlot == null || tank == null || !inventory.validSlot(slotFullIndex))
			return false;

		FluidStack contents = tank.getFluid();

		if (emptySlot != null && contents != null) {
			ItemStack filled = FluidContainerRegistry.fillFluidContainer(contents.copy(), emptySlot.copy());
			if (filled != null) {
				int capacity = FluidUtil.getContainerCapacity(contents, filled);

				if (tank.canDrainWithMap(capacity) && inventory.canAddStack(slotFullIndex, filled)) {
					inventory.addStack(slotFullIndex, filled);
					tank.drainWithMap(capacity, true);
					inventory.decrStackSize(slotEmptyIndex, 1);
					return true;
				}
			}
		}
		return false;
	}

	public boolean emptyTank(IntentoryStorage inventory, int slotEmptyIndex, int slotFullIndex, int tankIndex) {
		return emptyTank(inventory, slotEmptyIndex, slotFullIndex, getTank(tankIndex));
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		for (AdvFluidTank tank : tanks)
			if (tank.canAccept(fluid.getID()))
				return true;
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		for (AdvFluidTank tank : tanks)
			if (tank.canEject(fluid.getID()))
				return true;
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] info = new FluidTankInfo[tanks.size()];

		for (int x = 0; x < tanks.size(); x++)
			info[x] = tanks.get(x).getInfo();

		return info;
	}

	public LiquidGauge[] getLiquidGauges() {
		LiquidGauge[] gauges = new LiquidGauge[tanks.size()];

		for (int i = 0; i < tanks.size(); i++)
			gauges[i] = tanks.get(i).getLiquidGauge();

		return gauges;
	}
}
