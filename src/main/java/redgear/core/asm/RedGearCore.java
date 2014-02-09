package redgear.core.asm;

import java.io.File;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.item.ItemDebugTool;
import redgear.core.mod.CoreModUtils;
import redgear.core.mod.ModUtils;
import redgear.core.network.CoreCommonProxy;
import redgear.core.tile.TileEntitySmart;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Mod(modid = "redgear_core", name = "Red Gear Core", version = "@CoreVersion@", dependencies = "")
//@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {CorePacketHandler.buttonChannel }, packetHandler = CorePacketHandler.class)
@TransformerExclusions(value = {"redgear.core.asm", "redgear.core.mod" })
public class RedGearCore extends ModUtils {

	@Instance("RedGear|Core")
	public static RedGearCore instance;

	@SidedProxy(clientSide = "redgear.core.network.CoreClientProxy", serverSide = "redgear.core.network.CoreCommonProxy")
	public static CoreCommonProxy proxy;

	public static CoreModUtils util;
	public static File mcLocation;

	@Override
	protected void PreInit(FMLPreInitializationEvent event) {
		proxy.init();

		util.saveConfig();

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

	@Override
	protected void Init(FMLInitializationEvent event) {

	}

	@Override
	protected void PostInit(FMLPostInitializationEvent event) {

	}

	@Override
	@Mod.EventHandler
	public void PreInitialization(FMLPreInitializationEvent event) {
		super.PreInitialization(event);
	}

	@Override
	@Mod.EventHandler
	public void Initialization(FMLInitializationEvent event) {
		super.Initialization(event);
	}

	@Override
	@Mod.EventHandler
	public void PostInitialization(FMLPostInitializationEvent event) {
		super.PostInitialization(event);
	}
}
