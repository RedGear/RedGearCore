package redgear.core.energy.thermal;

public class TileEntityThermalConductor extends TileEntityThermalMachine{

	public TileEntityThermalConductor(int idleRate, float specificHeat, int conductivity) {
		super(idleRate, specificHeat, conductivity);
	}

	@Override
	protected void doPreWork() {
		
	}

	@Override
	protected void checkWork() {
		
	}

	@Override
	protected void doPostWork() {
		
	}

}
