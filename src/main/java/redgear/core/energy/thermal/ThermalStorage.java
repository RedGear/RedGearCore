package redgear.core.energy.thermal;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * A basic implementation of {@link IThermalStorage}.
 * 
 * Heavily inspired by / copied from CoFH Lib's Energy api.
 * Many thanks to King Lemming.
 * 
 * @author Blackhole
 */
public class ThermalStorage implements IThermalStorage {
	protected final float specificHeat; //amount of energy needed to raise temperature by one degree
	protected final float conductivity; //fraction of heat difference that can be transfered per tick. Must be less than 1.
	protected int heat;

	public ThermalStorage(float specificHeat, float conductivity) {
		this.specificHeat = specificHeat;
		this.conductivity = conductivity;
	}

	public ThermalStorage readFromNBT(NBTTagCompound nbt) {
		heat = nbt.getInteger("Heat");
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		if (heat < 0)
			heat = 0;
		nbt.setInteger("Heat", heat);
		return nbt;
	}

	public void setEnergyStored(int heat) {

		this.heat = heat;

		if (this.heat < 0)
			this.heat = 0;
	}

	public void modifyHeatStored(int heat) {

		this.heat += heat;

		if (this.heat < 0)
			this.heat = 0;
	}

	public void shareHeat(TileEntity tile, ForgeDirection side) {
		if (tile instanceof IThermalHandler)
			shareHeat((IThermalHandler) tile, side);
	}

	public void shareHeat(IThermalHandler other, ForgeDirection side) {
		if (other != null)
			modifyHeatStored(-other.shareHeat(side, new HeatStack(this), false));
	}

	public void shareHeat(ItemStack stack) {
		if (stack != null) {
			Item item = stack.getItem();
			if (item instanceof IThermalContainerItem)
				shareHeat((IThermalContainerItem) item, stack);
		}
	}

	public void shareHeat(IThermalContainerItem item, ItemStack stack) {
		if (item != null)
			modifyHeatStored(-item.shareHeat(stack, new HeatStack(this), false));
	}

	@Override
	public float getSpecificHeat() {
		return specificHeat;
	}

	@Override
	public float getConductivity() {
		return conductivity;
	}

	@Override
	public int getHeat() {
		return heat;
	}

	@Override
	public int shareHeat(HeatStack heatStack, boolean simulate) {
		int ans = heatStack.shareHeat(new HeatStack(this));
		if (simulate)
			heat += ans;

		return ans;
	}

}
