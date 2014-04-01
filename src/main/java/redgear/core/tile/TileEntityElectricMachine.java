package redgear.core.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

//@Optional.InterfaceList(value = {@Interface(iface = "IEnergyHandler", modid = "") })
public abstract class TileEntityElectricMachine extends TileEntityMachine implements IEnergyHandler {

	private int energy = 0;
	private final int capacity;

	public TileEntityElectricMachine(int idleRate, int powerCapacity) {
		super(idleRate);
		capacity = powerCapacity;
	}

	/**
	 * If the amount of energy requested is found, then it will be used and
	 * return true. If there isn't enough power it will do nothing and return
	 * false.
	 * 
	 * @param energyUse amount of power requested
	 * @return true if there is enough power, false if there is not
	 */
	@Override
	protected final boolean tryUseEnergy(int energyUse) {
		if (energy > energyUse) {
			energy -= energyUse;
			return true;
		} else
			return false;

	}

	/**
	 * Actually returns the maximum of the four energy buffers, since they can't
	 * be mixed anyway
	 * 
	 * @return
	 */
	protected final long getEnergyAmount() {
		return energy;
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("work", work);
		tag.setInteger("workTotal", workTotal);
		tag.setInteger("energy", energy);
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		work = tag.getInteger("work");
		workTotal = tag.getInteger("workTotal");
		energy = tag.getInteger("energy");
	}

	/**
	 * Add energy to an IEnergyHandler, internal distribution is left entirely to the IEnergyHandler.
	 * 
	 * @param from
	 *            Orientation the energy is received from.
	 * @param maxReceive
	 *            Maximum amount of energy to receive.
	 * @param simulate
	 *            If TRUE, the charge will only be simulated.
	 * @return Amount of energy that was (or would have been, if simulated) received.
	 */
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		int energyReceived = Math.min(capacity - energy, maxReceive);

		if (!simulate) {
			energy += energyReceived;
		}
		return energyReceived;
	}

	/**
	 * Remove energy from an IEnergyHandler, internal distribution is left entirely to the IEnergyHandler.
	 * 
	 * @param from
	 *            Orientation the energy is extracted from.
	 * @param maxExtract
	 *            Maximum amount of energy to extract.
	 * @param simulate
	 *            If TRUE, the extraction will only be simulated.
	 * @return Amount of energy that was (or would have been, if simulated) extracted.
	 */
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}

	/**
	 * Returns true if the Handler functions on a given side - if a Tile Entity can receive or send energy on a given side, this should return true.
	 */
	@Override
	public boolean canInterface(ForgeDirection from) {
		return true;
	}

	/**
	 * Returns the amount of energy currently stored.
	 */
	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energy;
	}

	/**
	 * Returns the maximum amount of energy that can be stored.
	 */
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return capacity;
	}
}
