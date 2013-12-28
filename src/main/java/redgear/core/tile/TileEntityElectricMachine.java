package redgear.core.tile;

import ic2.api.energy.prefab.BasicSink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import redgear.core.compat.Mods;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.electricity.ElectricityPack;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;

@InterfaceList(value = { @Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraftAPI|power"),
		@Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore")})
public abstract class TileEntityElectricMachine extends TileEntityMachine implements IPowerReceptor, IElectrical, IEnergyHandler {
	private final int euRate = 2;//2 energy per EU
	private final int mjRate = 5;//5 energy per MJ
	private final float rfRate = .5F; //1/2 energy per RF
	private final float ueRate = .05F; // 1/20 energy per UE. 
	
	//IC2
	private BasicSink ic2EnergySink;
	
	//Buildcraft
	private PowerHandler mjHandler;
	
	private float UEPower = 0;
	private final float UEMaxPower;
	
	private int RFPower = 0;
	private final int REMaxPower;
	
	public TileEntityElectricMachine(int idleRate, int powerCapacity){
		super(idleRate);
		initIC2(powerCapacity);
		initBC(powerCapacity);
		UEMaxPower = powerCapacity / ueRate;
		REMaxPower = (int) (powerCapacity / rfRate);
	}
	
	@Method(modid = "IC2")
	private void initIC2(int powerCapacity){
		ic2EnergySink = new BasicSink(this, powerCapacity / euRate, 3);
	}
	
	@Method(modid = "BuildCraftAPI|power")
	private void initBC(int powerCapacity){
		mjHandler = new PowerHandler(this, Type.MACHINE);
		mjHandler.configure(2.0F, 300.0F, 75.0F, powerCapacity / mjRate);
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		
		if (isClient())
    		return;//do nothing client-side
		
		if(Mods.IC2.isIn())
			ic2EnergySink.updateEntity();
		if(Loader.isModLoaded("BuildCraftAPI|power"))
			mjHandler.update();
	}
	
	private boolean isUEIn(){
		try {
			Class.forName("universalelectricity.core.UniversalElectricity");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * If the amount of energy requested is found, then it will be used and return true. If there isn't enough power it will do nothing and return false.
	 * @param energyUse amount of power requested
	 * @return true if there is enough power, false if there is not
	 */
	@Override
	protected boolean tryUseEnergy(int energyUse){
		if(Loader.isModLoaded("BuildCraftAPI|power") && mjHandler.getEnergyStored() >= energyUse / mjRate){
			mjHandler.useEnergy(energyUse / mjRate, energyUse / mjRate, true);
			return true;
		}
		else
			if(Mods.IC2.isIn() && ic2EnergySink.useEnergy(energyUse / euRate))
				return true;
			else
				if(isUEIn() && UEPower > energyUse / ueRate){
					UEPower -= energyUse / ueRate;
					return true;
				}
				else
					if(Mods.CoFHCore.isIn() && RFPower > energyUse / rfRate){
						RFPower -= energyUse / rfRate;
						return true;
					}
					return false;
		
	}
	
	/**
	 * Actually returns the maximum of the four energy buffers, since they can't be mixed anyway
	 * @return
	 */
	protected double getEnergyAmount(){
		double workingValue = 0;
		
		if(Loader.isModLoaded("BuildCraftAPI|power"))
			workingValue = mjHandler.getEnergyStored() / mjRate;
		
		if(Mods.IC2.isIn())
			workingValue = Math.max(workingValue, ic2EnergySink.getEnergyStored() / euRate);
		
		if(isUEIn())
			workingValue = Math.max(workingValue, UEPower / ueRate);
		
		if(Mods.CoFHCore.isIn())
			workingValue = Math.max(workingValue, RFPower / rfRate);
		
		return workingValue;
	}
	
	protected void configureMJPower(float minEnergyReceived, float maxEnergyReceived, float activationEnergy, float maxStoredEnergy){
		mjHandler.configure(minEnergyReceived, maxEnergyReceived, activationEnergy, maxStoredEnergy);
	}
	
	protected void configureEUPower(int powerCapacity, int  tier){
		ic2EnergySink = new BasicSink(this, powerCapacity, 1);
	}
	
	
    /**
     * Don't forget to override this function in all children if you want more vars!
     */
    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setInteger("work", work);
        tag.setInteger("workTotal", workTotal);
        if(Mods.IC2.isIn())
        	ic2EnergySink.writeToNBT(tag);
        if(Loader.isModLoaded("BuildCraftAPI|power"))
        	mjHandler.writeToNBT(tag);
        tag.setFloat("UEPower", UEPower);
        tag.setInteger("RFPower", RFPower);
    }

    /**
     * Don't forget to override this function in all children if you want more vars!
     */
    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        work = tag.getInteger("work");
        workTotal = tag.getInteger("workTotal");
        if(Mods.IC2.isIn())
        	ic2EnergySink.readFromNBT(tag);
        if(Loader.isModLoaded("BuildCraftAPI|power"))
        	mjHandler.readFromNBT(tag);
        UEPower = tag.getFloat("UEPower");
        RFPower = tag.getInteger("RFPower");
    }
    
	
	//IC2
    @Override
	public void invalidate(){
		ic2EnergySink.invalidate();
		super.invalidate();
	}
	
	@Override
	public void onChunkUnload(){
		ic2EnergySink.onChunkUnload();
		super.onChunkUnload();
	}
	  
	//Buildcraft
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		return mjHandler.getPowerReceiver();
	}

	@Override
	public void doWork(PowerHandler workProvider) {}

	@Override
	public World getWorld() {
		return worldObj;
	}
	
	//Universal Electricity
	
	@Override
	public float receiveElectricity(ForgeDirection from, ElectricityPack receive, boolean doReceive) {
		if(receive == null) 
			return 0F;
		
		if(doReceive)
			UEPower += receive.getWatts();
		
		return receive.getWatts();
	}

	@Override
	public ElectricityPack provideElectricity(ForgeDirection from, ElectricityPack request, boolean doProvide) {
		return null;
	}

	@Override
	public float getRequest(ForgeDirection direction) {
		return UEMaxPower - UEPower;
	}

	@Override
	public float getProvide(ForgeDirection direction) {
		return 0;
	}

	@Override
	public float getVoltage() {
		return 120F;
	}

	@Override
	public boolean canConnect(ForgeDirection direction) {
		return UEMaxPower > 0;
	}
	
	// Thermal Expansion

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate){
		int canHave = Math.min(REMaxPower - RFPower, maxReceive);
		
		if(!simulate)
			RFPower += canHave;
		
		return maxReceive - canHave;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate){
		return 0;
	}

	@Override
	public boolean canInterface(ForgeDirection from){
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from){
		return RFPower;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from){
		return REMaxPower;
	}
}
