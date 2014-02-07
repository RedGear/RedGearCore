package redgear.core.item;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import redgear.core.asm.RedGearCore;
import redgear.core.world.Location;

public class ItemDebugTool extends ItemGeneric{

	public ItemDebugTool(int Id) {
		super(Id, "debugger");
	}

	
	/**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset){
    	try{
    	
    	Location loc = new Location(x, y, z);
    	print(player, "Block ID: " + loc.getBlockId(world));
    	print(player, "Block Meta: " + loc.getBlockMeta(world));
    	
    	TileEntity tile = loc.getTile(world);
    	if(tile != null){ //Use reflection to print out all the values in the tile. 
    		for(Field f : tile.getClass().getDeclaredFields()){
    			try {
    				print(player, f.getName() + ": " + f.get(tile).toString());
				} catch (Exception e) {
					print(player, "Debugger bugged :(");
					RedGearCore.instance.logDebug("", e);
				}
    		}
    		
    		
    	}
    	
    	}
    	catch(Exception e){ //This try is just a paranoid double-check.
			print(player, "Debugger bugged :(");
			RedGearCore.instance.logDebug("", e);
		}
    	
    	
        return true;
    }
    
    /**
     * Sends the string to both the player and the console. (Assuming debugmode it turned on).
     * @param player
     * @param message
     */
    private void print(EntityPlayer player, String message){
    	player.addChatMessage(message);
    	//RedGearCore.instance.logDebug(message);
    }
}
