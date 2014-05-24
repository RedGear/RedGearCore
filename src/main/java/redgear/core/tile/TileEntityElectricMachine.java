package redgear.core.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public abstract class TileEntityElectricMachine extends TileEntityInventory implements IEnergyHandler {

	protected EnergyStorage storage;
	
	public TileEntityElectricMachine() {
		this(4);
	}
	
	public TileEntityElectricMachine(int idleRate) {
		this(idleRate, 32000);
	}

	public TileEntityElectricMachine(int idleRate, int powerCapacity) {
		super(idleRate);
		storage = new EnergyStorage(powerCapacity);
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
		if (storage.getEnergyStored() > energyUse) {
			storage.extractEnergy(energyUse, false);
			return true;
		} else
			return false;

	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		storage.writeToNBT(tag);
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		storage.readFromNBT(tag);
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
		return storage.receiveEnergy(maxReceive, simulate);
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
		return storage.extractEnergy(maxExtract, simulate);
	}

	/**
	 * Returns true if the Handler functions on a given side - if a Tile Entity can receive or send energy on a given side, this should return true.
	 */
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}
	
	/**
	 * Old CoFH energy connection check. Was updated to canConnectEnergy
	 */
	@Deprecated
	public boolean canInterface(ForgeDirection from) {
		return true;
	}

	/**
	 * Returns the amount of energy currently stored.
	 */
	@Override
	public int getEnergyStored(ForgeDirection from) {
		return storage.getEnergyStored();
	}

	/**
	 * Returns the maximum amount of energy that can be stored.
	 */
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return storage.getMaxEnergyStored();
	}
}
