package redgear.core.util;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import com.google.common.collect.ArrayListMultimap;

import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class CoreTradeHandler implements IVillageTradeHandler{

	private static CoreTradeHandler instance;
	
	public ArrayListMultimap <Integer, MerchantRecipe> villagerRecipes = ArrayListMultimap. <Integer, MerchantRecipe> create();

    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
        for (MerchantRecipe recipe : villagerRecipes.get(villager.getProfession()))
        	recipeList.add(recipe);

    }
    
    private CoreTradeHandler(){}
    
    public static CoreTradeHandler init(){
    	if(instance == null){
	    	instance = new CoreTradeHandler();
	    	for(Integer i : VillagerRegistry.getRegisteredVillagers())
	    		VillagerRegistry.instance().registerVillageTradeHandler(i, instance);
    	}
    	return instance;
    }
    

    /**
     * @param sell_item - The ItemStack being sold.
     * @param buy_item - The ItemStack being bought.
     */
    public static void addTradeRecipe(int ID, ItemStack sell_item, ItemStack buy_item){
    	addTradeRecipe(ID, sell_item, null, buy_item);

    }
   
    /**
     * @param sell_item - The ItemStack being sold.
     * @param sell_item_2 - The second ItemStack being sold.

     * @param buy_item - The ItemStack being bought.
     */
    public static void addTradeRecipe(int ID, ItemStack sell_item, ItemStack sell_item_2, ItemStack buy_item){
    	instance.villagerRecipes.put(ID, new MerchantRecipe(sell_item, sell_item_2, buy_item));
    }
    
    /**
     * @param sell_item - The ItemStack being sold.
     * @param buy_item - The ItemStack being bought.
     */
    public static void addTradeRecipe(EnumVillager villager, ItemStack sell_item, ItemStack buy_item){
    	addTradeRecipe(villager.ordinal(), sell_item, buy_item);
    }
    
    /**
     * @param sell_item - The ItemStack being sold.
     * @param buy_item - The ItemStack being bought.
     */
    public static void addTradeRecipe(EnumVillager villager, ItemStack sell_item, ItemStack sell_item_2, ItemStack buy_item){
    	addTradeRecipe(villager.ordinal(), sell_item, sell_item_2, buy_item);
    }

       
    public enum EnumVillager{
       Farmer,
       Librariean,
       Priest,
       Blacksmith,
       Butcher;
    }

}
