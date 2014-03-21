package redgear.core.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import redgear.core.fluids.AdvFluidTank;

public abstract class TileEntityMachine extends TileEntityTank {

	private final int idleRate;
	private int idle = 0;
	private long energyRate = 0;

	protected int workTotal = 0;
	protected int work = 0;

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

	public TileEntityMachine(int idleRate) {
		this.idleRate = idleRate;
		currMode = ejectMode.MACHINE;
	}

	@Override
	public void updateEntity() {
		if (isClient())
			return;//do nothing client-side

		if (idle-- <= 0) {
			idle = idleRate;

			doPreWork();

			if (work == 0)
				checkWork();

			if (work > 0 && tryUseEnergy(energyRate))
				if (--work <= 0) {
					workTotal = 0;
					doPostWork();
				}
		}
	}

	protected void addWork(int work, int energyRate) {
		workTotal = work;
		this.work = work;
		this.energyRate = energyRate;
	}

	protected void stopWork() {
		workTotal = 0;
		work = 0;
		energyRate = 0;
	}

	/**
	 * Called every time the idle timer ticks. Use this for free things like
	 * filling/emptying buckets
	 */
	protected abstract void doPreWork();

	/**
	 * Called after PreWork if work is 0, check for work here and it will be
	 * added before calling work.
	 */
	protected abstract void checkWork();

	/**
	 * If the amount of energy requested is found, then use it and return true,
	 * otherwise do nothing and return false
	 * 
	 * @param energy Amount of energy needed to work
	 * @return true if there is enough power, false if there is not
	 */
	protected abstract boolean tryUseEnergy(long energy);

	/**
	 * Called when work is done.
	 */
	protected abstract void doPostWork();

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("idle", idle);
		tag.setInteger("ejectMode", currMode.ordinal());
		tag.setLong("energyRate", energyRate);
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		idle = tag.getInteger("idle");
		currMode = ejectMode.valueOf(tag.getInteger("ejectMode"));
		energyRate = tag.getLong("energyRate");
	}

	protected void incrementEjectMode() {
		currMode = ejectMode.increment(currMode);
	}

	protected String getEjectMode() {
		return currMode.name();
	}

	/**
	 * Called when a player right clicks this tile while holding a Buildcraft
	 * wrench
	 * 
	 * @param player The Player who clicked.
	 * @param side The Side the player clicked on.
	 * @return true if the gui should open, false if it should not.
	 */
	@Override
	public boolean wrenched(EntityPlayer player, ForgeDirection side) {
		return !setDirection(side);
	}

	/**
	 * Called when a player right clicks this tile while holding a Buildcraft
	 * wrench and holding shift
	 * 
	 * @param player The Player who clicked
	 * @param side The Side the player clicked on
	 * @return true if the gui should open, false if it should not.
	 */
	@Override
	public boolean wrenchedShift(EntityPlayer player, ForgeDirection side) {
		incrementEjectMode();
		player.addChatMessage(player.func_145748_c_().appendText("Eject mode set to " + getEjectMode())); //TODO: Test this
		return false;
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
}
