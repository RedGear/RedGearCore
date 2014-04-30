package redgear.core.imc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IMCMessageHandler {
	
	void handle(String value);
	void handle(NBTTagCompound value);
	void handle(ItemStack value);
	

}
