package redgear.core.machine;

import net.minecraft.nbt.NBTTagCompound;


public class MachineHandler {
	private final IMachine machine;
	private final int idleRate;
	private int idle = 0;
	private int energyRate = 0;
	private int standby = 0;

	protected int workTotal = 0;
	protected int work = 0;
	
	
	public MachineHandler(IMachine machine, int idleRate) {
		this.machine = machine;
		this.idleRate = idleRate;
	}

	
	/**
	 * Make sure to call this EVERY update tick, but only on the server side.
	 */
	public void doWork() {
		if(standby > 0){
			standby--;
			return;
		}
		
		if (idle-- <= 0) {
			idle = idleRate;

			machine.doPreWork();

			if (work == 0)
				work = machine.checkWork();
		}
		
		if (work > 0 && machine.tryUseEnergy(energyRate))
			if (--work <= 0) {
				workTotal = 0;
				machine.doPostWork();
			}
		
	}
	
	
	
	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("idle", idle);
		tag.setInteger("energyRate", energyRate);
		tag.setInteger("work", work);
		tag.setInteger("workTotal", workTotal);
	}

	
	public void readFromNBT(NBTTagCompound tag) {
		idle = tag.getInteger("idle");
		energyRate = tag.getInteger("energyRate");
		work = tag.getInteger("work");
		workTotal = tag.getInteger("workTotal");
	}
	
	public int getWork(){
		return work;
	}

	public void addWork(int work) {
		this.work += work;
	}
	
	public int getWorkTotal(){
		return workTotal;
	}
	
	public void setWorkTotal(int workTotal){
		this.workTotal = workTotal;
	}
	
	public void setEnergyRate(int energyRate){
		this.energyRate = energyRate;
	}
	
	public int getIdle(){
		return idle;
	}
	
	public void setIdle(int idle){
		this.idle = idle;
	}
	
	public int getStandby(){
		return standby;
	}
	
	/**
	 * Orders this machine to wait for the specified time in ticks before performing any further operations.
	 * This includes counting down it's idle timer, as well as preWork, checkWork, and doPostWork. 
	 * 
	 * You can call this again with a value of 0 to stop waiting. 
	 * 
	 * Standby timer is NOT preserved on chunk unload. 
	 * @param ticks
	 */
	public void setStandby(int ticks){
		standby = ticks;
	}
	
	
}
