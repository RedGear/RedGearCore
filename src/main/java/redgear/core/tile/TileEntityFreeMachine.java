package redgear.core.tile;

public abstract class TileEntityFreeMachine extends TileEntityMachine{

	public TileEntityFreeMachine(int idleRate) {
		super(idleRate);
	}
	@Override
	protected boolean tryUseEnergy(int energy) {
		return true;
	}

}
