package redgear.core.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.UniversalClass;
import universalelectricity.api.energy.IEnergyInterface;

@UniversalClass
public abstract class TileEntityElectricMachine extends TileEntityMachine implements IEnergyInterface {

	private long power = 0;
	private final long maxPower;

	public TileEntityElectricMachine(int idleRate, long powerCapacity) {
		super(idleRate);
		maxPower = powerCapacity;
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
	protected final boolean tryUseEnergy(long energyUse) {
		if (power > energyUse) {
			power -= energyUse;
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
		return power;
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
		tag.setLong("power", power);
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
		power = tag.getLong("power");
	}

	@Override
	public final boolean canConnect(ForgeDirection side, Object source) {
		return true;
	}

	/**
	 * Adds energy to a block. Returns the quantity of energy that was accepted.
	 * This should always
	 * return 0 if the block cannot be externally charged.
	 * 
	 * @param from Orientation the energy is sent in from.
	 * @param receive Maximum amount of energy (joules) to be sent into the
	 * block.
	 * @param doReceive If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the block.
	 */
	@Override
	public final long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive) {
		long allowed = Math.min(maxPower - power, receive);

		if (doReceive)
			power += allowed;

		return allowed;
	}

	/**
	 * Removes energy from a block. Returns the quantity of energy that was
	 * extracted. This should
	 * always return 0 if the block cannot be externally discharged.
	 * 
	 * @param from Orientation the energy is requested from. This direction MAY
	 * be passed as
	 * "Unknown" if it is wrapped from another energy system that has no clear
	 * way to find
	 * direction. (e.g BuildCraft 4)
	 * @param energy Maximum amount of energy to be sent into the block.
	 * @param doExtract If false, the charge will only be simulated.
	 * @return Amount of energy that was given out by the block.
	 */
	@Override
	public final long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract) {
		return 0;
	}
}
