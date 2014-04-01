package redgear.core.energy.thermal;

/**
 * A heat storage interface designed to be used by Tiles as a way to encapsulate
 * and simplify their "heat inventories".
 * 
 * {@link ThermalStorage} uses this interface as an example.
 * 
 * Heavily inspired by / copied from CoFH Lib's Energy api.
 * Many thanks to King Lemming.
 * 
 * @author Blackhole
 */
public interface IThermalStorage {

	/** amount of heat needed to raise temperature by one degree **/
	public float getSpecificHeat();

	/**
	 * fraction of heat difference that can be transfered per tick. Must be less
	 * than 1.
	 **/
	public float getConductivity();

	/** amount of heat energy, measured in RF. **/
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
	 * @param heatStack A HeatStack object created with the parameters from the
	 * other object.
	 * @param simulate If TRUE the interaction should only be simulated, not
	 * actually done.
	 * @return The amount of heat this object accepting.
	 * This number should be SUBTRACTED from the calling object's heat value.
	 * It could be positive, meaning heat was added to the accepting object and
	 * should be removed from the caller,
	 * negative meaning it was taken from the accepting object and should be
	 * added to the caller, or 0 meaning no change.
	 */

	public int shareHeat(HeatStack heatStack, boolean simulate);
}
