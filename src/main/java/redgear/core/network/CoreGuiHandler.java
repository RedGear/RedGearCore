package redgear.core.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import redgear.core.asm.RedGearCore;
import redgear.core.tile.ITileFactory;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CoreGuiHandler implements IGuiHandler {
	private final Map<Integer, ITileFactory> dataMap = new HashMap<Integer, ITileFactory>();

	private CoreGuiHandler() {
	}

	private static CoreGuiHandler instance;

	public static CoreGuiHandler init() {
		if (instance == null) {
			instance = new CoreGuiHandler();
			NetworkRegistry.INSTANCE.registerGuiHandler(RedGearCore.inst, instance);
		}

		return instance;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (dataMap.containsKey(ID))
			return dataMap.get(ID).createContainer(player.inventory, world.getTileEntity(x, y, z));
		else {
			RedGearCore.inst.logDebug("Unknown Gui ID: " + ID);
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (dataMap.containsKey(ID))
			return dataMap.get(ID).createGui(player.inventory, world.getTileEntity(x, y, z));
		else {
			RedGearCore.inst.logDebug("Unknown Gui ID: " + ID);
			return null;
		}
	}
	
	public static int addGuiMap(ITileFactory factory){
		int index = instance.dataMap.size();
		instance.dataMap.put(index, factory);
		return index;
	}
}
