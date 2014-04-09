package redgear.core.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import redgear.core.fluids.AdvFluidTank;
import redgear.core.fluids.FluidUtil;
import redgear.core.render.LiquidGauge;

public abstract class TileEntityTank extends TileEntityInventory implements IFluidHandler {
	private final List<AdvFluidTank> tanks = new ArrayList<AdvFluidTank>();

	private ejectMode currMode;

	private enum ejectMode {
		OFF, MACHINE, ALL;

		public static ejectMode increment(ejectMode lastMode) {
			return lastMode == OFF ? MACHINE : lastMode == MACHINE ? ALL : OFF;
		}

		public static ejectMode valueOf(int ordinal) {
			return ordinal == 0 ? OFF : ordinal == 2 ? ALL : MACHINE;
		}
	}

	public TileEntityTank() {
		currMode = ejectMode.MACHINE;
	}

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

			if (filled > 0) {
				if (doFill)
					markDirty();
				return filled;
			}
		}

		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		FluidStack removed = null;

		for (AdvFluidTank tank : tanks) {
			removed = tank.drainWithMap(resource, doDrain);

			if (removed.amount > 0) {
				if (doDrain)
					markDirty();
				return removed;
			}
		}

		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack removed = null;

		for (AdvFluidTank tank : tanks) {
			removed = tank.drainWithMap(maxDrain, doDrain);

			if (removed != null && removed.amount > 0) {
				if (doDrain)
					markDirty();
				return removed;
			}
		}

		return null;
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
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
		tag.setInteger("ejectMode", currMode.ordinal());
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagList tagList = tag.getTagList("Tanks", 10);

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound invTag = tagList.getCompoundTagAt(i);
			byte slot = invTag.getByte("tank");
			AdvFluidTank tank = getTank(slot);
			if (tank != null)
				tank.readFromNBT(invTag);
		}
		currMode = ejectMode.valueOf(tag.getInteger("ejectMode"));
	}

	protected void incrementEjectMode() {
		currMode = ejectMode.increment(currMode);
	}

	protected String getEjectMode() {
		return currMode.name();
	}

	protected void ejectAllFluids() {
		int max = tanks();

		for (int i = 0; i < max; i++)
			ejectFluidAllSides(i);
	}

	protected void ejectFluidAllSides(int tankIndex) {
		AdvFluidTank temp = getTank(tankIndex);

		if (temp != null)
			ejectFluidAllSides(temp);
	}

	protected void ejectFluidAllSides(AdvFluidTank tank) {
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
			ejectFluid(side, tank);
	}

	protected void ejectFluid(ForgeDirection side, AdvFluidTank tank, int maxDrain) {
		if (tank == null || tank.getFluid() == null || currMode == ejectMode.OFF)
			return; //can't drain from a null or empty tank, duh

		TileEntity otherTile = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord
				+ side.offsetZ);

		if (otherTile != null && IFluidHandler.class.isAssignableFrom(otherTile.getClass())
				&& (currMode == ejectMode.ALL || TileEntityMachine.class.isAssignableFrom(otherTile.getClass()))) {//IFluidHandler
			FluidStack drain = tank.drainWithMap(maxDrain, false);
			if (drain == null)
				return;
			int fill = ((IFluidHandler) otherTile).fill(side.getOpposite(), drain, true);
			tank.drain(fill, true);//find out how much the tank can drain. Try to fill all that into the other tile. Actually drain all that the other tile took.
		}
	}

	protected void ejectFluid(ForgeDirection side, int tankIndex, int maxDrain) {
		ejectFluid(side, getTank(tankIndex), maxDrain);
	}

	protected void ejectFluid(ForgeDirection side, AdvFluidTank tank) {
		ejectFluid(side, tank, tank.getCapacity());
	}

	protected void ejectFluid(ForgeDirection side, int tankIndex) {
		AdvFluidTank temp = getTank(tankIndex);

		if (temp != null)
			ejectFluid(side, temp, temp.getCapacity());
	}

	protected void writeFluidStack(NBTTagCompound tag, String name, FluidStack stack) {
		if (stack == null)
			return;

		tag.setTag(name, stack.writeToNBT(new NBTTagCompound()));
	}

	protected FluidStack readFluidStack(NBTTagCompound tag, String name) {
		NBTTagCompound subTag = tag.getCompoundTag(name);

		if (subTag == null)
			return null;

		return FluidStack.loadFluidStackFromNBT(subTag);
	}

	protected boolean fillTank(int slotFullIndex, int slotEmptyIndex, AdvFluidTank tank) {
		ItemStack fullSlot = getStackInSlot(slotFullIndex);

		if (fullSlot == null || tank == null || !validSlot(slotEmptyIndex))
			return false;

		FluidStack contents = FluidContainerRegistry.getFluidForFilledItem(fullSlot);
		ItemStack emptyContainer = FluidUtil.getEmptyContainer(fullSlot);

		if (tank.canFillWithMap(contents) && canAddStack(slotEmptyIndex, emptyContainer)) {
			tank.fillWithMap(contents, true);

			if (emptyContainer != null)
				addStack(slotEmptyIndex, emptyContainer);

			decrStackSize(slotFullIndex, 1);
			return true;
		}

		return false;
	}

	protected boolean fillTank(int slotFullIndex, int slotEmptyIndex, int tankIndex) {
		return fillTank(slotFullIndex, slotEmptyIndex, getTank(tankIndex));
	}

	protected boolean emptyTank(int slotEmptyIndex, int slotFullIndex, AdvFluidTank tank) {
		ItemStack emptySlot = getStackInSlot(slotEmptyIndex);

		if (emptySlot == null || tank == null || !validSlot(slotFullIndex))
			return false;

		FluidStack contents = tank.getFluid();

		if (emptySlot != null && contents != null) {
			ItemStack filled = FluidContainerRegistry.fillFluidContainer(contents.copy(), emptySlot.copy());
			if (filled != null) {
				int capacity = FluidUtil.getContainerCapacity(contents, filled);

				if (tank.canDrainWithMap(capacity) && canAddStack(slotFullIndex, filled)) {
					addStack(slotFullIndex, filled);
					tank.drainWithMap(capacity, true);
					decrStackSize(slotEmptyIndex, 1);
					return true;
				}
			}
		}
		return false;
	}

	protected boolean emptyTank(int slotEmptyIndex, int slotFullIndex, int tankIndex) {
		return emptyTank(slotEmptyIndex, slotFullIndex, getTank(tankIndex));
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
