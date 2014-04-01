package redgear.core.energy.thermal;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class HeatStack {
	protected final float specificHeat; //amount of energy needed to raise temperature by one degree
	protected final float conductivity; //fraction of heat difference that can be transfered per tick. Must be less than 1.
	protected final int heat;
	
	
	public HeatStack(IThermalStorage thermal){
		this.specificHeat = thermal.getSpecificHeat();
		this.conductivity = thermal.getConductivity();
		this.heat = thermal.getHeat();
	}
	
	public HeatStack(IThermalHandler thermal, ForgeDirection side){
		this.specificHeat = thermal.getSpecificHeat();
		this.conductivity = thermal.getConductivity(side);
		this.heat = thermal.getHeat();
	}
	
	public HeatStack(IThermalContainerItem thermal, ItemStack stack){
		this.specificHeat = thermal.getSpecificHeat(stack);
		this.conductivity = thermal.getConductivity(stack);
		this.heat = thermal.getHeat(stack);
	}
	
	public HeatStack(Block block){
		this.specificHeat = ThermalMaterial.getSpecificHeat(block);
		this.conductivity = ThermalMaterial.getConductivity(block);
		this.heat = ThermalMaterial.getHeat(block);
	}
	
	public static HeatStack create(World world, int x, int y, int z, ForgeDirection side){
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if(tile instanceof IThermalHandler){
			return new HeatStack((IThermalHandler) tile, side);
		} else{
			return new HeatStack(world.getBlock(x, y, z));
		}
	}
	
	public float getSpecificHeat() {
		return specificHeat;
	}


	public float getConductivity() {
		return conductivity;
	}


	public int getHeat() {
		return heat;
	}
	
	public float getTemperature() {
		return heat / specificHeat;
	}
	
	/**
	 * Share heat with the given HeatStack
	 * @param other Another HeatStack to calculated the heat-sharing. 
	 * @return Amount of heat that the object THIS HeatStack represents should ADD. 
	 * It could be negative, meaning a loss in heat.
	 * 
	 *  Be sure to return this value to the other object through the return of shareHeat.
	 */
	public int shareHeat(HeatStack other){
		if (other == null)
			return 0;

		return (int) ((other.getTemperature() - this.getTemperature()) / (other.getConductivity() * this.getConductivity()));
	}
}
