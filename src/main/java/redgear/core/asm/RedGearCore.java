package redgear.core.asm;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.mod.ModUtils;
import redgear.core.render.CoreIconRegistry;
import redgear.core.tile.TileEntitySmart;
import redgear.core.util.CoreFuelHandler;
import redgear.core.util.CoreTradeHandler;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class RedGearCore extends DummyModContainer{
    public static ModUtils util;

    public RedGearCore()
    {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "RedGear|Core";
        meta.name = "Red Gear Core";
        meta.version = coreVersion;
        meta.authorList = Arrays.asList(new String[] {"Blackhole", "Enosphorous"});
        meta.url = "";
        meta.description = "";
    }
    
    private static final String coreVersion = "@CoreVersion@";

    @Override
    public boolean registerBus(EventBus bus, LoadController controller){
        bus.register(this);
        RedGearCoreLoadingPlugin.util.saveConfig();
        return true;
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event){
        util = new mod(event);
        
        CoreFuelHandler.init();
        CoreTradeHandler.init();
        CoreIconRegistry.init();
        GameRegistry.registerTileEntity(TileEntitySmart.class, "TileEntitySmart");
        
        if(util.getBoolean("registerVanillaItems")){
        	OreDictionary.registerOre("oreCoal", Block.oreCoal);
        	
	        OreDictionary.registerOre("gemDiamond", Item.diamond);
	        OreDictionary.registerOre("blockDiamond", Block.blockDiamond);
	        OreDictionary.registerOre("gemEmerald", Item.emerald);
	        OreDictionary.registerOre("blockEmerald", Block.blockEmerald);
	        OreDictionary.registerOre("ingotIron", Item.ingotIron);
	        OreDictionary.registerOre("blockIron", Block.blockIron);
	        OreDictionary.registerOre("ingotGold", Item.ingotGold);
	        OreDictionary.registerOre("blockGold", Block.blockGold);
        }
        
        util.saveConfig();
    }
    
    private class mod extends ModUtils{
    	
    	mod(FMLPreInitializationEvent event){
    		super(0, 0);
    		this.PreInitialization(event);
    	}

		@Override
		public void PreInit(FMLPreInitializationEvent event) {
			
		}

		@Override
		public void Init(FMLInitializationEvent event) {
			
		}

		@Override
		public void PostInit(FMLPostInitializationEvent event) {
			
		}
    	
    }
}
