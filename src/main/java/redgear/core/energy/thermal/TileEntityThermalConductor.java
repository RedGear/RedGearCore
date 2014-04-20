package redgear.core.energy.thermal;

public class TileEntityThermalConductor extends TileEntityThermalMachine{

	public TileEntityThermalConductor(int idleRate, float specificHeat, int conductivity) {
		super(idleRate, specificHeat, conductivity);
	}

	@Override
	protected boolean doPreWork() {
		return false;
	}

	@Override
	protected int checkWork() {
		return 0;
	}

	@Override
	protected boolean doPostWork() {
		return false;
	}

	@Override
	protected boolean doWork() {
		return false;
		
	}

}
