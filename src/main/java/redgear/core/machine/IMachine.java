package redgear.core.machine;

public interface IMachine {

	/**
	 * Called every time the idle timer ticks. Use this for free things like
	 * filling/emptying buckets or energy propagation. 
	 */
	public abstract void doPreWork();

	/**
	 * Called after PreWork if work is 0, check for work here and it will be
	 * added before calling work.
	 */
	public abstract int checkWork();

	/**
	 * If the amount of energy requested is found, then use it and return true,
	 * otherwise do nothing and return false
	 * 
	 * @param energy Amount of energy needed to work
	 * @return true if there is enough power, false if there is not
	 */
	public abstract boolean tryUseEnergy(int energy);

	/**
	 * Called when work is done.
	 * Use this to produce output or clear buffers. 
	 */
	public abstract void doPostWork();
}
