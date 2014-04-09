package redgear.core.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityMachine extends TileEntityTank {

	private final int idleRate;
	private int idle = 0;
	private int energyRate = 0;
	private int standby = 0;

	protected int workTotal = 0;
	protected int work = 0;

	public TileEntityMachine(int idleRate) {
		this.idleRate = idleRate;

	}

	@Override
	public void updateEntity() {
		if (isClient())
			return;//do nothing client-side

		if (standby > 0) {
			standby--;
			return;
		}

		if (idle-- <= 0) {
			idle = idleRate;

			doPreWork();

			if (work == 0)
				checkWork();
		}

		if (work > 0 && tryUseEnergy(energyRate))
			if (--work <= 0) {
				workTotal = 0;
				doPostWork();
			}

	}

	public int getWork() {
		return work;
	}

	public void addWork(int work) {
		this.work += work;
		workTotal = work;
	}

	public int getWorkTotal() {
		return workTotal;
	}

	public void setWorkTotal(int workTotal) {
		this.workTotal = workTotal;
	}

	public void setEnergyRate(int energyRate) {
		this.energyRate = energyRate;
	}

	public int getIdle() {
		return idle;
	}

	public void setIdle(int idle) {
		this.idle = idle;
	}

	public int getStandby() {
		return standby;
	}

	/**
	 * Orders this machine to wait for the specified time in ticks before
	 * performing any further operations.
	 * This includes counting down it's idle timer, as well as preWork,
	 * checkWork, and doPostWork.
	 * 
	 * You can call this again with a value of 0 to stop waiting.
	 * 
	 * Standby timer is NOT preserved on chunk unload.
	 * 
	 * @param ticks
	 */
	public void setStandby(int ticks) {
		standby = ticks;
	}

	protected void stopWork() {
		workTotal = 0;
		work = 0;
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
	protected abstract boolean tryUseEnergy(int energy);

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
		tag.setLong("energyRate", energyRate);
		tag.setInteger("work", work);
		tag.setInteger("workTotal", workTotal);
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		idle = tag.getInteger("idle");
		energyRate = tag.getInteger("energyRate");
		work = tag.getInteger("work");
		workTotal = tag.getInteger("workTotal");
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

}
