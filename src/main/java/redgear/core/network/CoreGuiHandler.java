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
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.IGuiHandler;

public class CoreGuiHandler implements IGuiHandler{
    private static HashMap<Integer, GuiData> dataMap = new HashMap();
    private static int counter = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if (dataMap.containsKey(ID))
        	return dataMap.get(ID).createContainer(player.inventory, world.getBlockTileEntity(x, y, z));
        else{
            RedGearCore.util.logDebug("Unknown Gui ID: " + ID);
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if (dataMap.containsKey(ID))
        	return dataMap.get(ID).createGui(player.inventory, world.getBlockTileEntity(x, y, z));
        else{
            RedGearCore.util.logDebug("Unknown Gui ID: " + ID);
            return null;
        }
    }

    public static int addGuiMap(String fileDirectory, String fileName, String guiName, int xSize, int ySize, int playerInvHeight){
        dataMap.put(counter, new GuiData(fileDirectory, fileName, guiName, xSize, ySize, playerInvHeight));
        return counter++;
    }
    
    public static int addGuiMap(String name){
    	return addGuiMap(name, name);
    }
    
    public static int addGuiMap(String name, String displayName){
    	return addGuiMap(StringHelper.parseModName(), "textures/gui/" + name + ".png", displayName, 0, 0, 84);
    }

    public static class GuiData{
    	private final String fileDirectory;
    	private final String fileName;
    	private final String guiName;
    	private final int xSize;
    	private final int ySize;
    	
    	private final int playerInvHeight;

    	GuiData(String fileDirectory, String fileName, String guiName, int xSize, int ySize,int playerInvHeight){
    		this.fileDirectory = fileDirectory;
    		this.fileName = fileName;
    		this.guiName = guiName;
    		this.xSize = xSize;
    		this.ySize = ySize;
    		this.playerInvHeight = playerInvHeight;
    	}
    	
    	public GuiGeneric createGui(InventoryPlayer inventoryPlayer, TileEntity tile){
    		return new GuiGeneric(createContainer(inventoryPlayer, tile), xSize, ySize, fileDirectory, fileName, guiName);
    	}
    	
    	public ContainerGeneric createContainer(InventoryPlayer inventoryPlayer, TileEntity tile){
    		return new ContainerGeneric(inventoryPlayer, tile, playerInvHeight);
    	}
    }
}
