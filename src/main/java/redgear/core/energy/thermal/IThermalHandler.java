package redgear.core.energy.thermal;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Use this interface on TileEntities that interact with thermal energy. 
 * You can use a {@link ThermalStorage} internally to simplify things. 
 * 
 * Heavily inspired by / copied from CoFH Lib's Energy api.
 * Many thanks to King Lemming.
 * 
 * @author Blackhole
 */
public interface IThermalHandler {

	/**
	 * Amount of heat needed to raise temperature by one degree
	 * This applies to the entire block, not any one side.
	 */
	public float getSpecificHeat();

	/**
	 * Fraction of heat difference that can be transfered per tick through this
	 * side. Must be less than 1.
	 */
	public float getConductivity(ForgeDirection side);

	/** amount of heat energy held by this handler, measured in RF. **/
	public int getHeat();

	/**
	 * Using the data provided in HeatStack, share your heat with it and return
	 * the amount taken or given.
	 * Positive numbers means heat was given to the stack, and so the caller
	 * should add it, while
	 * negative numbers mean that heat was taken and the caller should subtract
	 * it. 0 Means the objects are
	 * at the same temperature.
	 * 
	 * @param side The side of this tile the calling object is accessing. Unknown 
	 * should mean the other object is INSIDE this tile, like an item or internal 
	 * IThermalStorage.
	 * @param heatStack A HeatStack object created with the parameters from the
	 * other object.
	 * @param simulate If TRUE the interaction should only be simulated, not
	 * acutally done.
	 * @return The amount of heat this object accepting.
	 * This number should be SUBTRACTED to the calling object's heat value.
	 * It could be positive, meaning heat was added to the accepting object,
	 * negative meaning it was taken from the accepting object, or 0 meaning no
	 * change.
	 */

	public int shareHeat(ForgeDirection side, HeatStack heatStack, boolean simulate);
}
