package redgear.core.thermal;

import java.util.HashMap;

import net.minecraft.block.material.Material;

public class ThermalMaterial {
	
	private static final HashMap<Material, Integer> materialMap = new HashMap<Material, Integer>();
	private static final int ice = 30;
	private static final int metal = 20;
	private static final int water = 10;
	private static final int Default = 8;
	private static final int air = 4;
	
	static{
		put(Material.ice, ice);
		put(Material.snow, ice);
		put(Material.craftedSnow, ice);
		
		put(Material.iron, metal);
		put(Material.anvil, metal);
		
		put(Material.water, water);
		put(Material.glass, water);
		
		put(Material.grass, Default);
		put(Material.ground, Default);
		put(Material.wood, Default);
		put(Material.rock, Default);
		put(Material.sand, Default);
		put(Material.tnt, Default);
		put(Material.coral, Default);
		put(Material.pumpkin, Default);
		
		put(Material.leaves, air);
		put(Material.plants, air);
		put(Material.vine, air);
		put(Material.sponge, air);
		put(Material.circuits, air);
		put(Material.redstoneLight, air);
		put(Material.dragonEgg, air);
		put(Material.portal, air);
		put(Material.cake, air);
		put(Material.web, air);
		put(Material.piston, air);	
		put(Material.clay, air);
		put(Material.cactus, air);
		put(Material.air, air);
		
		
		put(Material.materialCarpet, 3);
		
		put(Material.cloth, 2);
		
		put(Material.lava, 0);
		put(Material.fire, 0);
		
	}
	
	public static void put(Material mat, int loss){
		materialMap.put(mat, loss);
	}
	
	public static int getHeatLoss(Material other){
		Integer value = materialMap.get(other);
		
		if(value == null)
			return Default;
		else
			return value;
	}
}
