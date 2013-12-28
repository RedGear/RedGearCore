package redgear.core.network;

import java.util.ArrayList;

import redgear.core.asm.RedGearCore;
import redgear.core.compat.ModConfigHelper;
import redgear.core.mod.RedGear;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = RedGear.NetworkID, name= RedGear.NetworkName, version= RedGear.NetworkVersion, dependencies= RedGear.NetworkDepend)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {CorePacketHandler.buttonChannel}, packetHandler = CorePacketHandler.class)
public class RedGearNetwork
{
    @Instance(RedGear.NetworkID)
    public static RedGearNetwork instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        NetworkRegistry.instance().registerGuiHandler(this, new CoreGuiHandler());
    }
}
