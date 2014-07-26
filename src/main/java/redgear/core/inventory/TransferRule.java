package redgear.core.inventory;

public enum TransferRule {
	INPUT, OUTPUT, BOTH, NEITHER;

	
	public boolean canInput(){
		return this == INPUT || this == BOTH;
	}
	
	public boolean canOutput(){
		return this == OUTPUT || this == BOTH;
	}
}
