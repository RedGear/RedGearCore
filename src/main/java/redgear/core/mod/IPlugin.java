package redgear.core.mod;

public interface IPlugin {
	
	public void preInit(ModUtils inst);
	
	public void Init(ModUtils inst);
	
	public void postInit(ModUtils inst);

}
