package redgear.core.energy.thermal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import redgear.core.tile.TileEntityMachine;
import redgear.core.world.WorldLocation;

public abstract class TileEntityThermalMachine extends TileEntityMachine implements IThermalHandler {
	protected ThermalStorage storage;
	private ForgeDirection heatCheckDirect;

	public TileEntityThermalMachine(int idleRate, float specificHeat, float conductivity) {
		super(idleRate);
		storage = new ThermalStorage(specificHeat, conductivity);
		heatCheckDirect = ForgeDirection.UP;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		shareHeat(heatCheckDirect);
		int ord = heatCheckDirect.ordinal() + 1;

		heatCheckDirect = ForgeDirection.VALID_DIRECTIONS[ord % 6];
	}
	
	
	private void shareHeat(ForgeDirection side){
		storage.shareHeat(new WorldLocation(this).translate(side, 1).getTile(), side);
	}
	
	
	/**
	 * If the amount of energy requested is found, then use it and return true,
	 * otherwise do nothing and return false
	 * 
	 * @param energy Amount of energy needed to work
	 * @return true if there is enough power, false if there is not
	 */
	@Override
	protected boolean tryUseEnergy(int energy) {
		if (energy <= storage.getHeat()) {
			storage.modifyHeatStored(-energy);
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
		tag.setInteger("heatCheckDirect", heatCheckDirect.ordinal());
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		storage.readFromNBT(tag);
		heatCheckDirect = ForgeDirection.getOrientation(tag.getInteger("heatCheckDirect"));
	}

	@Override
	public float getSpecificHeat() {
		return storage.getSpecificHeat();
	}

	@Override
	public float getConductivity(ForgeDirection side) {
		return storage.getConductivity();
	}

	@Override
	public int getHeat() {
		return storage.getHeat();
	}

	@Override
	public int shareHeat(ForgeDirection side, HeatStack heatStack, boolean simulate) {
		return storage.shareHeat(heatStack, simulate);
	}
}
