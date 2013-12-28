package redgear.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class CoreDungeonLoot {
	
	/**
	 * 
	 * @param chestType - The type of chest, in ChestGenHooks, that we're adding loot to.
	 * 
	 * @param loot - The ItemStack, SimpleItem, or Item that is being added.
	 * 
	 * @param maxStack - The maximum stack size for the loot. Minimum does not have to be
	 * set, assuming in most cases the minimum will be zero.
	 * 
	 * @param rarity - The enum LootRarity, with it's corresponding integer, that will
	 * determine the rarity of the loot.
	 * 
	 * {@link ChestGenHooks}
	 * 
	 * @author - Enosphorous
	 */
	
	public static final String[] dungeons = {ChestGenHooks.DUNGEON_CHEST, ChestGenHooks.MINESHAFT_CORRIDOR, ChestGenHooks.PYRAMID_DESERT_CHEST, ChestGenHooks.PYRAMID_JUNGLE_CHEST, ChestGenHooks.STRONGHOLD_CORRIDOR};
	
	public static void addLoot(String chestType, ItemStack loot, int maxStack, LootRarity rarity){
		 
		WeightedRandomChestContent lootStack = new WeightedRandomChestContent(loot, 1, maxStack, rarity.asInt());
		ChestGenHooks.getInfo(chestType).addItem(lootStack);
		
	}
	
	public static void addLoot(String chestType, SimpleItem loot, int maxStack, LootRarity rarity){
		
		WeightedRandomChestContent lootStack = new WeightedRandomChestContent(loot.getStack(), 1, maxStack, rarity.asInt());
		ChestGenHooks.getInfo(chestType).addItem(lootStack);
		
	}
	
	public static void addLoot(String chestType, Item loot, int maxStack, LootRarity rarity){
		
		
		WeightedRandomChestContent lootStack = new WeightedRandomChestContent(new ItemStack(loot, 1, 0), 1, maxStack, rarity.asInt());
		ChestGenHooks.getInfo(chestType).addItem(lootStack);
		
	}
	
	
	/**
	 * Methods that require less parameters, for even simpler use.
	 * 
	 * @param chestType - The ChestGenHooks chest type that the loot will be added to.
	 * 
	 * @param loot - The ItemStack that will be added.
	 * 
	 * @param maxStack - In some cases, in more common loot, the max stack size for a new
	 * loot item in the chest.
	 */
	
	public static void addRareLoot(String chestType, ItemStack loot){
		
		WeightedRandomChestContent lootStack = new WeightedRandomChestContent(loot, 1, 1, LootRarity.RARE.asInt());
		ChestGenHooks.getInfo(chestType).addItem(lootStack);
		
	}
	
	public static void addJunkLoot(String chestType, ItemStack loot, int maxStack){
		
		WeightedRandomChestContent lootStack = new WeightedRandomChestContent(loot, 1, maxStack, LootRarity.JUNK.asInt());
		ChestGenHooks.getInfo(chestType).addItem(lootStack);
		
	}
	
	public static void addEpicLoot(String chestType, ItemStack loot){
		
		WeightedRandomChestContent lootStack = new WeightedRandomChestContent(loot, 1, 1, LootRarity.EPIC.asInt());
		ChestGenHooks.getInfo(chestType).addItem(lootStack);
		
	}
	
	public static void addCommonLoot(String chestType, ItemStack loot, int maxStack){
		
		WeightedRandomChestContent lootStack = new WeightedRandomChestContent(loot, 1, maxStack, LootRarity.COMMON.asInt());
		ChestGenHooks.getInfo(chestType).addItem(lootStack);
		
	}
	
	public static void addLootToDungeons(ItemStack loot, LootRarity rarity){
		
		WeightedRandomChestContent lootStack = new WeightedRandomChestContent(loot, 1, 1, rarity.asInt());
		for(String chest : dungeons){
		ChestGenHooks.getInfo(chest).addItem(lootStack);
		}
		
	}
	
	public enum LootRarity {
		
		JUNK(65),
		WIDESPREAD(55),
		COMMON(40),
		UNCOMMON(30),
		RARE(10),
		EPIC(3);
		
		/**
		 * Getting the enum to an int, and making it 
		 * publicly accessible through the getInt method. 
		 */
		private int rarity;
		
		LootRarity(int level){
			
			this.rarity = level;
			
		}
		
		/**
		 * Gets the integer that corresponds with any entry in the enum.
		 * 
		 * @return - The integer.
		 */
		public int asInt(){
						
			return this.rarity;	
			
		}
		
	}
	
	

	

}
