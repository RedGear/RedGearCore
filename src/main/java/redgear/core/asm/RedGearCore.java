package redgear.core.asm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.item.ItemDebugTool;
import redgear.core.mod.ModUtils;
import redgear.core.network.CoreCommonProxy;
import redgear.core.tile.TileEntitySmart;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "redgear_core", name = "Red Gear Core", version = "@ModVersion@", dependencies = "")
public class RedGearCore extends ModUtils {

	@Instance("redgear_core")
	public static RedGearCore inst;

	@SidedProxy(clientSide = "redgear.core.network.CoreClientProxy", serverSide = "redgear.core.network.CoreCommonProxy")
	public static CoreCommonProxy proxy;

	@Override
	protected void PreInit(FMLPreInitializationEvent event) {
		proxy.init();

		CoreLoadingPlugin.util.saveConfig();

		GameRegistry.registerTileEntity(TileEntitySmart.class, "TileEntitySmart");

		new ItemDebugTool();

		if (getBoolean("registerVanillaItems")) {
			OreDictionary.registerOre("gemDiamond", Items.diamond);
			OreDictionary.registerOre("blockDiamond", Blocks.diamond_block);
			OreDictionary.registerOre("gemEmerald", Items.emerald);
			OreDictionary.registerOre("blockEmerald", Blocks.emerald_block);
			OreDictionary.registerOre("ingotIron", Items.iron_ingot);
			OreDictionary.registerOre("blockIron", Blocks.iron_block);
			OreDictionary.registerOre("ingotGold", Items.gold_ingot);
			OreDictionary.registerOre("blockGold", Blocks.gold_block);
		}
	}
	
	public static boolean isItem(Entity entity){
		return entity instanceof EntityItem;
	}

	@Override
	protected void Init(FMLInitializationEvent event) {

	}

	@Override
	protected void PostInit(FMLPostInitializationEvent event) {

	}

	@Override
	@EventHandler
	public void PreInitialization(FMLPreInitializationEvent event) {
		super.PreInitialization(event);
	}

	@Override
	@EventHandler
	public void Initialization(FMLInitializationEvent event) {
		super.Initialization(event);
	}

	@Override
	@EventHandler
	public void PostInitialization(FMLPostInitializationEvent event) {
		super.PostInitialization(event);
	}
}
