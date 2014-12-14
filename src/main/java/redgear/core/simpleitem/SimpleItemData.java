package redgear.core.simpleitem;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.api.item.ISimpleItem;
import redgear.core.asm.RedGearCore;
import redgear.core.util.NBTItem;
import redgear.core.util.SimpleItem;
import redgear.core.util.SimpleOre;
import redgear.core.util.WildcardItem;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

class SimpleItemData {
	String modName;
	String itemName;
	int meta;
	String oreName;
	String nbt;
	
	static SimpleItemData create(ISimpleItem item){
		if(item instanceof SimpleOre)
			return new SimpleItemData((SimpleOre) item);
		
		if(item instanceof NBTItem)
			return new SimpleItemData((NBTItem) item);
		
		return new SimpleItemData(item);
	}

	private SimpleItemData(ISimpleItem item) {
		UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(item.getItem());

		if (id == null)
			return;

		modName = id.modId;
		itemName = id.name;
		meta = item.getMeta();

		if (meta == OreDictionary.WILDCARD_VALUE)
			meta = -1;
		
		RedGearCore.inst.logDebug("Saving item: ", item.getDisplayName(), " Saved: ", modName, ":", itemName);
	}

	private SimpleItemData(SimpleOre item) {
		this(item.getTarget());
		oreName = item.oreName;
	}

	private SimpleItemData(NBTItem item) {
		this(item.getTarget());
		if (item.getNBT() != null && !item.getNBT().hasNoTags())
			nbt = item.getNBT().toString();
	}

	ISimpleItem parse() {
		ISimpleItem ans;

		ItemStack stack = SimpleItemHelper.helper.findStack(modName, itemName);

		if (meta < 0 || meta == OreDictionary.WILDCARD_VALUE)
			ans = new WildcardItem(stack);
		else
			ans = new SimpleItem(stack);

		if (oreName != null)
			ans = new SimpleOre(oreName, ans);
		if (nbt != null)
			try {
				ans = new NBTItem(ans, (NBTTagCompound) JsonToNBT.func_150315_a(nbt));
			} catch (NBTException e) {
			}

		RedGearCore.inst.logDebug("Loading item: ", modName, ":", itemName, " Loaded:  ", stack == null ? "null" : stack.getDisplayName());
		return ans;
	}
}