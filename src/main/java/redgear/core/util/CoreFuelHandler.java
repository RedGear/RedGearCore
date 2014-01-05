package redgear.core.util;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CoreFuelHandler implements IFuelHandler {
	
	private static CoreFuelHandler instance;
	
	private HashMap<SimpleItem, Integer> items = new HashMap<SimpleItem, Integer>();
	
	private CoreFuelHandler(){}
	
	public static CoreFuelHandler init(){
		if(instance == null){
			instance = new CoreFuelHandler();
			GameRegistry.registerFuelHandler(instance);
		}
		return instance;
	}
	
	public static void addFuel(SimpleItem fuel, int burnTime){
		init().items.put(fuel, burnTime);
	}
	
	public static void addFuel(ItemStack fuel, int burnTime){
		addFuel(new SimpleItem(fuel), burnTime);
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		Integer test = items.get(new SimpleItem(fuel));
		
		if(test == null)
			return 0;
		else
			return test.intValue();
	}

}
