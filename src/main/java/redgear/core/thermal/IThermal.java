package redgear.core.thermal;

/**
 * Defines an object as having thermal dynamic properties. 
 * @author Blackhole
 */
public interface IThermal {
	
	/**
	 * @return Temperature of this block. 
	 */
	public int getTemperature();
	
	public float getSpecificHeat();
	
	public int getHeat();
	
	/**
	 * Accept the heat energy offered from another object
	 * @param joules Amount of heat energy other object is giving.
	 * @return Amount of energy accepted.
	 */
	public int acceptHeat(int joules);
	
	/**
	 * Call acceptHeat for other and pass it as much heat as you want to provide.
	 * @param other Object wanting your excess heat. 
	 */
	public void emitHeat(IThermal other);
}
