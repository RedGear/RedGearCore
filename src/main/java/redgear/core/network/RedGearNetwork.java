package redgear.core.network;

import redgear.core.mod.ModUtils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "RedGear|Network", name= "Network", version= "@NetworkVersion@", dependencies= "required-after:RedGear|Core;")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {CorePacketHandler.buttonChannel}, packetHandler = CorePacketHandler.class)
public class RedGearNetwork extends ModUtils{
	
	public RedGearNetwork() {
		super(0, 0);
	}
	
    @Instance("RedGear|Network")
    public static RedGearNetwork instance;

	@Override
	protected void PreInit(FMLPreInitializationEvent event) {
		NetworkRegistry.instance().registerGuiHandler(this, new CoreGuiHandler());
	}

	@Override
	protected void Init(FMLInitializationEvent event) {
		
	}

	@Override
	protected void PostInit(FMLPostInitializationEvent event) {
		
	}
	
	@Mod.EventHandler public void PreInitialization(FMLPreInitializationEvent event){super.PreInitialization(event);}
	@Mod.EventHandler public void Initialization(FMLInitializationEvent event){super.Initialization(event);}
	@Mod.EventHandler public void PostInitialization(FMLPostInitializationEvent event){super.PostInitialization(event);}
}
