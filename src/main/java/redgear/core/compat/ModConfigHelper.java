package redgear.core.compat;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.asm.RedGearCore;
import redgear.core.asm.RedGearCoreLoadingPlugin;
import redgear.core.mod.FileHelper;
import redgear.core.util.StringHelper;

public class ModConfigHelper
{
    /**
     * Returns an itemstack of the item with the given ModId, itemname and meta
     * @param modId The Mod Id of the mod the item is from
     * @param itemName The registered name of the item
     * @param meta The meta to be given to the item
     * @return An itemstack of the item or null if the item was not found
     */
    public static ItemStack get(String itemName, int amount, int meta)
    {
        int Id = getId(itemName);

        if (Id == 0)
        {
            return null;
        }
        else
        {
            return new ItemStack(Id, amount, meta);
        }
    }

    /**
     * Returns an itemstack of the item with the given itemname
     * @param modId The Mod Id of the mod the item is from
     * @param itemName The registered name of the item
     * @return An itemstack of the item or null if the item was not found
     */
    public static ItemStack get(String itemName)
    {
        return get(itemName, 1, OreDictionary.WILDCARD_VALUE);
    }

    public static ItemStack get(String itemName, int meta)
    {
        return get(itemName, 1, meta);
    }

    /**
     * Searches for a block or item with the given name
     * @param itemName The registered name of the item
     * @return The item Id OR 0 if the item was not found
     */
    public static int getId(String itemName){
        for (int i = 1; i < Item.itemsList.length; i++)
            if (getName(i).equals(itemName))
                return i;

        RedGearCore.util.logDebug("Can't find item with name: " + itemName);
        return 0;
    }
    
    public static String getName(int index){
    	String name = "";
    		
    	if (index < Block.blocksList.length && (Block.blocksList[index] != null && Block.blocksList[index].blockID != 0))
        {
            Block block = Block.blocksList[index];
            name = block.getUnlocalizedName();

            if (isInvalidName(name) && Item.itemsList[index] != null)
            	name = Item.itemsList[index].getUnlocalizedName();
            
            if (isInvalidName(name) && Item.itemsList[index] != null)
            	name = Item.itemsList[index].getUnlocalizedName(new ItemStack(index, 1, 0));
            
            if (isInvalidName(name))
                name = block.getClass().getCanonicalName();
            
            if (isInvalidName(name))
            	name = block.getClass().getName();
        }
        else if (Item.itemsList[index] != null)
        {
            Item item = Item.itemsList[index];
            name = item.getUnlocalizedName();

            if (isInvalidName(name))
                name = item.getClass().getCanonicalName();
            
            if (isInvalidName(name))
            	name = item.getClass().getName();
        }
    	
    	return name;
    }
    
    private static boolean isInvalidName(String name){
    	return name == null || name.equals("") || name.endsWith(".null");
    }
    
    public static void printItemRegistry(){
		File output = new File(RedGearCoreLoadingPlugin.mcLocation, "Red Gear ItemRegistry.txt");
		ArrayList<String> lines = new ArrayList<String>();
		String name = "";
		
		for (int i = 1; i < Item.itemsList.length; i++){
			name = getName(i);
			if(!isInvalidName(name))
				lines.add(StringHelper.concat("Id: ", i, " Name: ", name));
		}
		
		FileHelper.writeLines(lines, output);
    }
}
