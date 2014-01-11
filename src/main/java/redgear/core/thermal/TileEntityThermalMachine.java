package redgear.core.thermal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import redgear.core.api.world.ILocation;
import redgear.core.tile.TileEntityMachine;
import redgear.core.world.Location;

public abstract class TileEntityThermalMachine extends TileEntityMachine implements IThermal {
	private final float specificHeat; //amount of energy needed to raise temperature by one degree
	private final int conductivity; //amount of heat that can be moved in one tick
	
	private int heat; //heat energy
	private ForgeDirection heatCheckDirect;
	
	public TileEntityThermalMachine(int idleRate, float specificHeat, int conductivity) {
		super(idleRate);
		this.specificHeat = specificHeat;
		this.conductivity = conductivity;
		heatCheckDirect = ForgeDirection.UP;
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		
		shareHeat(heatCheckDirect);
		int ord = heatCheckDirect.ordinal() + 1;
		
		heatCheckDirect = ForgeDirection.VALID_DIRECTIONS[ord > 5 ? 0 : ord];
	}
	
	private void shareHeat(ForgeDirection direct){
		ILocation otherLoc = new Location(this).translate(direct, 1);
		TileEntity otherTile = otherLoc.getTile(worldObj);
		
		if(otherTile != null && otherTile instanceof IThermal){
			IThermal thermal = (IThermal) otherTile;
			if(getTemperature() > thermal.getTemperature()){
				emitHeat(thermal);
			}
			else{
				thermal.emitHeat(this);
			}
		}
		else{
			heat -= ThermalMaterial.getHeatLoss(otherLoc.getBlockMaterial(worldObj));
		}
	}
	
	
	/**
	 * If the amount of energy requested is found, then use it and return true, otherwise do nothing and return false
	 * @param energy Amount of energy needed to work
	 * @return true if there is enough power, false if there is not
	 */
	protected boolean tryUseEnergy(long energy){
		if(energy <= heat){
			heat -= energy;
			return true;
		}
		else
			return false;
	}

	@Override
	public int getTemperature() {
		return (int) (heat / specificHeat);
	}
	
	@Override
	public float getSpecificHeat(){
		return specificHeat;
	}
	
	@Override
	public int getHeat(){
		return heat;
	}

	@Override
	public int acceptHeat(int joules) {
		int change = Math.min(joules, conductivity);
		heat += change;
		return change;
	}

	@Override
	public void emitHeat(IThermal other) {
		heat -= other.acceptHeat((int) Math.min(conductivity, (other.getSpecificHeat() / specificHeat) * (heat + other.getHeat()) - other.getHeat()));
	}
	
	/**
     * Don't forget to override this function in all children if you want more vars!
     */
    @Override
    public void writeToNBT(NBTTagCompound tag){
    	super.writeToNBT(tag);
    	tag.setInteger("heat", heat);
        tag.setInteger("heatCheckDirect", heatCheckDirect.ordinal());
    }
    
    
    /**
     * Don't forget to override this function in all children if you want more vars!
     */
    @Override
    public void readFromNBT(NBTTagCompound tag){
    	super.readFromNBT(tag);
    	heat = tag.getInteger("heat");
    	heatCheckDirect = ForgeDirection.getOrientation(tag.getInteger("heatCheckDirect"));
    }

}
