package redgear.core.asm;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.mod.CoreModUtils;
import redgear.core.mod.ModUtils;
import redgear.core.network.CommonProxyGeneric;
import redgear.core.network.CorePacketHandler;
import redgear.core.tile.TileEntitySmart;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Mod(modid = "RedGear|Core", name = "Red Gear Core", version = "@CoreVersion@", dependencies = "")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {CorePacketHandler.buttonChannel }, packetHandler = CorePacketHandler.class)
@TransformerExclusions(value = {"redgear.core.asm", "redgear.core.mod" })
public class RedGearCore extends ModUtils {

	public RedGearCore() {
		super(0, 0);
	}

	@Instance("RedGear|Core")
	public static RedGearCore instance;

	@SidedProxy(clientSide = "redgear.core.network.ClientProxyGeneric", serverSide = "redgear.core.network.CommonProxyGeneric")
	public static CommonProxyGeneric proxy;

	public static CoreModUtils util;
	public static File mcLocation;

	@Override
	protected void PreInit(FMLPreInitializationEvent event) {
		proxy.init();

		util.saveConfig();

		GameRegistry.registerTileEntity(TileEntitySmart.class, "TileEntitySmart");

		if (getBoolean("registerVanillaItems")) {
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
