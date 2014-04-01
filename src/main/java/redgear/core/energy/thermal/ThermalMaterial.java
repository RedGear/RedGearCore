package redgear.core.energy.thermal;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ThermalMaterial {
	
	private static final HashMap<Material, Float> materialMap = new HashMap<Material, Float>();
	private static final float metal = .9f;
	private static final float ice = .5f;
	private static final float Default = .3f;
	private static final float water = .1f;
	private static final float air = .05f;
	
	static{
		put(Material.iron, metal);
		put(Material.anvil, metal);
		
		
		put(Material.ice, ice);
		put(Material.snow, ice);
		put(Material.craftedSnow, ice);
		
		
		put(Material.water, water);
		
		
		put(Material.glass, Default);
		put(Material.grass, Default);
		put(Material.ground, Default);
		put(Material.wood, Default);
		put(Material.rock, Default);
		put(Material.sand, Default);
		put(Material.tnt, Default);
		put(Material.coral, Default);
		put(Material.gourd, Default);
		put(Material.cloth, Default);
		
		
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
		
		
		put(Material.carpet, air);
		
		
		
		put(Material.lava, air);
		put(Material.fire, air);
		
	}
	
	public static void put(Material mat, float loss){
		materialMap.put(mat, loss);
	}
	
	public static float getConductivity(Material other){
		Float value = materialMap.get(other);
		
		if(value == null)
			return Default;
		else
			return value;
	}
	
	
	public static float getSpecificHeat(Material other){
		return 0;
	}
	
	public static int getHeat(Material mat){
		if(isIce(mat))
			return 0;
		
		if(mat == Material.lava)
			return 1200;
		
		if(mat == Material.fire)
			return 1000;
		
		return 27; //about room temperature in C.
	}
	
	
	public static float getConductivity(Block other){
		return 0;
	}
	
	
	public static float getSpecificHeat(Block other){
		return 0;
	}
	
	public static int getHeat(Block mat){
		return 27; //about room temperature in C.
	}
	
	private static boolean isIce(Material mat){
		return mat == Material.ice || mat == Material.snow || mat == Material.craftedSnow;
	}
}
