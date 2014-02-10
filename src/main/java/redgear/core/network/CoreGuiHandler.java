package redgear.core.network;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import redgear.core.asm.RedGearCore;
import redgear.core.render.ContainerGeneric;
import redgear.core.render.GuiGeneric;
import redgear.core.util.StringHelper;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CoreGuiHandler implements IGuiHandler {
	private final HashMap<Integer, GuiData> dataMap = new HashMap<Integer, GuiData>();
	private int counter = 0;

	private CoreGuiHandler() {
	}

	private static CoreGuiHandler instance;

	public static CoreGuiHandler init() {
		if (instance == null) {
			instance = new CoreGuiHandler();
			NetworkRegistry.INSTANCE.registerGuiHandler(RedGearCore.instance, instance);
		}

		return instance;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (dataMap.containsKey(ID))
			return dataMap.get(ID).createContainer(player.inventory, world.getTileEntity(x, y, z));
		else {
			RedGearCore.util.logDebug("Unknown Gui ID: " + ID);
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (dataMap.containsKey(ID))
			return dataMap.get(ID).createGui(player.inventory, world.getTileEntity(x, y, z));
		else {
			RedGearCore.util.logDebug("Unknown Gui ID: " + ID);
			return null;
		}
	}

	private int addGuiMap(GuiData data) {
		dataMap.put(counter, data);
		return counter++;
	}

	public static int addGuiMap(String fileDirectory, String fileName, String guiName, int xSize, int ySize,
			int playerInvHeight) {
		return init().addGuiMap(new GuiData(fileDirectory, fileName, guiName, xSize, ySize, playerInvHeight));
	}

	public static int addGuiMap(String name) {
		return addGuiMap(name, name);
	}

	public static int addGuiMap(String name, String displayName) {
		return addGuiMap(StringHelper.getModId(), "textures/gui/" + name + ".png", displayName, 0, 0, 84);
	}

	public static class GuiData {
		private final String fileDirectory;
		private final String fileName;
		private final String guiName;
		private final int xSize;
		private final int ySize;

		private final int playerInvHeight;

		GuiData(String fileDirectory, String fileName, String guiName, int xSize, int ySize, int playerInvHeight) {
			this.fileDirectory = fileDirectory;
			this.fileName = fileName;
			this.guiName = guiName;
			this.xSize = xSize;
			this.ySize = ySize;
			this.playerInvHeight = playerInvHeight;
		}

		public GuiGeneric createGui(InventoryPlayer inventoryPlayer, TileEntity tile) {
			return new GuiGeneric(createContainer(inventoryPlayer, tile), xSize, ySize, fileDirectory, fileName,
					guiName);
		}

		public ContainerGeneric createContainer(InventoryPlayer inventoryPlayer, TileEntity tile) {
			return new ContainerGeneric(inventoryPlayer, tile, playerInvHeight);
		}
	}
}
